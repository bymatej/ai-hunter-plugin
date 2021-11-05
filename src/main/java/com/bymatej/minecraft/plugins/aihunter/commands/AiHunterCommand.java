package com.bymatej.minecraft.plugins.aihunter.commands;

import com.bymatej.minecraft.plugins.aihunter.events.HunterToggleEvent;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import static com.bymatej.minecraft.plugins.aihunter.utils.CommonUtils.log;
import static com.bymatej.minecraft.plugins.aihunter.utils.Constants.COMMAND_AIHUNTER_ARGUMENT_OFF;
import static com.bymatej.minecraft.plugins.aihunter.utils.Constants.COMMAND_AIHUNTER_ARGUMENT_ON;
import static com.bymatej.minecraft.plugins.aihunter.utils.HunterStatus.OFF;
import static com.bymatej.minecraft.plugins.aihunter.utils.HunterStatus.ON;
import static java.util.logging.Level.INFO;
import static java.util.logging.Level.WARNING;
import static org.apache.commons.lang.StringUtils.isBlank;
import static org.bukkit.Bukkit.getPluginManager;

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
        if (COMMAND_AIHUNTER_ARGUMENT_ON.equalsIgnoreCase(args[1])) {
            turnAiHunterOn(aiHunter);
        } else if (COMMAND_AIHUNTER_ARGUMENT_OFF.equalsIgnoreCase(args[1])) {
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
        HunterToggleEvent hunterToggleEvent = new HunterToggleEvent(aiHunter, ON);
        getPluginManager().callEvent(hunterToggleEvent);
        log("Hunter turned on");
    }

    private void turnAiHunterOff(Player aiHunter) {
        HunterToggleEvent hunterToggleEvent = new HunterToggleEvent(aiHunter, OFF);
        getPluginManager().callEvent(hunterToggleEvent);
        log("Hunter turned off");
    }

}
