package com.bymatej.minecraft.plugins.aihunter.entities.hunter;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "hunter", uniqueConstraints = {@UniqueConstraint(columnNames = "name")})

public class Hunter {

    @Id
    @GeneratedValue
    @Column
    private int id;

    @Column
    private String name;

    @Column(name = "death_location_x")
    private double deathLocationX;

    @Column(name = "death_location_y")
    private double deathLocationY;

    @Column(name = "death_location_z")
    private double deathLocationZ;

    @Column(name = "number_of_times_died")
    private int numberOfTimesDied;

    @Column(name = "hunt_start_time")
    private Date huntStarTime;

    public Hunter() {
    }

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
