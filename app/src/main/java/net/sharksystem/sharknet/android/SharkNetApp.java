package net.sharksystem.sharknet.android;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;

import net.sharksystem.R;
import net.sharksystem.SharkException;
import net.sharksystem.SharkPeer;
import net.sharksystem.SharkPeerFS;
import net.sharksystem.SharkStatusException;
import net.sharksystem.app.messenger.SharkMessengerComponent;
import net.sharksystem.asap.ASAP;
import net.sharksystem.asap.ASAPSecurityException;
import net.sharksystem.asap.android.Util;
import net.sharksystem.asap.android.apps.ASAPAndroidPeer;
import net.sharksystem.pki.HelperPKITests;
import net.sharksystem.pki.SharkPKIComponent;
import net.sharksystem.pki.SharkPKIComponentFactory;
import net.sharksystem.pki.android.SharkPKIReceivedCredentialMessageHandler;
import net.sharksystem.app.messenger.SharkMessengerComponentFactory;

import java.io.File;
import java.io.IOException;

public class SharkNetApp {
    private static final CharSequence APP_FOLDER_NAME = "SharkNet2_AppData";
    private static SharkNetApp singleton;
    private SharkPeer sharkPeer;
    private ASAPAndroidPeer asapAndroidPeer;
    private SharkPKIReceivedCredentialMessageHandler receivedCredentialListener;

    public static SharkNetApp getSharkNetApp() {
        if(SharkNetApp.singleton == null)
            throw new SharkStatusException("SharkNetApplication not yet initialized");

        return SharkNetApp.singleton;
    }

    public SharkPeer getSharkPeer() {
        if(this.sharkPeer == null)
            throw new SharkStatusException("Shark peer not yet initialized");

        return this.sharkPeer;
    }

    /////////////////////////////////////////////////////////////////////////////////////////////
    //                                     system setup                                        //
    /////////////////////////////////////////////////////////////////////////////////////////////

    public static void initializeSharkNetApp(Activity initialActivity, String ownerID)
            throws SharkException, IOException {
        ///////////////////////////////////// restore app data - if any
        if(SharkNetApp.singleton == null) {
            Log.d(getLogStart(), "going to initialize shark net application");
            SharkNetApp.singleton = new SharkNetApp(initialActivity, ownerID);

            // produce folder
            File rootDir =
                Util.getASAPRootDirectory(initialActivity, SharkNetApp.APP_FOLDER_NAME, ownerID);

            // produce application side shark peer
            SharkNetApp.singleton.sharkPeer = new SharkPeerFS(
                    SharkNetApp.singleton.getOwnerID(),
                    rootDir.getAbsolutePath()
            );

            /////////////////////////// setup PKI /////////////////////////////////////////////
            // create Android specific key store
            AndroidASAPKeyStore androidASAPKeyStore = null;
            try {
                androidASAPKeyStore = new AndroidASAPKeyStore(initialActivity, ownerID);
            } catch (ASAPSecurityException e) {
                Log.e(getLogStart(), "cannot create Android key store, fatal - give up: "
                        + e.getLocalizedMessage());
                throw new SharkException("cannot create Android key store", e);
            }

            // create a pki component factory with android key store
            SharkPKIComponentFactory pkiComponentFactory = new SharkPKIComponentFactory();

            // register this component with shark peer
            SharkNetApp.singleton.sharkPeer.addComponent(
                    pkiComponentFactory, SharkPKIComponent.class);

            SharkPKIComponent sharkPKI = (SharkPKIComponent)
                    SharkNetApp.singleton.sharkPeer.getComponent(SharkPKIComponent.class);

            ///////////////////////////////////// setup SharkMessenger
            // create messenger factory - needs a pki
            // get messenger factory with pki component as parameter.
            SharkMessengerComponentFactory messengerFactory =
                    new SharkMessengerComponentFactory(
                            (SharkPKIComponent) SharkNetApp.singleton.sharkPeer
                                    .getComponent(SharkPKIComponent.class));

            // register this component with shark peer
            SharkNetApp.singleton.sharkPeer.addComponent(
                    messengerFactory, SharkMessengerComponent.class);

            Log.d(getLogStart(), "shark net components added");
            ///////////////////////////////////// ignition

            // setup android (application side peer)
            ASAPAndroidPeer.initializePeer(
                    ownerID,
                    SharkNetApp.singleton.sharkPeer.getFormats(),
                    SharkNetApp.APP_FOLDER_NAME,
                    initialActivity);

            // TODO - need to inject keystore

            // launch service side
            ASAPAndroidPeer applicationSideASAPPeer = ASAPAndroidPeer.startPeer(initialActivity);
            Log.d(getLogStart(), "ASAP had a liftoff");

            // remember
            SharkNetApp.singleton.setApplicationSideASAPAndroidPeer(applicationSideASAPPeer);

            // use asap peer proxy for this app side shark peer
            SharkNetApp.singleton.sharkPeer.start(applicationSideASAPPeer);
            Log.d(getLogStart(), "shark net application launched");

            ///////////////////////////////////// testing: example data
            //Log.d(getLogStart(), "fill pki with example data");
            //HelperPKITests.fillWithExampleData((SharkPKIComponent) sharkPKI);
        } else {
            Log.d(getLogStart(), "shark net application already initialized - ignore");
        }
    }

    private void setApplicationSideASAPAndroidPeer(ASAPAndroidPeer asapAndroidPeer) {
        this.asapAndroidPeer = asapAndroidPeer;
    }

    public ASAPAndroidPeer getASAPAndroidPeer() {
//            return this.sharkPeer.getASAPPeer();
        return this.asapAndroidPeer;
    }

    // debug / tests
    SharkPKIReceivedCredentialMessageHandler getReceivedCredentialListener() {
        return this.receivedCredentialListener;
    }


    /////////////////////////////////////////////////////////////////////////////////////////////
    //                                    component getter                                     //
    /////////////////////////////////////////////////////////////////////////////////////////////

    public SharkPKIComponent getSharkPKI() {
        try {
            return (SharkPKIComponent) this.sharkPeer.getComponent(SharkPKIComponent.class);
        } catch (SharkException e) {
            // this cannot happen - this component is initialized in init
            throw new SharkStatusException("internal error: " + e.getLocalizedMessage());
        }
    }

    public SharkMessengerComponent getSharkMessenger() {
        try {
            return (SharkMessengerComponent)
                    this.sharkPeer.getComponent(SharkMessengerComponent.class);
        } catch (SharkException e) {
            // this cannot happen - this component is initialized in init
            throw new SharkStatusException("internal error: " + e.getLocalizedMessage());
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////
    //                                      GUI support                                        //
    /////////////////////////////////////////////////////////////////////////////////////////////

    public void setupDrawerLayout(Activity activity) {
        DrawerLayout mDrawerLayout = activity.findViewById(R.id.sharknet_drawer_layout);
        if(mDrawerLayout == null) {
            Log.d(getLogStart(), "cannot find drawer layout: " + R.id.sharknet_drawer_layout);
            return;
        }

        // add listener to drawer items
        NavigationView navigationView = activity.findViewById(R.id.sharknet_drawer_navigation_view);
        navigationView.setNavigationItemSelectedListener(
                new DrawerOnNavigationItemListener(activity, mDrawerLayout));
    }

    /////////////////////////////////////////////////////////////////////////////////////////////
    //                    application setting beside ASAP/Shark                                //
    /////////////////////////////////////////////////////////////////////////////////////////////
    public final static String PREFERENCES_FILE = "SharkNet2Identity";
    private final static String OWNER_NAME = "SharkNet2Identity_OwnerName";
    private final static String OWNER_ID = "SharkNet2Identity_OwnerID";

    public final static String DEFAULT_OWNER_NAME = "SNUser";
    private final static String DEFAULT_OWNER_ID = "Default_SN_USER_ID";

    private CharSequence ownerName;
    private CharSequence ownerID;

    public CharSequence getOwnerID() {
        return this.ownerID;
    }

    public CharSequence getOwnerName() {
        return this.ownerName;
    }

    private SharkNetApp(Context initialContext, String ownerID) {
        SharedPreferences sharedPref = initialContext.getSharedPreferences(
                PREFERENCES_FILE, Context.MODE_PRIVATE);

        if(sharedPref.contains(OWNER_NAME)) {
            this.ownerName = sharedPref.getString(OWNER_NAME, DEFAULT_OWNER_NAME);
        } else {
            this.ownerName = DEFAULT_OWNER_NAME;
        }

        this.ownerID = ownerID;
    }

    /**
     * Return ownerID. The id is created automatically when the system is initialized
     * @param context app context
     * @return String - ownerID
     * @throws SharkException if ownerID has not been created yet.
     * @see #initializeSystem(Context, CharSequence)
     */
    public static String getOwnerID(Context context) throws SharkException {
        SharedPreferences sharedPref = context.getSharedPreferences(
                PREFERENCES_FILE, Context.MODE_PRIVATE);

        if(sharedPref.contains(OWNER_ID)) {
            return sharedPref.getString(OWNER_ID, DEFAULT_OWNER_ID);
        } else throw new SharkException("not yet personalized - no id set");
    }

    /**
     * Static method to initialize system with a name.
     * An ID is automatically created and will not be changed.
     * @param ctx App context
     * @param ownerName The name the User defined for themselves.
     * @throws SharkException if owner name is null, empty or default
     * @see #DEFAULT_OWNER_NAME
     */
    public static void initializeSystem(Context ctx, CharSequence ownerName)
            throws SharkException {
        checkOwnerName(ownerName);

        SharedPreferences sharedPref = ctx.getSharedPreferences(
                PREFERENCES_FILE, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString(OWNER_NAME, ownerName.toString());

        // create owner id
//        if (!sharedPref.contains(OWNER_ID)) {
            String ownerID = ASAP.createUniqueID(); // TODO is changed on every startup - correct?
            editor.putString(OWNER_ID, ownerID);
//        }
        editor.apply();
    }

    /**
     * Checks owner name for valid syntax and throws exception on wrong syntax,
     * does nothing if syntax is correct
     * @param ownerName name to be checked
     * @throws SharkException if owner name is null empty or default
     * @see #DEFAULT_OWNER_NAME
     */
    private static void checkOwnerName(CharSequence ownerName) throws SharkException {
        if (ownerName == null || ownerName.equals(""))
            throw new SharkException("Owner name is null or empty");

        if (ownerName.equals(DEFAULT_OWNER_NAME))
            throw new SharkException("Owner name can't be default name: " + DEFAULT_OWNER_NAME);
    }

    /**
     * Change owner name (but not id)
     * @param ctx app context
     * @param ownerName new owner name
     * @throws SharkException if the new owner name is null, empty or equals the default owner name
     * @see #DEFAULT_OWNER_NAME
     */
    public void changeOwnerName(Context ctx, String ownerName) throws SharkException {
        checkOwnerName(ownerName);

        SharedPreferences sharedPref = ctx.getSharedPreferences(
                PREFERENCES_FILE, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(OWNER_NAME, ownerName);
        editor.apply();
        this.ownerName = ownerName;
    }

    /////////////////////////////////////////////////////////////////////////////////////////////
    //                                          utils                                          //
    /////////////////////////////////////////////////////////////////////////////////////////////
    private static String getLogStart() {
        return SharkNetApp.class.getSimpleName();
    }
}