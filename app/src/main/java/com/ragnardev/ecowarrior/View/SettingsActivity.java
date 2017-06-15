package com.ragnardev.ecowarrior.View;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.ragnardev.ecowarrior.R;

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

        TextView helloWorld = (TextView) findViewById(R.id.hello_world_view);
        Toast.makeText(this, "Welcome to SettingsActivity", Toast.LENGTH_SHORT).show();
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

}
