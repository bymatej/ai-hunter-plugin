package com.bymatej.minecraft.plugins.aihunter.commands;

import com.bymatej.minecraft.plugins.aihunter.events.HunterToggleEvent;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static com.bymatej.minecraft.plugins.aihunter.utils.LoggingUtils.log;
import static com.bymatej.minecraft.plugins.aihunter.utils.HunterStatus.ON;
import static java.lang.Integer.parseInt;
import static java.util.logging.Level.WARNING;
import static org.bukkit.Bukkit.getPluginManager;

public class AiHunterCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        try {
            executeCommand(sender, args);
            return true;
        } catch (CommandException ex) {
            log(WARNING, "Error executing the command.", ex);
        }

        return false;
    }

    private void executeCommand(CommandSender sender, String[] args) throws CommandException {
        log("Command is executing");

        if (sender instanceof Player && sender.hasPermission("terminatornpc.spawnterminator")) { //todo: rename permission
            validateCommand(sender, args);

            if (args.length == 1) {
                turnAiHunterOn(args[0], (Player) sender);
            }

            if (args.length == 2) {
                turnAiHunterOn(args[0], parseInt(args[1]), (Player) sender);
            }
        } else {
            String message = "You cannot execute this command. You're not a Player, or you don't have the permission.";
            sender.sendMessage(message);
            log(WARNING, message);
            throw new CommandException(message);
        }

    }

    private void validateCommand(CommandSender sender, String[] args) throws CommandException {
        if (args.length == 1) {
            validateDesiredAiHunterName(sender, args[0]);
        }

        if (args.length == 2) {
            validateDesiredAiHunterName(sender, args[0]);
            validateDesiredHunterAmount(sender, args[1]);
        }

        if (args.length > 2) {
            String message = "More than 2 parameters given. Unrecognized request";
            sender.sendMessage(message);
            log(WARNING, message);
            throw new CommandException(message);
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

    private void turnAiHunterOn(String aiHunterName, Player player) {
        turnAiHunterOn(aiHunterName, 1, player);
    }

    private void turnAiHunterOn(String aiHunterName, int numberOfHunters, Player player) {
        HunterToggleEvent hunterToggleEvent = new HunterToggleEvent(aiHunterName, numberOfHunters, ON, player);
        getPluginManager().callEvent(hunterToggleEvent);
        log("Hunter turned on");
    }

}
