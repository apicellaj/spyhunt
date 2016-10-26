package com.apicellaj.android.spyhunt.customlocations;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.apicellaj.android.spyhunt.R;
import com.apicellaj.android.spyhunt.addlocation.AddLocationActivity;
import com.apicellaj.android.spyhunt.data.Location;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class CustomLocationsFragment extends Fragment implements CustomLocationsContract.View {

    private static final int ADD_LOCATION_REQUEST = 1;

    private CustomLocationsContract.Presenter mPresenter;

    private ExpandableListView mListView;

    private CustomExpandableListAdapter mAdapter;

    private List<Location> mLocationsList;

    private TextView mEmptyTextView;

    public CustomLocationsFragment() {
        // Requires empty public constructor
    }

    public static CustomLocationsFragment newInstance() {
        return new CustomLocationsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_custom_locations, container, false);

        mListView = (ExpandableListView) root.findViewById(R.id.custom_locations_list_view);
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                int itemType = ExpandableListView.getPackedPositionType(id);

                if (itemType == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {
                    deleteLocation(position);
                }

                return true;
            }
        });

        mEmptyTextView = (TextView) root.findViewById(R.id.custom_locations_empty_text_view);

        FloatingActionButton addLocationButton =
                (FloatingActionButton) root.findViewById(R.id.add_location_fab);
        addLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchAddLocationActivity();
            }
        });

        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == ADD_LOCATION_REQUEST) {

            if (resultCode == Activity.RESULT_OK) {

                Bundle bundle = data.getBundleExtra("bundle");
                String json = bundle.getString("location");
                List<String> location = new ArrayList<>();
                try {
                    JSONArray jsonArray = new JSONArray(json);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        location.add(jsonArray.getString(i));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // We remove the title first so the remaining list contains the roles
                String title = location.remove(location.size() - 1);
                String[] roles = location.toArray(new String[location.size()]);

                if (title != null) {
                    Location locationToAdd = new Location(title, roles);
                    addLocation(locationToAdd);
                }

            }
        }

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
    public void setPresenter(CustomLocationsPresenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void showEmptyTextView() {
        mListView.setVisibility(View.GONE);
        mEmptyTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showListView(List<Location> locations) {
        if (mAdapter == null) {
            initiateListView(locations);
        } else {
            mLocationsList = locations;
            mAdapter.updateList(locations);
        }

        mListView.setVisibility(View.VISIBLE);
        mEmptyTextView.setVisibility(View.GONE);
    }

    private void initiateListView(List<Location> locations) {
        mLocationsList = locations;
        List<List<String>> rolesList = extractRolesFromLocations(locations);
        mAdapter = new CustomExpandableListAdapter(mLocationsList, rolesList);
        mListView.setAdapter(mAdapter);
    }

    private List<List<String>> extractRolesFromLocations(List<Location> locations) {
        List<List<String>> completeRolesList = new ArrayList<>();
        for (int i = 0; i < locations.size(); i++) {
            List<String> rolesList = new ArrayList<>();
            Location location = locations.get(i);
            String[] rolesArray = location.getRoles();
            Collections.addAll(rolesList, rolesArray);
            completeRolesList.add(i, rolesList);
        }
        return completeRolesList;
    }

    private void launchAddLocationActivity() {
        Intent intent = new Intent(getContext(), AddLocationActivity.class);
        startActivityForResult(intent, ADD_LOCATION_REQUEST);
    }

    private void deleteLocation(int position) {
        Location deleteLocation = mLocationsList.get(position);
        final String locationId = deleteLocation.getId();
        new AlertDialog.Builder(getContext())
                .setTitle(getString(R.string.delete_dialog_title))
                .setMessage(getString(R.string.delete_dialog_message))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        mPresenter.deleteLocation(locationId);
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void addLocation(Location location) {
        mPresenter.addNewLocation(location);
    }

    public class CustomExpandableListAdapter extends BaseExpandableListAdapter {

        private List<Location> mLocations;
        private List<List<String>> mRoles;

        CustomExpandableListAdapter(@NonNull List<Location> locations,
                                    @NonNull List<List<String>> roles) {
            mLocations = checkNotNull(locations, "locations cannot be null");
            mRoles = checkNotNull(roles, "roles cannot be null");
        }

        void updateList(List<Location> locations) {
            mLocations = locations;
            mRoles = extractRolesFromLocations(locations);
            notifyDataSetChanged();
        }

        @Override
        public int getGroupCount() {
            return mLocations.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return mRoles.get(groupPosition).size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return mLocations.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return mRoles.get(groupPosition).get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

            if (convertView == null) {
                LayoutInflater layoutInflater = (LayoutInflater) getContext()
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = layoutInflater.inflate(R.layout.list_parent_view, null);
            }
            TextView parentTextView = (TextView) convertView.findViewById(R.id.list_parent_text_view);

            String location = mLocations.get(groupPosition).getTitle();
            parentTextView.setText(location);

            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

            if (convertView == null) {
                LayoutInflater layoutInflater = (LayoutInflater) getContext()
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = layoutInflater.inflate(R.layout.list_child_view, null);
            }

            TextView childTextView = (TextView) convertView.findViewById(R.id.list_child_text_view);
            String role = mRoles.get(groupPosition).get(childPosition);
            childTextView.setText(role);

            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }
}
