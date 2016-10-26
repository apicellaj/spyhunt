package com.apicellaj.android.spyhunt.mainscreen;

import com.apicellaj.android.spyhunt.BasePresenter;
import com.apicellaj.android.spyhunt.BaseView;

public interface MainScreenContract {

    interface View extends BaseView<MainScreenPresenter> {

        void showLobby();

        void showHowToPlay();

        void showSettingsMenu();

    }

    interface Presenter extends BasePresenter {

        // No model layer to interact with

    }

}
