package net.sharksystem.bubble.crypto;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import net.sharksystem.storage.keystore.RSAKeystoreHandler;

import org.junit.Before;
import org.junit.Test;

import java.security.Key;

import timber.log.Timber;

import static org.junit.Assert.assertNotNull;

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

}