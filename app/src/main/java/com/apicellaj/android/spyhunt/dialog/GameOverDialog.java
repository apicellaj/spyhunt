package com.apicellaj.android.spyhunt.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.apicellaj.android.spyhunt.R;

import static android.view.View.GONE;

public class GameOverDialog extends AlertDialog {

    private final String mSpy;

    private TextView mGameOverTextView;
    private TextView mVoteTextView;
    private Button mRevealButton;
    private Button mCloseDialogButton;
    private TextView mResultsTextView;

    public GameOverDialog(@NonNull Context context, String spy) {
        super(context);
        mSpy = spy;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_game_over);

        mVoteTextView = (TextView) findViewById(R.id.game_over_dialog_vote_text);

        mRevealButton = (Button) findViewById(R.id.game_over_dialog_reveal_button);
        mRevealButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mVoteTextView.setVisibility(GONE);
                mRevealButton.setVisibility(GONE);
                mGameOverTextView.setVisibility(View.VISIBLE);
                mResultsTextView.setVisibility(View.VISIBLE);
                mCloseDialogButton.setVisibility(View.VISIBLE);
            }
        });

        mGameOverTextView = (TextView) findViewById(R.id.game_over_dialog_title);
        mGameOverTextView.setVisibility(GONE);

        mResultsTextView = (TextView) findViewById(R.id.game_over_dialog_results);
        mResultsTextView.setVisibility(GONE);
        mResultsTextView.setText(getContext().getString(R.string.game_over_results, mSpy));

        mCloseDialogButton = (Button) findViewById(R.id.game_over_dialog_close_button);
        mCloseDialogButton.setVisibility(GONE);
        mCloseDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
