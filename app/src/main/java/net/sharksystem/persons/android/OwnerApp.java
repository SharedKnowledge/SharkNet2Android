package net.sharksystem.persons.android;

import android.util.Log;

import net.sharksystem.asap.ASAPException;
import net.sharksystem.sharknet.android.SharkNetActivity;
import net.sharksystem.sharknet.android.SharkNetApp;

import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class OwnerApp {
    public static final CharSequence APP_NAME = "SN2Persons";
    public static final CharSequence CREDENTIAL_URI = "sn2://credential";
    public static final CharSequence CERTIFICATE_URI = "sn2://certificate";

    private static OwnerApp instance = null;
    private KeyPair rsaKeyPair = null;

    public static OwnerApp getOwnerApp() {
        if(OwnerApp.instance == null) {
            OwnerApp.instance = new OwnerApp();
        }

        return OwnerApp.instance;
    }

    private OwnerApp() {
        if(this.rsaKeyPair == null) {
            try {
                this.generateKeyPair();
            } catch (NoSuchAlgorithmException e) {
                Log.e(net.sharksystem.asap.util.Log.startLog(this).toString(),
                        "cannot create key pair - fatal");
            }
        }
    }

    void generateKeyPair() throws NoSuchAlgorithmException {
        SecureRandom secRandom = new SecureRandom();
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048, secRandom);
        rsaKeyPair = keyGen.generateKeyPair();
    }

    private byte[] getPublicKey() {
        return new byte[2048];
    }

    private CharSequence getOwnerName() {
        return SharkNetApp.getSharkNetApp().getASAPOwner();
    }

    public void sendCredentialMessage(SharkNetActivity snActivity, int randomInt)
            throws IOException, ASAPException {

        CredentialMessage credentialMessage =
                new CredentialMessage(randomInt, this.getOwnerName(), this.getPublicKey());

        snActivity.sendASAPMessage(APP_NAME,
                CREDENTIAL_URI,
                null,
                credentialMessage.getMessageAsBytes());
    }
}
