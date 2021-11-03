package com.bymatej.minecraft.plugins.aihunter.entities.hunter;

import javax.persistence.*;

@Entity
@Table(name = "hunter", uniqueConstraints = {@UniqueConstraint(columnNames = "name")})

public class Hunter {

    @Id
    @GeneratedValue
    @Column
    private int id;

    @Column
    private String name;

    @Column(name = "number_of_times_died")
    private int numberOfTimesDied;

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

    public int getNumberOfTimesDied() {
        return numberOfTimesDied;
    }

    public void setNumberOfTimesDied(int numberOfTimesDied) {
        this.numberOfTimesDied = numberOfTimesDied;
    }

}
