package com.bymatej.minecraft.plugins.aihunter.listeners;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.bymatej.minecraft.plugins.aihunter.events.HunterToggleEvent;
import com.bymatej.minecraft.plugins.aihunter.traits.HunterTrait;
import com.bymatej.minecraft.plugins.aihunter.utils.HunterStatus;

import static com.bymatej.minecraft.plugins.aihunter.AiHunterPlugin.getPluginReference;
import static com.bymatej.minecraft.plugins.aihunter.utils.HunterStatus.OFF;
import static com.bymatej.minecraft.plugins.aihunter.utils.HunterStatus.ON;
import static com.bymatej.minecraft.plugins.aihunter.utils.HunterUtils.createHunter;
import static com.bymatej.minecraft.plugins.aihunter.utils.HunterUtils.disarmHunter;
import static com.bymatej.minecraft.plugins.aihunter.utils.HunterUtils.freezeHunters;
import static com.bymatej.minecraft.plugins.aihunter.utils.LoggingUtils.log;
import static java.lang.String.format;
import static java.util.logging.Level.SEVERE;

public class HunterToggleEventListener implements Listener {

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
        getPluginReference().getAiHunters().clear(); // remove previous hunters
        int numberOfHunters = event.getNumberOfHunters();
        if (numberOfHunters == 1) {
            getPluginReference().setHunterId((getPluginReference().getHunterId() + 1));
            createHunter(event.getHunterName(), getPluginReference().getHunterId(), commandSender);
        } else {
            for (int i = 0; i < numberOfHunters; i++) {
                getPluginReference().setHunterId((getPluginReference().getHunterId() + 1));
                createHunter(event.getHunterName() + "-" + i, getPluginReference().getHunterId(), commandSender);
            }
        }

        commandSender.sendMessage(format("You just spawned %s AI %s. RUN!", numberOfHunters,
                                         numberOfHunters > 1 ? "Hunters" : "Hunter"));
        setWeather(commandSender);
        freezeHunters(commandSender);
    }

    private void hunterOff(HunterToggleEvent event) {
        Player commandSender = event.getPlayer();
        getPluginReference().getAiHunters().forEach(hunter -> {
            disarmHunter(hunter);
            HunterTrait hunterTrait = hunter.getOrAddTrait(HunterTrait.class);
            hunterTrait.setDelete(true);
            hunter.despawn();
            hunter.destroy();
        });
        getPluginReference().getAiHunters().clear();
        commandSender.sendMessage("Hunters are disabled. Thank God!");
        setWeather(commandSender);
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
