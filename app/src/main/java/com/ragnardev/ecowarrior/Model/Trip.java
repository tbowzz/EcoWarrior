package com.ragnardev.ecowarrior.Model;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Created by tyler on 6/14/17.
 */

public class Trip implements Serializable
{
    private int odometer;
    private double tripDistance;
    private int octane;
    private String brand;
    private double price;
    private double volume;
    private double efficiency;
    private double costPerHundred;

    public Trip(){}

    public Trip(int odometer, double tripDistance, int octane, String brand, double price, double volume)
    {
        this.odometer = odometer;
        this.tripDistance = tripDistance;
        this.octane = octane;
        this.brand = brand;
        this.price = price;
        this.volume = volume;

        NumberFormat format = new DecimalFormat("#0.00");

        efficiency = Double.parseDouble(format.format(tripDistance / volume));
        this.costPerHundred = Double.parseDouble(format.format((100 / efficiency) * price));
    }

    public int getOdometer()
    {
        return odometer;
    }

    public void setOdometer(int odometer)
    {
        this.odometer = odometer;
    }

    public double getTripDistance()
    {
        return tripDistance;
    }

    public void setTripDistance(double tripDistance)
    {
        this.tripDistance = tripDistance;
    }

    public int getOctane()
    {
        return octane;
    }

    public void setOctane(int octane)
    {
        this.octane = octane;
    }

    public String getBrand()
    {
        return brand;
    }

    public void setBrand(String brand)
    {
        this.brand = brand;
    }

    public double getPrice()
    {
        return price;
    }

    public void setPrice(double price)
    {
        this.price = price;
    }

    public double getVolume()
    {
        return volume;
    }

    public void setVolume(double volume)
    {
        this.volume = volume;
    }

    public double getEfficiency()
    {
        return efficiency;
    }

    public void setEfficiency(double efficiency)
    {
        this.efficiency = efficiency;
    }

    public double getCostPerHundred()
    {
        return costPerHundred;
    }

    public void setCostPerHundred(double costPerHundred)
    {
        this.costPerHundred = costPerHundred;
    }
}
