package com.ragnardev.ecowarrior.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tyler on 4/19/17.
 */

public class Vehicle implements Serializable
{
    private String vehicleId;
    private int odometer;
    private double distanceRecorded;
    private double volumeRecorded;
    private double averageEfficiency;
    private double epaEstimate;
    private List<Trip> recordedTrips;

    public Vehicle(){}

    /**
     * For pulling from database
     */
    public Vehicle(String vehicleId, int odometer, double distanceRecorded, double volumeRecorded, double epaEstimate)
    {
        this.vehicleId = vehicleId;
        this.odometer = odometer;
        this.distanceRecorded = distanceRecorded;
        this.volumeRecorded = volumeRecorded;
        this.averageEfficiency = 0;
        this.recordedTrips = new ArrayList<>();
        this.epaEstimate = epaEstimate;
    }

    public void addNewFillupData(Trip trip)
    {
//        int odometer = trip.getOdometer();
//        double tripDistance = trip.getTripDistance();
//        int octane = trip.getOctane();
//        String brand = trip.getBrand();
//        double price = trip.getPrice();
//        double volume = trip.getVolume();
////        double efficiency = trip.getEfficiency();
////        double costPerHundred = trip.getCostPerHundred();
//
//        Trip newTrip = new Trip(odometer, -1, octane, brand, price, -1);
//
//        // if there are no trips for this vehicle, init list and add newTrip
//        if(recordedTrips == null || recordedTrips.size() == 0)
//        {
//            recordedTrips = new ArrayList<>();
//            recordedTrips.add(newTrip);
//        }
//        // else add newTrip and complete calculations for the previous trip
//        else
//        {
//            recordedTrips.add(newTrip);
//
//            Trip lastTrip = recordedTrips.get(recordedTrips.size() - 2);
//            lastTrip.setEfficiency(tripDistance / volume);
//            lastTrip.setCostPerHundred(Calculator.calculateCostPerDistanceUnit(price, tripDistance));
//        }

        //TODO: Old way
        if(recordedTrips == null)
        {
            recordedTrips = new ArrayList<>();
        }

        recordedTrips.add(trip);

        this.distanceRecorded += trip.getTripDistance();
        this.volumeRecorded += trip.getVolume();
        setAverageEfficiency();
    }

    public String getVehicleId()
    {
        return vehicleId;
    }

    public int getOdometer()
    {
        return odometer;
    }

    public void setOdometer(int odometer)
    {
        this.odometer = odometer;
    }

    public double getDistanceRecorded()
    {
        return distanceRecorded;
    }

    public double getVolumeRecorded()
    {
        return volumeRecorded;
    }

    public double getAverageEfficiency()
    {
        return averageEfficiency;
    }

    public void setAverageEfficiency()
    {
        this.averageEfficiency = Calculator.calculateEfficiency(this.distanceRecorded, this.volumeRecorded);
    }

    public Trip getTripByOdo(int odometer)
    {
        for(Trip trip : recordedTrips)
        {
            if(trip.getOdometer() == odometer)
            {
                return trip;
            }
        }
        return null;
    }

    public List<Trip> getRecordedTrips()
    {
        return recordedTrips;
    }

    public void setRecordedTrips(List<Trip> recordedTrips)
    {
        this.recordedTrips = recordedTrips;
    }

    public void setVehicleId(String vehicleId)
    {
        this.vehicleId = vehicleId;
    }

    public void setDistanceRecorded(double distanceRecorded)
    {
        this.distanceRecorded = distanceRecorded;
    }

    public void setVolumeRecorded(double volumeRecorded)
    {
        this.volumeRecorded = volumeRecorded;
    }

    public void setAverageEfficiency(double averageEfficiency)
    {
        this.averageEfficiency = averageEfficiency;
    }

    public double getEpaEstimate()
    {
        return epaEstimate;
    }

    public void setEpaEstimate(double epaEstimate)
    {
        this.epaEstimate = epaEstimate;
    }
}
