package net.sharksystem.persons.android;

import net.sharksystem.asap.ASAPException;
import net.sharksystem.asap.ASAPStorage;
import net.sharksystem.crypto.ASAPCertificateStorage;
import net.sharksystem.crypto.ASAPCertificateStorageImpl;
import net.sharksystem.persons.CredentialMessage;
import net.sharksystem.persons.PersonsStorage;
import net.sharksystem.persons.PersonsStorageImpl;
import net.sharksystem.sharknet.android.SharkNetActivity;
import net.sharksystem.sharknet.android.SharkNetApp;

import java.io.IOException;

public class PersonsStorageAndroid extends PersonsStorageImpl {
    public static final CharSequence APP_NAME = "SN2Persons";
    public static final CharSequence CREDENTIAL_URI = "sn2://credential";
    public static final CharSequence CERTIFICATE_URI = "sn2://certificate";

    private static PersonsStorageAndroid instance = null;

    public PersonsStorageAndroid(ASAPStorage asapStorage) {
        super(new ASAPCertificateStorageImpl(asapStorage, 42, "DummyName"));
    }

    public static PersonsStorageAndroid getPersonsApp() {
        if(PersonsStorageAndroid.instance == null) {

            // TODO
            PersonsStorageAndroid.instance = new PersonsStorageAndroid(null);
        }

        return PersonsStorageAndroid.instance;
    }

    public CharSequence getOwnerName() {
        return SharkNetApp.getSharkNetApp().getASAPOwner();
    }

    public void sendCredentialMessage(SharkNetActivity snActivity, int randomInt, int userID)
            throws IOException, ASAPException {

        CredentialMessage credentialMessage =
                new CredentialMessage(randomInt, userID,
                        this.getOwnerName(), this.getPublicKey());

        snActivity.sendASAPMessage(APP_NAME,
                CREDENTIAL_URI,
                null,
                credentialMessage.getMessageAsBytes());
    }
}
