package net.sharksystem.sharknet.android;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;

import net.sharksystem.R;
import net.sharksystem.SharkException;
import net.sharksystem.android.util.DateTimeHelper;
import net.sharksystem.asap.android.apps.ASAPApplication;
import net.sharksystem.crypto.ASAPCertificateStorage;
import net.sharksystem.crypto.ASAPKeyStorage;
import net.sharksystem.crypto.SharkCryptoException;
import net.sharksystem.makan.android.MakanApp;
import net.sharksystem.persons.Owner;
import net.sharksystem.persons.android.AndroidASAPKeyStorage;
import net.sharksystem.persons.android.PersonsStorageAndroid;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SharkNetApp extends ASAPApplication {
    private static final String PERSONS_STORAGE_FILE_NAME = "sn2_personsStorageFile";
    private static SharkNetApp singleton;

    private SharkNetApp(List<CharSequence> appFormats) {
        super(appFormats);
    }

    public static SharkNetApp getSharkNetApp() {
        if(SharkNetApp.singleton == null) {
            // SN supports the following applications
            List<CharSequence> appFormats = new ArrayList<>();
            appFormats.add(PersonsStorageAndroid.APP_NAME);
            appFormats.add(MakanApp.APP_NAME);
            appFormats.add(ASAPCertificateStorage.APP_NAME);

            SharkNetApp.singleton = new SharkNetApp(appFormats);
        }

        return SharkNetApp.singleton;
    }

    public void setupDrawerLayout(Activity activity) {
        DrawerLayout mDrawerLayout = activity.findViewById(R.id.sharknet_drawer_layout);

        // add listener to drawer items
        NavigationView navigationView = activity.findViewById(R.id.sharknet_drawer_navigation_view);

        navigationView.setNavigationItemSelectedListener(
                new DrawerOnNavigationItemListener(activity, mDrawerLayout));
    }

    public File getPersonsStorageFile(boolean mustExist) throws SharkException {
        CharSequence asapRootFolder = this.getASAPRootFolder();

        String keyStoreFileName = asapRootFolder + "/" + PERSONS_STORAGE_FILE_NAME;
        File keyStoreFile = new File(keyStoreFileName);
        if(mustExist && !keyStoreFile.exists()) {
            throw new SharkException("persons storage file does not exist");
        }

        return keyStoreFile;
    }

    public boolean isStatusCanWork() {
        return this.getOwnerStorage().isOwnerSet();
    }

    /////////////////////////////////////////////////////////////////////////////////////////////
    //                                     owner settings                                      //
    /////////////////////////////////////////////////////////////////////////////////////////////

    public CharSequence getASAPOwnerID() {
        return this.getOwnerID();
    }

    public CharSequence getOwnerID() {
        return this.getOwnerStorage().getUUID();
    }

    private Owner ownerStorage = null;
    public Owner getOwnerStorage() {
        return this.getOwnerStorage(this.getActivity());
    }

    public Owner getOwnerStorage(Context context) {
        if(this.ownerStorage == null) {
            this.ownerStorage = new OwnerStorage(context);
        }

        return this.ownerStorage;
    }

    public final static String PREFERENCES_FILE = "SharkNet2Identity";
    private final static String OWNER_NAME = "SharkNet2Identity_OwnerName";
    private final static String OWNER_ID = "SharkNet2Identity_OwnerID";

    public final static String DEFAULT_OWNER_NAME = "SNUser";
    private final static String DEFAULT_OWNER_ID = "Default_SN_USER_ID";

    private class OwnerStorage implements Owner {
        private CharSequence ownerName;
        private CharSequence ownerID;

        private OwnerStorage(Context ctx) {
            SharedPreferences sharedPref = ctx.getSharedPreferences(
                    PREFERENCES_FILE, Context.MODE_PRIVATE);

            if(sharedPref.contains(OWNER_NAME)) {
                this.ownerName = sharedPref.getString(OWNER_NAME, DEFAULT_OWNER_NAME);
            } else {
                this.ownerName = DEFAULT_OWNER_NAME;
            }

            if(sharedPref.contains(OWNER_ID)) {
                this.ownerID = sharedPref.getString(OWNER_ID, DEFAULT_OWNER_ID);
            } else {
                this.ownerID = DEFAULT_OWNER_ID;
            }
        }

        @Override
        public boolean isOwnerSet() {
            return !this.ownerName.toString().equalsIgnoreCase(DEFAULT_OWNER_NAME);
        }

        public boolean isOwnerIDSet() {
            return !this.ownerID.toString().equalsIgnoreCase(DEFAULT_OWNER_ID);
        }

        @Override
        public void setDisplayName(CharSequence userName) {
            this.ownerName = userName;

            SharedPreferences sharedPref = SharkNetApp.this.getActivity().getSharedPreferences(
                    PREFERENCES_FILE, Context.MODE_PRIVATE);

            SharedPreferences.Editor editor = sharedPref.edit();

            editor.putString(OWNER_NAME, userName.toString());

            // create owner id
            if(this.ownerID.toString().equalsIgnoreCase(DEFAULT_OWNER_ID)) {
                // set id - once and only once.
                this.ownerID = UUID.randomUUID().toString();
                editor.putString(OWNER_ID, ownerID.toString());
            }

            editor.commit();
        }

        @Override
        public CharSequence getUUID() {
            return this.ownerID;
        }

        @Override
        public CharSequence getDisplayName() {
            return this.ownerName;
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////
    //                                     key storage                                         //
    /////////////////////////////////////////////////////////////////////////////////////////////

    private AndroidASAPKeyStorage androidASAPKeyStorage = null;

    public ASAPKeyStorage getASAPKeyStorage() {
        if(androidASAPKeyStorage == null) {
            // re-read from file system
            try {
                this.androidASAPKeyStorage = new AndroidASAPKeyStorage();
                File keyStoreFile = SharkNetApp.getSharkNetApp().getPersonsStorageFile(true);
                this.androidASAPKeyStorage.load(new FileInputStream(keyStoreFile));
            } catch (Exception e) {
                Log.d(this.getLogStart(), "probably key store file not found: "
                        + e.getLocalizedMessage());
            }
        }
        return this.androidASAPKeyStorage;
    }

    public void generateKeyPair() throws SharkException {
        this.androidASAPKeyStorage.generateKeyPair();
    }

    public boolean secureKeyAvailable() throws SharkCryptoException {
        return this.androidASAPKeyStorage.getCreationTime() != DateTimeHelper.TIME_NOT_SET;
    }

    private String getLogStart() {
        return net.sharksystem.asap.util.Log.startLog(this).toString();
    }
}
