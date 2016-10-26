package com.apicellaj.android.spyhunt.gamelobby;

import android.support.test.espresso.Espresso;
import android.support.test.rule.ActivityTestRule;
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
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Tests for the game lobby screen.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class GameLobbyScreenTest {

    @Rule
    public final ActivityTestRule<GameLobbyActivity> mGameLobbyActivityActivityTestRule =
            new ActivityTestRule<>(GameLobbyActivity.class);

    @Before
    public void registerIdlingResource() {
        Espresso.registerIdlingResources(
                mGameLobbyActivityActivityTestRule.getActivity().getCountingIdlingResource());
    }

    @Test
    public void errorShowOnInsufficientPlayers() {

        // Select eight players
        onView(withId(R.id.players_8_radio_button)).perform(click());

        // Clear existing players
        onView(withId(R.id.player_1_edit_text)).perform(clearText());
        onView(withId(R.id.player_2_edit_text)).perform(clearText());
        onView(withId(R.id.player_3_edit_text)).perform(clearText());
        onView(withId(R.id.player_4_edit_text)).perform(clearText());
        onView(withId(R.id.player_5_edit_text)).perform(clearText());
        onView(withId(R.id.player_6_edit_text)).perform(clearText());
        onView(withId(R.id.player_7_edit_text)).perform(clearText());
        onView(withId(R.id.player_8_edit_text)).perform(clearText());

        // Add players
        onView(withId(R.id.player_1_edit_text)).perform(typeText("PLAYER 1"));
        onView(withId(R.id.player_2_edit_text)).perform(typeText("PLAYER 2"));
        onView(withId(R.id.player_3_edit_text)).perform(typeText("PLAYER 3"));
        onView(withId(R.id.player_4_edit_text)).perform(typeText("PLAYER 4"));
        onView(withId(R.id.player_5_edit_text)).perform(typeText("PLAYER 5"));
        onView(withId(R.id.player_6_edit_text)).perform(typeText(""));
        onView(withId(R.id.player_7_edit_text)).perform(typeText("PLAYER 7"));
        onView(withId(R.id.player_8_edit_text)).perform(typeText("PLAYER 8"));

        // Start game
        onView(withId(R.id.start_game_button)).perform(scrollTo(), click());
        String emptyTaskMessageText = getTargetContext().getString(R.string.sufficient_players_error);
        onView(withText(emptyTaskMessageText)).check(matches(isDisplayed()));
    }

    /**
     * Unregister IdlingResource so it can be garbage collected and does not leak memory.
     */
    @After
    public void unregisterIdlingResource() {
        Espresso.unregisterIdlingResources(
                mGameLobbyActivityActivityTestRule.getActivity().getCountingIdlingResource());
    }
}
