package net.sharksystem.persons.android;

import android.content.Context;
import android.util.Log;

import net.sharksystem.SharkException;
import net.sharksystem.asap.ASAPEngineFS;
import net.sharksystem.asap.ASAPException;
import net.sharksystem.asap.ASAPSecurityException;
import net.sharksystem.asap.ASAPStorage;
import net.sharksystem.asap.android.Util;
import net.sharksystem.asap.android.apps.ASAPApplication;
import net.sharksystem.asap.android.apps.ASAPApplicationComponent;
import net.sharksystem.asap.android.apps.ASAPApplicationComponentHelper;
import net.sharksystem.asap.android.apps.ASAPComponentNotYetInitializedException;
import net.sharksystem.crypto.ASAPCertificate;
import net.sharksystem.crypto.ASAPCertificateStorage;
import net.sharksystem.crypto.ASAPCertificateStorageImpl;
import net.sharksystem.crypto.ASAPKeyStorage;
import net.sharksystem.persons.ASAPPKIImpl;
import net.sharksystem.persons.CredentialMessage;
import net.sharksystem.persons.FullAsapPKIStorage;
import net.sharksystem.persons.TestHelperPersonStorage;
import net.sharksystem.sharknet.android.AndroidASAPKeyStorage;
import net.sharksystem.sharknet.android.Owner;
import net.sharksystem.sharknet.android.OwnerFactory;
import net.sharksystem.sharknet.android.PersonsStorage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class PersonsStorageAndroidComponent extends FullAsapPKIStorage //ASAPPKIImpl
        implements ASAPApplicationComponent, PersonsStorage, OwnerFactory /*InMemoPersonsStorageImpl*/ {

    private static final String PERSONS_STORAGE_FILE_NAME = "sn2_personsStorageFile";

    private static PersonsStorageAndroidComponent instance = null;
    private final ASAPApplicationComponentHelper asapComponentHelper;
    private final OwnerFactory ownerFactory;
    private Set<CharSequence> selectedItemIDs = null;
    private CredentialMessage credentialMessage;
    private final AndroidASAPKeyStorage asapKeyStorage;

    private PersonsStorageAndroidComponent(ASAPApplication asapApplication,
                       OwnerFactory ownerFactory, ASAPStorage asapStorage,
                           AndroidASAPKeyStorage keyStorage)
            throws IOException, ASAPSecurityException {

        super(
            new ASAPCertificateStorageImpl(asapStorage,
                    asapApplication.getOwnerID(), // id
                    asapApplication.getOwnerName() // name
                ),
            keyStorage
            );

        // set up component helper
        this.asapComponentHelper = new ASAPApplicationComponentHelper();
        this.asapComponentHelper.setASAPApplication(asapApplication);

        this.asapKeyStorage = keyStorage;

        // remember owner storage
        this.ownerFactory = ownerFactory;

        // stored previous instance?
        try {
            File personsStorageFile = this.getPersonsStorageFile(true);
            InputStream is = new FileInputStream(personsStorageFile);
            this.load(is);
            is.close();
        }
        catch(SharkException e) {
            // switch debugging / productive version
//            Log.d(this.getLogStart(), "there is no persons storage persistence file - ok. Start empty");
            Log.d(this.getLogStart(), "there is no persons storage persistence file - fill with EXAMPLE DATA");
            TestHelperPersonStorage.fillWithExampleData(this);
            this.save();
        }
    }

    public static PersonsStorageAndroidComponent initialize(
            ASAPApplication asapApplication, OwnerFactory ownerFactory,
            AndroidASAPKeyStorage asapKeyStorage) {

        try {
            /*
            File personsStorageFile =
                    PersonsStorageAndroidComponent.getPersonsStorageFile(
                            asapApplication, true);
             */

            PersonsStorageAndroidComponent.instance = new PersonsStorageAndroidComponent(
                    asapApplication, ownerFactory,
                    ASAPEngineFS.getASAPStorage(
                            asapApplication.getOwnerID().toString(), // owner
                            asapApplication.getASAPComponentFolder( // folderName
                                    ASAPCertificateStorage.CERTIFICATE_APP_NAME).toString(),
                            ASAPCertificateStorage.CERTIFICATE_APP_NAME), // app name

//                    AndroidASAPKeyStorage.createASAPKeyStorage(personsStorageFile)
//                    AndroidASAPKeyStorage.initializeASAPKeyStorage(asapApplication.getActivity())
                    asapKeyStorage
            );

            return PersonsStorageAndroidComponent.instance;
        } catch (Exception e) {
            Log.e(net.sharksystem.asap.util.Log.startLog(ASAPPKIImpl.class).toString(),
                    "problems when creating ASAP Storage:" + e.getLocalizedMessage());
        }
        return null;
    }

    public static synchronized PersonsStorageAndroidComponent getPersonsStorage()
            throws ASAPComponentNotYetInitializedException {
        if(PersonsStorageAndroidComponent.instance == null) {
            throw new ASAPComponentNotYetInitializedException("component not yet initialized");
        }

        return PersonsStorageAndroidComponent.instance;
    }

    public Owner getOwnerData() {
        return this.ownerFactory.getOwnerData();
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
            throws IOException, ASAPSecurityException {

        ASAPCertificate cert = super.addAndSignPerson(userID, userName, publicKey, validSince);
        this.save();

        return cert;
    }

    void save() {
        try {
            File personsStorageFile = this.getPersonsStorageFile(false);
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
        } catch (ASAPSecurityException e) {
            // not found - return id instead
            return userID;
        }
    }

    public File getPersonsStorageFile(boolean mustExist) throws SharkException {
        String personsMementoFileName =
                this.getASAPApplication().getASAPRootFolder() + "/" + PERSONS_STORAGE_FILE_NAME;

        File personsMementoFile = new File(personsMementoFileName);
        if(mustExist && !personsMementoFile.exists()) {
            throw new SharkException("persons storage memento file does not exist");
        }

        return personsMementoFile;
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //                                   component methods                            //
    ////////////////////////////////////////////////////////////////////////////////////

    public static Collection<CharSequence> geRequiredFormats() {
        Collection<CharSequence> formats = new ArrayList<>();
        formats.add(PersonsStorageAndroidComponent.CREDENTIAL_APP_NAME);
        return formats;
    }

    @Override
    public Context getContext() throws ASAPException {
        return this.asapComponentHelper.getContext();
    }

    @Override
    public ASAPApplication getASAPApplication() {
        return this.asapComponentHelper.getASAPApplication();
    }

    public ASAPKeyStorage getASAPKeyStorage() {
        return this.asapKeyStorage;
    }

    public AndroidASAPKeyStorage getAndroidASAPKeyStorage() {
        return this.asapKeyStorage;
    }
}
