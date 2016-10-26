package com.apicellaj.android.spyhunt.data.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

/**
 * Class to open the pre-populated SQLite database with default locations
 */
public class LocationsDbHelper extends SQLiteAssetHelper {

    private static final String DATABASE_NAME = "spyhunt.db";

    private static final int DATABASE_VERSION = 1;

    LocationsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Not currently required
    }
}
