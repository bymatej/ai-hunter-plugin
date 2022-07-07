package com.bymatej.minecraft.plugins.aihunter.listeners;

import java.util.Optional;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import com.bymatej.minecraft.plugins.aihunter.events.HunterToggleEvent;

import net.citizensnpcs.api.npc.NPC;

import static com.bymatej.minecraft.plugins.aihunter.AiHunterPlugin.getPluginReference;
import static com.bymatej.minecraft.plugins.aihunter.utils.HunterStatus.OFF;
import static org.bukkit.Bukkit.getPluginManager;

public class EntityDeathEventListener implements Listener {

    @EventHandler
    public void onEntityDeathEvent(EntityDeathEvent event) {
        removeHunterIfKilledByPlayer(event); // todo do this only if set so in the config
    }

    private void removeHunterIfKilledByPlayer(EntityDeathEvent event) {
        Optional<NPC> hunterOptional = getPluginReference().getAiHunters().stream()
                                                           .filter(h -> h.getName().equalsIgnoreCase(event.getEntity().getName()))
                                                           .findFirst();

        if (hunterOptional.isPresent() && event.getEntity().getKiller() != null) {
            HunterToggleEvent hunterToggleEvent = new HunterToggleEvent(hunterOptional.get().getName(), 0, OFF, event.getEntity().getKiller());
            getPluginManager().callEvent(hunterToggleEvent);
        }
    }

}
