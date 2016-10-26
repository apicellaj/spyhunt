package com.apicellaj.android.spyhunt.customlocations;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.apicellaj.android.spyhunt.R;
import com.apicellaj.android.spyhunt.data.database.LocationsLocalDataSource;
import com.apicellaj.android.spyhunt.util.ActivityUtils;
import com.apicellaj.android.spyhunt.util.schedulers.SchedulerProvider;

public class CustomLocationsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_locations);

        // Change title
        getSupportActionBar().setTitle(R.string.custom_locations_screen_title);

        // Create the fragment
        CustomLocationsFragment customLocationsFragment =
                (CustomLocationsFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (customLocationsFragment == null) {
            customLocationsFragment = CustomLocationsFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), customLocationsFragment, R.id.contentFrame);
        }

        // Create LocationsLocalDataSource for interacting with SQLite database
        LocationsLocalDataSource dataSource =
                LocationsLocalDataSource.getInstance(this, SchedulerProvider.getInstance());

        // Create the presenter
        new CustomLocationsPresenter(dataSource, customLocationsFragment, SchedulerProvider.getInstance());
    }
}
