package com.bymatej.minecraft.plugins.commands;

import org.bukkit.Bukkit;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import static com.bymatej.minecraft.plugins.utils.CommonUtils.log;
import static java.util.Objects.requireNonNull;
import static java.util.logging.Level.INFO;
import static java.util.logging.Level.WARNING;
import static org.apache.commons.lang.StringUtils.isBlank;
import static org.bukkit.Material.*;
import static org.bukkit.attribute.Attribute.*;

public class AiHunterCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        try {
            if (sender instanceof Player ||
                sender instanceof ConsoleCommandSender) {
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
        aiHunter.chat("You are now an AI Hunter. You cannot die!");

        log("Is AI: " + aiHunter.hasAI());
        //aiHunter.setInvulnerable(true);
        aiHunter.setSaturation(10);
        aiHunter.setSaturatedRegenRate(1);
        AttributeInstance maxHealthAttribute = aiHunter.getAttribute(GENERIC_MAX_HEALTH);
        requireNonNull(maxHealthAttribute).setBaseValue(500);
        aiHunter.setHealth(500);
        aiHunter.setFoodLevel(20);
        aiHunter.getInventory().setHelmet(new ItemStack(IRON_HELMET));
        aiHunter.getInventory().setChestplate(new ItemStack(IRON_CHESTPLATE));
        aiHunter.getInventory().setLeggings(new ItemStack(IRON_LEGGINGS));
        aiHunter.getInventory().setBoots(new ItemStack(IRON_BOOTS));

        log("Hunter turned on");
    }

    private void turnAiHunterOff(Player aiHunter) {
        aiHunter.chat("You are now a regular mortal player. You can die easily! Watch out!!!");

        log("Is AI: " + aiHunter.hasAI());
        //aiHunter.setInvulnerable(false);
        aiHunter.setSaturation(0);
        aiHunter.setSaturatedRegenRate(10);
        AttributeInstance maxHealthAttribute = aiHunter.getAttribute(GENERIC_MAX_HEALTH);
        requireNonNull(maxHealthAttribute).setBaseValue(maxHealthAttribute.getDefaultValue());
        aiHunter.setHealth(20);
        aiHunter.setFoodLevel(20);
        aiHunter.getInventory().setHelmet(new ItemStack(AIR));
        aiHunter.getInventory().setChestplate(new ItemStack(AIR));
        aiHunter.getInventory().setLeggings(new ItemStack(AIR));
        aiHunter.getInventory().setBoots(new ItemStack(AIR));

        log("Hunter turned off");
    }

}
