package com.apicellaj.android.spyhunt.customlocations;

import com.apicellaj.android.spyhunt.BasePresenter;
import com.apicellaj.android.spyhunt.BaseView;
import com.apicellaj.android.spyhunt.data.Location;

import java.util.List;

public interface CustomLocationsContract {

    interface View extends BaseView<CustomLocationsPresenter> {

        void showEmptyTextView();

        void showListView(List<Location> locations);

        boolean isActive();

    }

    interface Presenter extends BasePresenter {

        void loadCustomLocations();

        void processLocations(List<Location> locations);

        void addNewLocation(Location location);

        void deleteLocation(String locationId);

    }
}
