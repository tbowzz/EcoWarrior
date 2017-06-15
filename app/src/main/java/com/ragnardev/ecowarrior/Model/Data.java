package com.ragnardev.ecowarrior.Model;

import android.util.Log;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.ragnardev.ecowarrior.View.MainActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tyler on 6/13/17.
 */

public class Data
{
    private static final String TAG = "Data";

    public static Data Instance = new Data();

    private List<Vehicle> vehicles;

    private DatabaseReference mDatabase;
    private FirebaseUser currentUser;

    private Data()
    {
        vehicles = new ArrayList<>();

        //TODO: Fix for users with no vehicles yet
        Vehicle focusst = new Vehicle("Ford Focus ST", 3150, 3049, 99, 24.4, 25);
        vehicles.add(focusst);
    }

    public void initializeModel()
    {
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Attach a listener to read the data at our posts reference
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<List<Vehicle>> genericTypeIndicator =new GenericTypeIndicator<List<Vehicle>>(){};

                List<Vehicle> firebaseVehicles = dataSnapshot.getValue(genericTypeIndicator);
                vehicles = firebaseVehicles;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }

    public void pushToFirebase()
    {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.setValue(vehicles);
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

    public FirebaseUser getCurrentUser()
    {
        return currentUser;
    }

    public void setCurrentUser(FirebaseUser currentUser)
    {
        this.currentUser = currentUser;
    }
}
