package com.ragnardev.ecowarrior.Model;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;
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
    private static final String UIDFILE = "curruid.ew";
    private static final String NAMEFILE = "currname.ew";

    public static ClientModel SINGLETON = new ClientModel();

    private String userUid;
    private String userDisplayName;

    private List<Vehicle> vehicles;

    private ClientModel()
    {
        vehicles = new ArrayList<>();
    }

    public String getUserUid() {
        return userUid;
    }

    public String getUserDisplayName() {
        return userDisplayName;
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

    //   LOGIN PERSISTENCE

    public boolean saveCurrentUser(FirebaseUser user, Context context)
    {
        this.userUid = user.getUid();
        this.userDisplayName = user.getDisplayName();

        try
        {
            FileOutputStream uidOs = context.openFileOutput(UIDFILE, Context.MODE_PRIVATE);
            FileOutputStream nameOs = context.openFileOutput(NAMEFILE, Context.MODE_PRIVATE);
            uidOs.write(userUid.getBytes());
            nameOs.write(userDisplayName.getBytes());
            uidOs.close();
            nameOs.close();
            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return false;
    }

    public boolean getSavedUser(Context context)
    {
        // check if file exists
        File file = context.getFileStreamPath(UIDFILE);
        if(file == null || !file.exists())
        {
            return false;
        }

        try
        {
            FileInputStream uidIs = context.openFileInput(UIDFILE);
            FileInputStream nameIs = context.openFileInput(NAMEFILE);

            BufferedReader br = new BufferedReader(new InputStreamReader(uidIs, StandardCharsets.UTF_8));
            this.userUid = br.readLine();
            uidIs.close();

            br = new BufferedReader(new InputStreamReader(nameIs, StandardCharsets.UTF_8));
            this.userDisplayName = br.readLine();

            br.close(); nameIs.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        if(this.userDisplayName == null || this.userUid == null) return false;
        else return true;
    }

    public void logout(Context context)
    {
        userUid = null;
        userDisplayName = null;
        vehicles = null;

        context.deleteFile(UIDFILE);
        context.deleteFile(NAMEFILE);
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
