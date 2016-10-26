package com.apicellaj.android.spyhunt.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * A repository to abstract SharedPreferences
 */
public class SharedPreferencesRepository {

    private static final String CUSTOM_LOCATIONS = "custom_locations";

    private static final String CUSTOM_TIMER = "custom_timer";

    private static final String CUSTOM_TIME = "custom_time";

    private static final String USES_SAVED_PLAYERS = "uses_saved_players";

    private static final String SAVED_PLAYERS = "saved_players";

    private final SharedPreferences mSharedPreferences;

    public SharedPreferencesRepository(Context context) {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public boolean getUsesCustomLocations() {
        return mSharedPreferences.getBoolean(CUSTOM_LOCATIONS, false);
    }

    public boolean getUsesCustomTimer() {
        return mSharedPreferences.getBoolean(CUSTOM_TIMER, false);
    }

    public int getCustomTime() {
        String time = mSharedPreferences.getString(CUSTOM_TIME, "7");
        return Integer.valueOf(time);
    }

    public boolean getUseSavedPlayers() {
        return mSharedPreferences.getBoolean(USES_SAVED_PLAYERS, false);
    }

    public void setSavedPlayers(List<String> players) {
        JSONArray jsonArray = new JSONArray();
        for (String player : players) {
            jsonArray.put(player);
        }
        mSharedPreferences.edit().putString(SAVED_PLAYERS, jsonArray.toString()).apply();
    }

    public List<String> getSavedPlayers() {
        List<String> savedPlayers = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(mSharedPreferences.getString(SAVED_PLAYERS, ""));
            for (int i = 0; i < jsonArray.length(); i++) {
                savedPlayers.add(jsonArray.getString(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return savedPlayers;
    }
}
