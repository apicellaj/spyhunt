package com.apicellaj.android.spyhunt.gamelobby;

import android.support.annotation.NonNull;

import com.apicellaj.android.spyhunt.data.Location;
import com.apicellaj.android.spyhunt.data.database.LocationsLocalDataSource;
import com.apicellaj.android.spyhunt.preferences.SharedPreferencesRepository;
import com.apicellaj.android.spyhunt.util.schedulers.BaseSchedulerProvider;

import java.util.List;
import java.util.Random;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.functions.Func1;
import rx.subscriptions.CompositeSubscription;

import static com.google.common.base.Preconditions.checkNotNull;

public class GameLobbyPresenter implements GameLobbyContract.Presenter {

    @NonNull
    private final LocationsLocalDataSource mDataSource;

    @NonNull
    private final SharedPreferencesRepository mPreferences;

    @NonNull
    private final GameLobbyContract.View mGameLobbyView;

    @NonNull
    private final BaseSchedulerProvider mSchedulerProvider;

    @NonNull
    private final CompositeSubscription mSubscriptions;

    public GameLobbyPresenter(@NonNull LocationsLocalDataSource dataSource,
                              @NonNull SharedPreferencesRepository preferences,
                              @NonNull GameLobbyContract.View gameLobbyView,
                              @NonNull BaseSchedulerProvider schedulerProvider) {
        mDataSource = checkNotNull(dataSource, "LocalDataSource cannot be null");
        mPreferences = checkNotNull(preferences, "sharedPreferences cannot be null");
        mGameLobbyView = checkNotNull(gameLobbyView, "customLocationsView cannot be null");
        mSchedulerProvider = checkNotNull(schedulerProvider, "schedulerProvider cannot be null");

        mSubscriptions = new CompositeSubscription();
        mGameLobbyView.setPresenter(this);
    }

    @Override
    public void subscribe() {
        loadPlayerNames();
        loadLocations();
    }

    @Override
    public void unsubscribe() {
        mSubscriptions.clear();
    }

    @Override
    public void loadPlayerNames() {
        if (mPreferences.getUseSavedPlayers()) {
            List<String> players = mPreferences.getSavedPlayers();
            mGameLobbyView.showPlayers(players);
        }
    }

    @Override
    public void savePlayerNames(List<String> players) {
        mPreferences.setSavedPlayers(players);
    }

    @Override
    public void loadLocations() {
        mSubscriptions.clear();
        Subscription subscription = mDataSource
                .getLocations()
                .flatMap(new Func1<List<Location>, Observable<Location>>() {
                    @Override
                    public Observable<Location> call(List<Location> locations) {
                        return Observable.from(locations);
                    }
                })
                .filter(new Func1<Location, Boolean>() {
                    @Override
                    public Boolean call(Location location) {
                        // Filter to allow all locations if use custom locations setting is on
                        // Otherwise only allow default locations (those without random UUIDs)
                        return mPreferences.getUsesCustomLocations() || location.getId().length() <= 2;
                    }
                })
                .toList()
                .subscribeOn(mSchedulerProvider.computation())
                .observeOn(mSchedulerProvider.ui())
                .subscribe(new Observer<List<Location>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(List<Location> locations) {
                        Location randomLocation = getRandomLocation(locations);
                        mGameLobbyView.setLocation(randomLocation);
                    }
                });
        mSubscriptions.add(subscription);
    }

    private Location getRandomLocation(List<Location> locations) {
        Random random = new Random();
        return locations.get(random.nextInt(locations.size()));
    }

}
