package com.bymatej.minecraft.plugins.aihunter.listeners;

import com.bymatej.minecraft.plugins.aihunter.events.HunterToggleEvent;
import com.bymatej.minecraft.plugins.aihunter.utils.HunterStatus;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import static com.bymatej.minecraft.plugins.aihunter.utils.CommonUtils.getPluginReference;
import static com.bymatej.minecraft.plugins.aihunter.utils.CommonUtils.log;
import static com.bymatej.minecraft.plugins.aihunter.utils.DbUtils.createHunter;
import static com.bymatej.minecraft.plugins.aihunter.utils.DbUtils.deleteHunter;
import static com.bymatej.minecraft.plugins.aihunter.utils.HunterStatus.OFF;
import static com.bymatej.minecraft.plugins.aihunter.utils.HunterStatus.ON;
import static com.bymatej.minecraft.plugins.aihunter.utils.HunterUtils.*;
import static java.lang.System.currentTimeMillis;
import static java.lang.Thread.sleep;
import static java.util.logging.Level.SEVERE;

public class HunterToggleEventListener implements Listener {

    @EventHandler
    public void onHunterToggleEvent(HunterToggleEvent event) {
        Player aiHunter = event.getPlayer();
        HunterStatus status = event.getStatus();

        if (ON.equals(status)) {
            hunterOn(aiHunter);
        } else if (OFF.equals(status)) {
            hunterOff(aiHunter);
        } else {
            log(SEVERE, "Unrecognized status while toggling the hunter. Nothing got done!");
        }

    }

    private void hunterOn(Player aiHunter) {
        deleteHunterIfExists(aiHunter);
        armHunter(aiHunter);
        createHunter(getHunterDataForPlayer(aiHunter));
        aiHunter.sendMessage("You are now an AI Hunter. You cannot die!");
        freezeHunter(aiHunter);
        // todo: execute baritone start hunter command
    }

    private void hunterOff(Player aiHunter) {
        deleteHunterIfExists(aiHunter);
        disarmHunter(aiHunter);
        aiHunter.sendMessage("You are now a regular mortal player. You can die easily! Watch out!!!");
        // todo: execute baritone start hunter command
    }

    private void deleteHunterIfExists(Player aiHunter) {
        try {
            deleteHunter(getHunterDataForPlayer(aiHunter));
        } catch (Exception e) {
            log("Cannot delete - hunter does not exist");
        }
    }

    private void freezeHunter(Player player) {
        player.setInvulnerable(false);
        FileConfiguration config = getPluginReference().getConfig();
        boolean isHunterInvulnerable = config.getBoolean("hunter_armed_invulnerable", false);

        // Todo: write more efficient logic that executes a piece of code every X/4 seconds
        // todo: send message that indicates how many seconds are left before hunter can move
        player.sendMessage("You will be teleported back for X seconds.");
        Location initialLocation = player.getLocation();
        int movementDelay = config.getInt("hunter_armed_move_delay_seconds", 10);
        long start = currentTimeMillis(); // start time is current time
        long finish = start + (1000 * movementDelay); // end time is start time + time in seconds configured in config.yml multiplied by 1000 to get milliseconds
        while (currentTimeMillis() < finish) {
            try {
                sleep(250); // wait quarter of a second
            } catch (InterruptedException e) {
                log(SEVERE, "Failed to use Thread.sleep(500);");
            }

            player.setInvulnerable(true);
            player.teleport(initialLocation);
            player.sendMessage(String.format("Teleporting you back to %s/%s/%s",
                    initialLocation.getX(),
                    initialLocation.getY(),
                    initialLocation.getZ()));
        }

        player.setInvulnerable(isHunterInvulnerable);
        player.sendMessage("You can hunt now!");
    }

}
