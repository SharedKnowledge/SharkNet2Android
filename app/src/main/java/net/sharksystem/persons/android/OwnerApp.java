package net.sharksystem.persons.android;

import net.sharksystem.asap.ASAPException;
import net.sharksystem.sharknet.android.SharkNetActivity;

import java.io.IOException;

public class OwnerApp {
    public static final CharSequence APP_NAME = "SN2Persons";
    public static final CharSequence CREDENTIAL_URI = "sn2://credential";
    public static final CharSequence CERTIFICATE_URI = "sn2://certificate";

    private static OwnerApp instance = null;

    public static OwnerApp getOwnerApp() {
        if(OwnerApp.instance == null) {
            OwnerApp.instance = new OwnerApp();
        }

        return OwnerApp.instance;
    }

    private OwnerApp() {

    }

    private byte[] getPublicKey() {
        return ("dummyKey".getBytes());
    }

    private String getOwnerName() {
        return "DummyName";
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
