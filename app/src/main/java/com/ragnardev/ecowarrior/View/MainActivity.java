package com.ragnardev.ecowarrior.View;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.firebase.auth.FirebaseAuth;
import com.ragnardev.ecowarrior.Model.Data;
import com.ragnardev.ecowarrior.Model.Trip;
import com.ragnardev.ecowarrior.Model.Vehicle;
import com.ragnardev.ecowarrior.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
{
    private static final String DIALOG_TRIP = "DialogTrip";

    private FloatingActionButton mAddTripButton;
    private FloatingActionButton mAddVehicleButton;
    private FloatingActionsMenu mAddMenu;

    private Vehicle currentVehicle;

    private DrawerLayout mDrawer;
    private RecyclerView vehicleRecyler;
    private ActionBarDrawerToggle mToggle;

    private RecyclerView tripsRecycler;
    private TripsAdapter mTripsAdapter;

    public static Intent newIntent(Context packageContext)
    {
        Intent i = new Intent(packageContext, MainActivity.class);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        //TODO: If no vehicle, prompt for creation
        currentVehicle = Data.Instance.getVehicles().get(0);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAddMenu = (FloatingActionsMenu) findViewById(R.id.add_menu);

        mAddTripButton = (FloatingActionButton) findViewById(R.id.add_trip_button);
        mAddTripButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                showAddTripDialog();
            }
        });

        mAddVehicleButton = (FloatingActionButton) findViewById(R.id.add_vehicle_button);
        mAddVehicleButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Snackbar.make(view, "Replace with AddVehicleActivity", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mToggle = new ActionBarDrawerToggle(
                this, mDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.setDrawerListener(mToggle);
        mToggle.syncState();

        vehicleRecyler = (RecyclerView) findViewById(R.id.left_drawer);
        vehicleRecyler.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        vehicleRecyler.setAdapter(new VehicleAdapter(Data.Instance.getVehicles()));


        LinearLayoutManager tripsLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, true);
        tripsLayoutManager.setStackFromEnd(true);
        tripsRecycler = (RecyclerView) findViewById(R.id.trips_recycler_view);
        tripsRecycler.setLayoutManager(tripsLayoutManager);

        if(currentVehicle.getRecordedTrips() == null)
        {
            currentVehicle.setRecordedTrips(new ArrayList<Trip>());
        }
        mTripsAdapter = new TripsAdapter(currentVehicle.getRecordedTrips());
        tripsRecycler.setAdapter(mTripsAdapter);
//        updateTripsView();
    }

    public void updateTripsView()
    {
        mTripsAdapter.notifyDataSetChanged();
        tripsRecycler.smoothScrollToPosition(currentVehicle.getRecordedTrips().size());
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
            setCurrentVehicle(Data.Instance.getVehicleById(vehicleId.getText().toString()));
            mTripsAdapter.setRecordedTrips(currentVehicle.getRecordedTrips());
            mTripsAdapter.notifyDataSetChanged();
            mDrawer.closeDrawer(GravityCompat.START);
        }
    }
//    ----------END VEHICLE RECYCLER------------------------------

    //    ------------TRIPS RECYCLER---------------------------------------------------
    private class TripsAdapter extends RecyclerView.Adapter<TripsHolder>
    {
        private List<Trip> recordedTrips;

        public TripsAdapter(List<Trip> recordedTrips)
        {
            this.recordedTrips = recordedTrips;
        }

        @Override
        public TripsHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());

            View view = layoutInflater.inflate(R.layout.trips_list_item, parent, false);

            return new TripsHolder(view);
        }

        @Override
        public void onBindViewHolder(TripsHolder holder, int position)
        {
            Trip trip = recordedTrips.get(position);
            holder.bindTrip(trip);
        }

        @Override
        public int getItemCount()
        {
            return recordedTrips.size();
        }

        public List<Trip> getRecordedTrips()
        {
            return recordedTrips;
        }

        public void setRecordedTrips(List<Trip> recordedTrips)
        {
            this.recordedTrips = recordedTrips;
        }
    }
    //    ------------TRIPS INFO HOLDER---------------------------------------------------
    private class TripsHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener
    {
        Trip holderTrip;

        TextView fuelType;
        TextView fuelOctane;
        TextView fuelPrice;

        TextView odometer;

        TextView mpg;
        TextView epaPercentage;

        TextView tripDistance;
        TextView volume;

        TextView costHundred;

        public TripsHolder(View itemView)
        {
            super(itemView);
            itemView.setOnLongClickListener(this);

            fuelType = (TextView) itemView.findViewById(R.id.fuel_brand);
            fuelOctane = (TextView) itemView.findViewById(R.id.fuel_octane);
            fuelPrice = (TextView) itemView.findViewById(R.id.fuel_price);

            odometer = (TextView) itemView.findViewById(R.id.odometer_reading);

            mpg = (TextView) itemView.findViewById(R.id.efficiency);
            epaPercentage = (TextView) itemView.findViewById(R.id.percentage);

            tripDistance = (TextView) itemView.findViewById(R.id.trip_distance);
            volume = (TextView) itemView.findViewById(R.id.volume);

            costHundred = (TextView) itemView.findViewById(R.id.hundred_mile_cost);
        }

        public void bindTrip(Trip trip)
        {
            holderTrip = trip;
            String fuelType = trip.getBrand() + ", ";
            String octane = Integer.toString(trip.getOctane());
            String price = Double.toString(trip.getPrice());

            String odo = " " + Integer.toString(trip.getOdometer());

            String mpg = Double.toString(trip.getEfficiency());
            double percentage = trip.getEfficiency() / currentVehicle.getEpaEstimate();
            String epaPercentage = formatPercentage(percentage);

            String tripDist = " " + Double.toString(trip.getTripDistance());
            String volume = Double.toString(trip.getVolume());

            String costHundred = "$" + Double.toString(trip.getCostPerHundred());

            this.fuelType.setText(fuelType);
            fuelOctane.setText(octane);
            fuelPrice.setText(price);

            odometer.setText(odo);

            this.mpg.setText(mpg);
            this.epaPercentage.setText(epaPercentage);

            tripDistance.setText(tripDist);
            this.volume.setText(volume);

            this.costHundred.setText(costHundred);
        }

        private String formatPercentage(double original)
        {
            NumberFormat format = new DecimalFormat("#0.0");

            double percentage;

            if(original > 1)
            {
                percentage = (original - 1) * 100;

                return "+" + format.format(percentage) + "%";
            }
            else
            {
                percentage = (1 - original) * 100;

                return "-" + format.format(percentage) + "%";
            }
        }

        @Override
        public boolean onLongClick(View v)
        {
            showEditTripDialog(holderTrip);
            return false;
        }
    }
//    ----------END TRIPS RECYCLER------------------------------

    void showAddTripDialog()
    {
        FragmentManager manager = getSupportFragmentManager();
        AddTripFragment dialog = AddTripFragment.newInstance(currentVehicle);
        dialog.show(manager, DIALOG_TRIP);
    }

    void showEditTripDialog(Trip trip)
    {
        FragmentManager manager = getSupportFragmentManager();
        AddTripFragment dialog = AddTripFragment.newInstance(trip, currentVehicle.getVehicleId());
        dialog.show(manager, DIALOG_TRIP);
    }

    @Override
    public void onBackPressed()
    {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        } else
        {
            super.onBackPressed();
            finish();
            FirebaseAuth.getInstance().signOut();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            Intent i = new Intent(this, SettingsActivity.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

//        if (id == R.id.nav_camera)
//        {
//            // Handle the camera action
//        }
//        else if (id == R.id.nav_gallery)
//        {
//
//        } else if (id == R.id.nav_slideshow)
//        {
//
//        } else if (id == R.id.nav_manage)
//        {
//
//        }
        if (id == R.id.nav_share)
        {

        } else if (id == R.id.nav_send)
        {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public Vehicle getCurrentVehicle()
    {
        return currentVehicle;
    }

    public void setCurrentVehicle(Vehicle currentVehicle)
    {
        this.currentVehicle = currentVehicle;
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener
    {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {
            selectItem(position);
        }
    }

    /** Swaps fragments in the main content view */
    private void selectItem(int position)
    {
        //TODO: Change the current vehicle
    }
}
