package com.bymatej.minecraft.plugins.aihunter;

import com.bymatej.minecraft.plugins.aihunter.data.hunter.HunterData;
import com.bymatej.minecraft.plugins.aihunter.utils.DbUtils;

import java.util.Random;

public final class Main {

    public static void main(String[] args) {
        System.out.println("Start");
        Random random = new Random();
        HunterData hunterData = new HunterData();
        hunterData.setName("a" + random.nextInt());
        hunterData.setDeathLocationX(1.1);
        hunterData.setDeathLocationY(2.2);
        hunterData.setDeathLocationZ(3.3);
        hunterData.setNumberOfTimesDied(4);
        hunterData.setHuntStarTime(null);
        DbUtils.createHunter(hunterData);
        System.out.println("End");
    }

}
