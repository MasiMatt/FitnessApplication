package com.example.assignment4;

/**
 * Created by Dream Team.
 *
 * Class that handles unlocking trophies
 */

public class TrophiesUnlocked {

    private Trophy[] trophies; // array of trophy objects

    // constructor
    public TrophiesUnlocked(){
        trophies = new Trophy[6];

        // initialize trophy array
        for(int i =0; i<6; i++)
            trophies[i] = new Trophy();

        //**some custom trophy achievements to unlock**//
        trophies[0].setId(0);
        trophies[0].setName("10 Meters");
        trophies[0].setDescription("Ran 10 meters.");

        trophies[1].setId(1);
        trophies[1].setName("50 Meters");
        trophies[1].setDescription("Traveled 50 meters.");

        trophies[2].setId(2);
        trophies[2].setName("200 Meters");
        trophies[2].setDescription("Traveled 200 meters.");

        trophies[3].setId(3);
        trophies[3].setName("10 Calories");
        trophies[3].setDescription("Burned 10 calories.");

        trophies[4].setId(4);
        trophies[4].setName("15 Calories");
        trophies[4].setDescription("Burned 15 calories.");

        trophies[5].setId(5);
        trophies[5].setName("20 Calories");
        trophies[5].setDescription("Burned 20 calories.");
    }

    // unlocks a trophy if requirement is met
    public int checkCompleted(double distance, int duration, double avgSpeed, double calsBurned){

        if(distance >= 10 && trophies[0].getUnlocked() != true){
            unlocked(0);
            return trophies[0].getId();
        }

        if(distance >= 50 && trophies[1].getUnlocked() != true){
            unlocked(1);
            return trophies[1].getId();
        }

        if(distance >= 200 && trophies[2].getUnlocked() != true) {
            unlocked(2);
            return trophies[2].getId();
        }

        if(calsBurned >= 10 && trophies[3].getUnlocked() != true){
            unlocked(3);
            return trophies[3].getId();
        }

        if(calsBurned >= 15 && trophies[4].getUnlocked() != true){
            unlocked(4);
            return trophies[4].getId();
        }

        if(calsBurned >= 20 && trophies[5].getUnlocked() != true){
            unlocked(5);
            return trophies[5].getId();
        }
        return -1;
    }

    // unlock trophy
    public void unlocked(int id){
        trophies[id].setUnlocked(true);
    }

    //returns trophy object
    public Trophy getTrophy(int i){
        return trophies[i];
    }
}
