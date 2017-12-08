package com.ragnardev.ecowarrior.View;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.firebase.auth.FirebaseAuth;
import com.ragnardev.ecowarrior.Model.ClientModel;
import com.ragnardev.ecowarrior.Model.Trip;
import com.ragnardev.ecowarrior.Model.Vehicle;
import com.ragnardev.ecowarrior.R;

import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class TripsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
{
    private TripsPresenter presenter;

    private static final String TAG = TripsActivity.class.getName();
    private static final String DIALOG_TRIP = "DialogTrip";
    private static final String DIALOG_VEHICLE = "DialogVehicle";

//    Views
    private Toolbar mToolbar;
    private TextView mUserNameTextView;
    private TextView mUserEmailTextView;
    private ImageView mUserPictureImageView;
    private FloatingActionButton mAddTripButton;
    private FloatingActionButton mAddVehicleButton;
    private FloatingActionsMenu mAddMenu;
    private NavigationView mNavigationView;
    private DrawerLayout mDrawer;
    private RecyclerView vehicleRecyler;
    private ActionBarDrawerToggle mToggle;
    private RecyclerView tripsRecycler;
    private TripsAdapter mTripsAdapter;

    private Vehicle currentVehicle;

    public static Intent newIntent(Context packageContext)
    {
        Intent i = new Intent(packageContext, TripsActivity.class);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trips);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        presenter = new TripsPresenter(this);

        //NAV VIEW INIT
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        onCreateNavMenu(mNavigationView.getMenu());
        mNavigationView.setNavigationItemSelectedListener(this);

        //HEADER INIT
        View header=mNavigationView.getHeaderView(0);
        mUserNameTextView = (TextView) header.findViewById(R.id.nav_user_name);
        mUserNameTextView.setText(ClientModel.SINGLETON.getUserDisplayName());

//        mUserEmailTextView = (TextView) header.findViewById(R.id.nav_email);
//        mUserEmailTextView.setText(ClientModel.SINGLETON.getCurrentUser().getEmail());

//        new DownloadImageTask((ImageView) header.findViewById(R.id.nav_user_profile_pic))
//                .execute(ClientModel.SINGLETON.getCurrentUser().getPhotoUrl().toString());

        //FAB INIT
        mAddMenu = (FloatingActionsMenu) findViewById(R.id.add_menu);

        mAddTripButton = (FloatingActionButton) findViewById(R.id.add_trip_button);
        mAddTripButton.setImageResource(R.drawable.gas_icon);
        mAddTripButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                showAddTripDialog();
            }
        });

        mAddVehicleButton = (FloatingActionButton) findViewById(R.id.add_vehicle_button);
        mAddVehicleButton.setImageResource(R.drawable.vehicle_icon);
        mAddVehicleButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                showAddVehicleDialog();
            }
        });

        //if the user has no vehicle, prompt for creation
        if(ClientModel.SINGLETON.getVehicles().size() != 0)
        {
            initializeRecyclers();
        }
    }

    void onDatabaseSync()
    {
        if(ClientModel.SINGLETON.getVehicles().size() == 0)
        {
            Toast.makeText(this, "Since you have no vehicles, you must add one.", Toast.LENGTH_LONG).show();
            showAddVehicleDialog();
        }
        else
        {
            initializeRecyclers();
            onCreateNavMenu(mNavigationView.getMenu());
        }
    }

    void initializeRecyclers()
    {
        currentVehicle = ClientModel.SINGLETON.getVehicles().get(0);

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mToggle = new ActionBarDrawerToggle(
                this, mDrawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.setDrawerListener(mToggle);
        mToggle.syncState();

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
    }

    public void updateTripsView()
    {
        mTripsAdapter.notifyDataSetChanged();
        tripsRecycler.smoothScrollToPosition(currentVehicle.getRecordedTrips().size());
    }

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

            View view = layoutInflater.inflate(R.layout.trips_list_card, parent, false);

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
            String mpg = Double.toString(trip.getEfficiency());
            double percentage = trip.getEfficiency() / currentVehicle.getEpaEstimate();
            String epaPercentage = formatPercentage(percentage);
            String costHundred = formatCost(trip.getCostPerHundred());
            String tripDist = " " + Double.toString(trip.getTripDistance()) + " miles";
            String volume = " " + Double.toString(trip.getVolume());
            String fuelType = trip.getBrand() + ", ";
            String octane = Integer.toString(trip.getOctane()) + " octane";
            String price = formatCost(trip.getPrice());
            String odo = " " + Integer.toString(trip.getOdometer()) + " miles";

            this.mpg.setText(mpg);
            this.epaPercentage.setText(epaPercentage);
            this.costHundred.setText(costHundred);
            tripDistance.setText(tripDist);
            this.volume.setText(volume);
            this.fuelType.setText(fuelType);
            fuelOctane.setText(octane);
            fuelPrice.setText(price);
            odometer.setText(odo);
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

        private String formatCost(double original)
        {
            NumberFormat format = new DecimalFormat("#0.00");

            return " $" + format.format(original);
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

    void showAddVehicleDialog()
    {
        FragmentManager manager = getSupportFragmentManager();
        AddVehicleFragment dialog = AddVehicleFragment.newInstance();
        dialog.show(manager, DIALOG_VEHICLE);
    }

    void showEditVehicleDialog(Vehicle vehicle)
    {
        FragmentManager manager = getSupportFragmentManager();
        AddVehicleFragment dialog = AddVehicleFragment.newInstance(vehicle);
        dialog.show(manager, DIALOG_VEHICLE);
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

    void onCreateNavMenu(Menu menu)
    {
        final SubMenu vehiclesMenu = menu.addSubMenu("Vehicles");
        final List<Vehicle> vehicles = ClientModel.SINGLETON.getVehicles();
        for(int i = 0; i < vehicles.size(); i++)
        {
            Vehicle vehicle = vehicles.get(i);
            final int j = i;
            vehiclesMenu.add(vehicle.getVehicleId());
            vehiclesMenu.getItem(i).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener()
            {
                @Override
                public boolean onMenuItemClick(MenuItem item)
                {
                    uncheckVehicles(vehiclesMenu);
                    item.setChecked(true);
                    navMenuItemClickHelper(j);
                    return true;
                }
            });
        }
        if(vehiclesMenu.size() > 0) vehiclesMenu.getItem(0).setChecked(true);
    }
    private void uncheckVehicles(SubMenu vehiclesMenu)
    {
        for(int i = 0; i < vehiclesMenu.size(); i++)
        {
            vehiclesMenu.getItem(i).setChecked(false);
        }
    }

    private void navMenuItemClickHelper(int j)
    {
        currentVehicle = ClientModel.SINGLETON.getVehicles().get(j);
        mTripsAdapter.setRecordedTrips(currentVehicle.getRecordedTrips());
        if(mTripsAdapter.getRecordedTrips() == null) mTripsAdapter.setRecordedTrips(new ArrayList<Trip>());
        mTripsAdapter.notifyDataSetChanged();
        mDrawer.closeDrawer(GravityCompat.START);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        //TODO: If you want to re-add the obd portion, uncomment this and in activity_main_drawer
//        if(id == R.id.nav_obd)
//        {
//            Intent obd = new Intent(this, MainActivity.class);
//            startActivity(obd);
//        }
        if(id == R.id.nav_settings)
        {
            Intent settings = new Intent(this, SettingsActivity.class);
            startActivity(settings);
        }



        mDrawer.closeDrawer(GravityCompat.START);
        return false;
    }

    public Vehicle getCurrentVehicle()
    {
        return currentVehicle;
    }

    public void setCurrentVehicle(Vehicle currentVehicle)
    {
        this.currentVehicle = currentVehicle;
    }

    public NavigationView getNavigationView() {
        return mNavigationView;
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap>
    {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
