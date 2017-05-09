package com.ragnardev.ecowarrior.DataAccess;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ragnardev.ecowarrior.Model.Vehicle;

/**
 * Created by tyler on 4/19/17.
 */

public class Database implements IDatabase
{
    /** Starts the sqlite driver connection
     * Static (once per class)
     */
    static
    {
        try
        {
            final String driver = "org.sqlite.JDBC";
            Class.forName(driver);
        }
        catch(ClassNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    private Connection connection;

    @Override
    public boolean insertVehicle(Vehicle vehicle)
    {
        VehiclesDAO vehiclesDAO = new VehiclesDAO();
        return vehiclesDAO.insertVehicle(connection, vehicle);
    }

    @Override
    public boolean updateVehicle(Vehicle vehicle)
    {
        VehiclesDAO vehiclesDAO = new VehiclesDAO();
        return vehiclesDAO.updateVehicle(connection, vehicle);
    }

    @Override
    public List<Vehicle> getAllVehicles(String vehicleId)
    {
        VehiclesDAO vehiclesDAO = new VehiclesDAO();
        return vehiclesDAO.getAllVehicles(connection);
    }

    public void openConnection()
    {
        try
        {
            final String CONNECTION_URL = "jdbc:sqlite:GasBuddyData.db";

            //Open a db connection
            connection = DriverManager.getConnection(CONNECTION_URL);

            //Start a transaction
            connection.setAutoCommit(false);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public void closeConnection(boolean commit)
    {
        try
        {
            if (commit)
            {
                connection.commit();
            }
            else
            {
                connection.rollback();
            }

            connection.close();
            connection = null;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public Connection getConnection()
    {
        return connection;
    }
}
