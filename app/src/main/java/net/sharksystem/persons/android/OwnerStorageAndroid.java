package net.sharksystem.persons.android;

import android.content.Context;
import android.content.SharedPreferences;

import net.sharksystem.SharkException;
import net.sharksystem.crypto.ASAPKeyStorage;
import net.sharksystem.crypto.InMemoASAPKeyStorage;
import net.sharksystem.crypto.SharkCryptoException;
import net.sharksystem.persons.Owner;
import net.sharksystem.sharknet.android.SharkNetApp;

import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;

public class OwnerStorageAndroid implements Owner {
    private static OwnerStorageAndroid instance;

    public final static String PREFERENCES_FILE = "SharkNet2Identity";
    private final static String OWNER_NAME = "SharkNet2Identity_OwnerName";
    private final static String OWNER_ID = "SharkNet2Identity_OwnerID";

    private final static String DEFAULT_OWNER_NAME = "SNUser";
    private final static String DEFAULT_OWNER_ID = "42";
    private final AndroidASAPKeyStorage androidASAPKeyStorage;
    private CharSequence ownerName;
    private boolean ownerNameSet;
    private CharSequence ownerID;
    private boolean ownerIDSet;

    private Context currentContext;

    public static OwnerStorageAndroid getOwnerStorageAndroid()
            throws UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException {

        return OwnerStorageAndroid.getOwnerStorageAndroid(SharkNetApp.getSharkNetApp().getActivity());
    }

    public static Owner getIdentityStorage(Context ctx)
            throws UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException {
        if(OwnerStorageAndroid.instance == null) {
            OwnerStorageAndroid.instance = new OwnerStorageAndroid(ctx);
        }

        OwnerStorageAndroid.instance.setCurrentContext(ctx);

        return OwnerStorageAndroid.instance;
    }

    public static OwnerStorageAndroid getOwnerStorageAndroid(Context ctx)
            throws UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException {

        if(OwnerStorageAndroid.instance == null) {
            OwnerStorageAndroid.getIdentityStorage(ctx);
        }

        return OwnerStorageAndroid.instance;
    }

    private OwnerStorageAndroid(Context ctx)
            throws UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException {
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

        // setup key store
        this.androidASAPKeyStorage = new AndroidASAPKeyStorage();
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
    public void setDisplayName(CharSequence userName) {
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
    public CharSequence getUUID() {
        return this.ownerID;
    }

    @Override
    public CharSequence getDisplayName() {
        return this.ownerName;
    }

    public void generateKeyPair() throws SharkException {
        this.androidASAPKeyStorage.generateKeyPair();
    }

    public ASAPKeyStorage getASAPKeyStorage() {
        return this.androidASAPKeyStorage;
    }
}
