package net.sharksystem.persons.android;

import android.util.Log;

import net.sharksystem.asap.ASAPEngineFS;
import net.sharksystem.asap.ASAPException;
import net.sharksystem.asap.ASAPStorage;
import net.sharksystem.crypto.ASAPCertificateStorage;
import net.sharksystem.persons.CredentialMessage;
import net.sharksystem.persons.InMemoPersonsStorageImpl;
import net.sharksystem.persons.PersonsStorageImpl;
import net.sharksystem.sharknet.android.SharkNetActivity;
import net.sharksystem.sharknet.android.SharkNetApp;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class PersonsStorageAndroid extends InMemoPersonsStorageImpl /*PersonsStorageImpl*/ {
    public static final CharSequence APP_NAME = "SN2Credentials";
    public static final CharSequence CREDENTIAL_URI = "sn2://credential";

    private static PersonsStorageAndroid instance = null;
    private Set<CharSequence> selectedItemIDs = null;

    private PersonsStorageAndroid(ASAPStorage asapStorage) {
        /*
        super(new ASAPCertificateStorageImpl(asapStorage,
                SharkNetApp.getSharkNetApp().getOwnerID(),
                SharkNetApp.getSharkNetApp().getASAPOwner()));
         */

        super(SharkNetApp.getSharkNetApp().getOwnerID(),
                SharkNetApp.getSharkNetApp().getASAPOwner());
    }

    public static PersonsStorageAndroid getPersonsApp() {
        if(PersonsStorageAndroid.instance == null) {
            try {
                PersonsStorageAndroid.instance = new PersonsStorageAndroid(
                        ASAPEngineFS.getASAPStorage(
                            SharkNetApp.getSharkNetApp().getASAPOwner().toString(),
                            SharkNetApp.getSharkNetApp().getASAPRootFolder().toString(),
                            ASAPCertificateStorage.APP_NAME));

                instance.fillWithExampleData();
            } catch (Exception e) {
                Log.e(net.sharksystem.asap.util.Log.startLog(PersonsStorageImpl.class).toString(),
                        "problems when creating ASAP Storage:" + e.getLocalizedMessage());
            }
        }

        return PersonsStorageAndroid.instance;
    }

    /*
    public CharSequence getOwnerName() {
        return SharkNetApp.getSharkNetApp().getASAPOwner();
    }
     */

    public void sendCredentialMessage(SharkNetActivity snActivity, int randomInt, CharSequence userID)
            throws IOException, ASAPException {

        CredentialMessage credentialMessage =
                new CredentialMessage(randomInt, userID,
                        this.getOwnerName(), this.getPublicKey());

        snActivity.sendASAPMessage(APP_NAME,
                CREDENTIAL_URI,
                credentialMessage.getMessageAsBytes());
    }

    public void setLastPersonsSelection(Set<CharSequence> selectedItemIDs) {
        this.selectedItemIDs = selectedItemIDs;
    }

    public Set<CharSequence> getLastPersonsSelection() {
        if(this.selectedItemIDs == null) return new HashSet();
        else return this.selectedItemIDs;
    }

    private Set<CharSequence> preselectedIDs = null;
    public void setPreselectionSet(Set<CharSequence> preselectedIDs) {
        this.preselectedIDs = preselectedIDs;
    }

    public Set<CharSequence> getPreselationSet() {
        if(this.preselectedIDs == null) return new HashSet();
        else return this.preselectedIDs;
    }

}
