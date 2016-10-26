package com.apicellaj.android.spyhunt.gamedetail;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
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

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.pressBack;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.not;

/**
 * Tests for the game detail screen.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class GameDetailScreenTest {

    private final static String PLAYERS_JSON = "[\"Player One\",\"Player Two\",\"Player Three\"]";

    private final static String ROLES_JSON = "[\"Butcher\",\"Cashier\",\"Spy\"]";

    private final static String LOCATION = "Supermarket";

    @Rule
    public final IntentsTestRule<GameDetailActivity> mGameDetailActivityIntentsTestRule =
            new IntentsTestRule<GameDetailActivity>(GameDetailActivity.class) {

                @Override
                protected Intent getActivityIntent() {
                    Context targetContext = InstrumentationRegistry.getTargetContext();
                    Intent startIntent = new Intent(targetContext, GameDetailActivity.class);
                    startIntent.putExtra("players", PLAYERS_JSON);
                    startIntent.putExtra("roles", ROLES_JSON);
                    startIntent.putExtra("locationTitle", LOCATION);
                    return startIntent;
                }
            };

    @Before
    public void registerIdlingResource() {
        Espresso.registerIdlingResources(
                mGameDetailActivityIntentsTestRule.getActivity().getCountingIdlingResource());
    }

    @Test
    public void disableRoleButtonAfterClick() {
        /**
         * Role button 1 test
         */

        // Role button is initially enabled
        onView(withId(R.id.role_1_button)).check(matches(isEnabled()));

        // Click role button
        onView(withId(R.id.role_1_button)).perform(click());

        // Press back to remove dialog
        onView(withText("Player One")).perform(pressBack());

        // Verify role button is now disabled
        onView(withId(R.id.role_1_button)).check(matches(not(isEnabled())));


        /**
         * Role button 2 test
         */

        // Role button is initially enabled
        onView(withId(R.id.role_2_button)).check(matches(isEnabled()));

        // Click role button
        onView(withId(R.id.role_2_button)).perform(click());

        // Press back to remove dialog
        onView(withText("Player Two")).perform(pressBack());

        // Verify role button is now disabled
        onView(withId(R.id.role_2_button)).check(matches(not(isEnabled())));
    }

    @Test
    public void hideLocationsGridViewTest() {
        // Verify that the locations grid view is visible
        onView(withId(R.id.game_detail_grid_view_layout)).check(matches(isDisplayed()));

        // Click hide locations button
        onView(withId(R.id.game_detail_show_locations_button)).perform(click());

        // Verify that the locations grid view is gone
        onView(withId(R.id.game_detail_grid_view_layout)).check(matches(not(isDisplayed())));
    }

    @Test
    public void hideRolesTest() {
        // Verify that the locations grid view is visible
        onView(withId(R.id.game_detail_role_layout)).check(matches(isDisplayed()));

        // Click hide roles button
        onView(withId(R.id.game_detail_show_roles_button)).perform(click());

        // Verify that the locations grid view is gone
        onView(withId(R.id.game_detail_role_layout)).check(matches(not(isDisplayed())));
    }

    /**
     * Unregister IdlingResource so it can be garbage collected and does not leak memory.
     */
    @After
    public void unregisterIdlingResource() {
        Espresso.unregisterIdlingResources(
                mGameDetailActivityIntentsTestRule.getActivity().getCountingIdlingResource());
    }

}
