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

/**
 * Created by tyler on 6/13/17.
 */

public class Data
{
    private static final String TAG = "Data";

    public static Data SINGLETON = new Data();

    private List<Vehicle> vehicles;

    private DatabaseReference mDatabase;
    private FirebaseUser currentUser;

    private Data()
    {
        vehicles = new ArrayList<>();

        //TODO: Fix for users with no vehicles yet
        Vehicle focusst = new Vehicle("Ford Focus ST", 3150, 3049, 99, 25);
        vehicles.add(focusst);
    }

    public void initializeModel(DataSnapshot dataSnapshot)
    {
        GenericTypeIndicator<List<Vehicle>> genericTypeIndicator =new GenericTypeIndicator<List<Vehicle>>(){};

        List<Vehicle> fireBaseVehicles = dataSnapshot.child(currentUser.getUid() + "_" + currentUser.getDisplayName()).getValue(genericTypeIndicator);

        if(fireBaseVehicles != null) this.setVehicles(fireBaseVehicles);
    }

    public void pullFromFirebase()
    {
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                GenericTypeIndicator<List<Vehicle>> genericTypeIndicator =new GenericTypeIndicator<List<Vehicle>>(){};

                List<Vehicle> fireBaseVehicles = dataSnapshot.child(currentUser.getUid() + "_" + currentUser.getDisplayName()).getValue(genericTypeIndicator);

                if(fireBaseVehicles != null) setVehicles(fireBaseVehicles);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    public void pushToFirebase()
    {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child(currentUser.getUid() + "_" + currentUser.getDisplayName()).setValue(vehicles);
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
}
