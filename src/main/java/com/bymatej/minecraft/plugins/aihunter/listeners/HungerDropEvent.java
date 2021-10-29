package com.bymatej.minecraft.plugins.aihunter.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class HungerDropEvent implements Listener {

    @EventHandler
    public void onHungerDropEvent(FoodLevelChangeEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (player.getName().equalsIgnoreCase("Matej2702")) { // todo: replace this with hunter name
                if (event.getFoodLevel() <= 6) {
                    event.setFoodLevel(20); // or 19 - decide todo
                }
            }
        }
    }

}
