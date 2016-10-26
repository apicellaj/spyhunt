package com.apicellaj.android.spyhunt.addlocation;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;

import com.apicellaj.android.spyhunt.R;
import com.apicellaj.android.spyhunt.util.ActivityUtils;
import com.apicellaj.android.spyhunt.util.EspressoIdlingResource;

public class AddLocationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);

        // Change title
        getSupportActionBar().setTitle(R.string.app_name);

        // Create the fragment
        AddLocationFragment addLocationFragment =
                (AddLocationFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (addLocationFragment == null) {
            addLocationFragment = AddLocationFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), addLocationFragment, R.id.contentFrame);
        }
    }

    @VisibleForTesting
    public IdlingResource getCountingIdlingResource() {
        return EspressoIdlingResource.getIdlingResource();
    }
}
