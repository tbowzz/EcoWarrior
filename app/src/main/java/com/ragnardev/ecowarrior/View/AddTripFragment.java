package com.ragnardev.ecowarrior.View;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.ragnardev.ecowarrior.Model.Data;
import com.ragnardev.ecowarrior.Model.Trip;
import com.ragnardev.ecowarrior.Model.Vehicle;
import com.ragnardev.ecowarrior.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Created by tyler on 5/2/17.
 */

public class AddTripFragment extends DialogFragment
{
    private static final String TAG = "AddTripFragment";
    private static final String ARG_VEHICLE = "currentvehiclearg";
    private static final String ARG_VEHICLE_ID = "idargument";
    private static final String ARG_TRIP = "edittriparg";

    private Dialog mDialog;

    private TextInputEditText mOdometerReading;
    private TextInputEditText mTripDistance;
    private TextInputEditText mFuelOctane;
    private TextInputEditText mFuelBrand;
    private TextInputEditText mFuelPrice;
    private TextInputEditText mGallonsFilled;

    private Vehicle mVehicle;
    private String mVehicleID;
    private Trip mTrip;
    private int mOriginalOdo;

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

    public static AddTripFragment newInstance(Trip trip, String vehicleID)
    {
        Bundle args = new Bundle();
        args.putSerializable(ARG_TRIP, trip);
        args.putSerializable(ARG_VEHICLE_ID, vehicleID);
        AddTripFragment fragment = new AddTripFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_add_trip, null);

        mVehicle = (Vehicle) getArguments().getSerializable(ARG_VEHICLE);
        if(mVehicle == null)
        {
            mTrip = (Trip) getArguments().getSerializable(ARG_TRIP);
            mVehicleID = (String) getArguments().getSerializable(ARG_VEHICLE_ID);
            mOriginalOdo = mTrip.getOdometer();
            setupEditTripDialog(v);
        }
        else
        {
            setupAddTripDialog(v);
        }

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        dialogBuilder.setView(v);
        dialogBuilder.setTitle(R.string.add_trip_title);
        dialogBuilder.setPositiveButton(R.string.add_trip, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                if(mVehicle != null)
                {
                    addNewTrip();
                }
                else
                {
                    saveEditedTrip();
                }
            }
        });
        dialogBuilder.setNegativeButton(android.R.string.cancel, null);
        mDialog = dialogBuilder.create();
        mDialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialog) {
                ((AlertDialog)dialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
            }
        });
        mDialog.setCanceledOnTouchOutside(false);

        return mDialog;
    }

    private void setupAddTripDialog(View v)
    {
        mOdometerReading = (TextInputEditText) v.findViewById(R.id.odometer_reading);
        mOdometerReading.addTextChangedListener(new GenericTextWatcher(mOdometerReading));

        mTripDistance = (TextInputEditText) v.findViewById(R.id.trip_distance);
        mTripDistance.addTextChangedListener(new GenericTextWatcher(mTripDistance));

        mFuelOctane = (TextInputEditText) v.findViewById(R.id.fuel_octane);
        mFuelOctane.addTextChangedListener(new GenericTextWatcher(mFuelOctane));

        mFuelBrand = (TextInputEditText) v.findViewById(R.id.fuel_brand);
        mFuelBrand.addTextChangedListener(new GenericTextWatcher(mFuelBrand));

        mFuelPrice = (TextInputEditText) v.findViewById(R.id.fuel_price);
        mFuelPrice.addTextChangedListener(new GenericTextWatcher(mFuelPrice));

        mGallonsFilled = (TextInputEditText) v.findViewById(R.id.gallons_filled);
        mGallonsFilled.addTextChangedListener(new GenericTextWatcher(mGallonsFilled));
    }

    private void setupEditTripDialog(View v)
    {
        mOdometerReading = (TextInputEditText) v.findViewById(R.id.odometer_reading);
        mOdometerReading.setText(Integer.toString(mTrip.getOdometer()));
        odometer = mTrip.getOdometer();
        mOdometerReading.addTextChangedListener(new GenericTextWatcher(mOdometerReading));

        mTripDistance = (TextInputEditText) v.findViewById(R.id.trip_distance);
        mTripDistance.setText(Double.toString(mTrip.getTripDistance()));
        tripDistance = mTrip.getTripDistance();
        mTripDistance.addTextChangedListener(new GenericTextWatcher(mTripDistance));

        mFuelOctane = (TextInputEditText) v.findViewById(R.id.fuel_octane);
        mFuelOctane.setText(Integer.toString(mTrip.getOctane()));
        fuelOctane = mTrip.getOctane();
        mFuelOctane.addTextChangedListener(new GenericTextWatcher(mFuelOctane));

        mFuelBrand = (TextInputEditText) v.findViewById(R.id.fuel_brand);
        mFuelBrand.setText(mTrip.getBrand());
        fuelBrand = mTrip.getBrand();
        mFuelBrand.addTextChangedListener(new GenericTextWatcher(mFuelBrand));

        mFuelPrice = (TextInputEditText) v.findViewById(R.id.fuel_price);
        mFuelPrice.setText(Double.toString(mTrip.getPrice()));
        fuelPrice = mTrip.getPrice();
        mFuelPrice.addTextChangedListener(new GenericTextWatcher(mFuelPrice));

        mGallonsFilled = (TextInputEditText) v.findViewById(R.id.gallons_filled);
        mGallonsFilled.setText(Double.toString(mTrip.getVolume()));
        gallonsFilled = mTrip.getVolume();
        mGallonsFilled.addTextChangedListener(new GenericTextWatcher(mGallonsFilled));
    }

    private void addNewTrip()
    {
        Trip newTrip = new Trip(odometer, tripDistance, fuelOctane, fuelBrand, fuelPrice, gallonsFilled);
        Data.Instance.getVehicleById(mVehicle.getVehicleId()).addTrip(newTrip);
        Data.Instance.getVehicleById(mVehicle.getVehicleId()).addNewFillupData(this.tripDistance, this.gallonsFilled);

        Toast.makeText(getContext(), "Trip added to " + mVehicle.getVehicleId(), Toast.LENGTH_SHORT).show();

        Data.Instance.pushToFirebase();

        ((MainActivity)getActivity()).updateTripsView();
    }

    private void saveEditedTrip()
    {
        Vehicle vehicle = Data.Instance.getVehicleById(mVehicleID);
        vehicle.getTripByOdo(mOriginalOdo).setBrand(fuelBrand);
        vehicle.getTripByOdo(mOriginalOdo).setOctane(fuelOctane);
        vehicle.getTripByOdo(mOriginalOdo).setOdometer(odometer);
        vehicle.getTripByOdo(mOriginalOdo).setPrice(fuelPrice);
        vehicle.getTripByOdo(mOriginalOdo).setVolume(gallonsFilled);
        vehicle.getTripByOdo(mOriginalOdo).setTripDistance(tripDistance);
        NumberFormat format = new DecimalFormat("#0.00");
        vehicle.getTripByOdo(mOriginalOdo).setEfficiency(Double.parseDouble(format.format(tripDistance / gallonsFilled)));
        vehicle.getTripByOdo(mOriginalOdo).setCostPerHundred(Double.parseDouble(format.format((100 / vehicle.getTripByOdo(odometer).getEfficiency()) * fuelPrice)));

        Toast.makeText(getContext(), "Trip edited for " + mVehicle.getVehicleId(), Toast.LENGTH_SHORT).show();

        Data.Instance.pushToFirebase();

        ((MainActivity)getActivity()).updateTripsView();
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

            switch(view.getId())
            {
                case R.id.odometer_reading:
                    if(text.length() == 0) odometer = 0;
                    else
                    {
                        odometer = Integer.parseInt(text);
                        if(mVehicle != null) mVehicle.setOdometer(odometer);
                    }
                    break;
                case R.id.trip_distance:
                    if(text.length() == 0) tripDistance = 0;
                    else tripDistance = Double.parseDouble(text);
                    break;
                case R.id.fuel_octane:
                    if(text.length() == 0) fuelOctane = 0;
                    else fuelOctane = Integer.parseInt(text);
                    break;
                case R.id.fuel_brand:
                    if(text.length() == 0) fuelBrand = null;
                    else fuelBrand = text;
                    break;
                case R.id.fuel_price:
                    if(text.length() == 0) fuelPrice = 0;
                    else fuelPrice = Double.parseDouble(text);
                    break;
                case R.id.gallons_filled:
                    if(text.length() == 0) gallonsFilled = 0;
                    else gallonsFilled = Double.parseDouble(text);
                    break;
                default:
                    Log.e(TAG, "View not found");
                    break;
            }
            enableConfirmationIfReady();
        }
    }
}
