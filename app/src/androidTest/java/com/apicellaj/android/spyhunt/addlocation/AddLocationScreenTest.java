package com.apicellaj.android.spyhunt.addlocation;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.apicellaj.android.spyhunt.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Tests for the add location screen.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class AddLocationScreenTest {

    @Rule
    public final IntentsTestRule<AddLocationActivity> mAddLocationActivityIntentsTestRule =
            new IntentsTestRule<>(AddLocationActivity.class);

    @Before
    public void registerIdlingResource() {
        Espresso.registerIdlingResources(
                mAddLocationActivityIntentsTestRule.getActivity().getCountingIdlingResource());
    }

    @Test
    public void errorShowOnEmptyLocation() {
        // Add empty location title
        onView(withId(R.id.add_location_title_edit_text)).perform(typeText(""));

        // Add location roles
        onView(withId(R.id.add_location_role1_edit_text)).perform(typeText("Test"));
        onView(withId(R.id.add_location_role2_edit_text)).perform(typeText("Test"));
        onView(withId(R.id.add_location_role3_edit_text)).perform(typeText("Test"));
        onView(withId(R.id.add_location_role4_edit_text)).perform(typeText("Test"));
        onView(withId(R.id.add_location_role5_edit_text)).perform(typeText("Test"));
        onView(withId(R.id.add_location_role6_edit_text)).perform(typeText("Test"));
        onView(withId(R.id.add_location_role7_edit_text)).perform(typeText("Test"));

        // Save the location
        onView(withId(R.id.save_location_fab)).perform(click());

        // Verify empty location snackbar is shown
        String emptyLocationMessageText = getTargetContext().getString(R.string.add_location_error);
        onView(withText(emptyLocationMessageText)).check(matches(isDisplayed()));
    }

    /**
     * Unregister IdlingResource so it can be garbage collected and does not leak memory.
     */
    @After
    public void unregisterIdlingResource() {
        Espresso.unregisterIdlingResources(
                mAddLocationActivityIntentsTestRule.getActivity().getCountingIdlingResource());
    }
}
