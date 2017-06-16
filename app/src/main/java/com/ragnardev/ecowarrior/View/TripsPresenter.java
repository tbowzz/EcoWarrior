package com.ragnardev.ecowarrior.View;

import com.ragnardev.ecowarrior.Model.ClientModel;

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
        if(arg.getClass() == Boolean.class)
        {

        }
    }
}
