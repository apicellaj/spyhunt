package com.apicellaj.android.spyhunt.mainscreen;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.apicellaj.android.spyhunt.R;
import com.apicellaj.android.spyhunt.util.ActivityUtils;
import com.apicellaj.android.spyhunt.util.schedulers.SchedulerProvider;

public class MainScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainscreen);

        // Change title
        getSupportActionBar().setTitle(R.string.app_name);

        // Create the fragment
        MainScreenFragment mainScreenFragment =
                (MainScreenFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (mainScreenFragment == null) {
            mainScreenFragment = MainScreenFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), mainScreenFragment, R.id.contentFrame);
        }

        // Create the presenter
        new MainScreenPresenter(mainScreenFragment, SchedulerProvider.getInstance());
    }
}
