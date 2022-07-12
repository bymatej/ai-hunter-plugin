package com.bymatej.minecraft.plugins.aihunter.utils;

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
import com.bymatej.minecraft.plugins.aihunter.events.HunterFreezeEvent;
import com.bymatej.minecraft.plugins.aihunter.loadout.HunterLoadout;
import com.bymatej.minecraft.plugins.aihunter.traits.HunterFollow;
import com.bymatej.minecraft.plugins.aihunter.traits.HunterTrait;

import net.citizensnpcs.api.ai.StuckAction;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.npc.CitizensNPC;
import net.citizensnpcs.trait.SkinTrait;

import static com.bymatej.minecraft.plugins.aihunter.AiHunterPlugin.getPluginReference;
import static java.lang.String.format;
import static java.util.Objects.requireNonNull;
import static java.util.UUID.randomUUID;
import static net.citizensnpcs.api.CitizensAPI.getNPCRegistry;
import static net.citizensnpcs.npc.EntityControllers.createForType;
import static org.bukkit.Bukkit.getPluginManager;
import static org.bukkit.ChatColor.AQUA;
import static org.bukkit.ChatColor.DARK_RED;
import static org.bukkit.Location.normalizeYaw;
import static org.bukkit.Material.valueOf;
import static org.bukkit.attribute.Attribute.GENERIC_MAX_HEALTH;

public class HunterUtils {

    private HunterUtils() {}

    public static void armHunter(NPC npc) {
        FileConfiguration config = getPluginReference().getConfig();
        // General setup
        Player hunter = (Player) npc.getEntity();

        if (hunter == null) {
            return;
        }

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

    public static void disarmHunter(NPC npc) {
        Player hunter = (Player) npc.getEntity();
        if (hunter == null) {
            return;
        }
        FileConfiguration config = getPluginReference().getConfig();
        // General setups
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

    public static void freezeHunters(Player commandSender) {
        FileConfiguration config = getPluginReference().getConfig();
        Location initialLocation = commandSender.getLocation();
        int movementDelay = config.getInt("hunter_move_delay_seconds", 10);
        boolean isHunterInvulnerable = config.getBoolean("hunter_armed_invulnerable", false);
        CountdownTimer countdownTimer = new CountdownTimer(getPluginReference(), movementDelay,
                                                           () -> getPluginReference()
                                                                   .getServer()
                                                                   .broadcastMessage(DARK_RED + "[HUNTERS ARE FROZEN] " + AQUA + format(
                                                                     "Hunters will be repeatedly teleported back to to %s/%s/%s for %s seconds.",
                                                                     initialLocation.getBlockX(),
                                                                     initialLocation.getBlockY(),
                                                                     initialLocation.getBlockZ(),
                                                                     movementDelay)),
                                                           () -> {
                                                               getPluginReference()
                                                                 .getServer()
                                                                 .broadcastMessage(DARK_RED + "[HUNT STARTED] " + AQUA + "The hunters are unfrozen!");

                                                               getPluginReference().getAiHunters().forEach(npc -> {
                                                                   Entity entity = npc.getEntity();
                                                                   entity.setInvulnerable(isHunterInvulnerable);

                                                                   HunterTrait hunterTrait = new HunterTrait(npc, new HunterLoadout(getPluginReference())); // todo: remove newLoadout
                                                                   npc.addTrait(hunterTrait);
                                                               });
                                                           },
                                                           timer -> {
                                                               getPluginReference()
                                                                 .getServer()
                                                                 .broadcastMessage(DARK_RED + "[COUNTDOWN] " + AQUA + format(
                                                                   "The hunter will start hunting you in %s seconds.", timer.getSecondsLeft()));

                                                               getPluginReference().getAiHunters().forEach(npc -> {
                                                                   HunterFreezeEvent hunterFreezeEvent = new HunterFreezeEvent(npc, initialLocation);
                                                                   getPluginManager().callEvent(hunterFreezeEvent);
                                                               });
                                                           });
        countdownTimer.scheduleTimer();
    }

    public static void createHunter(String hunterName, int id, Player commandSender) {//todo: finish up this method and add loadout stuff to the calling method before for loop and pass it on here
        // Create the hunter
        NPC npc = new CitizensNPC(randomUUID(), id, hunterName, createForType(EntityType.PLAYER), getNPCRegistry());
        npc.spawn(commandSender.getLocation());
        npc.data().set(NPC.DEFAULT_PROTECTED_METADATA, false);
        npc.getNavigator().getLocalParameters()
           .attackRange(getAttackRange())
           .baseSpeed(getBaseSpeed())
           .straightLineTargetingDistance(getStraightLineTargetingDistance())
           .stuckAction(getStuckAction())
           .range(getRange());

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

        getPluginReference().getAiHunters().add(npc);
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

    /**
     * number of blocks when hunter starts to attack
     * javadoc: https://jd.citizensnpcs.co/net/citizensnpcs/api/ai/NavigatorParameters.html
     *
     * @return attack range
     */
    private static int getAttackRange() {
        // todo: make configurable
        return 3; // default 10
    }

    /**
     * entity-dependant, might not make sense to use this at all
     * Maybe remove this so that the hunter is not faster than player
     *
     * @return base speed of an entity
     */
    private static float getBaseSpeed() {
        // todo: make configurable
        return 1.5F; // default 1.6F
    }

    /**
     * number of blocks when AI goes in a straight line to find the player
     *
     * @return straight line targeting distance
     */
    private static int getStraightLineTargetingDistance() {
        // todo: make configurable
        return 100; // default 100
    }

    /**
     * if npc gets stuck this happens (for example, teleporting)
     *
     * @return stuck action
     */
    private static StuckAction getStuckAction() {
        return new HunterStuckAction();
    }

    /**
     * range in blocks for NPC to "see" until it gives up
     *
     * @return range
     */
    private static int getRange() {
        // todo: make configurable
        return 40; // default 40
    }

}
