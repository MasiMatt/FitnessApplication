package com.example.assignment4;

/**
 * Created by Dream Team.
 *
 * Calculates distance between 2 pairs of latitude and longitude coordinates
 */

import com.google.android.gms.maps.model.LatLng;

import java.text.DecimalFormat;

public class DistanceCalculator {
    private double resMeters; // Distance in meters

    // Constructor
    public DistanceCalculator()
    {
        resMeters = 0.0;
    }

    // Calculates distance from 2 latlings
    public double CalculationByDistance(LatLng StartP, LatLng EndP) {

        int Radius = 6371;// radius of earth in Km

        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        resMeters = Radius * c * 1000; // distance in meters
        return resMeters;  // return distance
    }
}
