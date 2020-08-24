package net.sharksystem.persons.android;

import android.content.Context;
import android.content.SharedPreferences;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Log;

import net.sharksystem.SharkException;
import net.sharksystem.asap.util.DateTimeHelper;
import net.sharksystem.crypto.ASAPCertificateImpl;
import net.sharksystem.crypto.ASAPKeyStorage;
import net.sharksystem.crypto.InMemoASAPKeyStorage;
import net.sharksystem.crypto.SharkCryptoException;
import net.sharksystem.sharknet.android.SharkNetApp;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.util.Calendar;

import static net.sharksystem.sharknet.android.SharkNetApp.PREFERENCES_FILE;

public class AndroidASAPKeyStorage extends InMemoASAPKeyStorage implements ASAPKeyStorage {
    private static final String KEYPAIR_CREATION_TIME = "SharkNet2Identity_KeyPairCreationTime";
    public static final String KEYSTORE_NAME = "AndroidKeyStore";
    public static final String KEYSTORE_OWNER_ALIAS = "SN2_Owner_Keys";
    private static final int KEY_SIZE = 2048;
    private final static int ANY_PURPOSE = KeyProperties.PURPOSE_ENCRYPT |
            KeyProperties.PURPOSE_DECRYPT | KeyProperties.PURPOSE_SIGN |
            KeyProperties.PURPOSE_VERIFY;
    private static final String KEYSTORE_PWD = "SharkNet2Identity_KeyStorePWD";

    private long creationTime = DateTimeHelper.TIME_NOT_SET;
    private KeyStore keyStore = null;

    @Override
    public void generateKeyPair() throws SharkException {
        try {
            Calendar start = Calendar.getInstance();
            Calendar end = Calendar.getInstance();
            // start (now) + one year
            end.add(Calendar.YEAR, ASAPCertificateImpl.DEFAULT_CERTIFICATE_VALIDITY_IN_YEARS);

            /* if you change this - make intensive test on credential exchange / cert creation
            it took me some hours to figure that stuff out.
             */
            start.add(Calendar.DATE, -1); // to avoid key not yet valid problem

            StringBuilder sb = new StringBuilder();
            sb.append("create key pair with start: ");
            sb.append(DateTimeHelper.long2DateString(start.getTimeInMillis()));
            sb.append(" | end: ");
            sb.append(DateTimeHelper.long2DateString(end.getTimeInMillis()));

            Log.d(this.getLogStart(), sb.toString());

            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(
                    KeyProperties.KEY_ALGORITHM_RSA, KEYSTORE_NAME);

            keyPairGenerator.initialize(
                    new KeyGenParameterSpec.Builder(
                            KEYSTORE_OWNER_ALIAS, ANY_PURPOSE)
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
            this.save();
        } catch (Exception e) {
            String text = "problems when generating key pair: " + e.getMessage();
            Log.d(this.getLogStart(), text);
            throw new SharkException(text);
        }
    }

    private Context getContext() {
        return SharkNetApp.getSharkNetApp().getActivity();
    }

    private KeyStore getKeyStore() throws KeyStoreException {
        if(this.keyStore == null) {
            this.keyStore = KeyStore.getInstance(KEYSTORE_NAME);
            try {
                this.keyStore.load(null);
            } catch (IOException | NoSuchAlgorithmException | CertificateException e) {
                throw new KeyStoreException(e.getLocalizedMessage());
            }
        }

        return this.keyStore;
    }


    public void load(InputStream inputStream) throws SharkCryptoException, IOException {
        /* nothing to do when using android key storage
        KeyStore keyStore = null;
        try {
            // setup new one
            this.setPrivateKey(null);
            this.setPublicKey(null);
            this.keyStore = KeyStore.getInstance(KEYSTORE_NAME);
            keyStore.load(inputStream, this.getKeyStorePWD().toCharArray());
        } catch (NoSuchAlgorithmException | KeyStoreException | CertificateException e) {
            throw new SharkCryptoException(e.getLocalizedMessage());
        }
         */
    }

    private void save() {
        /* nothing to do when using android key storage
        try {
            File keyStoreFile = SharkNetApp.getSharkNetApp().getKeyStoreFile(false);
            KeyStore keyStore = this.getKeyStore();
            keyStore.store(new FileOutputStream(keyStoreFile), this.getKeyStorePWD().toCharArray());
        } catch (SharkException | KeyStoreException | CertificateException
                | NoSuchAlgorithmException | IOException e) {
            Log.e(this.getLogStart(), "cannot write key store file: " + e.getLocalizedMessage());
        }
         */
    }

    public void setKeyStorePWD(String pwd) {
        Log.d(this.getLogStart(), "set key  store pwd: ");
        SharedPreferences sharedPref = this.getContext().getSharedPreferences(
                PREFERENCES_FILE, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(KEYSTORE_PWD, pwd);

        // create owner id
        editor.commit();

        this.save();
    }

    public String getKeyStorePWD() throws SharkCryptoException {
        SharedPreferences sharedPref = this.getContext().getSharedPreferences(
                PREFERENCES_FILE, Context.MODE_PRIVATE);

        return sharedPref.getString(KEYSTORE_PWD, "geheim");
    }

    protected void reloadKeys() throws SharkCryptoException {
        try {
            KeyStore keyStore = this.getKeyStore();
            KeyStore.PrivateKeyEntry privateKeyEntry =
                    (KeyStore.PrivateKeyEntry) keyStore.getEntry(KEYSTORE_OWNER_ALIAS, null);

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
