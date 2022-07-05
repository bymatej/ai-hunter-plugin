package com.bymatej.minecraft.plugins.aihunter.actions;

import java.util.Random;

import com.bymatej.minecraft.plugins.aihunter.traits.HunterTrait;

import net.citizensnpcs.api.ai.Navigator;
import net.citizensnpcs.api.ai.StuckAction;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.trait.Trait;

public class HunterStuckAction implements StuckAction {

    Random random = new Random();

    @Override
    public boolean run(NPC npc, Navigator navigator) {
        for (Trait trait : npc.getTraits()) {
            if (trait instanceof HunterTrait) {
                HunterTrait hunterTrait = (HunterTrait) trait;
                if (hunterTrait.getTarget().getWorld().getUID().equals(hunterTrait.getLivingEntity().getWorld().getUID())) {
                    if (hunterTrait.getLivingEntity().getLocation().distance(hunterTrait.getTarget().getLocation()) > 200) {
                        hunterTrait.teleportToAvailableSlot();
                    } else {
                        hunterTrait.setTeleportTimer((random.nextInt(30) + 30) * 20);
                    }
                } else {
                    hunterTrait.updateTargetOtherWorldLocation();
                    hunterTrait.setDimensionFollow(5 * 20);
                }
            }
        }
        return true;
    }
}
