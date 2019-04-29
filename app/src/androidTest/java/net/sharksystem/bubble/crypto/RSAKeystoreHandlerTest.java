package net.sharksystem.bubble.crypto;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.util.Base64;
import android.util.Log;

import net.sharksystem.storage.keystore.RSAKeystoreHandler;

import org.junit.Before;
import org.junit.Test;

import java.security.Key;

import timber.log.Timber;

import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class RSAKeystoreHandlerTest {

    public static final String TAG = "RSATest";
    RSAKeystoreHandler keystoreHandler;

    @Before
    public void setUp() throws Exception {
        Context context = InstrumentationRegistry.getTargetContext();
        keystoreHandler = RSAKeystoreHandler.getInstance();
        Timber.plant(new Timber.DebugTree());
    }

    @Test
    public void generateSHA256Keys() {
        Key keyPair = keystoreHandler.getPublicKey();
        assertNotNull(keyPair);
    }

    @Test
    public void decrypt() {
        String toEncrypt = "Hello World";
        byte[] encryptWithPublicKeySHA256bytes = keystoreHandler.encrypt(toEncrypt.getBytes());
        byte[] decryptSHA256 = keystoreHandler.decrypt(encryptWithPublicKeySHA256bytes);
        assertNotNull(decryptSHA256);
    }

    @Test
    public void encrypt() {
        String toEncrypt = "Hello World";
        byte[] encryptWithPublicKeySHA256bytes = keystoreHandler.encrypt(toEncrypt.getBytes());
        assertNotNull(encryptWithPublicKeySHA256bytes);
    }

    @Test
    public void signData() {
        String dataToSign = "datadatadata";
        byte[] signedBytes = keystoreHandler.signData(dataToSign.getBytes());
        Log.d(TAG, "signData: " + Base64.encodeToString(signedBytes, Base64.DEFAULT));

        assertNotNull(signedBytes);
    }

    @Test
    public void verifyData() {
        String dataToSign = "datadatadata";
        byte[] signedBytes = keystoreHandler.signData(dataToSign.getBytes());

        boolean validSignature = keystoreHandler.verifySignature(dataToSign.getBytes(), signedBytes, keystoreHandler.getCertificate());
        Log.d(TAG, "signData: " + Base64.encodeToString(signedBytes, Base64.DEFAULT));

        assertTrue(validSignature);
    }

    @Test
    public void verifyDataChangeData() {
        String dataToSign = "datadatadata";
        byte[] signedBytes = keystoreHandler.signData(dataToSign.getBytes());

        String changedDataToSign = "changed";


        boolean validSignature = keystoreHandler.verifySignature(changedDataToSign.getBytes(), signedBytes, keystoreHandler.getCertificate());
        Log.d(TAG, "signData: " + Base64.encodeToString(signedBytes, Base64.DEFAULT));

        assertFalse(validSignature);
    }

    @Test
    public void verifyDataWithDiffrentPublicKey() {
        String dataToSign = "datadatadata";
        byte[] signedBytes = keystoreHandler.signData(dataToSign.getBytes());

        String changedDataToSign = "changed";


        boolean validSignature = keystoreHandler.verifySignature(changedDataToSign.getBytes(), signedBytes, keystoreHandler.getCertificate());
        Log.d(TAG, "signData: " + Base64.encodeToString(signedBytes, Base64.DEFAULT));

        assertFalse(validSignature);
    }

}