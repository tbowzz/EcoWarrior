package com.ragnardev.ecowarrior.View;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ragnardev.ecowarrior.Model.ClientModel;
import com.ragnardev.ecowarrior.Model.Trip;
import com.ragnardev.ecowarrior.Model.Vehicle;
import com.ragnardev.ecowarrior.Persistence.Firebase.FirebasePersistence;
import com.ragnardev.ecowarrior.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tyler on 4/21/17.
 */

public class SettingsActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        final Button pullButton = (Button) findViewById(R.id.pull_button);
        pullButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                new FirebasePersistence().updateClientModel();
                Snackbar.make(pullButton, "Pulled from Database", Snackbar.LENGTH_SHORT).show();
            }
        });

        final Button pushButton = (Button) findViewById(R.id.push_button);
        pushButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                new FirebasePersistence().updateServer();
                Snackbar.make(pushButton, "Pushed to Database", Snackbar.LENGTH_SHORT).show();
            }
        });

//        Toast.makeText(this, "Welcome to SettingsActivity", Toast.LENGTH_SHORT).show();
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu)
//    {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.main, menu);
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            //actionbar back button
            case android.R.id.home:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //    ------------VEHICLE RECYCLER---------------------------------------------------
    private class VehicleAdapter extends RecyclerView.Adapter<VehicleHolder>
    {
        private List<Vehicle> vehicles;

        public VehicleAdapter(List<Vehicle> vehicles)
        {
            this.vehicles = vehicles;
        }

        @Override
        public VehicleHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());

            View view = layoutInflater.inflate(R.layout.drawer_list_item, parent, false);

            return new VehicleHolder(view);
        }

        @Override
        public void onBindViewHolder(VehicleHolder holder, int position)
        {
            Vehicle vehicle = vehicles.get(position);
            holder.bindTrip(vehicle);
        }
        @Override
        public int getItemCount()
        {
            return vehicles.size();
        }
    }
    //    ------------VEHICLE INFO HOLDER---------------------------------------------------
    private class VehicleHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        TextView vehicleId;

        public VehicleHolder(View itemView)
        {
            super(itemView);

            itemView.setOnClickListener(this);
            vehicleId = (TextView) itemView.findViewById(R.id.vehicle_id_text);
        }

        public void bindTrip(Vehicle vehicle)
        {
            String id = vehicle.getVehicleId();
            vehicleId.setText(id);
        }

        @Override
        public void onClick(View v)
        {
            Toast.makeText(getApplicationContext(), "Vehicle " + vehicleId.getText() + " selected", Toast.LENGTH_SHORT).show();
//            setCurrentVehicle(ClientModel.SINGLETON.getVehicleById(vehicleId.getText().toString()));
//            mTripsAdapter.setRecordedTrips(currentVehicle.getRecordedTrips());
//            if(mTripsAdapter.getRecordedTrips() == null) mTripsAdapter.setRecordedTrips(new ArrayList<Trip>());
//            mTripsAdapter.notifyDataSetChanged();
//            mDrawer.closeDrawer(GravityCompat.START);
        }
    }
//    ----------END VEHICLE RECYCLER------------------------------

}
