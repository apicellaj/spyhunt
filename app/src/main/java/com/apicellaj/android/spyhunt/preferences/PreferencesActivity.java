package com.apicellaj.android.spyhunt.preferences;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.apicellaj.android.spyhunt.R;

public class PreferencesActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Change title
        getActionBar().setTitle(R.string.preferences_screen_title);

        // Create the fragment
        PreferencesFragment preferencesFragment =
                (PreferencesFragment) getFragmentManager().findFragmentById(R.id.contentFrame);
        if (preferencesFragment == null) {
            preferencesFragment = PreferencesFragment.newInstance();
            getFragmentManager().beginTransaction()
                    .replace(android.R.id.content, preferencesFragment)
                    .commit();
        }
    }
}
