package com.bymatej.minecraft.plugins.aihunter.listeners;

import java.util.Optional;

import org.bukkit.World;
import org.bukkit.command.CommandException;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.bymatej.minecraft.plugins.aihunter.events.HunterToggleEvent;
import com.bymatej.minecraft.plugins.aihunter.traits.HunterTrait;
import com.bymatej.minecraft.plugins.aihunter.utils.HunterStatus;

import net.citizensnpcs.api.npc.NPC;

import static com.bymatej.minecraft.plugins.aihunter.AiHunterPlugin.getPluginReference;
import static com.bymatej.minecraft.plugins.aihunter.utils.HunterStatus.OFF;
import static com.bymatej.minecraft.plugins.aihunter.utils.HunterStatus.ON;
import static com.bymatej.minecraft.plugins.aihunter.utils.HunterUtils.createHunter;
import static com.bymatej.minecraft.plugins.aihunter.utils.HunterUtils.disarmHunter;
import static com.bymatej.minecraft.plugins.aihunter.utils.HunterUtils.freezeHunters;
import static com.bymatej.minecraft.plugins.aihunter.utils.LoggingUtils.log;
import static java.lang.String.format;
import static java.util.logging.Level.SEVERE;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

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
        if (isNotBlank(event.getHunterName())) {
            Optional<NPC> optionalHunter = getPluginReference().getAiHunters().stream()
                                                               .filter(h -> h.getName().equalsIgnoreCase(event.getHunterName()))
                                                               .findFirst();

            if (optionalHunter.isPresent()) {
                NPC hunter = optionalHunter.get();
                disarmHunter(hunter);
                HunterTrait hunterTrait = hunter.getOrAddTrait(HunterTrait.class);
                hunterTrait.setDelete(true);
                hunter.despawn();
                hunter.destroy();
                getPluginReference().getAiHunters().remove(hunter);
                if (commandSender != null) {
                    commandSender.sendMessage("Hunters " + event.getHunterName() + " is removed!");
                } else {
                    getPluginReference().getServer().broadcastMessage("Hunters " + event.getHunterName() + " is removed!");
                }
            } else {
                throw new CommandException("Hunter with name " + event.getHunterName() + " does not exist.");
            }
        } else {
            getPluginReference().getAiHunters().forEach(hunter -> {
                disarmHunter(hunter);
                HunterTrait hunterTrait = hunter.getOrAddTrait(HunterTrait.class);
                hunterTrait.setDelete(true);
                hunter.despawn();
                hunter.destroy();
            });
            getPluginReference().getAiHunters().clear();
            if (commandSender != null) {
                commandSender.sendMessage("Hunters are disabled. Thank God!");
            } else {
                getPluginReference().getServer().broadcastMessage("Hunters are disabled. Thank God!");
            }
        }

        setWeather(commandSender);
    }

    private void setWeather(Player player) {
        if (player != null && getPluginReference().getConfig().getBoolean("day_and_clear_weather_on_hunter_toggle", true)) {
            World world = player.getWorld();
            world.setTime(1000L); // day
            world.setStorm(false); // no storm
            world.setThundering(false); // no thunder
        }
    }

}
