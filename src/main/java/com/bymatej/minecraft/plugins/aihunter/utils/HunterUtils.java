package com.bymatej.minecraft.plugins.aihunter.utils;

import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.npc.CitizensNPC;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import static com.bymatej.minecraft.plugins.aihunter.AiHunterPlugin.getPluginReference;
import static java.util.Objects.requireNonNull;
import static java.util.UUID.randomUUID;
import static net.citizensnpcs.api.CitizensAPI.getNPCRegistry;
import static net.citizensnpcs.npc.EntityControllers.createForType;
import static org.bukkit.Material.valueOf;
import static org.bukkit.attribute.Attribute.GENERIC_MAX_HEALTH;

public class HunterUtils {

//    public static HunterData getCurrentHunter() throws HunterException {
//        Hunter hunter = getHunter();
//        if (hunter != null) {
//            return entityToData(hunter);
//        }
//
//        return null;
//    }

    public static void removeCurrentHunter() {
//        try {
//            deleteHunter(getCurrentHunter());
//        } catch (HunterException e) {
//            log(SEVERE, "There was no hunter found on the server. Nothing was deleted");
//        }
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
        inventory.setHelmet(new ItemStack(valueOf(config.getString("hunter_helmet", "CARVED_PUMPKIN"))));
        inventory.setChestplate(new ItemStack(valueOf(config.getString("hunter_chestplate", "IRON_CHESTPLATE"))));
        inventory.setLeggings(new ItemStack(valueOf(config.getString("hunter_leggings", "IRON_LEGGINGS"))));
        inventory.setBoots(new ItemStack(valueOf(config.getString("hunter_boots", "GOLDEN_BOOTS"))));
        // Inventory
        ItemStack cobblestone = new ItemStack(valueOf(config.getString("hunter_blocks", "COBBLESTONE")));
        ItemStack cookedBeef = new ItemStack(valueOf(config.getString("hunter_food", "COOKED_BEEF")));
        cobblestone.setAmount(cobblestone.getMaxStackSize());
        cookedBeef.setAmount(cookedBeef.getMaxStackSize());
        inventory.setHeldItemSlot(0);
        inventory.setItem(0, new ItemStack(valueOf(config.getString("hunter_main_hand_item", "IRON_SWORD"))));
        inventory.setItem(1, new ItemStack(valueOf(config.getString("hunter_axe", "STONE_AXE"))));
        inventory.setItem(2, new ItemStack(valueOf(config.getString("hunter_main_pickaxe", "DIAMOND_PICKAXE"))));
        inventory.setItem(3, new ItemStack(valueOf(config.getString("hunter_shovel", "GOLDEN_SHOVEL"))));
        inventory.setItem(4, new ItemStack(valueOf(config.getString("hunter_bucket", "WATER_BUCKET"))));
        inventory.setItem(5, cobblestone);
        inventory.setItem(6, cobblestone);
        inventory.setItem(7, cookedBeef);
        inventory.setItem(8, cookedBeef);
        for (int i = 9; i <= 17; i++) {
            inventory.setItem(i, cobblestone);
        }
        for (int i = 18; i <= 20; i++) {
            inventory.setItem(i, new ItemStack(valueOf(config.getString("hunter_main_hand_item", "IRON_SWORD"))));
        }
        for (int i = 21; i < 23; i++) {
            inventory.setItem(i, cookedBeef);
        }
        for (int i = 23; i < 25; i++) {
            inventory.setItem(i, new ItemStack(valueOf(config.getString("hunter_secondary_pickaxe", "IRON_PICKAXE"))));
        }
        // Hands
        inventory.setItemInMainHand(new ItemStack(valueOf(config.getString("hunter_main_hand_item", "IRON_SWORD"))));
        inventory.setItemInOffHand(new ItemStack(valueOf(config.getString("hunter_off_hand_item", "SHIELD"))));
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
//        try {
//            HunterData currentHunter = getCurrentHunter();
//            return currentHunter != null &&
//                    player != null &&
//                    player.getName().equals(currentHunter.getName());
//        } catch (HunterException e) {
//            log(SEVERE, "Error defining if the player is hunter or not. Assuming it is not.");
//            return false;
//        }
        return false;
    }


    public static void createHunter(String hunterName, int id, Player commandSender) {
        NPC npc = new CitizensNPC(randomUUID(), id, hunterName, createForType(EntityType.PLAYER), getNPCRegistry());
        npc.spawn(commandSender.getLocation());
        npc.data().set(NPC.DEFAULT_PROTECTED_METADATA, false);
        // todo: finish method
    }

}
