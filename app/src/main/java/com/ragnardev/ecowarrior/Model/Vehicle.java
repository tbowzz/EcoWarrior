package com.ragnardev.ecowarrior.Model;

import java.io.Serializable;

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

    public Vehicle(){}

    /**
     * For pulling from database
     */
    public Vehicle(String vehicleId, int odometer, double distanceRecorded, double volumeRecorded, double averageEfficiency)
    {
        this.vehicleId = vehicleId;
        this.odometer = odometer;
        this.distanceRecorded = distanceRecorded;
        this.volumeRecorded = volumeRecorded;
        this.averageEfficiency = averageEfficiency;
    }

    public void addNewFillupData(double tripDistance, double tripVolume)
    {
        this.distanceRecorded += tripDistance;
        this.volumeRecorded += tripVolume;
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
}
