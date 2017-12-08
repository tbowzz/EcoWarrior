package com.ragnardev.ecowarrior.Persistence.Firebase;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.ragnardev.ecowarrior.Model.ClientModel;
import com.ragnardev.ecowarrior.Model.Vehicle;
import com.ragnardev.ecowarrior.Persistence.IPersistence;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tyler on 6/16/17.
 */

public class FirebasePersistence implements IPersistence
{
    private DatabaseReference mDatabase;
    private String currentUserUid;
    private String currentUserDisplayName;

    public FirebasePersistence()
    {
        this.currentUserUid = ClientModel.SINGLETON.getUserUid();
        this.currentUserDisplayName = ClientModel.SINGLETON.getUserDisplayName();
    }

    @Override
    public void updateClientModel()
    {
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                GenericTypeIndicator<List<Vehicle>> genericTypeIndicator =new GenericTypeIndicator<List<Vehicle>>(){};

                List<Vehicle> fireBaseVehicles = dataSnapshot.child(currentUserUid + "_" + currentUserDisplayName).getValue(genericTypeIndicator);

                if(fireBaseVehicles != null)
                {
                    ClientModel.SINGLETON.setVehicles(fireBaseVehicles);

                    ClientModel.SINGLETON.sendToObservers(fireBaseVehicles);
                }
                else
                {
                    ClientModel.SINGLETON.sendToObservers(new ArrayList<>());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    @Override
    public void updateServer()
    {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child(currentUserUid + "_" + currentUserDisplayName).setValue(ClientModel.SINGLETON.getVehicles());
    }
}
