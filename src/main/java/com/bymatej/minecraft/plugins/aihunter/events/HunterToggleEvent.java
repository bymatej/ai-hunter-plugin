package com.bymatej.minecraft.plugins.aihunter.events;

import com.bymatej.minecraft.plugins.aihunter.utils.HunterStatus;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

import static com.bymatej.minecraft.plugins.aihunter.utils.HunterStatus.OFF;

public class HunterToggleEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean isCancelled;
    private String hunterName;
    private int numberOfHunters;
    private HunterStatus status;

    public HunterToggleEvent(String hunterName, int numberOfHunters, HunterStatus status, Player player) {
        super(player);
        this.hunterName = hunterName;
        this.numberOfHunters = numberOfHunters;
        this.status = status;
    }

    public HunterToggleEvent(HunterStatus status, Player player) {
        super(player);
        this.status = status;
        if (OFF.equals(status)) {
            hunterName = null;
            numberOfHunters = 0;
        } else {
            this.hunterName = "CourageTheCowardlyDog";
            this.numberOfHunters = 1;
        }
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


    public String getHunterName() {
        return hunterName;
    }

    public void setHunterName(String hunterName) {
        this.hunterName = hunterName;
    }

    public int getNumberOfHunters() {
        return numberOfHunters;
    }

    public void setNumberOfHunters(int numberOfHunters) {
        this.numberOfHunters = numberOfHunters;
    }

    public HunterStatus getStatus() {
        return status;
    }

    public void setStatus(HunterStatus status) {
        this.status = status;
    }
}
