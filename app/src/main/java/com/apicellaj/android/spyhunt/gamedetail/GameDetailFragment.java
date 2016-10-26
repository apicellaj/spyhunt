package com.apicellaj.android.spyhunt.gamedetail;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.apicellaj.android.spyhunt.R;
import com.apicellaj.android.spyhunt.dialog.GameOverDialog;
import com.apicellaj.android.spyhunt.dialog.RoleDialog;
import com.apicellaj.android.spyhunt.util.TimeUtils;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.google.common.base.Preconditions.checkNotNull;

public class GameDetailFragment extends Fragment implements GameDetailContract.View {

    private GameDetailPresenter mPresenter;

    private GridView mGridView;

    private String mLocation;
    private List<String> mPlayerList, mRoleList;

    private LinearLayout mRolesLayout;
    private LinearLayout mLocationsLayout;
    private TextView mTimerTextView;
    private Button mTimerButton, mShowLocationsButton, mShowRolesButton, mEndGameButton;

    private Button[] mRoleButtons;

    private boolean mAreRolesHidden;
    private boolean mAreLocationsHidden;
    private boolean[] mRoleButtonStates;

    private int mGridViewHeight;

    private RoleDialog mRoleDialog;
    private GameOverDialog mGameOverDialog;

    public GameDetailFragment() {
        // Requires empty public constructor
    }

    public static GameDetailFragment newInstance(String players, String roles, String title) {
        Bundle args = new Bundle();
        args.putString("players", players);
        args.putString("roles", roles);
        args.putString("locationTitle", title);
        GameDetailFragment fragment = new GameDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            mAreRolesHidden = false;
            mAreLocationsHidden = false;
            mRoleButtonStates = new boolean[]{true, true, true, true, true, true, true, true};
            mGridViewHeight = -1;
        } else {
            mAreRolesHidden = savedInstanceState.getBoolean("mAreRolesHidden");
            mRoleButtonStates = savedInstanceState.getBooleanArray("roleButtonStates");
            mAreLocationsHidden = savedInstanceState.getBoolean("mAreLocationsHidden");
            mGridViewHeight = savedInstanceState.getInt("mGridViewHeight");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("mAreRolesHidden", mAreRolesHidden);
        outState.putBooleanArray("roleButtonStates", mRoleButtonStates);
        outState.putBoolean("mAreLocationsHidden", mAreLocationsHidden);
        outState.putLong("timeRemaining", mPresenter.getTimeRemaining());
        outState.putBoolean("isTimerPaused", mPresenter.isTimerPaused());
        outState.putInt("mGridViewHeight", mGridViewHeight);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.subscribe();
        if (mPresenter.isGameOver()) {
            finishGame();
            showPlayAgain();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.unsubscribe();
        if (mRoleDialog != null) {
            mRoleDialog.dismiss();
        }
        if (mGameOverDialog != null) {
            mGameOverDialog.dismiss();
        }
    }

    @Override
    public void setPresenter(GameDetailPresenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_game_detail, container, false);

        String playersJson = getArguments().getString("players");
        String rolesJson = getArguments().getString("roles");
        mLocation = getArguments().getString("locationTitle");
        mPlayerList = convertFromJson(playersJson);
        mRoleList = convertFromJson(rolesJson);

        mRolesLayout = (LinearLayout) root.findViewById(R.id.game_detail_role_layout);
        mLocationsLayout = (LinearLayout) root.findViewById(R.id.game_detail_grid_view_layout);

        mTimerButton = (Button) root.findViewById(R.id.game_detail_timer_button);
        mTimerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPresenter.isTimerPaused()) {
                    mPresenter.resumeTimer();
                    mTimerButton.setKeepScreenOn(true);
                } else {
                    mPresenter.pauseTimer();
                    mTimerButton.setKeepScreenOn(false);
                }
            }
        });

        mShowLocationsButton = (Button) root.findViewById(R.id.game_detail_show_locations_button);
        mShowLocationsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleShowHideLocations();
            }
        });

        mShowRolesButton = (Button) root.findViewById(R.id.game_detail_show_roles_button);
        mShowRolesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleShowHideRoles();
            }
        });

        Button roleButton1 = (Button) root.findViewById(R.id.role_1_button);
        Button roleButton2 = (Button) root.findViewById(R.id.role_2_button);
        Button roleButton3 = (Button) root.findViewById(R.id.role_3_button);
        Button roleButton4 = (Button) root.findViewById(R.id.role_4_button);
        Button roleButton5 = (Button) root.findViewById(R.id.role_5_button);
        Button roleButton6 = (Button) root.findViewById(R.id.role_6_button);
        Button roleButton7 = (Button) root.findViewById(R.id.role_7_button);
        Button roleButton8 = (Button) root.findViewById(R.id.role_8_button);

        mRoleButtons = new Button[]{roleButton1, roleButton2, roleButton3, roleButton4,
                roleButton5, roleButton6, roleButton7, roleButton8};

        for (Button button : mRoleButtons) {
            button.setOnClickListener(new RoleClickListener());
        }

        mEndGameButton = (Button) root.findViewById(R.id.game_detail_end_game_button);
        mEndGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mPresenter.isGameOver()) {
                    mPresenter.endGame();
                } else {
                    getActivity().finish();
                }
            }
        });

        setPlayerNamesOnRoleButtons();

        hideUnnecessaryRoleButtons();

        long timeRemaining;
        boolean isTimerPaused;

        if (savedInstanceState == null) {
            timeRemaining = TimeUtils.getMillisecondsFromMinutes(mPresenter.getTime());
            isTimerPaused = true;
        } else {
            timeRemaining = savedInstanceState.getLong("timeRemaining");
            isTimerPaused = savedInstanceState.getBoolean("isTimerPaused");
            mLocationsLayout.setVisibility(mAreLocationsHidden ? View.GONE : View.VISIBLE);
            mShowLocationsButton.setText(mAreLocationsHidden ?
                    getString(R.string.show_locations) : getString(R.string.hide_locations));
            mRolesLayout.setVisibility(mAreRolesHidden ? View.GONE : View.VISIBLE);
            mShowRolesButton.setText(mAreRolesHidden ?
                    getString(R.string.show_roles) : getString(R.string.hide_roles));
        }

        mTimerTextView = (TextView) root.findViewById(R.id.game_detail_timer_text_view);
        mPresenter.createTimer(timeRemaining);
        setTime(timeRemaining);

        if (!isTimerPaused) {
            mPresenter.resumeTimer();
        }

        toggleRoleButtonEnabledState(mPresenter.isGameOver());

        mGridView = (GridView) root.findViewById(R.id.game_detail_grid_view);

        return root;
    }

    private List<String> convertFromJson(String jsonString) {
        List<String> list = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++) {
                list.add(jsonArray.getString(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    private void toggleShowHideLocations() {
        mAreLocationsHidden = !mAreLocationsHidden;
        mLocationsLayout.setVisibility(mAreLocationsHidden ? View.GONE : View.VISIBLE);
        mShowLocationsButton.setText(mAreLocationsHidden ?
                getString(R.string.show_locations) : getString(R.string.hide_locations));
    }

    private void toggleShowHideRoles() {
        mAreRolesHidden = !mAreRolesHidden;
        mRolesLayout.setVisibility(mAreRolesHidden ? View.GONE : View.VISIBLE);
        mShowRolesButton.setText(mAreRolesHidden ?
                getString(R.string.show_roles) : getString(R.string.hide_roles));
    }

    private void setPlayerNamesOnRoleButtons() {
        for (int i = 0; i < mPlayerList.size(); i++) {
            mRoleButtons[i].setText(mPlayerList.get(i));
        }
    }

    private void hideUnnecessaryRoleButtons() {
        switch (mPlayerList.size()) {
            case 3:
                mRoleButtons[3].setVisibility(View.GONE);
            case 4:
                mRoleButtons[4].setVisibility(View.GONE);
            case 5:
                mRoleButtons[5].setVisibility(View.GONE);
            case 6:
                mRoleButtons[6].setVisibility(View.GONE);
            case 7:
                mRoleButtons[7].setVisibility(View.GONE);
            case 8:
                // Do nothing
        }
    }

    private void toggleRoleButtonEnabledState(boolean enabled) {
        for (int i = 0; i < mRoleButtons.length; i++) {
            if (!mRoleButtonStates[i]) {
                mRoleButtons[i].setEnabled(enabled);
            }
        }
    }

    private void setGridViewHeightBasedOnChildren(GridView gridView, int columns) {
        ListAdapter listAdapter = gridView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight, rows;
        int items = listAdapter.getCount();

        View listItem = listAdapter.getView(0, null, gridView);
        listItem.measure(0, 0);
        totalHeight = listItem.getMeasuredHeight();

        float x = 1;
        if (items > columns) {
            x = items / columns;
            rows = (int) (x + 1);
            totalHeight *= rows;
        }

        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        params.height = totalHeight / 2;
        gridView.setLayoutParams(params);
        mGridViewHeight = totalHeight / 2;
    }

    @Override
    public void setTime(long timeRemaining) {
        int minutes = TimeUtils.getMinutesFromMilliseconds(timeRemaining);
        int seconds = TimeUtils.getSeconds(timeRemaining);
        String minutesText = String.format(Locale.getDefault(), "%02d", minutes);
        String secondsText = String.format(Locale.getDefault(), "%02d", seconds);
        mTimerTextView.setText(minutesText + ":" + secondsText);
    }

    @Override
    public void setLocationTitles(List<String> locationTitles) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.grid_item, locationTitles);
        mGridView.setAdapter(adapter);
        mGridView.setNumColumns(2);

        // This workaround allows us to properly size the GridView on a configuration change
        // Using INVISIBLE instead of GONE works too, but isn't as nice of a solution
        if (mGridViewHeight > 0) {
            ViewGroup.LayoutParams params = mGridView.getLayoutParams();
            params.height = mGridViewHeight;
            mGridView.setLayoutParams(params);
        } else {
            setGridViewHeightBasedOnChildren(mGridView, mGridView.getNumColumns());
        }
    }

    @Override
    public boolean isAddedToActivity() {
        return this.isAdded();
    }

    @Override
    public void showPaused() {
        mTimerButton.setText(getString(R.string.start));
    }

    @Override
    public void showResumed() {
        mTimerButton.setText(getString(R.string.pause));
    }

    @Override
    public void showPlayAgain() {
        mEndGameButton.setText(getString(R.string.play_again));
    }

    @Override
    public void finishGame() {
        String spy = getSpy();
        createGameOverDialog(spy);
        // Allow players to see roles after the game
        toggleRoleButtonEnabledState(true);
    }

    private String getSpy() {
        String spy = "";
        for (int i = 0; i < mRoleList.size(); i++) {
            if (mRoleList.get(i).equals(getString(R.string.spy))) {
                spy = mPlayerList.get(i);
            }
        }
        return spy;
    }

    private class RoleClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (v instanceof Button) {
                int position = -1;
                switch (v.getId()) {
                    case R.id.role_1_button:
                        position = 0;
                        break;
                    case R.id.role_2_button:
                        position = 1;
                        break;
                    case R.id.role_3_button:
                        position = 2;
                        break;
                    case R.id.role_4_button:
                        position = 3;
                        break;
                    case R.id.role_5_button:
                        position = 4;
                        break;
                    case R.id.role_6_button:
                        position = 5;
                        break;
                    case R.id.role_7_button:
                        position = 6;
                        break;
                    case R.id.role_8_button:
                        position = 7;
                        break;
                }

                String player = mPlayerList.get(position);
                String role = mRoleList.get(position);

                createRoleDialog(player, role, mLocation);

                // Players can only look at their role once unless the game is over
                if (!mPresenter.isGameOver()) {
                    v.setEnabled(false);
                    mRoleButtonStates[position] = false;
                }
            }
        }
    }

    private void createRoleDialog(String player, String role, String location) {
        mRoleDialog = new RoleDialog(getContext(), player, location, role);
        mRoleDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mRoleDialog.show();
    }

    private void createGameOverDialog(String spy) {
        if (mGameOverDialog != null) {
            return;
        }
        mGameOverDialog = new GameOverDialog(getContext(), spy);
        mGameOverDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mGameOverDialog.show();
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }
}
