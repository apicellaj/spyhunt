package com.apicellaj.android.spyhunt.customlocations;

import android.support.annotation.NonNull;

import com.apicellaj.android.spyhunt.data.Location;
import com.apicellaj.android.spyhunt.data.database.LocationsLocalDataSource;
import com.apicellaj.android.spyhunt.util.schedulers.BaseSchedulerProvider;

import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.functions.Func1;
import rx.subscriptions.CompositeSubscription;

import static com.google.common.base.Preconditions.checkNotNull;

public class CustomLocationsPresenter implements CustomLocationsContract.Presenter {

    @NonNull
    private final LocationsLocalDataSource mLocalDataSource;

    @NonNull
    private final CustomLocationsContract.View mCustomLocationsView;

    @NonNull
    private final BaseSchedulerProvider mSchedulerProvider;

    @NonNull
    private final CompositeSubscription mSubscriptions;

    public CustomLocationsPresenter(@NonNull LocationsLocalDataSource localDataSource,
                                    @NonNull CustomLocationsContract.View customLocationsView,
                                    @NonNull BaseSchedulerProvider schedulerProvider) {
        mLocalDataSource = checkNotNull(localDataSource, "localDataSource cannot be null");
        mCustomLocationsView = checkNotNull(customLocationsView, "customLocationsView cannot be null");
        mSchedulerProvider = checkNotNull(schedulerProvider, "schedulerProvider cannot be null");

        mSubscriptions = new CompositeSubscription();
        mCustomLocationsView.setPresenter(this);
    }

    @Override
    public void subscribe() {
        loadCustomLocations();
    }

    @Override
    public void unsubscribe() {
        mSubscriptions.clear();
    }

    @Override
    public void loadCustomLocations() {
        mSubscriptions.clear();
        Subscription subscription = mLocalDataSource
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
                        // Filter to get custom locations (where the id is a random UUID)
                        return location.getId().length() > 2;
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
                        processLocations(locations);
                    }
                });
        mSubscriptions.add(subscription);
    }

    @Override
    public void processLocations(List<Location> locations) {
        if (locations.isEmpty()) {
            mCustomLocationsView.showEmptyTextView();
        } else {
            mCustomLocationsView.showListView(locations);
        }
    }

    @Override
    public void addNewLocation(Location location) {
        mLocalDataSource.addLocation(location);
    }

    @Override
    public void deleteLocation(String locationId) {
        mLocalDataSource.deleteLocation(locationId);
        loadCustomLocations();
    }
}
