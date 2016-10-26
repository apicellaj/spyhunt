package com.apicellaj.android.spyhunt.util;

import android.support.test.espresso.IdlingResource;

/**
 * Contains a static reference to {@link android.support.test.espresso.IdlingResource}
 */
public class EspressoIdlingResource {

    private static final String RESOURCE = "GLOBAL";

    private static final SimpleCountingIdlingResource mCountingIdlingResource =
            new SimpleCountingIdlingResource(RESOURCE);

    public static void increment() {
        mCountingIdlingResource.increment();
    }

    public static void decrement() {
        mCountingIdlingResource.decrement();
    }

    public static IdlingResource getIdlingResource() {
        return mCountingIdlingResource;
    }
}
