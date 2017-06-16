package com.ragnardev.ecowarrior.Model;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by tyler on 6/13/17.
 */

public class ClientModel extends Observable
{
    private static final String TAG = "ClientModel";

    public static ClientModel SINGLETON = new ClientModel();

    private FirebaseUser currentUser;
    private List<Vehicle> vehicles;

    private ClientModel()
    {
        vehicles = new ArrayList<>();

        //TODO: Fix for users with no vehicles yet
//        Vehicle focusst = new Vehicle("Ford Focus ST", 3150, 3049, 99, 25);
//        vehicles.add(focusst);
    }

    public List<Vehicle> getVehicles()
    {
        return vehicles;
    }

    public Vehicle getVehicleById(String vehicleId)
    {
        for(Vehicle vehicle : vehicles)
        {
            if(vehicle.getVehicleId().equals(vehicleId))
                return vehicle;
        }
        return null;
    }

    public void setVehicles(List<Vehicle> vehicles)
    {
        this.vehicles = vehicles;
    }

    public boolean addVehicle(Vehicle vehicle)
    {
        return vehicles.add(vehicle);
    }

    public FirebaseUser getCurrentUser()
    {
        return currentUser;
    }

    public void setCurrentUser(FirebaseUser currentUser)
    {
        this.currentUser = currentUser;
    }

    //    OBSERVABLE

    public void sendToObservers(Object arg)
    {
        setChanged();
        notifyObservers(arg);
        clearChanged();
    }

    @Override
    public synchronized void addObserver(Observer o) {
        super.addObserver(o);
    }

    @Override
    public synchronized void deleteObserver(Observer o) {
        super.deleteObserver(o);
    }
}
