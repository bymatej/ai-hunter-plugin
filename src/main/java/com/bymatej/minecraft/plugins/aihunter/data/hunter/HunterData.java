package com.bymatej.minecraft.plugins.aihunter.data.hunter;

import javax.persistence.Column;
import java.util.Date;

public class HunterData {
    private int id;

    private String name;

    private double deathLocationX;

    private double deathLocationY;

    private double deathLocationZ;

    private int numberOfTimesDied;

    private Date huntStarTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
