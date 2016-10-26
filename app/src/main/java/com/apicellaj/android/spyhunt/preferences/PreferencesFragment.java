package com.apicellaj.android.spyhunt.preferences;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.apicellaj.android.spyhunt.R;

public class PreferencesFragment extends PreferenceFragment {

    public PreferencesFragment() {
        // Requires empty public constructor
    }

    public static PreferencesFragment newInstance() {
        return new PreferencesFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from XML
        addPreferencesFromResource(R.xml.preferences);
    }
}
