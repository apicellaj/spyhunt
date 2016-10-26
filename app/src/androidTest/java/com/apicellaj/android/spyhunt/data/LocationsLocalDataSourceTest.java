package com.apicellaj.android.spyhunt.data;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.apicellaj.android.spyhunt.data.database.LocationsLocalDataSource;
import com.apicellaj.android.spyhunt.util.schedulers.BaseSchedulerProvider;
import com.apicellaj.android.spyhunt.util.schedulers.ImmediateSchedulerProvider;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;
import rx.observers.TestSubscriber;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

/**
 * Integration test for the {@link com.apicellaj.android.spyhunt.data.database.LocationsLocalDataSource}, which uses the {@link com.apicellaj.android.spyhunt.data.database.LocationsDbHelper}
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class LocationsLocalDataSourceTest {

    private final static String LOCATION1 = "location1";

    private final static String LOCATION2 = "location2";

    private final static String[] ROLES1 = {"a", "b", "c", "d", "e", "f", "g"};

    private final static String[] ROLES2 = {"1", "2", "3", "4", "5", "6", "7"};

    private LocationsLocalDataSource mLocalDataSource;

    @Before
    public void setup() {
        LocationsLocalDataSource.destroyInstance();
        BaseSchedulerProvider schedulerProvider = new ImmediateSchedulerProvider();

        mLocalDataSource = LocationsLocalDataSource.getInstance(
                InstrumentationRegistry.getTargetContext(), schedulerProvider);
    }

    @After
    public void cleanUp() {
        mLocalDataSource.deleteAllCustomLocations();
    }

    @Test
    public void testPreConditions() {
        assertNotNull(mLocalDataSource);
    }

    @Test
    public void addLocation_retrievesLocations() {
        // Given a new location
        Location newLocation = new Location(LOCATION1, ROLES1);

        // When saved into the repository
        mLocalDataSource.addLocation(newLocation);

        // Then the location can be retrieved from the repository
        TestSubscriber<Location> testSubscriber = new TestSubscriber<>();
        mLocalDataSource.getLocation(newLocation.getId()).subscribe(testSubscriber);
        testSubscriber.assertValue(newLocation);
    }

    @Test
    public void deleteLocation_retrievesEmptyList() {
        // Given a single new custom location
        Location newLocation = new Location(LOCATION1, ROLES1);

        // When saved into the repository
        mLocalDataSource.addLocation(newLocation);

        // When the single custom location is deleted
        mLocalDataSource.deleteLocation(newLocation.getId());

        // Then the retrieved locations is an empty list
        TestSubscriber<List<Location>> testSubscriber = new TestSubscriber<>();
        mLocalDataSource.getLocations()
                .flatMap(new Func1<List<Location>, Observable<Location>>() {
                    @Override
                    public Observable<Location> call(List<Location> locations) {
                        return Observable.from(locations);
                    }
                })
                .filter(new Func1<Location, Boolean>() {
                    @Override
                    public Boolean call(Location location) {
                        return location.getId().length() > 3;
                    }
                })
                .toList()
                .subscribe(testSubscriber);
        List<Location> result = testSubscriber.getOnNextEvents().get(0);
        assertThat(result.isEmpty(), is(true));
    }

    @Test
    public void getLocations() {
        // Given two new locations in the repository
        final Location newLocation1 = new Location(LOCATION1, ROLES1);
        mLocalDataSource.addLocation(newLocation1);
        final Location newLocation2 = new Location(LOCATION2, ROLES2);
        mLocalDataSource.addLocation(newLocation2);

        // Then the two locations can be retrieved
        TestSubscriber<List<Location>> testSubscriber = new TestSubscriber<>();
        mLocalDataSource.getLocations()
                .flatMap(new Func1<List<Location>, Observable<Location>>() {
                    @Override
                    public Observable<Location> call(List<Location> locations) {
                        return Observable.from(locations);
                    }
                })
                .filter(new Func1<Location, Boolean>() {
                    @Override
                    public Boolean call(Location location) {
                        return location.getId().length() > 3;
                    }
                })
                .toList()
                .subscribe(testSubscriber);

        List<Location> result = testSubscriber.getOnNextEvents().get(0);
        assertThat(result, hasItems(newLocation1, newLocation2));
    }
}
