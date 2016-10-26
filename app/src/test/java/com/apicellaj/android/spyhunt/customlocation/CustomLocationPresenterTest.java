package com.apicellaj.android.spyhunt.customlocation;

import com.apicellaj.android.spyhunt.customlocations.CustomLocationsContract;
import com.apicellaj.android.spyhunt.customlocations.CustomLocationsPresenter;
import com.apicellaj.android.spyhunt.data.Location;
import com.apicellaj.android.spyhunt.data.database.LocationsLocalDataSource;
import com.apicellaj.android.spyhunt.util.schedulers.BaseSchedulerProvider;
import com.apicellaj.android.spyhunt.util.schedulers.ImmediateSchedulerProvider;
import com.google.common.collect.Lists;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import rx.Observable;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the implementation of {@link CustomLocationsPresenter}.
 */
public class CustomLocationPresenterTest {

    private static List<Location> LOCATIONS;

    private static List<Location> MOCK_DEFAULT_LOCATIONS;

    private static List<Location> EMPTY_LOCATIONS;

    @Mock
    private LocationsLocalDataSource mDataSource;

    @Mock
    private CustomLocationsContract.View mCustomLocationsView;

    private BaseSchedulerProvider mSchedulerProvider;

    private CustomLocationsPresenter mCustomLocationsPresenter;

    @Before
    public void setupMocksAndView() {
        MockitoAnnotations.initMocks(this);

        mSchedulerProvider = new ImmediateSchedulerProvider();

        when(mCustomLocationsView.isActive()).thenReturn(true);

        LOCATIONS = Lists.newArrayList(new Location("Title1", new String[7]),
                new Location("Title2", new String[7]), new Location("Title3", new String[7]));

        MOCK_DEFAULT_LOCATIONS = Lists.newArrayList(new Location("1", "Title1", new String[7]),
                new Location("2", "Title2", new String[7]),
                new Location("3", "Title3", new String[7]));

        EMPTY_LOCATIONS = Lists.newArrayList();
    }

    @Test
    public void loadCustomLocations_showEmptyWithEmptyList() {
        mCustomLocationsPresenter =
                new CustomLocationsPresenter(mDataSource, mCustomLocationsView, mSchedulerProvider);

        when(mDataSource.getLocations()).thenReturn(Observable.just(EMPTY_LOCATIONS));

        // When an empty list of custom locations are loaded
        mCustomLocationsPresenter.loadCustomLocations();

        // Then an empty TextView should appear
        verify(mCustomLocationsView).showEmptyTextView();
    }

    @Test
    public void loadCustomLocations_showEmptyWithDefaultLocationsList() {
        mCustomLocationsPresenter =
                new CustomLocationsPresenter(mDataSource, mCustomLocationsView, mSchedulerProvider);

        when(mDataSource.getLocations()).thenReturn(Observable.just(MOCK_DEFAULT_LOCATIONS));

        // When only default locations are loaded
        mCustomLocationsPresenter.loadCustomLocations();

        // Then an empty TextView should appear
        verify(mCustomLocationsView).showEmptyTextView();
    }

    @Test
    public void loadCustomLocations_showListViewWithLocations() {
        mCustomLocationsPresenter =
                new CustomLocationsPresenter(mDataSource, mCustomLocationsView, mSchedulerProvider);

        when(mDataSource.getLocations()).thenReturn(Observable.just(LOCATIONS));

        // When a list of custom locations are loaded
        mCustomLocationsPresenter.loadCustomLocations();

        // Then the ListView should be populated
        verify(mCustomLocationsView).showListView(anyListOf(Location.class));
    }

    @Test
    public void addNewLocation_callsRepoAndAddsLocation() {
        mCustomLocationsPresenter =
                new CustomLocationsPresenter(mDataSource, mCustomLocationsView, mSchedulerProvider);

        // When a new location is added
        mCustomLocationsPresenter.addNewLocation(any(Location.class));

        // The LocalDataSource should add the location
        verify(mDataSource).addLocation(any(Location.class));
    }

    @Test
    public void deleteLocation_callsRepoAndDeletesLocation() {
        mCustomLocationsPresenter =
                new CustomLocationsPresenter(mDataSource, mCustomLocationsView, mSchedulerProvider);

        when(mDataSource.getLocations()).thenReturn(Observable.just(LOCATIONS));

        // When a location is deleted
        mCustomLocationsPresenter.deleteLocation(any(String.class));

        // The LocalDataSource should remove the location
        verify(mDataSource).deleteLocation(any(String.class));

        // The ListView should then be populated with any remaining locations
        verify(mCustomLocationsView).showListView(anyListOf(Location.class));
    }
}
