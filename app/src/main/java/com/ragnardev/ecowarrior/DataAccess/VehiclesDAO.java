package com.ragnardev.ecowarrior.DataAccess;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.ragnardev.ecowarrior.Model.Vehicle;

/**
 * Created by tyler on 4/19/17.
 */

public class VehiclesDAO
{
    private static final String CREATE_VEHICLES_TABLE = "create table if not exists Vehicles" +
            "(vehicleId TEXT NOT NULL, " +
            "odometer INT NOT NULL, " +
            "distanceRecorded FLOAT NOT NULL, " +
            "volumeRecorded FLOAT NOT NULL, " +
            "averageEfficiency FLOAT NOT NULL" +
            ");";

    private static final String INSERT_INTO_VEHICLES_TABLE = "insert into Vehicles" +
            "(vehicleId, odometer, distanceRecorded, volumeRecorded, averageEfficiency) values (?, ?, ?, ?, ?);";

    protected boolean insertVehicle(Connection connection, Vehicle vehicle)
    {
        Statement stat = null;
        try
        {
            stat = connection.createStatement();
            stat.executeUpdate(CREATE_VEHICLES_TABLE);

            PreparedStatement prep = connection.prepareStatement(INSERT_INTO_VEHICLES_TABLE);
            prep.setString(1, vehicle.getVehicleId());
            prep.setString(2, Integer.toString(vehicle.getOdometer()));
            prep.setString(3, Double.toString(vehicle.getDistanceRecorded()));
            prep.setString(4, Double.toString(vehicle.getVolumeRecorded()));
            prep.setString(5, Double.toString(vehicle.getAverageEfficiency()));
            prep.addBatch();

            prep.executeBatch();

        } catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
        finally
        {
            if(stat != null)
            {
                try
                {
                    stat.close();
                } catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    //TODO: complete the updateVehicle method
    protected boolean updateVehicle(Connection connection, Vehicle vehicle)
    {
        return false;
    }

    protected ArrayList<Vehicle> getAllVehicles(Connection connection)
    {
        Statement stat = null;
        ResultSet rs = null;
        Vehicle vehicle = null;
        ArrayList<Vehicle> vehicles = null;
        try
        {
            stat = connection.createStatement();
            stat.executeUpdate(CREATE_VEHICLES_TABLE);

            String GET_ALL_VEHICLES =
                    "SELECT * " +
                            "FROM Vehicles";

//            String GET_VEHICLE =
//                    "SELECT * " +
//                            "FROM Vehicles " +
//                            "WHERE vehicleId = \'" + vehicleId + "\'";

            rs = stat.executeQuery(GET_ALL_VEHICLES);

            if(!rs.next())
            {
                System.out.println("no data found (VehiclesDAO.java)");
                return null;
            }
            vehicles = new ArrayList<>();
            String vehicleId = rs.getString("vehicleId");
            int odometer = Integer.parseInt(rs.getString("odometer"));
            double distanceRecorded = Double.parseDouble(rs.getString("distanceRecorded"));
            double volumeRecorded = Double.parseDouble(rs.getString("volumeRecorded"));
            double averageEfficiency = Double.parseDouble(rs.getString("averageEfficiency"));
            vehicle = new Vehicle(vehicleId, odometer, distanceRecorded, volumeRecorded, averageEfficiency);
            vehicles.add(vehicle);
            while(rs.next())
            {
                vehicleId = rs.getString("vehicleId");
                odometer = Integer.parseInt(rs.getString("odometer"));
                distanceRecorded = Double.parseDouble(rs.getString("distanceRecorded"));
                volumeRecorded = Double.parseDouble(rs.getString("volumeRecorded"));
                averageEfficiency = Double.parseDouble(rs.getString("averageEfficiency"));
                vehicle = new Vehicle(vehicleId, odometer, distanceRecorded, volumeRecorded, averageEfficiency);
                vehicles.add(vehicle);
            }

        } catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
        finally
        {
            if(rs != null)
            {
                try
                {
                    rs.close();
                } catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }
            if(stat != null)
            {
                try
                {
                    stat.close();
                } catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }
        }
        return vehicles;
    }
}
