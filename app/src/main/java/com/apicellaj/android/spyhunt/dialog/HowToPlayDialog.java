package com.apicellaj.android.spyhunt.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;

import com.apicellaj.android.spyhunt.R;

public class HowToPlayDialog extends AlertDialog {

    public HowToPlayDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_how_to_play);

        Button closeWindowButton = (Button) findViewById(R.id.how_to_play_dialog_close_button);
        closeWindowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
