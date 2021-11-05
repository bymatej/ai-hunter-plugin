package com.bymatej.minecraft.plugins.aihunter.events;

import com.bymatej.minecraft.plugins.aihunter.utils.HunterStatus;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class HunterToggleEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean isCancelled;
    private HunterStatus status;

    public HunterToggleEvent(Player aiHunter, HunterStatus status) {
        super(aiHunter);
        this.status = status;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean isCancelled) {
        this.isCancelled = isCancelled;
    }

    public HunterStatus getStatus() {
        return status;
    }

    public void setStatus(HunterStatus status) {
        this.status = status;
    }
}
