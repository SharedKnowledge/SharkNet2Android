package net.sharksystem.persons.android;

import android.content.Context;
import android.content.SharedPreferences;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Log;

import net.sharksystem.SharkException;
import net.sharksystem.android.util.DateTimeHelper;
import net.sharksystem.crypto.ASAPCertificateImpl;
import net.sharksystem.crypto.ASAPKeyStorage;
import net.sharksystem.crypto.InMemoASAPKeyStorage;
import net.sharksystem.crypto.SharkCryptoException;
import net.sharksystem.sharknet.android.SharkNetApp;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableEntryException;
import java.util.Calendar;

import static net.sharksystem.persons.android.OwnerStorageAndroid.PREFERENCES_FILE;

public class AndroidASAPKeyStorage extends InMemoASAPKeyStorage implements ASAPKeyStorage {
    private static final String KEYPAIR_CREATION_TIME = "SharkNet2Identity_KeyPairCreationTime";
    private static final String KEYSTORE_NAME = "AndroidKeyStore";
    private static final String KEYSTORE_OWNER_ALIAS = "SN2_Owner_Keys";
    private static final int KEY_SIZE = 2048;
    private final static int ANY_PURPOSE = KeyProperties.PURPOSE_ENCRYPT |
            KeyProperties.PURPOSE_DECRYPT | KeyProperties.PURPOSE_SIGN |
            KeyProperties.PURPOSE_VERIFY;

    private long creationTime = DateTimeHelper.TIME_NOT_SET;

    @Override
    public void generateKeyPair() throws SharkException {
        try {
            Calendar start = Calendar.getInstance();
            Calendar end = Calendar.getInstance();
            //Jahr von heute plus YEAR Jahre
            end.add(Calendar.YEAR, ASAPCertificateImpl.DEFAULT_CERTIFICATE_VALIDITY_IN_YEARS);

            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(
                    KeyProperties.KEY_ALGORITHM_RSA, KEYSTORE_NAME);

            keyPairGenerator.initialize(
                    new KeyGenParameterSpec.Builder(KEYSTORE_OWNER_ALIAS, ANY_PURPOSE)
                            .setRandomizedEncryptionRequired(false)
                            .setDigests(
                                    KeyProperties.DIGEST_NONE, KeyProperties.DIGEST_MD5,
                                    KeyProperties.DIGEST_SHA1, KeyProperties.DIGEST_SHA224,
                                    KeyProperties.DIGEST_SHA256, KeyProperties.DIGEST_SHA384,
                                    KeyProperties.DIGEST_SHA512)

                            .setSignaturePaddings(KeyProperties.SIGNATURE_PADDING_RSA_PSS)
                            .setEncryptionPaddings(
                                    KeyProperties.ENCRYPTION_PADDING_NONE,
                                    KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1,
                                    KeyProperties.ENCRYPTION_PADDING_RSA_OAEP)
                            .setUserAuthenticationRequired(false)
                            .setKeyValidityStart(start.getTime())
                            .setKeyValidityEnd(end.getTime())
                            .setKeySize(KEY_SIZE)
                            .build());

            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            this.setPrivateKey(keyPair.getPrivate());
            this.setPublicKey(keyPair.getPublic());

            this.setCreationTime(System.currentTimeMillis());

        } catch (Exception e) {
            String text = "problems when generating key pair: " + e.getMessage();
            Log.d(this.getLogStart(), text);
            throw new SharkException(text);
        }
    }

    private Context getContext() {
        return SharkNetApp.getSharkNetApp().getActivity();
    }

    protected void reloadKeys() throws SharkCryptoException {
        try {
            KeyStore keyStore = KeyStore.getInstance(KEYSTORE_NAME);
            KeyStore.PrivateKeyEntry privateKeyEntry =
                    (KeyStore.PrivateKeyEntry)keyStore.getEntry(KEYSTORE_OWNER_ALIAS, null);

            super.setPrivateKey(privateKeyEntry.getPrivateKey());
            super.setPublicKey(privateKeyEntry.getCertificate().getPublicKey());
        } catch (KeyStoreException | UnrecoverableEntryException | NoSuchAlgorithmException e) {
            throw new SharkCryptoException(e.getLocalizedMessage());
        }
    }

    @Override
    public PrivateKey getPrivateKey() throws SharkCryptoException {
        try {
            super.getPrivateKey();
        }
        catch(SharkCryptoException e) {
            // maybe not yet loaded
            this.reloadKeys();
        }
        return super.getPrivateKey();
    }

    @Override
    public PublicKey getPublicKey() throws SharkCryptoException {
        try {
            super.getPublicKey();
        }
        catch(SharkCryptoException e) {
            // maybe not yet loaded
            this.reloadKeys();
        }
        return super.getPublicKey();
    }

    public void setCreationTime(long time) {
        Log.d(this.getLogStart(), "set new creation time: " + DateTimeHelper.long2DateString(time));
        this.creationTime = time;
        SharedPreferences sharedPref = this.getContext().getSharedPreferences(
                PREFERENCES_FILE, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putLong(KEYPAIR_CREATION_TIME, time);

        // create owner id
        editor.commit();
    }
        @Override
    public long getCreationTime() throws SharkCryptoException {
        SharedPreferences sharedPref = this.getContext().getSharedPreferences(
                PREFERENCES_FILE, Context.MODE_PRIVATE);
        if(this.creationTime == DateTimeHelper.TIME_NOT_SET) {
            this.creationTime = sharedPref.getLong(KEYPAIR_CREATION_TIME, DateTimeHelper.TIME_NOT_SET);
        }

        return this.creationTime;
    }

    protected String getLogStart() {
        return this.getClass().getSimpleName();
    }
}
