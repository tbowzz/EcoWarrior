package com.ragnardev.ecowarrior.View;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.ragnardev.ecowarrior.Model.ClientModel;
import com.ragnardev.ecowarrior.Persistence.Firebase.FirebasePersistence;
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

}
