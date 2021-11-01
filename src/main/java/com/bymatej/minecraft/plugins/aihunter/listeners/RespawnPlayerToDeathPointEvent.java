package com.bymatej.minecraft.plugins.aihunter.listeners;

import com.bymatej.minecraft.plugins.aihunter.data.hunter.HunterData;
import com.bymatej.minecraft.plugins.aihunter.entities.hunter.Hunter;
import com.bymatej.minecraft.plugins.aihunter.utils.DbUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.Date;

import static com.bymatej.minecraft.plugins.aihunter.utils.CommonUtils.log;

public class RespawnPlayerToDeathPointEvent implements Listener {

    @EventHandler
    public void onRespawnPlayerToDeathPoint(PlayerRespawnEvent event) {
        // todo: respawn to death point
        event.getPlayer().sendMessage("You respawned, dude!");

        Player aiHunter = event.getPlayer();
        HunterData hunterData = new HunterData();
        hunterData.setName(aiHunter.getName());
        // todo: remove below, as it might not be needed
        hunterData.setDeathLocationX(aiHunter.getLocation().getX());
        hunterData.setDeathLocationY(aiHunter.getLocation().getY());
        hunterData.setDeathLocationZ(aiHunter.getLocation().getZ());
        hunterData.setNumberOfTimesDied(0);
        hunterData.setHuntStarTime(new Date());

        Hunter hunter = DbUtils.getHunterByName(hunterData);

        if (hunter == null) {
            // if the event triggerer (player) is not our hunter, exit
            return;
        }

        // Todo: find fix for sending command, as player is reported as not existent while dead
//        ConsoleCommandSender console = getServer().getConsoleSender();
//        String command = "aihunter " + event.getPlayer().getName() + " on";
//        log("COMMAND WAS: " + command);
//        try {
//            Thread.sleep(4000L);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        dispatchCommand(console, command);

//        DbUtils.
//        event.setRespawnLocation();



        // todo: store world to DB along with coords, for now hack it with aihunter var (store Location object instead of coords)
        Location location = new Location(aiHunter.getWorld(), hunterData.getDeathLocationX(), hunterData.getDeathLocationY(), hunterData.getDeathLocationZ());
        event.setRespawnLocation(location);

        /* todo: new flow might be as follows:
        - player dies
        - SaveCoordinatesOnDeathEventHandler is triggered
        - we store coordinates, world and we define a new boolean flag hasDied
        - we set a flag to "has died
        - after player respawns wait until the player is visible, then execute AI hunter on command again and set has died to false
         */

    }

}
