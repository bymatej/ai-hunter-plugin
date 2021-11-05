package com.bymatej.minecraft.plugins.aihunter.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import static com.bymatej.minecraft.plugins.aihunter.utils.CommonUtils.log;
import static com.bymatej.minecraft.plugins.aihunter.utils.DbUtils.createHunter;
import static com.bymatej.minecraft.plugins.aihunter.utils.DbUtils.deleteHunter;
import static com.bymatej.minecraft.plugins.aihunter.utils.HunterUtils.*;
import static java.lang.System.currentTimeMillis;
import static java.util.logging.Level.INFO;
import static java.util.logging.Level.WARNING;
import static org.apache.commons.lang.StringUtils.isBlank;

public class AiHunterCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        try {
            if (sender instanceof Player || sender instanceof ConsoleCommandSender) {
                executeCommand(sender, args);

                return true;
            }
        } catch (CommandException ex) {
            log(WARNING, "Error executing the command.", ex);
            return false;
        }

        return false;
    }

    private void executeCommand(CommandSender sender, String[] args) throws CommandException {
        log("Command is executing");

        validateCommand(sender, args);
        Player aiHunter = getAiHunterPlayer(sender, args[0]);
        if ("on".equalsIgnoreCase(args[1])) {
            turnAiHunterOn(aiHunter);
        } else if ("off".equalsIgnoreCase(args[1])) {
            turnAiHunterOff(aiHunter);
        } else {
            String message = "Unrecognized parameter " + args[1];
            sender.sendMessage(message);
            log(WARNING, message);
            throw new CommandException(message);
        }

    }

    private Player getAiHunterPlayer(CommandSender sender, String playerName) {
        Player player = Bukkit.getPlayer(playerName);
        if (player == null) {
            String message = "Player was not found (got null).";
            sender.sendMessage(message);
            log(WARNING, message);
            throw new CommandException(message);
        }

        return player;
    }

    private void validateCommand(CommandSender sender, String[] args) throws CommandException {
        if (args.length > 2) {
            String message = "There should not be more than two arguments for this command.";
            sender.sendMessage(message);
            log(WARNING, message);
            throw new CommandException(message);
        } else if (args.length < 2) {
            String message = "There should be at least two arguments for this command.";
            sender.sendMessage(message);
            log(WARNING, message);
            throw new CommandException(message);
        } else if (isBlank(args[0]) || isBlank(args[1])) {
            String message = "Both first and second arguments must not be blank or null.";
            sender.sendMessage(message);
            log(WARNING, message);
            throw new CommandException(message);
        } else {
            log(INFO, "Both arguments present are correct: " + args[0] + " and " + args[1]);
        }
    }

    private void turnAiHunterOn(Player aiHunter) {
        try {
            deleteHunter(getHunterDataForPlayer(aiHunter));
        } catch (Exception e) {
            log("Cannot delete - hunter does not exist");
        }

        armHunter(aiHunter);
        createHunter(getHunterDataForPlayer(aiHunter));
        aiHunter.chat("You are now an AI Hunter. You cannot die!");
        freezeHunter(aiHunter);
        log("Hunter turned on");
    }

    private void turnAiHunterOff(Player aiHunter) {
        try {
            deleteHunter(getHunterDataForPlayer(aiHunter));
        } catch (Exception e) {
            log("Cannot delete - hunter does not exist");
        }

        disarmHunter(aiHunter);
        aiHunter.chat("You are now a regular mortal player. You can die easily! Watch out!!!");
        log("Hunter turned off");
    }

    private void freezeHunter(Player player) {
        // Todo: find a better way to execute a piece of code every X seconds
        player.sendMessage("You will be teleported back for X seconds.");
        Location initialLocation = player.getLocation();
        long start = currentTimeMillis(); // start time is current time
        long finish = start + 10000; // end time is start time + 10000 ms (10s)
        while (currentTimeMillis() < finish) {
            long thousandMilis = finish - currentTimeMillis();
            boolean isOneSecond = thousandMilis % 1000 == 0;
            if (isOneSecond) {
                player.teleport(initialLocation);
            }
        }

        player.sendMessage("You can hunt now!");
    }

}
