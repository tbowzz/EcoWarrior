package com.ragnardev.ecowarrior.View;

import android.icu.text.LocaleDisplayNames;
import android.util.Log;
import android.widget.Toast;

import com.ragnardev.ecowarrior.Model.ClientModel;
import com.ragnardev.ecowarrior.Model.Vehicle;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by tyler on 6/16/17.
 */

public class TripsPresenter implements Observer
{
    private TripsActivity activity;

    public TripsPresenter(TripsActivity activity)
    {
        this.activity = activity;
        ClientModel.SINGLETON.addObserver(this);
    }

    @Override
    public void update(Observable o, Object arg)
    {
        if(arg.getClass() == ArrayList.class)
        {
            activity.onDatabaseSync();
        }
    }
}
