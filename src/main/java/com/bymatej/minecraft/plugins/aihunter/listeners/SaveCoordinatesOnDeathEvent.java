package com.bymatej.minecraft.plugins.aihunter.listeners;

import com.bymatej.minecraft.plugins.aihunter.data.hunter.HunterData;
import org.bukkit.Color;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.Date;

import static com.bymatej.minecraft.plugins.aihunter.utils.CommonUtils.log;
import static com.bymatej.minecraft.plugins.aihunter.utils.DbUtils.updateHunterCoordinates;

public class SaveCoordinatesOnDeathEvent implements Listener {

    @EventHandler
    public void onSaveCoordinatesOnDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        double x = player.getLocation().getX();
        double y = player.getLocation().getY();
        double z = player.getLocation().getZ();
        // todo: store world too

        String message = "Player " + player.getName() + " died at coordinates " + x + "/" + y + "/" + z;
        log(message);
        player.sendMessage(Color.RED + message);

        HunterData hunterData = new HunterData();
        hunterData.setName(player.getName());
        hunterData.setDeathLocationX(player.getLocation().getX());
        hunterData.setDeathLocationY(player.getLocation().getY());
        hunterData.setDeathLocationZ(player.getLocation().getZ());
        hunterData.setNumberOfTimesDied(0);
        hunterData.setHuntStarTime(new Date());

//        updateHunterCoordinates(hunterData);

        updateHunterCoordinates(hunterData); // todo: check if this is needed
    }

}
