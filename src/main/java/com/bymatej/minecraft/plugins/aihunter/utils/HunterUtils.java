package com.bymatej.minecraft.plugins.aihunter.utils;

import java.util.Date;

import org.bukkit.entity.Player;

import com.bymatej.minecraft.plugins.aihunter.data.hunter.HunterData;
import com.bymatej.minecraft.plugins.aihunter.entities.hunter.Hunter;
import com.bymatej.minecraft.plugins.aihunter.exceptions.HunterException;

import static com.bymatej.minecraft.plugins.aihunter.data.hunter.HunterConverter.entityToData;
import static com.bymatej.minecraft.plugins.aihunter.utils.CommonUtils.log;
import static com.bymatej.minecraft.plugins.aihunter.utils.DbUtils.deleteHunter;
import static com.bymatej.minecraft.plugins.aihunter.utils.DbUtils.getHunter;
import static com.bymatej.minecraft.plugins.aihunter.utils.DbUtils.getHunterByName;
import static java.util.logging.Level.SEVERE;
import static org.bukkit.Bukkit.getOnlinePlayers;

public class HunterUtils {

    public static HunterData getCurrentHunter() throws HunterException {
        Hunter hunter = getHunter();
        if (hunter != null) {
            return entityToData(hunter);
        }

        return getCurrentHunterWithSafetyNet();
    }

    public static void removeCurrentHunter() {
        try {
            deleteHunter(getCurrentHunter());
        } catch (HunterException e) {
            log(SEVERE, "There was no hunter found on the server. Nothing was deleted", e);
        }
    }

    public static HunterData getHunterDataForPlayer(Player player) {
        return getHunterDataForPlayer(player, null, null, null);
    }

    public static HunterData getHunterDataForPlayer(Player player, Integer numberOfTimesDied, Date huntStartTime, Integer databaseId) {
        HunterData hunterData = new HunterData();
        hunterData.setName(player.getName());
        hunterData.setDeathLocationX(player.getLocation().getX());
        hunterData.setDeathLocationY(player.getLocation().getY());
        hunterData.setDeathLocationZ(player.getLocation().getZ());
        if (numberOfTimesDied != null) {
            hunterData.setNumberOfTimesDied(numberOfTimesDied);
        }
        if (huntStartTime != null) {
            hunterData.setHuntStarTime(huntStartTime);
        }
        if (databaseId != null) {
            hunterData.setId(databaseId);
        }

        return hunterData;
    }

    private static HunterData getCurrentHunterWithSafetyNet() throws HunterException {
        HunterData hunterData = null;
        for (Player p : getOnlinePlayers()) {
            Hunter hunter = getHunterByName(getHunterDataForPlayer(p));
            if (hunter != null) {
                hunterData = entityToData(hunter);
                break;
            }
        }

        if (hunterData != null) {
            return hunterData;
        } else {
            throw new HunterException();
        }
    }

}
