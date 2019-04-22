package net.sharksystem.identity.android;

import android.content.Context;
import android.content.SharedPreferences;

import net.sharksystem.android.util.Constants;
import net.sharksystem.storage.SharedPreferencesHandler;

import identity.Person;

public class IdentityStorageAndroid implements SharkIdentityStorage {
    private static IdentityStorageAndroid instance;

    private final static String PREFERENCES_FILE = "SharkNet2Identity";
    private final static String OWNER_NAME = "SharkNet2Identity_OwnerName";
    private final static String OWNER_ID = "SharkNet2Identity_OwnerID";
    private final static String DEFAULT_OWNER_NAME = "dummy name";
    private final static String DEFAULT_OWNER_ID = "42";

    private CharSequence ownerName;
    private CharSequence ownerID;

    private Context currentContext;
    private SharedPreferencesHandler sharedPreferencesHandler;



    private IdentityStorageAndroid(Context ctx) {
        this.currentContext = ctx;
        this.sharedPreferencesHandler = new SharedPreferencesHandler(this.currentContext);

        if(sharedPreferencesHandler.getValue(Constants.KEY_ALIAS_USER)!= null) {
            this.ownerName = sharedPreferencesHandler.getValue(Constants.KEY_ALIAS_USER);
        }
        if(sharedPreferencesHandler.getValue(Constants.UUID_USER)!= null) {
            this.ownerID = sharedPreferencesHandler.getValue(Constants.UUID_USER);
        }


//        SharedPreferences sharedPref = ctx.getSharedPreferences(
//                PREFERENCES_FILE, Context.MODE_PRIVATE);
//        this.ownerName = sharedPref.getString(OWNER_NAME, DEFAULT_OWNER_NAME);
//        this.ownerID = sharedPref.getString(OWNER_ID, DEFAULT_OWNER_ID);
    }

    public static SharkIdentityStorage getIdentityStorage(Context ctx) {
        if(IdentityStorageAndroid.instance == null) {
            IdentityStorageAndroid.instance = new IdentityStorageAndroid(ctx);
        }

        IdentityStorageAndroid.instance.setCurrentContext(ctx);

        return IdentityStorageAndroid.instance;
    }


    private void setCurrentContext(Context ctx) {
        this.currentContext = ctx;
    }

    @Override
    public CharSequence getNameByID(CharSequence userID) {
        if(this.ownerID.toString().equalsIgnoreCase(userID.toString())) {
            return this.ownerName;
        }
        // else:
        // dummy
        return userID;
    }

    @Override
    public Person getPersonByID(CharSequence userID) {
        if(this.ownerID.toString().equalsIgnoreCase(userID.toString())) {
            return new PersonIdentity(this.ownerName, this.ownerID);
        }

        // dummy
        return new PersonIdentity("dummy name", "43");
    }

    @Override
    public void setOwnerName(CharSequence userName) {
        this.ownerName = userName;

        sharedPreferencesHandler.writeValue(Constants.KEY_ALIAS_USER, userName.toString());

//        SharedPreferences sharedPref = this.currentContext.getSharedPreferences(
//                PREFERENCES_FILE, Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPref.edit();
//        editor.putString(OWNER_NAME, userName.toString());
//        editor.commit();
    }

    @Override
    public void setOwnerID(CharSequence ownerID) {
        this.ownerID = ownerID;

        sharedPreferencesHandler.writeValue(Constants.UUID_USER, ownerID.toString());

//
//        SharedPreferences sharedPref = this.currentContext.getSharedPreferences(
//                PREFERENCES_FILE, Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPref.edit();
//        editor.putString(OWNER_ID, ownerID.toString());
//        editor.commit();
    }

//    @Override
//    public void setNewOwnerUUID(String userNameString) {
//        CharSequence uuid = userNameString + "_"
//                + String.valueOf(System.currentTimeMillis());
//
//        this.setOwnerID(uuid);
//    }

    @Override
    public CharSequence getOwnerID() {
//        return this.ownerID;
        return sharedPreferencesHandler.getValue(Constants.UUID_USER);
    }

    @Override
    public CharSequence getOwnerName() {
//        return this.ownerName;
        return sharedPreferencesHandler.getValue(Constants.KEY_ALIAS_USER);

    }
}
