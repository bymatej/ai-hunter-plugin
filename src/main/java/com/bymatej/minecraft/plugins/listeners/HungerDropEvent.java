package com.bymatej.minecraft.plugins.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class HungerDropEvent implements Listener {

    @EventHandler
    public void onHungerDropEvent(FoodLevelChangeEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (player.getName().equalsIgnoreCase("Matej2702")) { // todo: replace this line
                //event.setCancelled(true);
                if (event.getFoodLevel() <= 6) {
                    event.setFoodLevel(20); // or 19 - decide todo
                }
            } else {
                //event.setCancelled(false);
            }
        }

        // todo: pseudo code:
        // if player == hunter
        /*
        event.setCancelled(true);
        event.setFoodLevel(20);
        */

    }

}
