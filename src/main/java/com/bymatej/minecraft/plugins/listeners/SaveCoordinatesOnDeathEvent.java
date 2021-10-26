package com.bymatej.minecraft.plugins.listeners;

import com.bymatej.minecraft.plugins.utils.CommonUtils;
import org.bukkit.Color;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import static com.bymatej.minecraft.plugins.utils.CommonUtils.log;

public class SaveCoordinatesOnDeathEvent implements Listener {

    @EventHandler
    public void onSaveCoordinatesOnDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        double x = player.getLocation().getX();
        double y = player.getLocation().getY();
        double z = player.getLocation().getZ();

        String message = "Player " + player.getName() + " died at coordinates " + x + "/" + y + "/" + z;
        log(message);
        player.sendMessage(Color.RED + message);
    }

}
