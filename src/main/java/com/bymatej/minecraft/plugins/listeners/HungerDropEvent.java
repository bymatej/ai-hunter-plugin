package com.bymatej.minecraft.plugins.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class HungerDropEvent implements Listener {

    @EventHandler
    public void onHungerDropEvent(FoodLevelChangeEvent event) {
        // if player == hunter
        /*
        event.setCancelled(true);
        event.setFoodLevel(20);
        */

    }

}
