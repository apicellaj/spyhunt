package com.apicellaj.android.spyhunt.gamedetail;

import com.apicellaj.android.spyhunt.BasePresenter;
import com.apicellaj.android.spyhunt.BaseView;

import java.util.List;

interface GameDetailContract {

    interface View extends BaseView<GameDetailPresenter> {

        void setLocationTitles(List<String> locationTitles);

        boolean isAddedToActivity();

        void setTime(long timeRemaining);

        void showPaused();

        void showResumed();

        void showPlayAgain();

        void finishGame();

        boolean isActive();

    }

    interface Presenter extends BasePresenter {

        void loadLocations();

        void createTimer(long timeRemaining);

        void pauseTimer();

        void resumeTimer();

        void endGame();

        boolean isTimerPaused();

        long getTimeRemaining();

        boolean isGameOver();

        int getTime();

    }
}
