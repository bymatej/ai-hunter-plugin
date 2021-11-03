package com.bymatej.minecraft.plugins.aihunter.utils;

import com.bymatej.minecraft.plugins.aihunter.data.hunter.HunterData;
import com.bymatej.minecraft.plugins.aihunter.entities.hunter.Hunter;
import com.bymatej.minecraft.plugins.aihunter.exceptions.HunterException;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Date;

import static com.bymatej.minecraft.plugins.aihunter.data.hunter.HunterConverter.entityToData;
import static com.bymatej.minecraft.plugins.aihunter.utils.CommonUtils.getPluginReference;
import static com.bymatej.minecraft.plugins.aihunter.utils.CommonUtils.log;
import static com.bymatej.minecraft.plugins.aihunter.utils.DbUtils.*;
import static java.util.Objects.requireNonNull;
import static java.util.logging.Level.SEVERE;
import static org.bukkit.Bukkit.getOnlinePlayers;
import static org.bukkit.Material.valueOf;
import static org.bukkit.attribute.Attribute.GENERIC_MAX_HEALTH;

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
            log(SEVERE, "There was no hunter found on the server. Nothing was deleted");
        }
    }

    public static HunterData getHunterDataForPlayer(Player player) {
        return getHunterDataForPlayer(player, null, null, null);
    }

    public static HunterData getHunterDataForPlayer(Player player, Integer numberOfTimesDied, Date huntStartTime, Integer databaseId) {
        HunterData hunterData = new HunterData();
        hunterData.setName(player.getName());

        if (numberOfTimesDied != null) {
            hunterData.setNumberOfTimesDied(numberOfTimesDied);
        }

        if (databaseId != null) {
            hunterData.setId(databaseId);
        }

        return hunterData;
    }

    public static void armHunter(Player hunter) {
        FileConfiguration config = getPluginReference().getConfig();
        hunter.setInvulnerable(config.getBoolean("hunter_armed_invulnerable", false));
        hunter.setSaturation((float) config.getDouble("hunter_armed_saturation", 10));
        hunter.setSaturatedRegenRate(config.getInt("hunter_armed_saturation_regen_rate", 1));
        AttributeInstance maxHealthAttribute = hunter.getAttribute(GENERIC_MAX_HEALTH);
        requireNonNull(maxHealthAttribute).setBaseValue(config.getDouble("hunter_armed_max_health", 500));
        hunter.setHealth(config.getDouble("hunter_armed_health", 500));
        hunter.setFoodLevel(config.getInt("hunter_armed_food_level", 20));
        hunter.getInventory().setHelmet(new ItemStack(valueOf(config.getString("hunter_armed_helmet", "CARVED_PUMPKIN"))));
        hunter.getInventory().setChestplate(new ItemStack(valueOf(config.getString("hunter_armed_chestplate", "IRON_CHESTPLATE"))));
        hunter.getInventory().setLeggings(new ItemStack(valueOf(config.getString("hunter_armed_leggings", "IRON_LEGGINGS"))));
        hunter.getInventory().setBoots(new ItemStack(valueOf(config.getString("hunter_armed_boots", "GOLDEN_BOOTS"))));
        hunter.getWorld().setPVP(config.getBoolean("hunter_armed_pvp", true));
    }

    public static void disarmHunter(Player hunter) {
        FileConfiguration config = getPluginReference().getConfig();
        hunter.setInvulnerable(config.getBoolean("hunter_disarmed_invulnerable", false));
        hunter.setSaturation((float) config.getDouble("hunter_disarmed_saturation", 0));
        hunter.setSaturatedRegenRate(config.getInt("hunter_disarmed_saturation_regen_rate", 1));
        AttributeInstance maxHealthAttribute = hunter.getAttribute(GENERIC_MAX_HEALTH);
        requireNonNull(maxHealthAttribute).setBaseValue(config.getDouble("hunter_disarmed_max_health", maxHealthAttribute.getDefaultValue()));
        hunter.setHealth(config.getDouble("hunter_disarmed_health", 20));
        hunter.setFoodLevel(config.getInt("hunter_disarmed_food_level", 20));
        hunter.getInventory().setHelmet(new ItemStack(valueOf(config.getString("hunter_disarmed_helmet", "AIR"))));
        hunter.getInventory().setChestplate(new ItemStack(valueOf(config.getString("hunter_disarmed_chestplate", "AIR"))));
        hunter.getInventory().setLeggings(new ItemStack(valueOf(config.getString("hunter_disarmed_leggings", "AIR"))));
        hunter.getInventory().setBoots(new ItemStack(valueOf(config.getString("hunter_disarmed_boots", "AIR"))));
        hunter.getWorld().setPVP(config.getBoolean("hunter_disarmed_pvp", true));
    }

    public static boolean isPlayerHunter(Player player) {
        try {
            HunterData currentHunter = getCurrentHunter();
            return currentHunter != null &&
                    player != null &&
                    player.getName().equals(currentHunter.getName());
        } catch (HunterException e) {
            log(SEVERE, "Error defining if the player is hunter or not. Assuming it is not.");
            return false;
        }
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
