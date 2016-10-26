package com.apicellaj.android.spyhunt.gamelobby;

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

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the implementation of {@link GameLobbyPresenter}.
 */
public class GameLobbyPresenterTest {

    private static List<Location> LOCATIONS;

    @Mock
    private LocationsLocalDataSource mDataSource;

    @Mock
    private SharedPreferencesRepository mPreferences;

    @Mock
    private GameLobbyContract.View mGameLobbyView;

    private BaseSchedulerProvider mSchedulerProvider;

    private GameLobbyPresenter mGameLobbyPresenter;

    @Before
    public void setupMocksAndView() {
        MockitoAnnotations.initMocks(this);

        mSchedulerProvider = new ImmediateSchedulerProvider();

        when(mGameLobbyView.isActive()).thenReturn(true);

        LOCATIONS = Lists.newArrayList(new Location("Title1", new String[7]),
                new Location("Title2", new String[7]), new Location("Title3", new String[7]));
    }

    @Test
    public void loadPlayersNames_callsShowsPlayers() {
        mGameLobbyPresenter =
                new GameLobbyPresenter(mDataSource, mPreferences, mGameLobbyView, mSchedulerProvider);

        when(mPreferences.getUseSavedPlayers()).thenReturn(true);

        // When the player names are loaded
        mGameLobbyPresenter.loadPlayerNames();

        // Then the names are shown
        verify(mGameLobbyView).showPlayers(anyListOf(String.class));
    }

    @Test
    public void loadLocations_callsRepoAndSetsLocation() {
        mGameLobbyPresenter =
                new GameLobbyPresenter(mDataSource, mPreferences, mGameLobbyView, mSchedulerProvider);

        when(mDataSource.getLocations()).thenReturn(Observable.just(LOCATIONS));
        when(mPreferences.getUsesCustomLocations()).thenReturn(true);

        // When locations are loaded
        mGameLobbyPresenter.loadLocations();

        // Then a random location is set
        verify(mGameLobbyView).setLocation(any(Location.class));
    }

    @Test
    public void savePlayers_callsPreferencesAndSaves() {
        mGameLobbyPresenter =
                new GameLobbyPresenter(mDataSource, mPreferences, mGameLobbyView, mSchedulerProvider);

        // When player names are saved
        mGameLobbyPresenter.savePlayerNames(anyListOf(String.class));

        // Then they are stored into preferences
        verify(mPreferences).setSavedPlayers(anyListOf(String.class));
    }
}