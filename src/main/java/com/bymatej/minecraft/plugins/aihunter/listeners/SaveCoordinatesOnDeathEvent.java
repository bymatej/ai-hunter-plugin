package com.bymatej.minecraft.plugins.aihunter.listeners;

import com.bymatej.minecraft.plugins.aihunter.data.HunterData;
import org.bukkit.Color;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.Date;

import static com.bymatej.minecraft.plugins.aihunter.utils.CommonUtils.log;
import static com.bymatej.minecraft.plugins.aihunter.utils.DbUtils.storeHunterData;
import static com.bymatej.minecraft.plugins.aihunter.utils.DbUtils.updateHunterCoordinates;

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

        HunterData hunterData = new HunterData(player.getName(),
                player.getLocation().getX(),
                player.getLocation().getY(),
                player.getLocation().getZ(),
                0,
                new Date()
        );
        updateHunterCoordinates(hunterData);
    }

}
