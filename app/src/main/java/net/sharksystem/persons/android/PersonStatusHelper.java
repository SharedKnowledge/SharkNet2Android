package net.sharksystem.persons.android;

import net.sharksystem.asap.android.Util;
import net.sharksystem.asap.android.apps.ASAPComponentNotYetInitializedException;
import net.sharksystem.asap.persons.CredentialMessage;

import java.util.HashSet;
import java.util.Set;

public class PersonStatusHelper /*extends FullAsapPKIStorage*/ //ASAPPKIImpl
// /*InMemoPersonsStorageImpl*/
{

    private static PersonStatusHelper instance = null;
    private Set<CharSequence> selectedItemIDs = null;
    private CredentialMessage credentialMessage;

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


    /*
    private static final String PERSONS_STORAGE_FILE_NAME = "sn2_personsStorageFile";
    private final ASAPApplicationComponentHelper asapComponentHelper;
    private final OwnerFactory ownerFactory;
    private final AndroidASAPKeyStorage asapKeyStorage;
     */

        /*
    private PersonsStorageAndroidComponent(ASAPApplication asapApplication,
                       OwnerFactory ownerFactory, ASAPStorage asapStorage,
                           AndroidASAPKeyStorage keyStorage)
            throws IOException, ASAPSecurityException {

        super(
            new ASAPCertificateStorageImpl(asapStorage,
                    SharkNetApp.getSharkNetApp().getID(), // id
                    SharkNetApp.getSharkNetApp().getDisplayName() // name
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
         */

    /*
    public static PersonsStorageAndroidComponent initialize(
            ASAPApplication asapApplication, OwnerFactory ownerFactory,
            AndroidASAPKeyStorage asapKeyStorage) {

        try {
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
    */

    public static synchronized PersonStatusHelper getPersonsStorage()
            throws ASAPComponentNotYetInitializedException {
        if(PersonStatusHelper.instance == null) {
            throw new ASAPComponentNotYetInitializedException("component not yet initialized");
        }

        return PersonStatusHelper.instance;
    }

    /*
    public Owner getOwnerData() {
        return this.ownerFactory.getOwnerData();
    }

    public CharSequence getOwnerName() {
        return SharkNetApp.getSharkNetApp().getASAPOwner();
    }
     */

    //////////////////////////////////////////////////////////////////////////////////////////
    //                        decorator to implement persistence                            //
    //////////////////////////////////////////////////////////////////////////////////////////

    /*
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
     */

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

    private String getLogStart() {
        return Util.getLogStart(this).toString();
    }

    /*
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
     */

    /*
    public File getPersonsStorageFile(boolean mustExist) throws SharkException {
        String personsMementoFileName =
                this.getASAPApplication().getASAPRootFolder() + "/" + PERSONS_STORAGE_FILE_NAME;

        File personsMementoFile = new File(personsMementoFileName);
        if(mustExist && !personsMementoFile.exists()) {
            throw new SharkException("persons storage memento file does not exist");
        }

        return personsMementoFile;
    }
     */

    ////////////////////////////////////////////////////////////////////////////////////
    //                                   component methods                            //
    ////////////////////////////////////////////////////////////////////////////////////

    /*
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
     */
}
