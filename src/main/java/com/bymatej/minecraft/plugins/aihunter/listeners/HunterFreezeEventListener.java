package com.bymatej.minecraft.plugins.aihunter.listeners;

import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.bymatej.minecraft.plugins.aihunter.events.HunterFreezeEvent;

import net.citizensnpcs.api.npc.NPC;

import static com.bymatej.minecraft.plugins.aihunter.utils.LoggingUtils.log;
import static org.bukkit.event.player.PlayerTeleportEvent.TeleportCause.PLUGIN;

public class HunterFreezeEventListener implements Listener {

    private final boolean debug = false;

    @EventHandler
    public void onHunterFreezeEvent(HunterFreezeEvent event) {
        if (event != null && event.getHunter() != null && event.getHunter().isSpawned()) {
            NPC hunter = event.getHunter();
            hunter.teleport(event.getFreezeLocation(), PLUGIN);

            Entity eventEntity = event.getEntity();
            eventEntity.setInvulnerable(true);
        } else {
            if (debug) {
                printDebuggingInfo(event);
            }
        }
    }

    private void printDebuggingInfo(HunterFreezeEvent event) {
        log("No event, no hunter, or hunter not spawned!");
        log("Event: " + event);
        if (event != null) {
            log("Hunter: " + event.getHunter());
            if (event.getHunter() != null) {
                log("Hunter name: " + event.getHunter().getName());
                log("Hunter spawned: " + event.getHunter().isSpawned());
            }
        }
    }

}
