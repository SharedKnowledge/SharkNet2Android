package net.sharksystem.sharknet.android;

import android.app.Activity;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;

import net.sharksystem.R;
import net.sharksystem.asap.android.apps.ASAPApplication;
import net.sharksystem.asap.android.apps.ASAPComponentNotYetInitializedException;
import net.sharksystem.asap.sharknet.android.SNChannelsComponent;
import net.sharksystem.crypto.ASAPCertificateStorage;
import net.sharksystem.makan.android.MakanApp;
import net.sharksystem.persons.android.PersonsStorageAndroidComponent;

import java.util.ArrayList;
import java.util.List;

public class SharkNetApp extends ASAPApplication implements OwnerFactory {
    private static SharkNetApp singleton;
    private Owner ownerStorage = null;

    /**
     * set up service site
     * @param appFormats
     */
    private SharkNetApp(List<CharSequence> appFormats, Activity initActivity) {
        // start asap app
        super(appFormats, initActivity);

        // initialize owner storage
        this.ownerStorage = new OwnerStorage(initActivity);
    }

    /**
     * Initialize app site
     */
    public void startASAPApplication() {
        /* just to demonstrate that this method exists. There is nothing to do,though:
            format was already set in constructor
            getOwnerID is available ownerID available - call super
        */

        // do not forget to call super method
        super.startASAPApplication();
    }

    public static boolean isStarted() { return SharkNetApp.singleton != null; }

    static SharkNetApp initializeSharkNetApp(Activity initActivity) {
        if(SharkNetApp.singleton == null) {
            Log.d(getLogStart(), "initialize / startup");
            // SN supports the following applications
            List<CharSequence> appFormats = new ArrayList<>();

            ///////////////// collect required component formats //////////////////////////////
            for(CharSequence f : PersonsStorageAndroidComponent.geRequiredFormats()) {
                appFormats.add(f);
            }

            ///////////////// old fashioned - they do not follow component guidelines  ////////
            //appFormats.add(PersonsStorageAndroid.CREDENTIAL_APP_NAME);
            appFormats.add(MakanApp.APP_NAME);
            appFormats.add(ASAPCertificateStorage.CERTIFICATE_APP_NAME);

            ///////////////// initialize application object  ///////////////////////////////////
            SharkNetApp.singleton = new SharkNetApp(appFormats, initActivity);

            // read owner information from preference or somewhere else
            Owner owner = SharkNetApp.singleton.getOwner(initActivity);

            ///////////////// initialize application components //////////////////////////////
            // key storage
            AndroidASAPKeyStorage asapKeyStorage =
                    AndroidASAPKeyStorage.initializeASAPKeyStorage(
                            initActivity,
                            owner.getUUID(),
                            owner.getDisplayName());

            // persons / contacts
            PersonsStorageAndroidComponent.initialize(
                    SharkNetApp.singleton, // ASAPApplication
                    SharkNetApp.singleton, // OwnerFactory
                    asapKeyStorage
            );

            // snChannels
            SNChannelsComponent.initialize(
                    SharkNetApp.singleton, // ASAPApplication
                    asapKeyStorage, // BasicKeyStore
                    SharkNetApp.singleton, // OwnerFactory
                    PersonsStorageAndroidComponent.getPersonsStorage() // PersonStorage
            );

            // all components put together - launch the system
            SharkNetApp.singleton.startASAPApplication();
        }

        return SharkNetApp.getSharkNetApp();
    }

    public static SharkNetApp getSharkNetApp() {
        if(SharkNetApp.singleton == null) throw
                new ASAPComponentNotYetInitializedException("failed to initialize SharkNetApplication");

        return SharkNetApp.singleton;
    }

    public void setupDrawerLayout(Activity activity) {
        DrawerLayout mDrawerLayout = activity.findViewById(R.id.sharknet_drawer_layout);
        if(mDrawerLayout == null) {
            Log.d(this.getLogStart(), "cannot find drawer layout: " + R.id.sharknet_drawer_layout);
            return;
        }

        // add listener to drawer items
        NavigationView navigationView = activity.findViewById(R.id.sharknet_drawer_navigation_view);

        navigationView.setNavigationItemSelectedListener(
                new DrawerOnNavigationItemListener(activity, mDrawerLayout));
    }

    /////////////////////////////////////////////////////////////////////////////////////////////
    //                                     owner settings                                      //
    /////////////////////////////////////////////////////////////////////////////////////////////

    private static String getLogStart() {
        return SharkNetApp.class.getSimpleName();
    }

    public Owner getOwner(Activity activity) {
        if(activity == null) return this.ownerStorage;

        // after initialization - create preferences access with active activity
        this.ownerStorage = new OwnerStorage(activity);
        return this.ownerStorage;
    }

    @Override
    public Owner getOwner() {
        return this.getOwner(this.getActivity());
    }

    public CharSequence getOwnerID() { return this.getOwner().getUUID(); }
    public CharSequence getOwnerName() {
        return this.getOwner().getDisplayName();
    }
}
