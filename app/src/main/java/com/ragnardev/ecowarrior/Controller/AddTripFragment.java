package com.ragnardev.ecowarrior.Controller;

import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;

import com.ragnardev.ecowarrior.Model.Vehicle;
import com.ragnardev.ecowarrior.R;

/**
 * Created by tyler on 5/2/17.
 */

public class AddTripFragment extends DialogFragment
{
    private static final String ARG_VEHICLE = "currentvehiclearg";

    private Dialog mDialog;

    private TextInputEditText mOdometerReading;
    private TextInputEditText mTripDistance;
    private TextInputEditText mFuelOctane;
    private TextInputEditText mFuelBrand;
    private TextInputEditText mFuelPrice;
    private TextInputEditText mGallonsFilled;

    private Vehicle mVehicle;

    private int odometer;
    private double tripDistance;
    private int fuelOctane;
    private String fuelBrand;
    private double fuelPrice;
    private double gallonsFilled;


    public static AddTripFragment newInstance(Vehicle vehicle)
    {
        Bundle args = new Bundle();
        args.putSerializable(ARG_VEHICLE, vehicle);
        AddTripFragment fragment = new AddTripFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        mVehicle = (Vehicle) getArguments().getSerializable(ARG_VEHICLE);

        View v = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_add_trip, null);

        mOdometerReading = (TextInputEditText) v.findViewById(R.id.odometer_reading);
        mOdometerReading.addTextChangedListener(new GenericTextWatcher(mOdometerReading));

        mTripDistance = (TextInputEditText) v.findViewById(R.id.trip_distance);
        mTripDistance.addTextChangedListener(new GenericTextWatcher(mTripDistance));

        mFuelOctane = (TextInputEditText) v.findViewById(R.id.fuel_octane);
        mFuelOctane.addTextChangedListener(new GenericTextWatcher(mFuelBrand));

        mFuelBrand = (TextInputEditText) v.findViewById(R.id.fuel_brand);
        mFuelBrand.addTextChangedListener(new GenericTextWatcher(mFuelBrand));

        mFuelPrice = (TextInputEditText) v.findViewById(R.id.fuel_price);
        mFuelPrice.addTextChangedListener(new GenericTextWatcher(mFuelPrice));

        mGallonsFilled = (TextInputEditText) v.findViewById(R.id.gallons_filled);
        mGallonsFilled.addTextChangedListener(new GenericTextWatcher(mGallonsFilled));

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        dialogBuilder.setView(v);
        dialogBuilder.setTitle(R.string.add_trip_title);
        dialogBuilder.setPositiveButton(R.string.add_trip, null);
        //dialogBuilder.setNegativeButton(android.R.string.cancel, null);
        mDialog = dialogBuilder.create();
        mDialog.setCanceledOnTouchOutside(false);

        return mDialog;
    }

    private void enableConfirmationIfReady()
    {
        if(odometer > 0
            && tripDistance > 0
            && fuelOctane > 0
            && fuelBrand != null
            && fuelPrice > 0
            && gallonsFilled > 0)
        {
            ((AlertDialog)mDialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
        }
        else
        {
            ((AlertDialog)mDialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
        }
    }

    private class GenericTextWatcher implements TextWatcher
    {
        private View view;
        private GenericTextWatcher(View view)
        {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        public void afterTextChanged(Editable editable)
        {
            String text = editable.toString();
            //TODO: fix the null errors when converting to numbers
            switch(view.getId())
            {
                case R.id.odometer_reading:
                    odometer = Integer.parseInt(mOdometerReading.getText().toString());
                    mVehicle.setOdometer(odometer);
                case R.id.trip_distance:
                    tripDistance = Double.parseDouble(mTripDistance.getText().toString());
                case R.id.fuel_octane:
                    fuelOctane = Integer.parseInt(mOdometerReading.getText().toString());
                case R.id.fuel_brand:
                    fuelBrand = mFuelBrand.getText().toString();
                case R.id.fuel_price:
                    fuelPrice = Double.parseDouble(mOdometerReading.getText().toString());
                case R.id.gallons_filled:
                    gallonsFilled = Double.parseDouble(mGallonsFilled.getText().toString());
            }
            enableConfirmationIfReady();
        }
    }
}
