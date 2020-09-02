package net.sharksystem.sharknet.android;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import net.sharksystem.asap.ASAP;

public class OwnerStorage implements Owner {
    public final static String PREFERENCES_FILE = "SharkNet2Identity";
    private final static String OWNER_NAME = "SharkNet2Identity_OwnerName";
    private final static String OWNER_ID = "SharkNet2Identity_OwnerID";

    public final static String DEFAULT_OWNER_NAME = "SNUser";
    private final static String DEFAULT_OWNER_ID = "Default_SN_USER_ID";
    private final Activity activity;

    private CharSequence ownerName;
    private CharSequence ownerID;

    OwnerStorage(Activity activity) {
        this.activity = activity;
        SharedPreferences sharedPref = activity.getSharedPreferences(
                OwnerStorage.PREFERENCES_FILE, Context.MODE_PRIVATE);

        if(sharedPref.contains(OwnerStorage.OWNER_NAME)) {
            this.ownerName = sharedPref.getString(OwnerStorage.OWNER_NAME, OwnerStorage.DEFAULT_OWNER_NAME);
        } else {
            this.ownerName = OwnerStorage.DEFAULT_OWNER_NAME;
        }

        if(sharedPref.contains(OwnerStorage.OWNER_ID)) {
            this.ownerID = sharedPref.getString(OwnerStorage.OWNER_ID, OwnerStorage.DEFAULT_OWNER_ID);
        } else {
            this.ownerID = OwnerStorage.DEFAULT_OWNER_ID;
        }
    }

    private Activity getActivity() {
        return this.activity;
    }

    @Override
    public boolean isOwnerSet() {
        return !this.ownerName.toString().equalsIgnoreCase(OwnerStorage.DEFAULT_OWNER_NAME);
    }

    public boolean isOwnerIDSet() {
        return !this.ownerID.toString().equalsIgnoreCase(OwnerStorage.DEFAULT_OWNER_ID);
    }

    void setDisplayName(Context ctx, CharSequence userName) {
        this.ownerName = userName;

        SharedPreferences sharedPref = ctx.getSharedPreferences(
                OwnerStorage.PREFERENCES_FILE, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString(OwnerStorage.OWNER_NAME, userName.toString());

        // create owner id
        if(this.ownerID.toString().equalsIgnoreCase(OwnerStorage.DEFAULT_OWNER_ID)) {
            // set id - once and only once.
//                this.ownerID = UUID.randomUUID().toString();
            this.ownerID = ASAP.createUniqueID();
            editor.putString(OwnerStorage.OWNER_ID, ownerID.toString());
        }

        editor.commit();
    }

    public void setDisplayName(CharSequence userName) {
        Context ctx = this.getActivity();
        this.ownerName = userName;

        SharedPreferences sharedPref = ctx.getSharedPreferences(
                OwnerStorage.PREFERENCES_FILE, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString(OwnerStorage.OWNER_NAME, userName.toString());

        // create owner id
        if(this.ownerID.toString().equalsIgnoreCase(OwnerStorage.DEFAULT_OWNER_ID)) {
            // set id - once and only once.
//                this.ownerID = UUID.randomUUID().toString();
            this.ownerID = ASAP.createUniqueID();
            editor.putString(OwnerStorage.OWNER_ID, ownerID.toString());
        }

        editor.commit();
    }

    @Override
    public CharSequence getUUID() {
        return this.ownerID;
    }

    @Override
    public CharSequence getDisplayName() {
        return this.ownerName;
    }

}
