package net.sharksystem.sharknet.android;

import android.app.Activity;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;

import net.sharksystem.R;
import net.sharksystem.SharkException;
import net.sharksystem.asap.android.Util;
import net.sharksystem.asap.android.apps.ASAPApplication;
import net.sharksystem.crypto.ASAPCertificateStorage;
import net.sharksystem.makan.android.MakanApp;
import net.sharksystem.persons.android.PersonsStorageAndroid;
import net.sharksystem.persons.android.OwnerStorageAndroid;

import java.io.File;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.util.ArrayList;
import java.util.List;

public class SharkNetApp extends ASAPApplication {
    private static final String KEYSTORE_FILE_NAME = "sn2_keystoreFile";
    private static SharkNetApp singleton;

    private SharkNetApp(List<CharSequence> appFormats) {
        super(appFormats);
    }

    public CharSequence getASAPOwner(Activity activity) {
        try {
            return OwnerStorageAndroid.getIdentityStorage(activity).getDisplayName();
        } catch (UnrecoverableKeyException | NoSuchAlgorithmException | KeyStoreException e) {
            Log.e(this.getLogStart(), "serious problem: " + e.getLocalizedMessage());
        }

        return null;
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

    public File getKeyStoreFile(boolean mustExist) throws SharkException {
        CharSequence asapRootFolder = this.getASAPRootFolder();

        String keyStoreFileName = asapRootFolder + "/" + KEYSTORE_FILE_NAME;
        File keyStoreFile = new File(keyStoreFileName);
        if(mustExist && !keyStoreFile.exists()) {
            throw new SharkException("keystore file does not exist");
        }

        return keyStoreFile;
    }

    /*
    public void sendASAPMessage(Context ctx, CharSequence uri, CharSequence aaspMessage) {
        Message msg = Message.obtain(null, ASAPServiceMethods.ADD_MESSAGE, 0, 0);
        super.sendMessage2Service(msg);
    }
     */

    public CharSequence getOwnerID() {
        try {
            return OwnerStorageAndroid.getIdentityStorage(this.getActivity()).getUUID();
        } catch (UnrecoverableKeyException | NoSuchAlgorithmException | KeyStoreException e) {
            Log.e(this.getLogStart(), "serious problem: " + e.getLocalizedMessage());
        }

        return null;
    }

    public boolean isOwnerSet() {
        try {
            return OwnerStorageAndroid.getIdentityStorage(this.getActivity()).isOwnerSet();
        } catch (UnrecoverableKeyException | NoSuchAlgorithmException | KeyStoreException e) {
            Log.e(this.getLogStart(), "serious problem: " + e.getLocalizedMessage());
        }

        return false;
    }

    private String getLogStart() {
        return net.sharksystem.asap.util.Log.startLog(this).toString();
    }
}
