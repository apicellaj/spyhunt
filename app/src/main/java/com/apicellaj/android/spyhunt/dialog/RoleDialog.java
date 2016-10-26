package com.apicellaj.android.spyhunt.dialog;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.apicellaj.android.spyhunt.R;

public class RoleDialog extends AlertDialog {

    private final String mPlayer;
    private final String mLocation;
    private final String mRole;

    public RoleDialog(@NonNull Context context, String player, String location, String role) {
        super(context);
        mPlayer = player;
        mLocation = location;
        mRole = role;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_role);

        TextView playerTextView = (TextView) findViewById(R.id.role_dialog_player_text);
        playerTextView.setText(mPlayer);
        TextView locationTextView = (TextView) findViewById(R.id.role_dialog_location_text);
        locationTextView.setText(getContext().getString(R.string.role_dialog_location, mLocation));
        TextView roleTextView = (TextView) findViewById(R.id.role_dialog_role_text);
        roleTextView.setText(getContext().getString(R.string.role_dialog_role, mRole));

        Button closeDialogButton = (Button) findViewById(R.id.role_dialog_close_button);
        closeDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        if (mRole.equals(getContext().getString(R.string.spy))) {
            String unknown = getContext().getString(R.string.location_unknown);
            locationTextView.setText(getContext().getString(R.string.role_dialog_location, unknown));
            roleTextView.setTextColor(Color.RED);
        }
    }
}
