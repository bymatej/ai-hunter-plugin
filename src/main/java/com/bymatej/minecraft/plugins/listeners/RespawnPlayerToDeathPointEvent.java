package com.bymatej.minecraft.plugins.listeners;

import com.bymatej.minecraft.plugins.utils.CommonUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

import static com.bymatej.minecraft.plugins.utils.CommonUtils.log;
import static org.bukkit.Bukkit.dispatchCommand;
import static org.bukkit.Bukkit.getServer;

public class RespawnPlayerToDeathPointEvent implements Listener {

    @EventHandler
    public void onRespawnPlayerToDeathPoint(PlayerRespawnEvent event) {
        // todo: respawn to death point
        // store the coordinates of the deathpoint on SaveCoordinatesOnDeathEvent
        event.getPlayer().sendMessage("You respawned, dude!");

        ConsoleCommandSender console = getServer().getConsoleSender();
        String command = "aihunter " + event.getPlayer().getName() + " on";
        log("COMMAND WAS: " + command);
        try {
            Thread.sleep(4000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        dispatchCommand(console, command);
    }

}
