package com.apicellaj.android.spyhunt.gamedetail;

import com.apicellaj.android.spyhunt.data.Location;
import com.apicellaj.android.spyhunt.data.database.LocationsLocalDataSource;
import com.apicellaj.android.spyhunt.preferences.SharedPreferencesRepository;
import com.apicellaj.android.spyhunt.util.schedulers.BaseSchedulerProvider;
import com.apicellaj.android.spyhunt.util.schedulers.ImmediateSchedulerProvider;
import com.google.common.collect.Lists;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import rx.Observable;

import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the implementation of {@link GameDetailPresenter}.
 */
public class GameDetailPresenterTest {

    private static List<Location> LOCATIONS;

    @Mock
    private LocationsLocalDataSource mDataSource;

    @Mock
    private SharedPreferencesRepository mPreferences;

    @Mock
    GameDetailContract.View mGameDetailView;

    private BaseSchedulerProvider mSchedulerProvider;

    private GameDetailPresenter mGameDetailPresenter;

    @Before
    public void setupMocksAndView() {
        MockitoAnnotations.initMocks(this);

        mSchedulerProvider = new ImmediateSchedulerProvider();

        when(mGameDetailView.isActive()).thenReturn(true);

        LOCATIONS = Lists.newArrayList(new Location("Title1", new String[7]),
                new Location("Title2", new String[7]), new Location("Title3", new String[7]));

    }

    @Test
    public void loadLocations_callsRepoAndPopulatesViewWithLocations() {
        mGameDetailPresenter =
                new GameDetailPresenter(mDataSource, mPreferences, mGameDetailView, mSchedulerProvider);

        when(mDataSource.getLocations()).thenReturn(Observable.just(LOCATIONS));

        mGameDetailPresenter.loadLocations();

        verify(mGameDetailView).setLocationTitles(anyListOf(String.class));
    }

    @Test
    public void getTime_callsSharedPreferencesForTime() {
        mGameDetailPresenter =
                new GameDetailPresenter(mDataSource, mPreferences, mGameDetailView, mSchedulerProvider);

        when(mPreferences.getUsesCustomTimer()).thenReturn(true);

        // When presenter gets the time
        mGameDetailPresenter.getTime();

        // Then the preferences must determine if a custom timer is being used
        verify(mPreferences).getUsesCustomTimer();

        // And that the custom time was fetched from SharedPreferences
        verify(mPreferences).getCustomTime();
    }

    @Test
    public void createTimer_showsTimerPaused() {
        mGameDetailPresenter =
                new GameDetailPresenter(mDataSource, mPreferences, mGameDetailView, mSchedulerProvider);
    }
}