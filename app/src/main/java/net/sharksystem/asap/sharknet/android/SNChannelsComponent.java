package net.sharksystem.asap.sharknet.android;

import android.content.Context;
import android.text.Editable;
import android.util.Log;

import net.sharksystem.asap.ASAPChannel;
import net.sharksystem.asap.ASAPException;
import net.sharksystem.asap.ASAPMessages;
import net.sharksystem.asap.ASAPStorage;
import net.sharksystem.asap.android.apps.ASAPApplication;
import net.sharksystem.asap.android.apps.ASAPApplicationComponent;
import net.sharksystem.asap.android.apps.ASAPApplicationComponentHelper;
import net.sharksystem.asap.android.apps.ASAPComponentNotYetInitializedException;
import net.sharksystem.asap.android.apps.ASAPMessageReceivedListener;
import net.sharksystem.crypto.BasicKeyStore;
import net.sharksystem.persons.ASAPPKI;
import net.sharksystem.sharknet.android.PersonsStorage;
import net.sharksystem.sharknet.android.OwnerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public class SNChannelsComponent implements
        ASAPApplicationComponent, ASAPMessageReceivedListener, PersonsStorage {
    public static final CharSequence APP_NAME = "SharkNet";
    private static final String KEY_NAME_SN_CHANNEL_NAME = "snChannelName";
    private static SNChannelsComponent instance;
    private final ASAPApplicationComponentHelper asapComponentHelper;
    private final BasicKeyStore basicKeyStore;
    private final OwnerFactory ownerFactory;
    private final PersonsStorage personsStorage;
    private final ASAPPKI asapPKI;

    public SNChannelsComponent(ASAPApplication asapApplication,
                   BasicKeyStore basicKeyStore,
                   ASAPPKI asapPKI,
                   OwnerFactory ownerFactory,
                   PersonsStorage personsStorage) {

        // set up component helper
        this.asapComponentHelper = new ASAPApplicationComponentHelper();
        this.asapComponentHelper.setASAPApplication(asapApplication);
        this.basicKeyStore = basicKeyStore;
        this.asapPKI = asapPKI;
        this.ownerFactory = ownerFactory;
        this.personsStorage = personsStorage;
    }

    /**
     * Is to be called during system setup from overall ASAPApplication
     * @param asapApplication
     */
    public static void initialize(ASAPApplication asapApplication,
                      BasicKeyStore basicKeyStore,
                      ASAPPKI asapPKI,
                      OwnerFactory ownerFactory,
                      PersonsStorage personsStorage) {
        try {
            SNChannelsComponent.instance = new SNChannelsComponent(
                    asapApplication,
                    basicKeyStore,
                    asapPKI,
                    ownerFactory,
                    personsStorage
            );

            // add chunk received listener
            asapApplication.addASAPMessageReceivedListener(
                    SNChannelsComponent.APP_NAME,
                    SNChannelsComponent.instance);

        } catch (Exception e) {
            Log.e(net.sharksystem.asap.util.Log.startLog(SNChannelsComponent.class).toString(),
                    "problems when creating ASAP Storage:" + e.getLocalizedMessage());
        }
    }

    /**
     * Factory method - component must be initialized before
     * @return
     * @throws ASAPComponentNotYetInitializedException
     */
    public static synchronized SNChannelsComponent getSharkNetChannelComponent()
            throws ASAPComponentNotYetInitializedException {
        if(SNChannelsComponent.instance == null) {
            throw new ASAPComponentNotYetInitializedException("component not yet initialized");
        }

        return SNChannelsComponent.instance;
    }

    public BasicKeyStore getBasicKeyStore() {
        return this.basicKeyStore;
    }

    public ASAPPKI getAsapPKI() { return this.asapPKI; }

    public CharSequence getOwnerID() {
        return this.ownerFactory.getOwner().getUUID();
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //                                   component methods                            //
    ////////////////////////////////////////////////////////////////////////////////////

    public static Collection<CharSequence> geRequiredFormats() {
        Collection<CharSequence> formats = new ArrayList<>();
        formats.add(SNChannelsComponent.APP_NAME);
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

    public void removeAll() {
        Log.d(this.getLogStart(), "NYI: remove all not yet implemented");
    }

    private ASAPStorage getASAPApplicationStorage() throws IOException, ASAPException {
        return this.asapComponentHelper.getASAPApplication().
                getASAPStorage(SNChannelsComponent.APP_NAME);
    }

    public void createChannel(Editable uri, Editable name) throws IOException, ASAPException {
        Log.d(this.getLogStart(), "createChannel()");

        ASAPStorage asapStorage = this.getASAPApplicationStorage();

        asapStorage.createChannel(uri);
        asapStorage.putExtra(uri, KEY_NAME_SN_CHANNEL_NAME, name.toString());
    }

    public SNChannelInformation getSNChannelInformation(int position)
            throws IOException, ASAPException {

        ASAPStorage asapStorage = this.getASAPApplicationStorage();

        CharSequence uri = asapStorage.getChannelURIs().get(position);
        return new SNChannelInformation(uri,
                asapStorage.getExtra(uri, SNChannelsComponent.KEY_NAME_SN_CHANNEL_NAME));
    }

    public int size() throws IOException, ASAPException {
        return this.getASAPApplicationStorage().getChannelURIs().size();
    }


    public ASAPChannel getStorage(CharSequence uri) throws IOException, ASAPException {
        return this.getASAPApplicationStorage().getChannel(uri);
    }

    /////////////////////////////////////////////////////////////////////////////////////////
    //                                   received listener                                 //
    /////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void asapMessagesReceived(ASAPMessages asapMessages) {
        // TODO
        Log.d(this.getLogStart(), "NYI: asapMessagesReceived()");
    }

    //////////////////////// helper
    private String getLogStart() {
        return this.getClass().getSimpleName() + ": ";
    }

    @Override
    public CharSequence getPersonName(CharSequence peerID) throws ASAPException {
        return this.personsStorage.getPersonName(peerID);
    }
}
