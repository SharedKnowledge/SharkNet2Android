package net.sharksystem.identity.android;

import android.content.Context;
import android.content.SharedPreferences;

public class IdentityStorageAndroid implements SharkIdentityStorage {
    private static IdentityStorageAndroid instance;

    private final static String PREFERENCES_FILE = "SharkNet2Identity";
    private final static String OWNER_NAME = "SharkNet2Identity_OwnerName";
    private final static String OWNER_ID = "SharkNet2Identity_OwnerID";

    private final static String DEFAULT_OWNER_NAME = "SNUser";
    private final static String DEFAULT_OWNER_ID = "42";
    private CharSequence ownerName;
    private boolean ownerNameSet;
    private CharSequence ownerID;
    private boolean ownerIDSet;

    private Context currentContext;

    public static SharkIdentityStorage getIdentityStorage(Context ctx) {
        if(IdentityStorageAndroid.instance == null) {
            IdentityStorageAndroid.instance = new IdentityStorageAndroid(ctx);
        }

        IdentityStorageAndroid.instance.setCurrentContext(ctx);

        return IdentityStorageAndroid.instance;
    }

    private IdentityStorageAndroid(Context ctx) {
        this.currentContext = ctx;

        SharedPreferences sharedPref = ctx.getSharedPreferences(
                PREFERENCES_FILE, Context.MODE_PRIVATE);

        if(sharedPref.contains(OWNER_NAME)) {
            this.ownerName = sharedPref.getString(OWNER_NAME, DEFAULT_OWNER_NAME);
            this.ownerNameSet = true;
        } else {
            this.ownerName = DEFAULT_OWNER_NAME;
            this.ownerNameSet = false;
        }

        if(sharedPref.contains(OWNER_ID)) {
            this.ownerID = sharedPref.getString(OWNER_ID, DEFAULT_OWNER_ID);
            this.ownerIDSet = true;
        } else {
            this.ownerID = DEFAULT_OWNER_ID;
            this.ownerIDSet = false;
        }
    }

    @Override
    public boolean isOwnerSet() {
        return this.ownerNameSet;
    }

    public boolean isOwnerIDSet() {
        return this.ownerIDSet;
    }

    private void setCurrentContext(Context ctx) {
        this.currentContext = ctx;
    }

    @Override
    public void setOwnerName(CharSequence userName) {
        this.ownerName = userName;

        SharedPreferences sharedPref = this.currentContext.getSharedPreferences(
                PREFERENCES_FILE, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString(OWNER_NAME, userName.toString());

        // create owner id

        // TODO: dummy version of an id
        String ownerID = "ID_of_" + userName;
        editor.putString(OWNER_ID, ownerID);

        editor.commit();

    }

    @Override
    public CharSequence getOwnerID() {
        return this.ownerID;
    }

    @Override
    public CharSequence getOwnerName() {
        return this.ownerName;
    }
}
