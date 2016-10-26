package com.apicellaj.android.spyhunt.gamelobby;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.apicellaj.android.spyhunt.R;
import com.apicellaj.android.spyhunt.data.Location;
import com.apicellaj.android.spyhunt.gamedetail.GameDetailActivity;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class GameLobbyFragment extends Fragment implements GameLobbyContract.View {

    private static final int MAX_NUMBER_OF_PLAYERS = 8;

    private GameLobbyPresenter mPresenter;

    private RadioGroup mPlayersGroup;

    private EditText mPlayer1;
    private EditText mPlayer2;
    private EditText mPlayer3;
    private EditText mPlayer4;
    private EditText mPlayer5;
    private EditText mPlayer6;
    private EditText mPlayer7;
    private EditText mPlayer8;

    private int[] playerNameEditTextIds;

    private Location mRandomLocation;

    public GameLobbyFragment() {
        // Requires empty public constructor
    }

    public static GameLobbyFragment newInstance() {
        return new GameLobbyFragment();
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
    public void setPresenter(GameLobbyPresenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_game_lobby, container, false);

        mPlayersGroup = (RadioGroup) root.findViewById(R.id.players_radio_group);
        mPlayersGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                updateVisibilityForPlayerNameFields();
            }
        });

        mPlayer1 = (EditText) root.findViewById(R.id.player_1_edit_text);
        mPlayer2 = (EditText) root.findViewById(R.id.player_2_edit_text);
        mPlayer3 = (EditText) root.findViewById(R.id.player_3_edit_text);
        mPlayer4 = (EditText) root.findViewById(R.id.player_4_edit_text);
        mPlayer5 = (EditText) root.findViewById(R.id.player_5_edit_text);
        mPlayer6 = (EditText) root.findViewById(R.id.player_6_edit_text);
        mPlayer7 = (EditText) root.findViewById(R.id.player_7_edit_text);
        mPlayer8 = (EditText) root.findViewById(R.id.player_8_edit_text);

        playerNameEditTextIds = new int[]{R.id.player_1_edit_text, R.id.player_2_edit_text,
                R.id.player_3_edit_text, R.id.player_4_edit_text, R.id.player_5_edit_text,
                R.id.player_6_edit_text, R.id.player_7_edit_text, R.id.player_8_edit_text};

        Button startGameButton = (Button) root.findViewById(R.id.start_game_button);
        startGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasAllPlayerNames()) {
                    savePlayers(getPlayerNamesForSaving());
                    startGame(getPlayers());
                } else {
                    showMessage(getString(R.string.sufficient_players_error));
                }
            }
        });

        if (savedInstanceState == null) {
            mPlayersGroup.check(R.id.players_3_radio_button);
        }

        updateVisibilityForPlayerNameFields();

        return root;
    }

    /**
     * Validates that all name fields have been entered for the appropriate number of players
     */
    private boolean hasAllPlayerNames() {
        if (getView() == null) {
            return false;
        }
        boolean hasAllPlayerNames = true;
        int numberOfPlayers = getNumberOfPlayers();
        int[] playerNameEditTextId = new int[]{R.id.player_1_edit_text, R.id.player_2_edit_text,
                R.id.player_3_edit_text, R.id.player_4_edit_text, R.id.player_5_edit_text,
                R.id.player_6_edit_text, R.id.player_7_edit_text, R.id.player_8_edit_text};
        for (int i = 0; i < numberOfPlayers; i++) {
            EditText editText = (EditText) getView().findViewById(playerNameEditTextId[i]);
            if (TextUtils.isEmpty(getText(editText))) {
                hasAllPlayerNames = false;
                editText.setError(getString(R.string.sufficient_players_error));
            }
        }
        return hasAllPlayerNames;
    }

    private String getText(EditText editText) {
        return editText.getText().toString().trim();
    }

    private void updateVisibilityForPlayerNameFields() {
        showAllPlayerNameFields();
        int numberOfPlayers = getNumberOfPlayers();
        switch (numberOfPlayers) {
            case 3:
                mPlayer4.setVisibility(View.GONE);
            case 4:
                mPlayer5.setVisibility(View.GONE);
            case 5:
                mPlayer6.setVisibility(View.GONE);
            case 6:
                mPlayer7.setVisibility(View.GONE);
            case 7:
                mPlayer8.setVisibility(View.GONE);
            case 8:
                // Do nothing
        }
    }

    private int getNumberOfPlayers() {
        int buttonId = mPlayersGroup.getCheckedRadioButtonId();
        switch (buttonId) {
            case R.id.players_3_radio_button:
                return 3;
            case R.id.players_4_radio_button:
                return 4;
            case R.id.players_5_radio_button:
                return 5;
            case R.id.players_6_radio_button:
                return 6;
            case R.id.players_7_radio_button:
                return 7;
            case R.id.players_8_radio_button:
                return 8;
            default:
                return 3;
        }
    }

    private void showAllPlayerNameFields() {
        mPlayer1.setVisibility(View.VISIBLE);
        mPlayer2.setVisibility(View.VISIBLE);
        mPlayer3.setVisibility(View.VISIBLE);
        mPlayer4.setVisibility(View.VISIBLE);
        mPlayer5.setVisibility(View.VISIBLE);
        mPlayer6.setVisibility(View.VISIBLE);
        mPlayer7.setVisibility(View.VISIBLE);
        mPlayer8.setVisibility(View.VISIBLE);
    }

    private void showMessage(String message) {
        if (getView() != null) {
            Snackbar.make(getView(), message, Snackbar.LENGTH_SHORT).show();
        }
    }

    private List<String> getPlayers() {
        List<String> players = new ArrayList<>();
        int numberOfPlayers = getNumberOfPlayers();
        for (int i = 0; i < numberOfPlayers; i++) {
            EditText editText = (EditText) getView().findViewById(playerNameEditTextIds[i]);
            String player = editText.getText().toString();
            players.add(player);
        }
        return players;
    }

    private List<String> getPlayerNamesForSaving() {
        List<String> players = new ArrayList<>();
        for (int i = 0; i < MAX_NUMBER_OF_PLAYERS; i++) {
            EditText editText = (EditText) getView().findViewById(playerNameEditTextIds[i]);
            String player = editText.getText().toString();
            players.add(player);
        }
        return players;
    }

    private void savePlayers(List<String> players) {
        mPresenter.savePlayerNames(players);
    }

    private void startGame(List<String> players) {
        String playersJson = getPlayersInJson(players);
        String rolesJson = getRolesInJson(players.size());
        Intent intent = new Intent(getContext(), GameDetailActivity.class);
        intent.putExtra("players", playersJson);
        intent.putExtra("roles", rolesJson);
        intent.putExtra("locationTitle", mRandomLocation.getTitle());
        startActivity(intent);
    }

    private String getPlayersInJson(List<String> players) {
        JSONArray jsonArray = new JSONArray();
        for (String player : players) {
            jsonArray.put(player);
        }
        return jsonArray.toString();
    }

    private String getRolesInJson(int numberOfPlayers) {
        List<String> roles = new ArrayList<>();
        roles.addAll(Arrays.asList(mRandomLocation.getRoles()));
        Collections.shuffle(roles);
        roles = roles.subList(0, numberOfPlayers - 1);
        roles.add(getString(R.string.spy));
        Collections.shuffle(roles);
        JSONArray jsonArray = new JSONArray();
        for (String role : roles) {
            jsonArray.put(role);
        }
        return jsonArray.toString();
    }

    @Override
    public void showPlayers(List<String> players) {
        for (int i = 0; i < players.size(); i++) {
            EditText editText = (EditText) getView().findViewById(playerNameEditTextIds[i]);
            editText.setText(players.get(i));
        }
    }

    @Override
    public void setLocation(Location location) {
        mRandomLocation = location;
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }
}
