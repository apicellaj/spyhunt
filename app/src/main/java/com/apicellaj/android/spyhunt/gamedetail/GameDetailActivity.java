package com.apicellaj.android.spyhunt.gamedetail;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;

import com.apicellaj.android.spyhunt.R;
import com.apicellaj.android.spyhunt.data.database.LocationsLocalDataSource;
import com.apicellaj.android.spyhunt.preferences.SharedPreferencesRepository;
import com.apicellaj.android.spyhunt.util.ActivityUtils;
import com.apicellaj.android.spyhunt.util.EspressoIdlingResource;
import com.apicellaj.android.spyhunt.util.schedulers.SchedulerProvider;

public class GameDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_detail);

        // Change title
        getSupportActionBar().setTitle(R.string.game_detail_screen_title);

        //Create the fragment
        GameDetailFragment gameDetailFragment =
                (GameDetailFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (gameDetailFragment == null) {
            Intent gameLobbyIntent = getIntent();
            String players = gameLobbyIntent.getStringExtra("players");
            String roles = gameLobbyIntent.getStringExtra("roles");
            String location = gameLobbyIntent.getStringExtra("locationTitle");
            gameDetailFragment = GameDetailFragment.newInstance(players, roles, location);
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), gameDetailFragment, R.id.contentFrame);
        }

        SchedulerProvider schedulerProvider = SchedulerProvider.getInstance();

        LocationsLocalDataSource data = LocationsLocalDataSource.getInstance(this, schedulerProvider);
        SharedPreferencesRepository preferences = new SharedPreferencesRepository(this);

        // Create the presenter
        new GameDetailPresenter(data, preferences, gameDetailFragment, schedulerProvider);
    }

    @VisibleForTesting
    public IdlingResource getCountingIdlingResource() {
        return EspressoIdlingResource.getIdlingResource();
    }
}
