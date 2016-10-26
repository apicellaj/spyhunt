package com.apicellaj.android.spyhunt.gamelobby;

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

public class GameLobbyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_lobby);

        // Change title
        getSupportActionBar().setTitle(R.string.game_lobby_screen_title);

        // Create the fragment
        GameLobbyFragment gameLobbyFragment =
                (GameLobbyFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (gameLobbyFragment == null) {
            gameLobbyFragment = GameLobbyFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), gameLobbyFragment, R.id.contentFrame);
        }

        SchedulerProvider schedulerProvider = SchedulerProvider.getInstance();

        LocationsLocalDataSource dataSource = LocationsLocalDataSource.getInstance(this, schedulerProvider);

        SharedPreferencesRepository preferences = new SharedPreferencesRepository(this);

        // Create the presenter
        new GameLobbyPresenter(dataSource, preferences, gameLobbyFragment, schedulerProvider);
    }

    @VisibleForTesting
    public IdlingResource getCountingIdlingResource() {
        return EspressoIdlingResource.getIdlingResource();
    }
}
