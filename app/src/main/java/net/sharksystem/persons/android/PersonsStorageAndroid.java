package net.sharksystem.persons.android;

import android.util.Log;

import net.sharksystem.SharkException;
import net.sharksystem.asap.ASAPEngineFS;
import net.sharksystem.asap.ASAPStorage;
import net.sharksystem.asap.android.Util;
import net.sharksystem.crypto.ASAPCertificate;
import net.sharksystem.crypto.ASAPCertificateStorage;
import net.sharksystem.crypto.ASAPCertificateStorageImpl;
import net.sharksystem.crypto.ASAPKeyStorage;
import net.sharksystem.crypto.SharkCryptoException;
import net.sharksystem.persons.CredentialMessage;
import net.sharksystem.persons.PersonsStorageImpl;
import net.sharksystem.sharknet.android.SharkNetApp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.PublicKey;
import java.util.HashSet;
import java.util.Set;

public class PersonsStorageAndroid extends PersonsStorageImpl /*InMemoPersonsStorageImpl*/ {
    public static final String SN_ANDROID_DEFAULT_SIGNING_ALGORITHM = "SHA256withRSA/PSS";

    private static PersonsStorageAndroid instance = null;
    private Set<CharSequence> selectedItemIDs = null;
    private CredentialMessage credentialMessage;

    private PersonsStorageAndroid(ASAPStorage asapStorage, ASAPKeyStorage keyStorage)
            throws SharkException, IOException {
        super(
            new ASAPCertificateStorageImpl(asapStorage,
                    SharkNetApp.getSharkNetApp().getOwnerID(), // id
                    SharkNetApp.getSharkNetApp().getOwnerStorage().getDisplayName() // name
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

    public boolean isMe(CharSequence userID) {
        return this.getOwnerID().equals(userID);
    }

    public static synchronized PersonsStorageAndroid getPersonsStorage() {
        if(PersonsStorageAndroid.instance == null) {
            try {
                PersonsStorageAndroid.instance = new PersonsStorageAndroid(
                        ASAPEngineFS.getASAPStorage(
                            SharkNetApp.getSharkNetApp().getASAPOwnerID().toString(), // owner
                            SharkNetApp.getSharkNetApp().getASAPRootFolder().
                                    toString() +"/" + ASAPCertificateStorage.CERTIFICATE_APP_NAME, // folder
                            ASAPCertificateStorage.CERTIFICATE_APP_NAME), // app name
                        SharkNetApp.getSharkNetApp().getASAPKeyStorage()
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

    @Override
    public ASAPCertificate addAndSignPerson(
            CharSequence userID, CharSequence userName, PublicKey publicKey, long validSince)
            throws SharkCryptoException, IOException {

        ASAPCertificate cert = super.addAndSignPerson(userID, userName, publicKey, validSince);
        this.save();

        return cert;
    }

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
/*
    public CredentialMessage createCredentialMessage() throws SharkCryptoException {
            return this.createCredentialMessage();

            CredentialMessage credentialMessage =
                    new CredentialMessage(
                            SharkNetApp.getSharkNetApp().getOwnerStorage().getUUID(),
                            SharkNetApp.getSharkNetApp().getOwnerStorage().getDisplayName(),
                            this.getKeysCreationTime(), this.getPublicKey());

            return credentialMessage;
    }
 */

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

    public void setReceivedCredential(CredentialMessage credentialMessage) {
        this.credentialMessage = credentialMessage;
    }

    public CredentialMessage getReceivedCredential() {
        return this.credentialMessage;
    }

    public Set<CharSequence> getPreselectionSet() {
        if(this.preselectedIDs == null) return new HashSet();
        else return this.preselectedIDs;
    }

    private String getLogStart() {
        return Util.getLogStart(this).toString();
    }

    public CharSequence getPersonName(CharSequence userID) {
        if(userID.equals(this.getOwnerID())) {
            return "You";
        }

        try {
            return this.getPersonValues(userID).getName();
        } catch (SharkException e) {
            // not found - return id instead
            return userID;
        }
    }
}
