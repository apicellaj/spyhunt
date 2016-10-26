package com.apicellaj.android.spyhunt.addlocation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.apicellaj.android.spyhunt.R;

import org.json.JSONArray;

public class AddLocationFragment extends Fragment {

    private EditText[] mRoleEditText;

    public AddLocationFragment() {
        // Requires empty public constructor
    }

    public static AddLocationFragment newInstance() {
        return new AddLocationFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_add_location, container, false);

        EditText title = (EditText) root.findViewById(R.id.add_location_title_edit_text);
        EditText role1 = (EditText) root.findViewById(R.id.add_location_role1_edit_text);
        EditText role2 = (EditText) root.findViewById(R.id.add_location_role2_edit_text);
        EditText role3 = (EditText) root.findViewById(R.id.add_location_role3_edit_text);
        EditText role4 = (EditText) root.findViewById(R.id.add_location_role4_edit_text);
        EditText role5 = (EditText) root.findViewById(R.id.add_location_role5_edit_text);
        EditText role6 = (EditText) root.findViewById(R.id.add_location_role6_edit_text);
        EditText role7 = (EditText) root.findViewById(R.id.add_location_role7_edit_text);

        mRoleEditText = new EditText[]{role1, role2, role3, role4, role5, role6, role7, title};

        FloatingActionButton floatingActionButton =
                (FloatingActionButton) root.findViewById(R.id.save_location_fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (allFieldsHaveInput()) {
                    addLocation();
                } else {
                    showError();
                }
            }
        });

        return root;
    }

    /**
     * @return true if all fields have non-empty input
     */
    private boolean allFieldsHaveInput() {
        for (EditText editText : mRoleEditText) {
            if (isTextEmpty(editText)) {
                return false;
            }
        }
        return true;
    }

    private boolean isTextEmpty(EditText editText) {
        return TextUtils.isEmpty(getText(editText));
    }

    private void addLocation() {
        JSONArray jsonArray = new JSONArray();
        for (EditText editText : mRoleEditText) {
            jsonArray.put(editText.getText().toString());
        }

        Intent returnIntent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("location", jsonArray.toString());

        returnIntent.putExtra("bundle", bundle);
        getActivity().setResult(Activity.RESULT_OK, returnIntent);
        getActivity().finish();
    }

    private String getText(EditText editText) {
        return editText.getText().toString().trim();
    }

    private void showError() {
        if (getView() == null) {
            return;
        }
        showMessage(getString(R.string.add_location_error));
        for (EditText editText : mRoleEditText) {
            if (isTextEmpty(editText)) {
                editText.setError(getString(R.string.add_location_field_error));
            }
        }
    }

    private void showMessage(String message) {
        if (getView() != null) {
            Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
        }
    }
}
