package com.bymatej.minecraft.plugins.aihunter.data.hunter;

import com.bymatej.minecraft.plugins.aihunter.entities.hunter.Hunter;

public class HunterConverter {

    public static HunterData entityToData(Hunter hunter) {
        HunterData hunterData = new HunterData();
        hunterData.setId(hunter.getId());
        hunterData.setName(hunter.getName());
        hunterData.setNumberOfTimesDied(hunter.getNumberOfTimesDied());
        return hunterData;
    }

    public static Hunter dataToEntity(HunterData hunterData) {
        Hunter hunter = new Hunter();
        hunter.setId(hunterData.getId());
        hunter.setName(hunterData.getName());
        hunter.setNumberOfTimesDied(hunterData.getNumberOfTimesDied());
        return hunter;
    }

}
