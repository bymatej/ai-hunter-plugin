package com.bymatej.minecraft.plugins.aihunter.utils;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.util.Vector;

import com.bymatej.minecraft.plugins.aihunter.actions.HunterStuckAction;
import com.bymatej.minecraft.plugins.aihunter.listeners.HunterToggleEventListener;
import com.bymatej.minecraft.plugins.aihunter.loadout.HunterLoadout;
import com.bymatej.minecraft.plugins.aihunter.traits.HunterFollow;
import com.bymatej.minecraft.plugins.aihunter.traits.HunterTrait;

import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.npc.CitizensNPC;
import net.citizensnpcs.trait.SkinTrait;

import static com.bymatej.minecraft.plugins.aihunter.AiHunterPlugin.getPluginReference;
import static com.bymatej.minecraft.plugins.aihunter.utils.LoggingUtils.log;
import static java.lang.String.format;
import static java.lang.System.currentTimeMillis;
import static java.lang.Thread.sleep;
import static java.util.Objects.requireNonNull;
import static java.util.UUID.randomUUID;
import static java.util.logging.Level.SEVERE;
import static net.citizensnpcs.api.CitizensAPI.getNPCRegistry;
import static net.citizensnpcs.npc.EntityControllers.createForType;
import static org.bukkit.Location.normalizeYaw;
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

    public static void armHunter(NPC npc) {
        FileConfiguration config = getPluginReference().getConfig();
        // General setup
        Player hunter = (Player) npc.getEntity();
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

    /**
     * Possibly the weirdest function I've ever written
     *
     * @param npc
     */
    public static void freezeHunter(NPC npc) {
        Entity entity = npc.getEntity();
        entity.setInvulnerable(false);
        FileConfiguration config = getPluginReference().getConfig();
        boolean isHunterInvulnerable = config.getBoolean("hunter_armed_invulnerable", false);

        // Todo: write more efficient logic that executes a piece of code every X/4 seconds and prints the message every second
        Location initialLocation = entity.getLocation();
        int movementDelay = config.getInt("hunter_move_delay_seconds", 10);
        long start = currentTimeMillis(); // start time is current time
        long finish = start + (1000L * movementDelay); // end time is start time + time in seconds configured in config.yml multiplied by 1000 to get milliseconds
        int secondDivider = 4; // quarter
        int secondControl = 1;
        entity.sendMessage(format("You will be teleported back to to %s/%s/%s for %s seconds.",
                                  initialLocation.getX(),
                                  initialLocation.getY(),
                                  initialLocation.getZ(),
                                  movementDelay));
        while (currentTimeMillis() < finish) {
            try {
                sleep(1000 / secondDivider); // wait quarter of a second
            } catch (InterruptedException e) {
                log(SEVERE, "Failed to use Thread.sleep(500);");
            }

            // Teleporting
            entity.setInvulnerable(true);
            entity.teleport(initialLocation);

            // Printing message
            if (secondControl <= secondDivider + 1) { // +1 because we "used" one quarter, so we need to add it back
                secondControl++;
            }

            if (secondControl == secondDivider + 1) {
                int secLeft = --movementDelay;
                entity.sendMessage(format("You need to wait for %s more seconds before you can move.", secLeft));
                getPluginReference().getServer().broadcastMessage((format("You need to wait for %s more seconds before you can move.", secLeft)));
                secondControl = 1;
            }
        }

        entity.setInvulnerable(isHunterInvulnerable);
        entity.sendMessage("You can hunt now!");
    }

    public static void freezeHunter2(NPC npc) {
        Entity entity = npc.getEntity();
        entity.setInvulnerable(false);
        FileConfiguration config = getPluginReference().getConfig();
        boolean isHunterInvulnerable = config.getBoolean("hunter_armed_invulnerable", false);

        Location initialLocation = entity.getLocation();
        int movementDelay = config.getInt("hunter_move_delay_seconds", 10);
        entity.sendMessage(format("You will be teleported back to to %s/%s/%s for %s seconds.",
                                  initialLocation.getX(),
                                  initialLocation.getY(),
                                  initialLocation.getZ(),
                                  movementDelay));

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            int counter = 1;

            @Override
            public void run() {
                // Teleporting
                entity.setInvulnerable(true);
                entity.teleport(initialLocation);

                // Messages
                int secLeft = movementDelay;
                secLeft--;
                entity.sendMessage(format("You need to wait for %s more seconds before you can move.", secLeft));
                getPluginReference().getServer().broadcastMessage((format("The hunter will start hunting you in %s seconds.", secLeft)));

                // Flow control for timer
                counter++;
                if (counter > movementDelay) {
                    timer.cancel();
                }
            }
        }, 0L, movementDelay * 1000L);

        entity.setInvulnerable(isHunterInvulnerable);
        entity.sendMessage("You can hunt now!");
    }

    public static void freezeHunter3(NPC npc, Player commandSender) {
        Entity entity = npc.getEntity();
        entity.setInvulnerable(false);
        FileConfiguration config = getPluginReference().getConfig();
        boolean isHunterInvulnerable = config.getBoolean("hunter_armed_invulnerable", false);

        Location initialLocation = commandSender.getLocation();
        int movementDelay = config.getInt("hunter_move_delay_seconds", 10);
        entity.sendMessage(format("You will be teleported back to to %s/%s/%s for %s seconds.",
                                  initialLocation.getX(),
                                  initialLocation.getY(),
                                  initialLocation.getZ(),
                                  movementDelay));
        getPluginReference().getServer().broadcastMessage(format("Hunter will be repeatedly teleported back to to %s/%s/%s for %s seconds.",
                                                                 initialLocation.getX(),
                                                                 initialLocation.getY(),
                                                                 initialLocation.getZ(),
                                                                 movementDelay));

        long lastSec = 0;
        long initSec = System.currentTimeMillis() / 1000;
        long sec = 0;
        while (true) {
            if (sec == 0) {
                sec = initSec;
            } else {
                sec = System.currentTimeMillis() / 1000;
            }

            if (sec != lastSec) {
                // Teleporting
                entity.setInvulnerable(true);
                entity.teleport(initialLocation);

                // Messages
                long secLeft = 0;
                if (lastSec == 0) {
                    secLeft = movementDelay;
                } else {
                    secLeft = movementDelay - (lastSec - initSec);
                }
                entity.sendMessage(format("You need to wait for %s more seconds before you can move.", secLeft));
                getPluginReference().getServer().broadcastMessage((format("The hunter will start hunting you in %s seconds.", secLeft)));
                Bukkit.getLogger().log(Level.INFO, "Hunter sleeps for " + secLeft + " more seconds");
                lastSec = sec;
            }

            if ((lastSec - initSec) == movementDelay) { //todo: move to while instead of while(true)
                break;
            }
        }

        entity.setInvulnerable(isHunterInvulnerable);
        entity.sendMessage("You can hunt now!");
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

    public static void createHunter(String hunterName, int id, Player commandSender) {//todo: finish up this method and add loadout stuff to the calling method before for loop and pass it on here
        // Create the hunter
        NPC npc = new CitizensNPC(randomUUID(), id, hunterName, createForType(EntityType.PLAYER), getNPCRegistry());
        npc.spawn(commandSender.getLocation());
        npc.data().set(NPC.DEFAULT_PROTECTED_METADATA, false);
        npc.getNavigator().getLocalParameters()
           .attackRange(6) // was 10, now is 6
           .baseSpeed(1.4F)  // was 1.6 now is 1.4
           .straightLineTargetingDistance(200)  // was 100 now is 200
           .stuckAction(new HunterStuckAction())
           .range(60);  // was 40, now is 60

        // Arm the hunter
        armHunter(npc);

        HunterFollow followTrait = new HunterFollow();
        followTrait.linkToNPC(npc);
        followTrait.run();
        followTrait.toggle(commandSender, false);
        //
        npc.addTrait(followTrait);

        HunterTrait hunterTrait = new HunterTrait(npc, new HunterLoadout(getPluginReference())); // todo: remove newLoadout

        npc.addTrait(hunterTrait);

        SkinTrait skinTrait = npc.getOrAddTrait(SkinTrait.class);
        skinTrait.setSkinName(hunterName);

        HunterToggleEventListener.aiHunters.add(npc);

        // Freeze the hunter
        //freezeHunter(npc);//todo
        //freezeHunter2(npc);//todo
        freezeHunter3(npc, commandSender);//todo
    }

    public static boolean isLookingTowards(Location myLoc, Location theirLoc, float yawLimit, float pitchLimit) {
        Vector rel = theirLoc.toVector().subtract(myLoc.toVector()).normalize();
        float yaw = normalizeYaw(myLoc.getYaw());
        float yawHelp = getYaw(rel);
        if (!(Math.abs(yawHelp - yaw) < yawLimit ||
              Math.abs(yawHelp + 360 - yaw) < yawLimit ||
              Math.abs(yaw + 360 - yawHelp) < yawLimit)) {
            return false;
        }
        float pitch = myLoc.getPitch();
        float pitchHelp = getPitch(rel);
        return Math.abs(pitchHelp - pitch) < yawLimit;
    }

    private static float getPitch(Vector vector) {
        double dx = vector.getX();
        double dy = vector.getY();
        double dz = vector.getZ();
        double forward = Math.sqrt((dx * dx) + (dz * dz));
        double pitch = Math.atan2(dy, forward) * (180.0 / Math.PI);
        return (float) pitch;
    }

    private static float getYaw(Vector vector) {
        double dx = vector.getX();
        double dz = vector.getZ();
        double yaw = 0;
        // Set yaw
        if (dx != 0) {
            // Set yaw start value based on dx
            if (dx < 0) {
                yaw = 1.5 * Math.PI;
            } else {
                yaw = 0.5 * Math.PI;
            }
            yaw -= Math.atan(dz / dx); // or atan2?
        } else if (dz < 0) {
            yaw = Math.PI;
        }
        return (float) (-yaw * (180.0 / Math.PI));
    }

}
