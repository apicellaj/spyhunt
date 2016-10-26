package com.apicellaj.android.spyhunt.data;

import android.support.annotation.NonNull;

import com.google.common.base.Objects;

import java.util.Arrays;
import java.util.UUID;

/**
 * Immutable model class for a Location
 */
public class Location {

    @NonNull
    private final String mId;

    @NonNull
    private final String mTitle;

    @NonNull
    private final String[] mRoles;

    /**
     * Constructor for a Location
     *
     * @param title Title of the location
     * @param roles A list of seven possible roles people at the location might have
     */
    public Location(@NonNull String title, @NonNull String[] roles) {
        mId = UUID.randomUUID().toString();
        mTitle = title;
        mRoles = roles;
    }

    /**
     * Constructor for a Location with known UUID
     *
     * @param id    String UUID of the entry
     * @param title Title of the location
     * @param roles A list of seven possible roles people at the location might have
     */
    public Location(@NonNull String id, @NonNull String title, @NonNull String[] roles) {
        mId = id;
        mTitle = title;
        mRoles = roles;
    }

    @NonNull
    public String getId() {
        return mId;
    }

    @NonNull
    public String getTitle() {
        return mTitle;
    }

    @NonNull
    public String[] getRoles() {
        return mRoles;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Location location = (Location) obj;
        return Objects.equal(mId, location.mId) &&
                Objects.equal(mTitle, location.mTitle) &&
                Objects.equal(Arrays.toString(mRoles), Arrays.toString(location.mRoles));
    }

    @Override
    public int hashCode() {
        // Use Arrays.toString(mRoles) to prevent hash issues with the String array
        return Objects.hashCode(mId, mTitle, Arrays.toString(mRoles));
    }

    @Override
    public String toString() {
        return mTitle;
    }
}
