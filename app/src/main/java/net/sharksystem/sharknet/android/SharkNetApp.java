package net.sharksystem.sharknet.android;

import android.app.Activity;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import net.sharksystem.R;
import net.sharksystem.asap.android.apps.ASAPApplication;
import net.sharksystem.identity.android.IdentityStorageAndroid;

public class SharkNetApp extends ASAPApplication {

    private static SharkNetApp singleton;

    private SharkNetApp() {
        super(); // not needed - but to make clear - we use default constructor
    }

    public CharSequence getASAPOwner(Activity activity) {
        return IdentityStorageAndroid.getIdentityStorage(activity).getOwnerName();
    }

    public static SharkNetApp getSharkNetApp() {
        if(SharkNetApp.singleton == null) {
            SharkNetApp.singleton = new SharkNetApp();
        }

        return SharkNetApp.singleton;
    }

    public String getApplicationRootFolder(String appName) {
        return this.getASAPRootFolder() + "/" + appName;
    }

    public void setupDrawerLayout(Activity activity) {
        DrawerLayout mDrawerLayout = activity.findViewById(R.id.sharknet_drawer_layout);

        // add listener to drawer items
        NavigationView navigationView = activity.findViewById(R.id.sharknet_drawer_navigation_view);

        navigationView.setNavigationItemSelectedListener(
                new DrawerOnNavigationItemListener(activity, mDrawerLayout));
    }

    /*
    public void sendASAPMessage(Context ctx, CharSequence uri, CharSequence aaspMessage) {
        Message msg = Message.obtain(null, ASAPServiceMethods.ADD_MESSAGE, 0, 0);
        super.sendMessage2Service(msg);
    }
     */

    public CharSequence getOwnerID() {
        return IdentityStorageAndroid.getIdentityStorage(this.getActivity()).getOwnerID();
    }
}
