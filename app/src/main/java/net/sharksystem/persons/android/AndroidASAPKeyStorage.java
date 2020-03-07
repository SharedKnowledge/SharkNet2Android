package net.sharksystem.persons.android;

import android.content.Context;
import android.icu.text.SymbolTable;
import android.security.KeyPairGeneratorSpec;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Log;

import net.sharksystem.SharkException;
import net.sharksystem.crypto.ASAPCertificate;
import net.sharksystem.crypto.ASAPCertificateImpl;
import net.sharksystem.crypto.ASAPKeyStorage;
import net.sharksystem.crypto.InMemoASAPKeyStorage;
import net.sharksystem.crypto.SharkCryptoException;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableEntryException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Calendar;

import javax.security.auth.x500.X500Principal;

public class AndroidASAPKeyStorage extends InMemoASAPKeyStorage implements ASAPKeyStorage {
    private Context ctx;
    private static final String KEYSTORE_NAME = "AndroidKeyStore";
    private static final String KEYSTORE_OWNER_ALIAS = "SN2_Owner_Keys";
    private static final int KEY_SIZE = 2048;
    private final static int ANY_PURPOSE = KeyProperties.PURPOSE_ENCRYPT |
            KeyProperties.PURPOSE_DECRYPT | KeyProperties.PURPOSE_SIGN |
            KeyProperties.PURPOSE_VERIFY;

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

        } catch (Exception e) {
            String text = "problems when generating key pair: " + e.getMessage();
            Log.d(this.getLogStart(), text);
            throw new SharkException(text);
        }
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

    @Override
    public long getCreationTime() throws SharkCryptoException {
        Log.e(this.getLogStart(), "TODO: use real key creation timestamp");
        return System.currentTimeMillis();
    }

    protected String getLogStart() {
        return this.getClass().getSimpleName();
    }
}
