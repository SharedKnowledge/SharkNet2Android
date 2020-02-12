package net.sharksystem.persons.android;

import net.sharksystem.asap.ASAPException;
import net.sharksystem.persons.PersonsApp;
import net.sharksystem.sharknet.android.SharkNetActivity;
import net.sharksystem.sharknet.android.SharkNetApp;

import java.io.IOException;

public class PersonsAppAndroid extends PersonsApp {
    public static final CharSequence APP_NAME = "SN2Persons";
    public static final CharSequence CREDENTIAL_URI = "sn2://credential";
    public static final CharSequence CERTIFICATE_URI = "sn2://certificate";

    private static PersonsAppAndroid instance = null;

    public static PersonsAppAndroid getPersonsApp() {
        if(PersonsAppAndroid.instance == null) {
            PersonsAppAndroid.instance = new PersonsAppAndroid();
        }

        return PersonsAppAndroid.instance;
    }

    private CharSequence getOwnerName() {
        return SharkNetApp.getSharkNetApp().getASAPOwner();
    }

    public void sendCredentialMessage(SharkNetActivity snActivity, int randomInt, int userID)
            throws IOException, ASAPException {

        CredentialMessage credentialMessage =
                new CredentialMessage(randomInt, userID,
                        this.getOwnerName(), this.getOwnerPublicKey());

        snActivity.sendASAPMessage(APP_NAME,
                CREDENTIAL_URI,
                null,
                credentialMessage.getMessageAsBytes());
    }
}
