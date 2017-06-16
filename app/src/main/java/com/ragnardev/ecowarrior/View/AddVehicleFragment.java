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

import com.ragnardev.ecowarrior.Model.ClientModel;
import com.ragnardev.ecowarrior.Model.Vehicle;
import com.ragnardev.ecowarrior.Persistence.Firebase.FirebasePersistence;
import com.ragnardev.ecowarrior.R;

/**
 * Created by tyler on 6/15/17.
 */

public class AddVehicleFragment extends DialogFragment
{
    private static final String TAG = "AddVehicleFragment";
    private static final String ARG_VEHICLE = "currentvehiclearg";

    private Dialog mDialog;
    private String mTitleText;

    private Vehicle mVehicle;

    private TextInputEditText mVehicleIdEditText;
    private TextInputEditText mOdometerEditText;
    private TextInputEditText mEpaEditText;

    private String odometer = "";
    private String vehicleId = "";
    private String epaEstimate = "";

    public static AddVehicleFragment newInstance()
    {
        Bundle args = new Bundle();
        AddVehicleFragment fragment = new AddVehicleFragment();
//        fragment.setArguments(args);
        return fragment;
    }

    public static AddVehicleFragment newInstance(Vehicle vehicle)
    {
        Bundle args = new Bundle();
        args.putSerializable(ARG_VEHICLE, vehicle);
        AddVehicleFragment fragment = new AddVehicleFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_add_vehicle, null);

        initializeAddVehicleViews(v);
        //TODO: add edit vehicle functionality

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        dialogBuilder.setView(v);
        dialogBuilder.setTitle(mTitleText);
        dialogBuilder.setPositiveButton(R.string.add_vehicle, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                if(mVehicle == null)
                {
                    addNewVehicle();
                }
                else
                {
                    saveEditedVehicle();
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

    private void addNewVehicle()
    {
        Vehicle vehicle = new Vehicle(vehicleId, Integer.parseInt(odometer), 0, 0, Double.parseDouble(epaEstimate));
        ClientModel.SINGLETON.addVehicle(vehicle);

        Toast.makeText(getActivity().getApplicationContext(), vehicleId + " added!", Toast.LENGTH_SHORT).show();

        if(ClientModel.SINGLETON.getVehicles().size() == 1)
        {
            ((TripsActivity)getActivity()).initializeRecyclers();
        }

        ((TripsActivity)getActivity()).updateTripsView();

        new FirebasePersistence().updateServer();
    }

    private void saveEditedVehicle()
    {

    }

    private void initializeAddVehicleViews(View v)
    {
        mTitleText = "New Vehicle";

        mVehicleIdEditText = (TextInputEditText) v.findViewById(R.id.vehicle_id_text);
        mVehicleIdEditText.addTextChangedListener(new GenericTextWatcher(mVehicleIdEditText));

        mOdometerEditText = (TextInputEditText) v.findViewById(R.id.odometer_reading);
        mOdometerEditText.addTextChangedListener(new GenericTextWatcher(mOdometerEditText));

        mEpaEditText = (TextInputEditText) v.findViewById(R.id.epa_estimate);
        mEpaEditText.addTextChangedListener(new GenericTextWatcher(mEpaEditText));
    }

    private void initializeEditVehicleViews(View v)
    {
        mTitleText = "Edit Vehicle";

        mVehicleIdEditText = (TextInputEditText) v.findViewById(R.id.vehicle_id_text);
        mVehicleIdEditText.addTextChangedListener(new GenericTextWatcher(v));
        vehicleId = mVehicle.getVehicleId();
        mVehicleIdEditText.setText(vehicleId);

        mOdometerEditText = (TextInputEditText) v.findViewById(R.id.odometer_reading);
        mOdometerEditText.addTextChangedListener(new GenericTextWatcher(v));
        odometer = Integer.toString(mVehicle.getOdometer());
        mOdometerEditText.setText(odometer);

        mEpaEditText = (TextInputEditText) v.findViewById(R.id.epa_estimate);
        mEpaEditText.addTextChangedListener(new GenericTextWatcher(v));
        epaEstimate = Double.toString(mVehicle.getEpaEstimate());
        mEpaEditText.setText(epaEstimate);
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
                case R.id.vehicle_id_text:
                    vehicleId = text;
                    break;
                case R.id.odometer_reading:
                    odometer = text;
                    break;
                case R.id.epa_estimate:
                    epaEstimate = text;
                    break;
                default:
                    Log.e(TAG, "View not found");
                    break;
            }
            enableConfirmationIfReady();
        }
    }

    private void enableConfirmationIfReady()
    {
        if(odometer.length() > 0
                && vehicleId.length() > 0
                && epaEstimate.length() > 0)
        {
            ((AlertDialog)mDialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
        }
        else
        {
            ((AlertDialog)mDialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
        }
    }
}
