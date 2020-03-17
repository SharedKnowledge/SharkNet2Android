package net.sharksystem.persons.android;

import android.util.Log;

import net.sharksystem.SharkException;
import net.sharksystem.asap.ASAPEngineFS;
import net.sharksystem.asap.ASAPException;
import net.sharksystem.asap.ASAPStorage;
import net.sharksystem.asap.android.Util;
import net.sharksystem.crypto.ASAPCertificateStorage;
import net.sharksystem.crypto.ASAPCertificateStorageImpl;
import net.sharksystem.crypto.ASAPKeyStorage;
import net.sharksystem.crypto.SharkCryptoException;
import net.sharksystem.persons.CredentialMessage;
import net.sharksystem.persons.PersonsStorageImpl;
import net.sharksystem.sharknet.android.SharkNetActivity;
import net.sharksystem.sharknet.android.SharkNetApp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Set;

public class PersonsStorageAndroid extends PersonsStorageImpl /*InMemoPersonsStorageImpl*/ {
    public static final CharSequence APP_NAME = "SN2Credentials";
    public static final CharSequence CREDENTIAL_URI = "sn2://credential";
    private static final String SN_ANDROID_DEFAULT_SIGNING_ALGORITHM = "SHA256withRSA/PSS";

    private static PersonsStorageAndroid instance = null;
    private Set<CharSequence> selectedItemIDs = null;

    private PersonsStorageAndroid(ASAPStorage asapStorage, ASAPKeyStorage keyStorage)
            throws SharkException, IOException {
        super(
            new ASAPCertificateStorageImpl(asapStorage,
                    SharkNetApp.getSharkNetApp().getOwnerID(),
                    SharkNetApp.getSharkNetApp().getASAPOwner()
                ), 
            keyStorage,
            SN_ANDROID_DEFAULT_SIGNING_ALGORITHM
            );

        // stored previous instance?
        try {
            File personsStorageFile = SharkNetApp.getSharkNetApp().getPersonsStorageFile(true);
            InputStream is = new FileInputStream(personsStorageFile);
            this.load(is);
            is.close();
        }
        catch(SharkException e) {
            Log.d(this.getLogStart(), "there is no persons storage persistence file - ok. Start empty");
        }
    }

    public static PersonsStorageAndroid getPersonsApp() {
        if(PersonsStorageAndroid.instance == null) {
            try {
                PersonsStorageAndroid.instance = new PersonsStorageAndroid(
                        ASAPEngineFS.getASAPStorage(
                            SharkNetApp.getSharkNetApp().getASAPOwner().toString(), // owner
                            SharkNetApp.getSharkNetApp().getASAPRootFolder().
                                    toString() +"/" + ASAPCertificateStorage.APP_NAME, // folder
                            ASAPCertificateStorage.APP_NAME), // app name
                        OwnerStorageAndroid.getOwnerStorageAndroid().getASAPKeyStorage()
                );

                //instance.fillWithExampleData();
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

    //////////////////////////////////////////////////////////////////////////////////////////
    //                        decorator to implement persistence                            //
    //////////////////////////////////////////////////////////////////////////////////////////

    void save() {
        try {
            File personsStorageFile =
                    SharkNetApp.getSharkNetApp().getPersonsStorageFile(false);
            OutputStream os = new FileOutputStream(personsStorageFile);
            this.store(os);
            os.close();
        } catch (SharkException | IOException e) {
            Log.e(this.getLogStart(), "cannot create persistence file for persons storage");
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////
    //                        helper to exchange stati between activities                   //
    //////////////////////////////////////////////////////////////////////////////////////////

    public void sendCredentialMessage(SharkNetActivity snActivity, int randomInt, CharSequence userID)
            throws IOException, ASAPException, SharkCryptoException {

        CredentialMessage credentialMessage =
                new CredentialMessage(randomInt, userID,
                        this.getOwnerName(), this.getKeysCreationTime(),
                        this.getPublicKey());

        snActivity.sendASAPMessage(APP_NAME, CREDENTIAL_URI,
                credentialMessage.getMessageAsBytes(), true);
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

    public Set<CharSequence> getPreselectionSet() {
        if(this.preselectedIDs == null) return new HashSet();
        else return this.preselectedIDs;
    }

    private String getLogStart() {
        return Util.getLogStart(this).toString();
    }
}
