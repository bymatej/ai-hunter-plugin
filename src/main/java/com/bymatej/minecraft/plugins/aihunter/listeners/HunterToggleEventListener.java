package com.bymatej.minecraft.plugins.aihunter.listeners;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.bymatej.minecraft.plugins.aihunter.events.HunterToggleEvent;
import com.bymatej.minecraft.plugins.aihunter.utils.HunterStatus;
import com.bymatej.minecraft.plugins.aihunter.utils.HunterUtils;

import net.citizensnpcs.api.npc.NPC;

import static com.bymatej.minecraft.plugins.aihunter.AiHunterPlugin.getPluginReference;
import static com.bymatej.minecraft.plugins.aihunter.utils.HunterStatus.OFF;
import static com.bymatej.minecraft.plugins.aihunter.utils.HunterStatus.ON;
import static com.bymatej.minecraft.plugins.aihunter.utils.HunterUtils.createHunter;
import static com.bymatej.minecraft.plugins.aihunter.utils.LoggingUtils.log;
import static java.lang.String.format;
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
        aiHunters.clear();
        aiHunters = new LinkedList<>(); // remove all hunters
        int numberOfHunters = event.getNumberOfHunters();
        if (numberOfHunters == 1) {
            createHunter(event.getHunterName(), hunterId++, commandSender);
        } else {
            for (int i = 0; i < numberOfHunters; i++) {
                createHunter(event.getHunterName() + "-" + i, hunterId++, commandSender);
            }
        }

        commandSender.sendMessage(format("You just spawned %s AI %s. RUN!", numberOfHunters,
                                         numberOfHunters > 1 ? "Hunters" : "Hunter"));
        setWeather(commandSender);
        //aiHunters.forEach(HunterUtils::freezeHunter);
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

    private void setWeather(Player player) {
        if (getPluginReference().getConfig().getBoolean("day_and_clear_weather_on_hunter_toggle", true)) {
            World world = player.getWorld();
            world.setTime(1000L); // day
            world.setStorm(false); // no storm
            world.setThundering(false); // no thunder
        }
    }

}
