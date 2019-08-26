package net.sharksystem.sharknet.android;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import net.sharksystem.R;
import net.sharksystem.asap.ASAPEngineFS;
import net.sharksystem.asap.android.ASAPApplicationHelper;
import net.sharksystem.asap.android.ASAPServiceMethods;
import net.sharksystem.identity.android.IdentityStorageAndroid;

import java.io.File;

import identity.IdentityStorage;

public class SharkNetApp extends ASAPApplicationHelper {

    private static SharkNetApp singleton;
    private static final String LOGSTART = "SN2App";

    private SharkNetApp(Activity activity) {
        super(activity, null, SharkNetApp.getOwner());
    }

    public static CharSequence getOwner() {
        // TODO - find user name somewhere in shared preferences
        return "DummyOwner";
    }

    public CharSequence getOwnerName() {
        return SharkNetApp.getOwner();
    }

    public IdentityStorage getIdentityStorage() {
        return IdentityStorageAndroid.getIdentityStorage(this.getActivity());
    }

    public static SharkNetApp getSharkNetApp(Activity activity) {
        if(SharkNetApp.singleton == null) {
            SharkNetApp.singleton = new SharkNetApp(activity);
        }

        SharkNetApp.singleton.setActivity(activity);

        return SharkNetApp.singleton;
    }

    public void setupDrawerLayout(Activity activity) {
        DrawerLayout mDrawerLayout = activity.findViewById(R.id.sharknet_drawer_layout);

        // add listener to drawer items
        NavigationView navigationView = activity.findViewById(R.id.sharknet_drawer_navigation_view);

        navigationView.setNavigationItemSelectedListener(
                new DrawerOnNavigationItemListener(activity, mDrawerLayout));
    }

    public File getASAPRootDirectory() {
        return (Environment.getExternalStoragePublicDirectory(
                ASAPEngineFS.DEFAULT_ROOT_FOLDER_NAME));
    }

    public String getASAPAppRootFolderName(String appFolderName) {
        return this.getASAPRootDirectory().getAbsolutePath() + "/" + appFolderName;
    }

    public void sendASAPMessage(Context ctx, CharSequence uri, CharSequence aaspMessage) {
        Message msg = Message.obtain(null, ASAPServiceMethods.ADD_MESSAGE, 0, 0);
        super.sendMessage2Service(msg);
    }

    public CharSequence getOwnerID() {
        return IdentityStorageAndroid.getIdentityStorage(this.getActivity()).getOwnerID();
    }
}
