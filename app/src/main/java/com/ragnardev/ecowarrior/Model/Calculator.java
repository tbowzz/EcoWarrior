package com.ragnardev.ecowarrior.Model;

/**
 * Created by tyler on 4/19/17.
 */

public class Calculator
{
    public static double calculateEfficiency(double distance, double volume)
    {
        return distance/volume;
    }

    public static double calculateCostPerDistanceUnit(double cost, double distance)
    {
        return cost/distance;
    }
}
