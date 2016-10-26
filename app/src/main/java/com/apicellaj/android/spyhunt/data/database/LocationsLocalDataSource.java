package com.apicellaj.android.spyhunt.data.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.apicellaj.android.spyhunt.data.Location;
import com.apicellaj.android.spyhunt.data.database.LocationsPersistenceContract.LocationEntry;
import com.apicellaj.android.spyhunt.util.schedulers.BaseSchedulerProvider;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

import static com.google.common.base.Preconditions.checkNotNull;

public class LocationsLocalDataSource implements LocationsDataSource {

    @Nullable
    private static LocationsLocalDataSource INSTANCE;

    @NonNull
    private final BriteDatabase mDatabaseHelper;

    @NonNull
    private final Func1<Cursor, Location> mLocationsMapperFunction;

    // Prevent direct instantiation
    private LocationsLocalDataSource(@NonNull Context context,
                                     @NonNull BaseSchedulerProvider schedulerProvider) {
        checkNotNull(context, "context cannot be null");
        checkNotNull(schedulerProvider, "scheduleProvider cannot be null");
        LocationsDbHelper dbHelper = new LocationsDbHelper(context);
        SqlBrite sqlBrite = SqlBrite.create();
        mDatabaseHelper = sqlBrite.wrapDatabaseHelper(dbHelper, schedulerProvider.io());
        mLocationsMapperFunction = new Func1<Cursor, Location>() {
            @Override
            public Location call(Cursor c) {
                String itemId = c.getString(c.getColumnIndexOrThrow(LocationEntry.COLUMN_NAME_LOCATION_ID));
                String title = c.getString(c.getColumnIndexOrThrow(LocationEntry.COLUMN_NAME_LOCATION_TITLE));
                String role1 = c.getString(c.getColumnIndexOrThrow(LocationEntry.COLUMN_NAME_LOCATION_ROLE_1));
                String role2 = c.getString(c.getColumnIndexOrThrow(LocationEntry.COLUMN_NAME_LOCATION_ROLE_2));
                String role3 = c.getString(c.getColumnIndexOrThrow(LocationEntry.COLUMN_NAME_LOCATION_ROLE_3));
                String role4 = c.getString(c.getColumnIndexOrThrow(LocationEntry.COLUMN_NAME_LOCATION_ROLE_4));
                String role5 = c.getString(c.getColumnIndexOrThrow(LocationEntry.COLUMN_NAME_LOCATION_ROLE_5));
                String role6 = c.getString(c.getColumnIndexOrThrow(LocationEntry.COLUMN_NAME_LOCATION_ROLE_6));
                String role7 = c.getString(c.getColumnIndexOrThrow(LocationEntry.COLUMN_NAME_LOCATION_ROLE_7));
                String[] roles = {role1, role2, role3, role4, role5, role6, role7};
                return new Location(itemId, title, roles);
            }
        };
    }

    public static LocationsLocalDataSource getInstance(
            @NonNull Context context,
            @NonNull BaseSchedulerProvider schedulerProvider) {
        if (INSTANCE == null) {
            INSTANCE = new LocationsLocalDataSource(context, schedulerProvider);
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    public void deleteAllCustomLocations() {
        mDatabaseHelper.delete(LocationEntry.CUSTOM_TABLE_NAME, null);
    }

    @Override
    public Observable<List<Location>> getLocations() {

        // Provide Iterable<String> to monitor multiple tables for changes
        ArrayList<String> tableNames = new ArrayList<>();
        tableNames.add(LocationEntry.DEFAULT_TABLE_NAME);
        tableNames.add(LocationEntry.CUSTOM_TABLE_NAME);

        String sqlQuery = "SELECT * FROM " + LocationEntry.DEFAULT_TABLE_NAME +
                " UNION SELECT * FROM " + LocationEntry.CUSTOM_TABLE_NAME +
                " ORDER BY " + LocationEntry.COLUMN_NAME_LOCATION_TITLE + " ASC " + ";";
        return mDatabaseHelper.createQuery(tableNames, sqlQuery)
                .mapToList(mLocationsMapperFunction).first();
    }

    @Override
    public Observable<Location> getLocation(@NonNull String locationId) {
        String sql = String.format("SELECT * FROM %s WHERE %s LIKE ?",
                LocationEntry.CUSTOM_TABLE_NAME, LocationEntry.COLUMN_NAME_LOCATION_ID);
        return mDatabaseHelper.createQuery(LocationEntry.CUSTOM_TABLE_NAME, sql, locationId)
                .mapToOneOrDefault(mLocationsMapperFunction, null);
    }

    @Override
    public void addLocation(@NonNull Location location) {
        checkNotNull(location);
        ContentValues values = new ContentValues();
        String[] roles = location.getRoles();
        values.put(LocationEntry.COLUMN_NAME_LOCATION_ID, location.getId());
        values.put(LocationEntry.COLUMN_NAME_LOCATION_TITLE, location.getTitle());
        values.put(LocationEntry.COLUMN_NAME_LOCATION_ROLE_1, roles[0]);
        values.put(LocationEntry.COLUMN_NAME_LOCATION_ROLE_2, roles[1]);
        values.put(LocationEntry.COLUMN_NAME_LOCATION_ROLE_3, roles[2]);
        values.put(LocationEntry.COLUMN_NAME_LOCATION_ROLE_4, roles[3]);
        values.put(LocationEntry.COLUMN_NAME_LOCATION_ROLE_5, roles[4]);
        values.put(LocationEntry.COLUMN_NAME_LOCATION_ROLE_6, roles[5]);
        values.put(LocationEntry.COLUMN_NAME_LOCATION_ROLE_7, roles[6]);
        mDatabaseHelper.insert(LocationEntry.CUSTOM_TABLE_NAME, values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    @Override
    public void deleteLocation(@NonNull String locationId) {
        String selection = LocationEntry.COLUMN_NAME_LOCATION_ID + " LIKE ?";
        String[] selectionArgs = {locationId};
        mDatabaseHelper.delete(LocationEntry.CUSTOM_TABLE_NAME, selection, selectionArgs);
    }
}
