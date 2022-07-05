package com.bymatej.minecraft.plugins.aihunter.commands;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

import com.bymatej.minecraft.plugins.aihunter.actions.HunterStuckAction;
import com.bymatej.minecraft.plugins.aihunter.events.HunterToggleEvent;
import com.bymatej.minecraft.plugins.aihunter.traits.HunterFollow;
import com.bymatej.minecraft.plugins.aihunter.utils.HunterUtils;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.trait.SkinTrait;

import static com.bymatej.minecraft.plugins.aihunter.AiHunterPlugin.getPluginReference;
import static com.bymatej.minecraft.plugins.aihunter.utils.HunterStatus.OFF;
import static com.bymatej.minecraft.plugins.aihunter.utils.HunterStatus.ON;
import static com.bymatej.minecraft.plugins.aihunter.utils.LoggingUtils.log;
import static java.lang.Integer.parseInt;
import static java.util.logging.Level.WARNING;
import static org.bukkit.Bukkit.getPluginManager;

public class AiHunterCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        try {
            if (command.getName().equalsIgnoreCase("test")) {
                 executeTestCommand2(sender, args);
            } else {
                executeCommand(sender, args);
            }
            return true;
        } catch (CommandException ex) {
            log(WARNING, "Error executing the command.", ex);
        }

        return false;
    }

    private void executeTestCommand(CommandSender sender, String[] args) throws CommandException {
        getPluginReference().getServer().broadcastMessage("Executing test command");
        Player player = (Player) sender;
        Location spawnLocation = player.getLocation();
        spawnLocation.setX(spawnLocation.getX() + 2);
        spawnLocation.setY(spawnLocation.getY() + 2);
        spawnLocation.setZ(spawnLocation.getZ() + 2);

        NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, "LOLO");
        npc.spawn(spawnLocation);
        npc.data().set(NPC.DEFAULT_PROTECTED_METADATA, false);
        npc.getNavigator().getLocalParameters()
           .attackRange(5) // was 10, now is 5
           .baseSpeed(1.5F)  // was 1.6 now is 1.5
           .straightLineTargetingDistance(100)
           .stuckAction(new HunterStuckAction())
           .range(40);
        HunterFollow followTrait = new HunterFollow();
        followTrait.linkToNPC(npc);
        followTrait.run();
        followTrait.toggle(player, false);
        npc.addTrait(followTrait);
        //HunterTrait hunterTrait = new HunterTrait(npc, new HunterLoadout(getPluginReference()));
        //npc.addTrait(hunterTrait);
        SkinTrait skinTrait = npc.getOrAddTrait(SkinTrait.class);
        skinTrait.setSkinName("LOLO");
        if (npc.isSpawned()) {
            npc.teleport(spawnLocation, PlayerTeleportEvent.TeleportCause.PLUGIN);
        }
        getPluginReference().getServer().broadcastMessage("Executed test command");
    }

    private void executeTestCommand2(CommandSender sender, String[] args) throws CommandException {
        getPluginReference().getServer().broadcastMessage("Executing test command");

        HunterUtils.freezeHunters((Player) sender);

        getPluginReference().getServer().broadcastMessage("Executed test command");
    }

    private void executeCommand(CommandSender sender, String[] args) throws CommandException {
        log("Command is executing");

        if (sender instanceof Player && sender.hasPermission("aihunter.spawnhunter")) {
            Player player = (Player) sender;

            if (args.length == 0) {
                turnAiHunterOn(null, player);
            }

            if (args.length == 1) {
                if (StringUtils.isNotBlank(args[0]) && args[0].equalsIgnoreCase("off")) {
                    turnAiHuntersOff(player);
                } else {
                    validateCommand(sender, args);
                    turnAiHunterOn(args[0], player);
                }
            }

            if (args.length == 2) {
                validateCommand(sender, args);
                turnAiHunterOn(args[0], parseInt(args[1]), player);
            }
        } else {
            String message = "You cannot execute this command. You're not a Player, or you don't have the permission.";
            sender.sendMessage(message);
            log(WARNING, message);
            throw new CommandException(message);
        }

    }

    private void validateCommand(CommandSender sender, String[] args) throws CommandException {
        if (args.length > 2) {
            String message = "More than 2 parameters given. Unrecognized request";
            sender.sendMessage(message);
            log(WARNING, message);
            throw new CommandException(message);
        }

        if (args.length == 1) {
            validateDesiredAiHunterName(sender, args[0]);
        }

        if (args.length == 2) {
            validateDesiredAiHunterName(sender, args[0]);
            validateDesiredHunterAmount(sender, args[1]);
        }
    }

    private void validateDesiredAiHunterName(CommandSender sender, String aiHunterName) {
        // Name must not be null/blank
        if (StringUtils.isBlank(aiHunterName)) {
            String message = "Hunter name must not be blank!";
            sender.sendMessage(message);
            log(WARNING, message);
            throw new CommandException(message);
        }

        // The AI hunter must not have the same name as the real (human) player on the server
        // But if a real (human) player that joins the server has the same name as the already existing hunter, that's fine
        Player player = Bukkit.getPlayer(aiHunterName);
        if (player != null) {
            String message = "There is a real (human) player with that name on the server. Don't try to trick people!";
            sender.sendMessage(message);
            log(WARNING, message);
            throw new CommandException(message);
        }

        // AI hunter must not be named On or Off
        if (aiHunterName.equalsIgnoreCase("on") ||
            aiHunterName.equalsIgnoreCase("off")) {
            String message = "You cannot name your hunter \"on\" or \"off\"!";
            sender.sendMessage(message);
            log(WARNING, message);
            throw new CommandException(message);
        }
    }

    private void validateDesiredHunterAmount(CommandSender sender, String desiredNumberOfHunters) {
        int numberOfHunters;
        try {
            numberOfHunters = parseInt(desiredNumberOfHunters);
        } catch (NumberFormatException e) {
            String message = String.format("\"%s\" is not a number!", desiredNumberOfHunters);
            sender.sendMessage(message);
            log(WARNING, message);
            throw new CommandException(message);
        }

        if (numberOfHunters < 1 || numberOfHunters > 200) {
            String message = "Number of hunters is 0 or it is greater than 200. Not enough, or enough to kill a server. Change the number!";
            sender.sendMessage(message);
            log(WARNING, message);
            throw new CommandException(message);
        }
    }

    private void turnAiHuntersOff(Player player) {
        HunterToggleEvent hunterToggleEvent = new HunterToggleEvent(null, 0, OFF, player);
        getPluginManager().callEvent(hunterToggleEvent);
        log("Hunters turned off");
    }

    private void turnAiHunterOn(String aiHunterName, Player player) {
        turnAiHunterOn(aiHunterName, 1, player);
    }

    private void turnAiHunterOn(String aiHunterName, int numberOfHunters, Player player) {
        if (StringUtils.isBlank(aiHunterName)) {
            aiHunterName = "Bot Mutant";
        }

        HunterToggleEvent hunterToggleEvent = new HunterToggleEvent(aiHunterName, numberOfHunters, ON, player);
        getPluginManager().callEvent(hunterToggleEvent);
        log("Hunter turned on");
    }

}
