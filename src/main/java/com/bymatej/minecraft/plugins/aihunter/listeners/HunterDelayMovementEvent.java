package com.bymatej.minecraft.plugins.aihunter.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import static com.bymatej.minecraft.plugins.aihunter.utils.HunterUtils.isPlayerHunter;
import static java.lang.System.currentTimeMillis;

public class HunterDelayMovementEvent implements Listener {

    public void onHunterDelayMovement(PlayerMoveEvent event) {
        Player aiHunter = event.getPlayer();

        if (isPlayerHunter(aiHunter)) {
            long finish = currentTimeMillis() + 10000; // end time is current time + 10000 ms (10s)
            while (currentTimeMillis() < finish) {
                event.setCancelled(true);
            }
        }

        event.setCancelled(false);
    }

}
