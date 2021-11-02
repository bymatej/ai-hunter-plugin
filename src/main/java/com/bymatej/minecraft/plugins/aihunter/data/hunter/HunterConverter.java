package com.bymatej.minecraft.plugins.aihunter.data.hunter;

import com.bymatej.minecraft.plugins.aihunter.entities.hunter.Hunter;

public class HunterConverter {

    public static HunterData entityToData(Hunter hunter) {
        HunterData hunterData = new HunterData();
        hunterData.setId(hunter.getId());
        hunterData.setName(hunter.getName());
        hunterData.setDeathLocationX(hunter.getDeathLocationX());
        hunterData.setDeathLocationY(hunter.getDeathLocationY());
        hunterData.setDeathLocationZ(hunter.getDeathLocationZ());
        hunterData.setNumberOfTimesDied(hunter.getNumberOfTimesDied());
        hunterData.setHuntStarTime(hunter.getHuntStarTime());
        return hunterData;
    }

    public static Hunter dataToEntity(HunterData hunterData) {
        Hunter hunter = new Hunter();
        hunter.setId(hunterData.getId());
        hunter.setName(hunterData.getName());
        hunter.setDeathLocationX(hunterData.getDeathLocationX());
        hunter.setDeathLocationY(hunterData.getDeathLocationY());
        hunter.setDeathLocationZ(hunterData.getDeathLocationZ());
        hunter.setNumberOfTimesDied(hunterData.getNumberOfTimesDied());
        hunter.setHuntStarTime(hunterData.getHuntStarTime());
        return hunter;
    }

}
