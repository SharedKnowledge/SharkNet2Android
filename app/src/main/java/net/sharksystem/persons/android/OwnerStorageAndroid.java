package net.sharksystem.persons.android;

import android.content.Context;
import android.content.SharedPreferences;

import net.sharksystem.crypto.ASAPKeyStorage;
import net.sharksystem.crypto.InMemoASAPKeyStorage;
import net.sharksystem.crypto.SharkCryptoException;
import net.sharksystem.persons.Owner;

import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;

public class OwnerStorageAndroid implements Owner, ASAPKeyStorage {
    private static final String PRIVATE_KEY_ALIAS = "PRIVATE_KEY_ALIAS";
    private static final String PUBLIC_KEY_ALIAS = "PUBLIC_KEY_ALIAS";
    private static final String KEYS_CREATION_TIME = "KEYS_CREATION_TIME";
    private static OwnerStorageAndroid instance;

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

    public static Owner getIdentityStorage(Context ctx)
            throws UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException {
        if(OwnerStorageAndroid.instance == null) {
            OwnerStorageAndroid.instance = new OwnerStorageAndroid(ctx);
        }

        OwnerStorageAndroid.instance.setCurrentContext(ctx);

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

        this.initKeyStorage(ctx);
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

    /////////////////////////////////////////////////////////////////////////////////////////////
    //                                    asap key storage                                     //
    /////////////////////////////////////////////////////////////////////////////////////////////

    private InMemoASAPKeyStorage inMemoASAPKeyStorage = null;
    private KeyStore keyStore;
    private static final char[] password = "geheim".toCharArray(); // TODO :)

    private void initKeyStorage(Context ctx)
            throws KeyStoreException, UnrecoverableKeyException, NoSuchAlgorithmException {

        this.inMemoASAPKeyStorage = new InMemoASAPKeyStorage();

        // TODO
        /*
        SharedPreferences sharedPref = this.currentContext.getSharedPreferences(
                PREFERENCES_FILE, Context.MODE_PRIVATE);

        long creationTime = sharedPref.getLong(KEYS_CREATION_TIME, 0);
        this.inMemoASAPKeyStorage.setCreationTime(creationTime);

        this.keyStore = KeyStore.getInstance(KeyStore.getDefaultType());

        // TODO
        this.inMemoASAPKeyStorage.storePrivateKey(
                (PrivateKey)

        this.inMemoASAPKeyStorage.storePublicKey(
                (PublicKey) this.keyStore.getKey(PUBLIC_KEY_ALIAS, password));

         */
    }

    @Override
    public void storePrivateKey(PrivateKey privateKey) {
        this.inMemoASAPKeyStorage.storePrivateKey(privateKey);
    }

    @Override
    public void storePublicKey(PublicKey publicKey) {
        this.inMemoASAPKeyStorage.storePublicKey(publicKey);
    }

    @Override
    public void setCreationTime(long l) {
        this.inMemoASAPKeyStorage.setCreationTime(l);
    }

    @Override
    public PrivateKey retrievePrivateKey() throws SharkCryptoException {
        return this.inMemoASAPKeyStorage.retrievePrivateKey();
    }

    @Override
    public PublicKey retrievePublicKey() throws SharkCryptoException {
        return this.inMemoASAPKeyStorage.retrievePublicKey();
    }

    @Override
    public long getCreationTime() throws SharkCryptoException {
        return this.inMemoASAPKeyStorage.getCreationTime();
    }
}
