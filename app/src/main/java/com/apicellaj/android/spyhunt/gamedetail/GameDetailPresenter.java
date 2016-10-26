package com.apicellaj.android.spyhunt.gamedetail;

import android.os.CountDownTimer;
import android.support.annotation.NonNull;

import com.apicellaj.android.spyhunt.data.Location;
import com.apicellaj.android.spyhunt.data.database.LocationsLocalDataSource;
import com.apicellaj.android.spyhunt.preferences.SharedPreferencesRepository;
import com.apicellaj.android.spyhunt.util.schedulers.BaseSchedulerProvider;

import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.functions.Func1;
import rx.subscriptions.CompositeSubscription;

import static com.google.common.base.Preconditions.checkNotNull;

public class GameDetailPresenter implements GameDetailContract.Presenter {

    private static final int DEFAULT_TIME = 8;
    private static final int DECREMENT_INTERVAL = 1000;
    private static final long MAX_TIME = Long.MAX_VALUE;

    @NonNull
    private final LocationsLocalDataSource mDataSource;

    @NonNull
    private final SharedPreferencesRepository mPreferences;

    @NonNull
    private final GameDetailContract.View mGameDetailView;

    @NonNull
    private final BaseSchedulerProvider mSchedulerProvider;

    @NonNull
    private final CompositeSubscription mSubscriptions;

    private GameCountDownTimer mCountDownTimer;

    public GameDetailPresenter(@NonNull LocationsLocalDataSource dataSource,
                               @NonNull SharedPreferencesRepository preferences,
                               @NonNull GameDetailContract.View gameDetailView,
                               @NonNull BaseSchedulerProvider schedulerProvider) {
        mDataSource = checkNotNull(dataSource);
        mPreferences = checkNotNull(preferences);
        mGameDetailView = checkNotNull(gameDetailView);
        mSchedulerProvider = checkNotNull(schedulerProvider);

        mSubscriptions = new CompositeSubscription();
        mGameDetailView.setPresenter(this);
    }

    @Override
    public void subscribe() {
        loadLocations();
    }

    @Override
    public void unsubscribe() {
        mSubscriptions.clear();
    }

    @Override
    public void loadLocations() {

        mSubscriptions.clear();
        Subscription subscription = mDataSource
                .getLocations()
                .flatMap(new Func1<List<Location>, Observable<Location>>() {
                    @Override
                    public Observable<Location> call(List<Location> locations) {
                        return Observable.from(locations);
                    }
                })
                .filter(new Func1<Location, Boolean>() {
                    @Override
                    public Boolean call(Location location) {
                        return mPreferences.getUsesCustomLocations() || location.getId().length() <= 2;
                    }
                })
                .map(new Func1<Location, String>() {
                    @Override
                    public String call(Location location) {
                        return location.getTitle();
                    }
                })
                .toList()
                .subscribeOn(mSchedulerProvider.computation())
                .observeOn(mSchedulerProvider.ui())
                .subscribe(new Observer<List<String>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(List<String> locations) {
                        processLocations(locations);
                    }
                });
        mSubscriptions.add(subscription);
    }

    private void processLocations(List<String> locations) {
        mGameDetailView.setLocationTitles(locations);
    }

    @Override
    public int getTime() {
        return mPreferences.getUsesCustomTimer() ? mPreferences.getCustomTime() : DEFAULT_TIME;
    }

    @Override
    public void createTimer(long timeRemaining) {
        // If the game is over we do not create another timer
        if (mCountDownTimer != null && mCountDownTimer.isGameOver()) {
            return;
        }
        mCountDownTimer = new GameCountDownTimer(timeRemaining, MAX_TIME, DECREMENT_INTERVAL);
    }

    @Override
    public void pauseTimer() {
        mCountDownTimer.pause();
        mGameDetailView.showPaused();
    }

    @Override
    public void resumeTimer() {
        mCountDownTimer.resume();
        mGameDetailView.showResumed();
    }

    @Override
    public void endGame() {
        mCountDownTimer.setTimeRemaining(0);
    }

    @Override
    public long getTimeRemaining() {
        return mCountDownTimer.getTimeRemaining();
    }

    @Override
    public boolean isTimerPaused() {
        return mCountDownTimer.isPaused();
    }

    @Override
    public boolean isGameOver() {
        return mCountDownTimer.isGameOver();
    }

    private class GameCountDownTimer extends CountDownTimer {
        private long mTimeRemaining;
        private boolean isPaused;
        private boolean isGameOver;

        GameCountDownTimer(long timeRemaining, long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
            mTimeRemaining = timeRemaining;
            isPaused = true;
            isGameOver = timeRemaining <= 0;
            start();
        }

        @Override
        public void onTick(long millisUntilFinished) {
            if (isGameOver) {
                return;
            }
            if (mGameDetailView.isAddedToActivity() && !isPaused) {
                mTimeRemaining -= DECREMENT_INTERVAL;
                mGameDetailView.setTime(mTimeRemaining);
            }
            if (mTimeRemaining <= 0) {
                mTimeRemaining = 0;
                mGameDetailView.setTime(mTimeRemaining);
                mGameDetailView.showPlayAgain();
                isGameOver = true;
                onFinish();
            }
        }

        @Override
        public void onFinish() {
            if (mGameDetailView.isAddedToActivity()) {
                mGameDetailView.finishGame();
            }
            cancel();
        }

        void pause() {
            isPaused = true;
        }

        void resume() {
            isPaused = false;
        }

        long getTimeRemaining() {
            return mTimeRemaining;
        }

        void setTimeRemaining(long timeRemaining) {
            mTimeRemaining = timeRemaining;
        }

        boolean isPaused() {
            return isPaused;
        }

        boolean isGameOver() {
            return isGameOver;
        }
    }
}
