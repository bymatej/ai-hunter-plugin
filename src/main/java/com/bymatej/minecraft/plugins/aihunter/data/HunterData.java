package com.bymatej.minecraft.plugins.aihunter.data;

import java.util.Date;

public class HunterData {

    private String name;

    private double deathLocationX;

    private double deathLocationY;

    private double deathLocationZ;

    private int numberOfTimesDied;

    private Date huntStarTime;

    public HunterData() {
    }

    public HunterData(String name, double deathLocationX, double deathLocationY, double deathLocationZ, int numberOfTimesDied, Date huntStarTime) {
        this.name = name;
        this.deathLocationX = deathLocationX;
        this.deathLocationY = deathLocationY;
        this.deathLocationZ = deathLocationZ;
        this.numberOfTimesDied = numberOfTimesDied;
        this.huntStarTime = huntStarTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getDeathLocationX() {
        return deathLocationX;
    }

    public void setDeathLocationX(double deathLocationX) {
        this.deathLocationX = deathLocationX;
    }

    public double getDeathLocationY() {
        return deathLocationY;
    }

    public void setDeathLocationY(double deathLocationY) {
        this.deathLocationY = deathLocationY;
    }

    public double getDeathLocationZ() {
        return deathLocationZ;
    }

    public void setDeathLocationZ(double deathLocationZ) {
        this.deathLocationZ = deathLocationZ;
    }

    public int getNumberOfTimesDied() {
        return numberOfTimesDied;
    }

    public void setNumberOfTimesDied(int numberOfTimesDied) {
        this.numberOfTimesDied = numberOfTimesDied;
    }

    public Date getHuntStarTime() {
        return huntStarTime;
    }

    public void setHuntStarTime(Date huntStarTime) {
        this.huntStarTime = huntStarTime;
    }
}
