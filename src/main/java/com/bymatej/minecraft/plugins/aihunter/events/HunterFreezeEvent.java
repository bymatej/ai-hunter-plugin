package com.bymatej.minecraft.plugins.aihunter.events;

import org.bukkit.Location;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;

import net.citizensnpcs.api.npc.NPC;

public class HunterFreezeEvent extends EntityEvent implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    private boolean cancelled = false;

    private NPC hunter;

    private Location freezeLocation;

    public HunterFreezeEvent(NPC hunter, Location freezeLocation) {
        super(hunter.getEntity());
        this.hunter = hunter;
        this.freezeLocation = freezeLocation;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public NPC getHunter() {
        return hunter;
    }

    public void setHunter(NPC hunter) {
        this.hunter = hunter;
    }

    public Location getFreezeLocation() {
        return freezeLocation;
    }

    public void setFreezeLocation(Location freezeLocation) {
        this.freezeLocation = freezeLocation;
    }
}
