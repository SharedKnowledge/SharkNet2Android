package net.sharksystem.sharknet.android;

import android.content.Context;
import android.content.SharedPreferences;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Log;

import net.sharksystem.SharkException;
import net.sharksystem.asap.ASAPException;
import net.sharksystem.asap.ASAPSecurityException;
import net.sharksystem.asap.android.apps.ASAPComponentNotYetInitializedException;
import net.sharksystem.asap.util.DateTimeHelper;
import net.sharksystem.crypto.ASAPCertificateImpl;
import net.sharksystem.crypto.ASAPKeyStorage;
import net.sharksystem.crypto.BasicKeyStore;
import net.sharksystem.crypto.InMemoASAPKeyStorage;
import net.sharksystem.crypto.SharkCryptoException;
import net.sharksystem.persons.ASAPKeyStoreWithWriteAccess;
import net.sharksystem.persons.ASAPPKI;
import net.sharksystem.persons.SampleFullAsapPKIStorage;
import net.sharksystem.persons.android.PersonsStorageAndroidComponent;

import java.io.IOException;
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

import static net.sharksystem.sharknet.android.OwnerStorage.PREFERENCES_FILE;

/**
 * Overwrites key creation and add kex persistence to the more general super class
 */
public class AndroidASAPKeyStorage extends InMemoASAPKeyStorage
//        SampleFullAsapPKIStorage
//        implements ASAPKeyStoreWithWriteAccess, ASAPPKI, BasicKeyStore
{

    private static final String KEYPAIR_CREATION_TIME = "ASAPCertificatesKeyPairCreationTime";
    public static final String KEYSTORE_NAME = "AndroidKeyStore";
    public static final String KEYSTORE_OWNER_ALIAS = "ASAPCertificatesKeysOwner";
    public static final String DEFAULT_KEYSTORE_PWD = "asap4ever";
    private static final int KEY_SIZE = 2048;
    private final static int ANY_PURPOSE = KeyProperties.PURPOSE_ENCRYPT |
            KeyProperties.PURPOSE_DECRYPT | KeyProperties.PURPOSE_SIGN |
            KeyProperties.PURPOSE_VERIFY;
    private static final String KEYSTORE_PWD = "ASAPCertificatesKeyStorePWD";

    private long creationTime = DateTimeHelper.TIME_NOT_SET;
    private KeyStore keyStore = null;
    private Context initialContext = null;

    public AndroidASAPKeyStorage(Context initialContext,
                                     CharSequence ownerID, CharSequence ownerName)
                throws ASAPSecurityException {

        //super(ownerID, ownerName);
        this.initialContext = initialContext;
    }

    @Override
    public void generateKeyPair() throws ASAPSecurityException {
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
//            this.save();
        } catch (Exception e) {
            String text = "problems when generating key pair: " + e.getMessage();
            Log.d(this.getLogStart(), text);
            throw new ASAPSecurityException(text);
        }
    }

    private Context getContext() {
        try {
            return PersonsStorageAndroidComponent.getPersonsStorage().getContext();
        } catch (ASAPException e) {
            Log.d(this.getLogStart(), "cannot get context: " + e.getLocalizedMessage());
        } catch(ASAPComponentNotYetInitializedException e) {
            // startup is sometimes a tricky thing
            return this.initialContext;
        }

        return null;
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


        /* nothing to do when using android key storage
    public void load(InputStream inputStream) throws SharkCryptoException, IOException {
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
    }
         */

        /* nothing to do when using android key storage
    private void save() {
        try {
            File keyStoreFile = SharkNetApp.getSharkNetApp().getKeyStoreFile(false);
            KeyStore keyStore = this.getKeyStore();
            keyStore.store(new FileOutputStream(keyStoreFile), this.getKeyStorePWD().toCharArray());
        } catch (SharkException | KeyStoreException | CertificateException
                | NoSuchAlgorithmException | IOException e) {
            Log.e(this.getLogStart(), "cannot write key store file: " + e.getLocalizedMessage());
        }
    }
         */

    public void setKeyStorePWD(String pwd) {
        Log.d(this.getLogStart(), "set key  store pwd: ");
        SharedPreferences sharedPref = this.getContext().getSharedPreferences(
                PREFERENCES_FILE, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(KEYSTORE_PWD, pwd);

        // create owner id
        editor.commit();

        //this.save();
    }

    public String getKeyStorePWD() throws SharkCryptoException {
        SharedPreferences sharedPref = this.getContext().getSharedPreferences(
                PREFERENCES_FILE, Context.MODE_PRIVATE);

        return sharedPref.getString(KEYSTORE_PWD, DEFAULT_KEYSTORE_PWD);
    }

    protected void reloadKeys() throws ASAPSecurityException {
        Log.d(this.getLogStart(), "reload private keys from android key storage");
        try {
            KeyStore keyStore = this.getKeyStore();
            KeyStore.PrivateKeyEntry privateKeyEntry =
                    (KeyStore.PrivateKeyEntry) keyStore.getEntry(KEYSTORE_OWNER_ALIAS, null);

            super.setPrivateKey(privateKeyEntry.getPrivateKey());
            super.setPublicKey(privateKeyEntry.getCertificate().getPublicKey());
        } catch (KeyStoreException | UnrecoverableEntryException | NoSuchAlgorithmException e) {
            throw new ASAPSecurityException(e.getLocalizedMessage());
        }
    }

    @Override
    public PrivateKey getPrivateKey() throws ASAPSecurityException {
        try {
            super.getPrivateKey();
        }
        catch(ASAPSecurityException e) {
            // maybe not yet loaded
            this.reloadKeys();
        }
        return super.getPrivateKey();
    }

    @Override
    public PublicKey getPublicKey() throws ASAPSecurityException {
        try {
            super.getPublicKey();
        }
        catch(ASAPSecurityException e) {
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
    public long getCreationTime() throws ASAPSecurityException {
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

    /////////////////////////////////////////////////////////////////////////////////////////////
    //                                     key storage                                         //
    /////////////////////////////////////////////////////////////////////////////////////////////

    private static AndroidASAPKeyStorage instance = null;

    public static AndroidASAPKeyStorage initializeASAPKeyStorage(
            Context initialContext, CharSequence ownerID, CharSequence ownerName) {
        if(instance == null) {
            // re-read from file system
            try {
                AndroidASAPKeyStorage.instance =
                        new AndroidASAPKeyStorage(initialContext, ownerID, ownerName);
//                AndroidASAPKeyStorage.instance.load(new FileInputStream(keyStoreFile));
            } catch (Exception e) {
                Log.d("AndroidASAPKeyStorage", "probably key store file not found: "
                        + e.getLocalizedMessage());
            }
        }
        return instance;
    }

    public static ASAPKeyStorage getASAPKeyStorage() {
        if(instance == null) {
            throw new ASAPComponentNotYetInitializedException("asap key storage must be initialized first");
        }
        return instance;
    }

    static AndroidASAPKeyStorage getAndroidASAPKeyStorage() {
        return (AndroidASAPKeyStorage) AndroidASAPKeyStorage.getASAPKeyStorage();
    }

    public boolean secureKeyAvailable() throws ASAPSecurityException {
        return instance.getCreationTime() != DateTimeHelper.TIME_NOT_SET;
    }
}
