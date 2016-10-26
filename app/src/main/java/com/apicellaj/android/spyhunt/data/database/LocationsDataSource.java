package com.apicellaj.android.spyhunt.data.database;

import android.support.annotation.NonNull;

import com.apicellaj.android.spyhunt.data.Location;

import java.util.List;

import rx.Observable;

/**
 * Entry point for accessing locations data.
 */
interface LocationsDataSource {

    Observable<List<Location>> getLocations();

    Observable<Location> getLocation(String locationId);

    void addLocation(@NonNull Location location);

    void deleteLocation(@NonNull String locationId);
}
