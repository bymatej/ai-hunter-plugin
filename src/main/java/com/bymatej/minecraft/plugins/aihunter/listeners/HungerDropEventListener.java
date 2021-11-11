package com.bymatej.minecraft.plugins.aihunter.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class HungerDropEventListener implements Listener {

    @EventHandler
    public void onHungerDropEvent(FoodLevelChangeEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
//            feedHunter(player, event);
        }
    }

    private void feedHunter(Player player, FoodLevelChangeEvent event) {
//        HunterData currentHunter = null;
//        try {
//            currentHunter = getCurrentHunter();
//        } catch (HunterException e) {
//            log(SEVERE, "There was no hunter found on the server.");
//        }
//
//        if (currentHunter == null) {
//            return;
//        }
//
//        if (player.getName().equalsIgnoreCase(currentHunter.getName())) {
//            if (event.getFoodLevel() <= 6) {
//                event.setFoodLevel(getPluginReference().getConfig().getInt("hunter_armed_food_regen_level", 20));
//            }
//        }
    }

}
