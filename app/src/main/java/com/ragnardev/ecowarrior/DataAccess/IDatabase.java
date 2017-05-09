package com.ragnardev.ecowarrior.DataAccess;

import com.ragnardev.ecowarrior.Model.Vehicle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tyler on 5/9/17.
 */

public interface IDatabase
{
    public boolean insertVehicle(Vehicle vehicle);

    public boolean updateVehicle(Vehicle vehicle);

    public List<Vehicle> getAllVehicles(String vehicleId);
}
