package com.apicellaj.android.spyhunt.gamelobby;

import com.apicellaj.android.spyhunt.BasePresenter;
import com.apicellaj.android.spyhunt.BaseView;
import com.apicellaj.android.spyhunt.data.Location;

import java.util.List;

public interface GameLobbyContract {

    interface View extends BaseView<GameLobbyPresenter> {

        void showPlayers(List<String> players);

        void setLocation(Location location);

        boolean isActive();

    }

    interface Presenter extends BasePresenter {

        void loadPlayerNames();

        void savePlayerNames(List<String> players);

        void loadLocations();

    }
}
