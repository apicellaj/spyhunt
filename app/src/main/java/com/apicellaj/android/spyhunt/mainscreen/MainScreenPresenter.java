package com.apicellaj.android.spyhunt.mainscreen;

import android.support.annotation.NonNull;

import com.apicellaj.android.spyhunt.util.schedulers.BaseSchedulerProvider;

import rx.subscriptions.CompositeSubscription;

import static com.google.common.base.Preconditions.checkNotNull;

public class MainScreenPresenter implements MainScreenContract.Presenter {


    @NonNull
    private final MainScreenContract.View mMainScreenView;

    @NonNull
    private final BaseSchedulerProvider mSchedulerProvider;

    @NonNull
    private final CompositeSubscription mSubscriptions;

    public MainScreenPresenter(@NonNull MainScreenContract.View mainScreenView,
                               @NonNull BaseSchedulerProvider schedulerProvider) {
        mMainScreenView = checkNotNull(mainScreenView, "mainScreenView cannot be null");
        mSchedulerProvider = checkNotNull(schedulerProvider, "scheduleProvider cannot be null");

        mSubscriptions = new CompositeSubscription();
        mMainScreenView.setPresenter(this);
    }

    @Override
    public void subscribe() {
        // There is nothing we need to do when subscribed
    }

    @Override
    public void unsubscribe() {
        mSubscriptions.clear();
    }
}
