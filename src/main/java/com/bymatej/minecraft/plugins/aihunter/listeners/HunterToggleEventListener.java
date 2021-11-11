package com.bymatej.minecraft.plugins.aihunter.listeners;

import com.bymatej.minecraft.plugins.aihunter.events.HunterToggleEvent;
import com.bymatej.minecraft.plugins.aihunter.utils.HunterStatus;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.LinkedList;
import java.util.List;

import static com.bymatej.minecraft.plugins.aihunter.AiHunterPlugin.getPluginReference;
import static com.bymatej.minecraft.plugins.aihunter.utils.HunterStatus.OFF;
import static com.bymatej.minecraft.plugins.aihunter.utils.HunterStatus.ON;
import static com.bymatej.minecraft.plugins.aihunter.utils.HunterUtils.createHunter;
import static com.bymatej.minecraft.plugins.aihunter.utils.LoggingUtils.log;
import static java.lang.String.format;
import static java.lang.System.currentTimeMillis;
import static java.lang.Thread.sleep;
import static java.util.logging.Level.SEVERE;

public class HunterToggleEventListener implements Listener {

    public static List<NPC> aiHunters = new LinkedList<>();
    static int hunterId = 0;

    @EventHandler
    public void onHunterToggleEvent(HunterToggleEvent event) {
        HunterStatus status = event.getStatus();
        if (ON.equals(status)) {
            hunterOn(event);
        } else if (OFF.equals(status)) {
            hunterOff(event);
        } else {
            log(SEVERE, "Unrecognized status while toggling the hunter. Nothing got done!");
        }
    }

    private void hunterOn(HunterToggleEvent event) {
        Player commandSender = event.getPlayer();
        aiHunters = new LinkedList<>(); // remove all hunters
        int numberOfHunters = event.getNumberOfHunters();
        for (int i = 0; i < numberOfHunters; i++){
            createHunter(event.getHunterName(), hunterId++, commandSender);
        }
//        armHunter(aiHunter); // todo: arm hunter inside HunterUtils
        commandSender.sendMessage(format("You just spawned %s AI %s. RUN!", numberOfHunters,
                numberOfHunters > 1 ? "Hunters" : "Hunter"));
//        freezeHunter(aiHunter);
        setWeather(commandSender);
        // todo: execute command to start hunter
    }

    private void hunterOff(HunterToggleEvent event) {
        Player commandSender = event.getPlayer();
        aiHunters = new LinkedList<>(); // remove all hunters
//        disarmHunter(aiHunter);
        commandSender.sendMessage("Hunters are disabled. Thank God!");
        setWeather(commandSender);
        // todo: execute command to stop hunter
    }

    private void deleteHunterIfExists(Player aiHunter) {

    }

    /**
     * Possibly the weirdest function I've ever written
     *
     * @param player
     */
    private void freezeHunter(Player player) {
        player.setInvulnerable(false);
        FileConfiguration config = getPluginReference().getConfig();
        boolean isHunterInvulnerable = config.getBoolean("hunter_armed_invulnerable", false);

        // Todo: write more efficient logic that executes a piece of code every X/4 seconds and prints the message every second
        Location initialLocation = player.getLocation();
        int movementDelay = config.getInt("hunter_move_delay_seconds", 10);
        long start = currentTimeMillis(); // start time is current time
        long finish = start + (1000L * movementDelay); // end time is start time + time in seconds configured in config.yml multiplied by 1000 to get milliseconds
        int secondDivider = 4; // quarter
        int secondControl = 1;
        player.sendMessage(format("You will be teleported back to to %s/%s/%s for %s seconds.",
                initialLocation.getX(),
                initialLocation.getY(),
                initialLocation.getZ(),
                movementDelay));
        while (currentTimeMillis() < finish) {
            try {
                sleep(1000 / secondDivider); // wait quarter of a second
            } catch (InterruptedException e) {
                log(SEVERE, "Failed to use Thread.sleep(500);");
            }

            // Teleporting
            player.setInvulnerable(true);
            player.teleport(initialLocation);

            // Printing message
            if (secondControl <= secondDivider + 1) { // +1 because we "used" one quarter, so we need to add it back
                secondControl++;
            }

            if (secondControl == secondDivider + 1) {
                player.sendMessage(format("You need to wait for %s more seconds before you can move.", --movementDelay));
                secondControl = 1;
            }
        }

        player.setInvulnerable(isHunterInvulnerable);
        player.sendMessage("You can hunt now!");
    }

    private void setWeather(Player player) {
        if (getPluginReference().getConfig().getBoolean("day_and_clear_weather_on_hunter_toggle", true)) {
            World world = player.getWorld();
            world.setTime(1000L); // day
            world.setStorm(false); // no storm
            world.setThundering(false); // no thunder
        }
    }

}
