package com.apicellaj.android.spyhunt.mainscreen;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.apicellaj.android.spyhunt.R;
import com.apicellaj.android.spyhunt.dialog.HowToPlayDialog;
import com.apicellaj.android.spyhunt.gamelobby.GameLobbyActivity;
import com.apicellaj.android.spyhunt.preferences.PreferencesActivity;

import static com.google.common.base.Preconditions.checkNotNull;

public class MainScreenFragment extends Fragment implements MainScreenContract.View {

    private MainScreenContract.Presenter mPresenter;

    public MainScreenFragment() {
        // Requires empty public constructor
    }

    public static MainScreenFragment newInstance() {
        return new MainScreenFragment();
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.subscribe();
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.unsubscribe();
    }

    @Override
    public void setPresenter(MainScreenPresenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_mainscreen, container, false);

        Button startGameButton = (Button) root.findViewById(R.id.start_game_button);
        startGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLobby();
            }
        });

        Button howToPlayButton = (Button) root.findViewById(R.id.how_to_play_button);
        howToPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHowToPlay();
            }
        });

        setHasOptionsMenu(true);

        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.mainscreen_fragment_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_settings:
                showSettingsMenu();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showLobby() {
        Intent intent = new Intent(getContext(), GameLobbyActivity.class);
        startActivity(intent);
    }

    @Override
    public void showHowToPlay() {
        HowToPlayDialog howToPlayDialog = new HowToPlayDialog(getContext());
        howToPlayDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        howToPlayDialog.show();
    }

    @Override
    public void showSettingsMenu() {
        Intent intent = new Intent(getContext(), PreferencesActivity.class);
        startActivity(intent);
    }
}
