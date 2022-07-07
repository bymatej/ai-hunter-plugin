package com.bymatej.minecraft.plugins.aihunter.commands;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.bymatej.minecraft.plugins.aihunter.events.HunterToggleEvent;
import com.bymatej.minecraft.plugins.aihunter.traits.HunterFollow;
import com.bymatej.minecraft.plugins.aihunter.traits.HunterTrait;
import com.bymatej.minecraft.plugins.aihunter.utils.LoggingUtils;
import com.bymatej.minecraft.plugins.aihunter.utils.TableGenerator;

import net.citizensnpcs.api.npc.NPC;

import static com.bymatej.minecraft.plugins.aihunter.AiHunterPlugin.getPluginReference;
import static com.bymatej.minecraft.plugins.aihunter.commands.CommandConstants.AiHunter.ALL;
import static com.bymatej.minecraft.plugins.aihunter.commands.CommandConstants.AiHunter.COMMAND_NAME;
import static com.bymatej.minecraft.plugins.aihunter.commands.CommandConstants.AiHunter.CREATE;
import static com.bymatej.minecraft.plugins.aihunter.commands.CommandConstants.AiHunter.FORBIDDEN_KEYWORDS;
import static com.bymatej.minecraft.plugins.aihunter.commands.CommandConstants.AiHunter.INFO;
import static com.bymatej.minecraft.plugins.aihunter.commands.CommandConstants.AiHunter.LIST;
import static com.bymatej.minecraft.plugins.aihunter.commands.CommandConstants.AiHunter.MAXIMUM_ARGUMENTS_NUMBER;
import static com.bymatej.minecraft.plugins.aihunter.commands.CommandConstants.AiHunter.MINIMUM_ARGUMENTS_NUMBER;
import static com.bymatej.minecraft.plugins.aihunter.commands.CommandConstants.AiHunter.PAUSE;
import static com.bymatej.minecraft.plugins.aihunter.commands.CommandConstants.AiHunter.REMOVE;
import static com.bymatej.minecraft.plugins.aihunter.commands.CommandConstants.AiHunter.RESUME;
import static com.bymatej.minecraft.plugins.aihunter.commands.CommandConstants.AiHunter.VALID_FIRST_PARAMETERS;
import static com.bymatej.minecraft.plugins.aihunter.utils.HunterStatus.OFF;
import static com.bymatej.minecraft.plugins.aihunter.utils.HunterStatus.ON;
import static com.bymatej.minecraft.plugins.aihunter.utils.LoggingUtils.log;
import static com.bymatej.minecraft.plugins.aihunter.utils.TableGenerator.Alignment.LEFT;
import static java.lang.Integer.parseInt;
import static java.lang.String.format;
import static java.lang.String.join;
import static java.util.Collections.singletonList;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Level.WARNING;
import static org.apache.commons.lang.StringUtils.isNotBlank;
import static org.apache.commons.lang.StringUtils.isNumeric;
import static org.bukkit.Bukkit.getPluginManager;

public class AiHunterCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        try {
            executeCommand(command, sender, args);
            return true;
        } catch (CommandException ex) {
            log(WARNING, "Error executing the command.", ex);
        }

        return false;
    }

    private void executeCommand(Command command, CommandSender sender, String[] args) throws CommandException {
        log("Command is executing");

        if (sender instanceof Player && sender.hasPermission("aihunter.spawnhunter")) {
            Player player = (Player) sender;
            validateCommand(command, sender, args);

            if (args[0].equalsIgnoreCase(CREATE)) {
                handleCreateCommands(player, args);
            } else if (args[0].equalsIgnoreCase(REMOVE)) {
                handleRemoveCommands(player, args);
            } else if (args[0].equalsIgnoreCase(LIST)) {
                handleListCommands(player, args);
            } else if (args[0].equalsIgnoreCase(INFO)) {
                handleInfoCommands(player, args);
            } else if (args[0].equalsIgnoreCase(PAUSE)) {
                handlePauseCommands(player, args); // todo: this does not work
            } else if (args[0].equalsIgnoreCase(RESUME)) {
                handleResumeCommands(player, args); // todo: this does not work
            } else {
                String message = "This should never happen...";
                sender.sendMessage(message);
                log(SEVERE, message);
                throw new CommandException(message);
            }
        } else {
            String message = "You cannot execute this command. You're not a Player, or you don't have the permission.";
            sender.sendMessage(message);
            log(WARNING, message);
            throw new CommandException(message);
        }

    }

    private void validateCommand(Command command, CommandSender sender, String[] args) throws CommandException {
        String message = "Something went wrong while executing the command!";
        boolean isInvalid = false;

        if (command != null && isNotBlank(command.getName()) && !command.getName().equalsIgnoreCase(COMMAND_NAME)) {
            isInvalid = true;
        }

        if (args.length < MINIMUM_ARGUMENTS_NUMBER) {
            message = "Less than " + MINIMUM_ARGUMENTS_NUMBER + " parameters given. Unrecognized request";
            isInvalid = true;
        }

        if (args.length > MAXIMUM_ARGUMENTS_NUMBER) {
            message = "More than " + MAXIMUM_ARGUMENTS_NUMBER + " parameters given. Unrecognized request";
            isInvalid = true;
        }

        if (args.length == 1 && !VALID_FIRST_PARAMETERS.contains(args[0])) {
            message = "Parameter " + args[0] + " is not a valid first parameter. Valid parameters are: " + join(",", VALID_FIRST_PARAMETERS);
            isInvalid = true;
        }

        if (isInvalid) {
            sender.sendMessage(message);
            log(WARNING, message);
            throw new CommandException(message);
        }
    }

    private void handleCreateCommands(Player commandSender, String[] args) {
        switch (args.length) {
            case 1:
                turnAiHunterOn("", commandSender);
                break;
            case 2:
                validateDesiredAiHunterName(commandSender, args[1]);
                turnAiHunterOn(args[1], commandSender);
                break;
            case 3:
                validateDesiredHunterAmount(commandSender, args[2]);
                turnAiHunterOn(args[1], parseInt(args[2]), commandSender);
                break;
            default:
                throw new CommandException("Invalid amount of command arguments");
        }
    }

    private void handleRemoveCommands(Player commandSender, String[] args) {
        switch (args.length) {
            case 1:
                turnAiHuntersOff(commandSender);
                break;
            case 2:
                if (args[1].equalsIgnoreCase(ALL)) {
                    turnAiHuntersOff(commandSender);
                } else {
                    validateDesiredAiHunterName(commandSender, args[1]);
                    turnAiHunterOff(args[1], commandSender);
                }
                break;
            default:
                throw new CommandException("Invalid amount of command arguments");
        }
    }

    private void handleListCommands(Player commandSender, String[] args) {
        switch (args.length) {
            case 1:
                displayInfoForAllHunters(commandSender);
                break;
            case 2:
                if (args[1].equalsIgnoreCase(ALL)) {
                    displayInfoForAllHunters(commandSender);
                } else {
                    throw new CommandException("Second argument can only be " + ALL);
                }
                break;
            default:
                throw new CommandException("Invalid amount of command arguments");
        }
    }

    private void handleInfoCommands(Player commandSender, String[] args) {
        if (args.length == 2) {
            displayInfoForHunterName(commandSender, args[1]);
        } else {
            throw new CommandException("Invalid usage for info command. Correct usage is: aihunter info <hunter_name>");
        }
    }

    private void handlePauseCommands(Player commandSender, String[] args) {
        switch (args.length) {
            case 1:
                pauseAllHunters();
                break;
            case 2:
                pauseHunterForName(args[1]);
                break;
            default:
                throw new CommandException("Invalid amount of command arguments");
        }
    }

    private void handleResumeCommands(Player commandSender, String[] args) {
        switch (args.length) {
            case 1:
                resumeAllHunters(commandSender);
                break;
            case 2:
                resumeHunterForName(commandSender, args[1]);
                break;
            default:
                throw new CommandException("Invalid amount of command arguments");
        }
    }

    private void displayHunterInfo(Player sender, List<NPC> hunters) {
        // hunter ID, hunter name, hunter's location
        TableGenerator tableGenerator = new TableGenerator(LEFT, LEFT, LEFT);
        tableGenerator.addRow("Hunter ID", "Hunter name", "Hunter's location");
        hunters.forEach(hunter -> {
            if (hunter.getEntity() != null) {
                Location loc = hunter.getEntity().getLocation();
                String location = format("%s / %s / %s", loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
                tableGenerator.addRow(Integer.toString(hunter.getId()), hunter.getName(), location);
            } else {
                LoggingUtils.log("There was no Entity found for hunter " + hunter.getName());
            }
        });

        tableGenerator.generate().forEach(sender::sendMessage);
    }

    private void displayInfoForAllHunters(Player sender) {
        displayHunterInfo(sender, getPluginReference().getAiHunters());
    }

    private void displayInfoForHunterName(Player sender, String hunterName) {
        Optional<NPC> optionalHunter = getPluginReference().getAiHunters().stream()
                                                  .filter(h -> h.getName().equalsIgnoreCase(hunterName))
                                                  .findFirst();

        if (optionalHunter.isPresent()) {
            displayHunterInfo(sender, singletonList(optionalHunter.get()));
        } else {
            throw new CommandException("There is no hunter with name " + hunterName);
        }
    }

    private void pauseHunter(List<NPC> hunters) {
        hunters.forEach(hunter -> {
            hunter.getEntity().setInvulnerable(true);
            HunterTrait hunterTrait = hunter.getOrAddTrait(HunterTrait.class);
            hunterTrait.setDelete(true);
            HunterFollow followTrait = hunter.getOrAddTrait(HunterFollow.class);
            followTrait.setEnabled(false);
        });

        getPluginReference().getServer().broadcastMessage("Paused hunters are: " + hunters.stream()
                                                                                          .map(NPC::getName)
                                                                                          .collect(Collectors.joining(",")));
    }

    private void pauseAllHunters() {
        pauseHunter(getPluginReference().getAiHunters());
    }

    private void pauseHunterForName(String hunterName) {
        Optional<NPC> optionalHunter = getPluginReference().getAiHunters().stream()
                                                  .filter(h -> h.getName().equalsIgnoreCase(hunterName))
                                                  .findFirst();

        if (optionalHunter.isPresent()) {
            pauseHunter(singletonList(optionalHunter.get()));
        } else {
            throw new CommandException("There is no hunter with name " + hunterName);
        }
    }

    private void resumeHunter(Player sender, List<NPC> hunters) {
        hunters.forEach(hunter -> {
            hunter.getEntity().setInvulnerable(false); // todo: get from config yml
            HunterTrait hunterTrait = hunter.getOrAddTrait(HunterTrait.class);
            hunterTrait.setDelete(false);
            HunterFollow followTrait = hunter.getOrAddTrait(HunterFollow.class);
            followTrait.setEnabled(true);
        });

        getPluginReference().getServer().broadcastMessage("Resumed hunters are: " + hunters.stream()
                                                                                           .map(NPC::getName)
                                                                                           .collect(Collectors.joining(",")));
    }

    private void resumeAllHunters(Player sender) {
        resumeHunter(sender, getPluginReference().getAiHunters());
    }

    private void resumeHunterForName(Player sender, String hunterName) {
        Optional<NPC> optionalHunter = getPluginReference().getAiHunters().stream()
                                                  .filter(h -> h.getName().equalsIgnoreCase(hunterName))
                                                  .findFirst();

        if (optionalHunter.isPresent()) {
            resumeHunter(sender, singletonList(optionalHunter.get()));
        } else {
            throw new CommandException("There is no hunter with name " + hunterName);
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

        // AI hunter must not be named after certain forbidden keywords
        if (FORBIDDEN_KEYWORDS.contains(aiHunterName)) {
            String message = "Second argument must contain the hunter name, and must not contain these keywords: " + join(",", FORBIDDEN_KEYWORDS);
            sender.sendMessage(message);
            log(WARNING, message);
            throw new CommandException(message);
        }
    }

    private void validateDesiredHunterAmount(CommandSender sender, String desiredNumberOfHunters) {
        int numberOfHunters;
        try {
            if (isNumeric(desiredNumberOfHunters)) {
                numberOfHunters = parseInt(desiredNumberOfHunters);
            } else {
                throw new NumberFormatException("Third argument must be a number");
            }
        } catch (NumberFormatException e) {
            String message = format("\"%s\" is not a number!", desiredNumberOfHunters);
            sender.sendMessage(message);
            log(WARNING, message);
            throw new CommandException(message);
        }

        if (numberOfHunters < 1 || numberOfHunters > 20) {
            String message = "Number of hunters is 0 or it is greater than 20. Not enough, or enough to kill a server. Change the number!";
            sender.sendMessage(message);
            log(WARNING, message);
            throw new CommandException(message);
        }
    }

    private void turnAiHunterOff(String hunterName, Player player) {
        HunterToggleEvent hunterToggleEvent = new HunterToggleEvent(hunterName, 0, OFF, player);
        getPluginManager().callEvent(hunterToggleEvent);
        log("Hunters turned off");
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

        String finalAiHunterName = aiHunterName;
        boolean hunterExists = getPluginReference().getAiHunters().stream()
                                                   .anyMatch(h -> h.getName().equalsIgnoreCase(finalAiHunterName));
        if (hunterExists) {
            aiHunterName = aiHunterName + " - " + getPluginReference().getHunterId() + 1;
        }

        HunterToggleEvent hunterToggleEvent = new HunterToggleEvent(aiHunterName, numberOfHunters, ON, player);
        getPluginManager().callEvent(hunterToggleEvent);
        log("Hunter turned on");
    }

}
