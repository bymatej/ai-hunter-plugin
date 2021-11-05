package com.bymatej.minecraft.plugins.aihunter.utils;

import com.bymatej.minecraft.plugins.aihunter.data.hunter.HunterData;
import com.bymatej.minecraft.plugins.aihunter.entities.hunter.Hunter;
import com.bymatej.minecraft.plugins.aihunter.exceptions.HunterException;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.Date;

import static com.bymatej.minecraft.plugins.aihunter.data.hunter.HunterConverter.entityToData;
import static com.bymatej.minecraft.plugins.aihunter.utils.CommonUtils.getPluginReference;
import static com.bymatej.minecraft.plugins.aihunter.utils.CommonUtils.log;
import static com.bymatej.minecraft.plugins.aihunter.utils.DbUtils.*;
import static java.util.Objects.requireNonNull;
import static java.util.logging.Level.SEVERE;
import static org.bukkit.Bukkit.getOnlinePlayers;
import static org.bukkit.Material.*;
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
        // General setup
        hunter.setInvulnerable(config.getBoolean("hunter_armed_invulnerable", false));
        hunter.setSaturation((float) config.getDouble("hunter_armed_saturation", 10));
        hunter.setSaturatedRegenRate(config.getInt("hunter_armed_saturation_regen_rate", 1));
        AttributeInstance maxHealthAttribute = hunter.getAttribute(GENERIC_MAX_HEALTH);
        requireNonNull(maxHealthAttribute).setBaseValue(config.getDouble("hunter_armed_max_health", 500));
        hunter.setHealth(config.getDouble("hunter_armed_health", 500));
        hunter.setFoodLevel(config.getInt("hunter_armed_food_level", 20));
        // Head
        PlayerInventory inventory = hunter.getInventory();
        inventory.setHelmet(new ItemStack(valueOf(config.getString("hunter_armed_helmet", "CARVED_PUMPKIN"))));
        inventory.setChestplate(new ItemStack(valueOf(config.getString("hunter_armed_chestplate", "IRON_CHESTPLATE"))));
        inventory.setLeggings(new ItemStack(valueOf(config.getString("hunter_armed_leggings", "IRON_LEGGINGS"))));
        inventory.setBoots(new ItemStack(valueOf(config.getString("hunter_armed_boots", "GOLDEN_BOOTS"))));
        // Inventory
        ItemStack cobblestone = new ItemStack(COBBLESTONE);
        ItemStack cookedBeef = new ItemStack(COOKED_BEEF);
        cobblestone.setAmount(cobblestone.getMaxStackSize());
        cookedBeef.setAmount(cookedBeef.getMaxStackSize());
        inventory.setHeldItemSlot(0);
        inventory.setItem(0, new ItemStack(IRON_SWORD));
        inventory.setItem(1, new ItemStack(STONE_AXE));
        inventory.setItem(2, new ItemStack(DIAMOND_PICKAXE));
        inventory.setItem(3, new ItemStack(GOLDEN_SHOVEL));
        inventory.setItem(4, new ItemStack(WATER_BUCKET));
        inventory.setItem(5, cobblestone);
        inventory.setItem(6, cobblestone);
        inventory.setItem(7, cookedBeef);
        inventory.setItem(8, cookedBeef);
        for (int i = 9; i <= 17; i++) {
            inventory.setItem(i, cobblestone);
        }
        for (int i = 18; i <= 20; i++) {
            inventory.setItem(i, new ItemStack(IRON_SWORD));
        }
        for (int i = 21; i < 23; i++) {
            inventory.setItem(i, cookedBeef);
        }
        for (int i = 23; i < 25; i++) {
            inventory.setItem(i, new ItemStack(IRON_PICKAXE));
        }
        // Hands
        inventory.setItemInMainHand(new ItemStack(IRON_SWORD));
        inventory.setItemInOffHand(new ItemStack(SHIELD));
        // World
        hunter.getWorld().setPVP(config.getBoolean("hunter_armed_pvp", true));
    }

    public static void disarmHunter(Player hunter) {
        FileConfiguration config = getPluginReference().getConfig();
        // General setup
        hunter.setInvulnerable(config.getBoolean("hunter_disarmed_invulnerable", false));
        hunter.setSaturation((float) config.getDouble("hunter_disarmed_saturation", 0));
        hunter.setSaturatedRegenRate(config.getInt("hunter_disarmed_saturation_regen_rate", 1));
        AttributeInstance maxHealthAttribute = hunter.getAttribute(GENERIC_MAX_HEALTH);
        requireNonNull(maxHealthAttribute).setBaseValue(config.getDouble("hunter_disarmed_max_health", maxHealthAttribute.getDefaultValue()));
        hunter.setHealth(config.getDouble("hunter_disarmed_health", 20));
        hunter.setFoodLevel(config.getInt("hunter_disarmed_food_level", 20));
        // Inventory
        hunter.getInventory().clear();
        // World
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
