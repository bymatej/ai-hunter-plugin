package com.bymatej.minecraft.plugins.aihunter.listeners;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

import static com.bymatej.minecraft.plugins.aihunter.utils.HunterUtils.armHunter;
import static com.bymatej.minecraft.plugins.aihunter.utils.HunterUtils.isPlayerHunter;

public class RespawnPlayerToDeathPointEvent implements Listener {

    @EventHandler
    public void onRespawnPlayerToDeathPoint(PlayerRespawnEvent event) {
        if (isPlayerHunter(event.getPlayer())) {
            Player aiHunter = event.getPlayer();
            final Location location = aiHunter.getLocation();
            aiHunter.sendMessage("You respawned right where you died!");
            event.setRespawnLocation(location);
            armHunter(aiHunter);
        }
    }

}
