package com.example.assignment4;

/**
 * Created by Dream Team.
 *
 * Class that holds the stats of the user
 */

public class RunStats {

    private int id;
    private double distance;
    private String duration;
    private double averageSpeed;
    private double calorieBurn;

    // constructor
    public RunStats()
    {
        id = 0;
        distance = 0.0;
        duration = "0";
        averageSpeed = 0.0;
        calorieBurn = 0.0;
    }

    // parameter constructor with id
    public RunStats(int id, double distance, String duration, double averageSpeed, double calorieBurn)
    {
        this.id=id;
        this.distance=distance;
        this.duration=duration;
        this.averageSpeed=averageSpeed;
        this.calorieBurn=calorieBurn;
    }

    // parameter constructor without id
    public RunStats(double distance,String duration, double averageSpeed, double calorieBurn)
    {
        this.distance=distance;
        this.duration=duration;
        this.averageSpeed=averageSpeed;
        this.calorieBurn=calorieBurn;
    }

    /*****Setters********/
    public void setId(int id) {
        this.id = id;
    }

    public void setDistance(double distance) {
        this.distance=distance;
    }

    public void setDuration(String duration) {
        this.duration=duration;
    }

    public void setAverageSpeed(double averageSpeed) {
        this.averageSpeed = averageSpeed;
    }

    public void setCalorieBurn(double calorieBurn) {
        this.calorieBurn = calorieBurn;
    }

    /*****getters********/
    public int getId() {
        return id;
    }

    public double getDistance() {
        return distance;
    }

    public String getDuration() {
        return duration;
    }

    public double getAverageSpeed() {
        return averageSpeed;
    }

    public double getCalorieBurn() {
        return calorieBurn;
    }
}
