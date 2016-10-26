package com.apicellaj.android.spyhunt.data.database;

import android.provider.BaseColumns;

class LocationsPersistenceContract {

    // To prevent instantiation of the contract class
    private LocationsPersistenceContract() {
    }

    // Contract for table contents
    static abstract class LocationEntry implements BaseColumns {
        static final String DEFAULT_TABLE_NAME = "default_locations";
        static final String CUSTOM_TABLE_NAME = "custom_locations";
        static final String COLUMN_NAME_LOCATION_ID = "id";
        static final String COLUMN_NAME_LOCATION_TITLE = "title";
        static final String COLUMN_NAME_LOCATION_ROLE_1 = "role1";
        static final String COLUMN_NAME_LOCATION_ROLE_2 = "role2";
        static final String COLUMN_NAME_LOCATION_ROLE_3 = "role3";
        static final String COLUMN_NAME_LOCATION_ROLE_4 = "role4";
        static final String COLUMN_NAME_LOCATION_ROLE_5 = "role5";
        static final String COLUMN_NAME_LOCATION_ROLE_6 = "role6";
        static final String COLUMN_NAME_LOCATION_ROLE_7 = "role7";
    }
}
