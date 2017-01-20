package com.example.assignment4;

/**
 * Created by Dream Team.
 *
 * Class that holds all the trophy parameters
 */

public class Trophy {
    private int id;
    private String name;
    private String description;
    private boolean unlocked = false;

    // constructor
    public Trophy(){
        id = 0;
        name = "n/a";
        description = "n/a";
        unlocked = false;
    }

    /*****Setters******/
    public void setId(int i){
        id = i;
    }

    public void setName(String s){
        name = s;
    }

    public void setDescription(String s){
        description = s;
    }

    public void setUnlocked(boolean u){
        unlocked = u;
    }

    /*****Getters******/
    public int getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    public String getDescription(){
        return description;
    }

    public boolean getUnlocked(){
        return unlocked;
    }
}
