package net.sharksystem.sharknet.android;

import android.app.Activity;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import net.sharksystem.R;
import net.sharksystem.asap.android.apps.ASAPApplication;
import net.sharksystem.makan.android.MakanApp;
import net.sharksystem.persons.android.PersonsAppAndroid;
import net.sharksystem.persons.android.OwnerStorageAndroid;

import java.util.ArrayList;
import java.util.List;

public class SharkNetApp extends ASAPApplication {

    private static SharkNetApp singleton;

    private SharkNetApp(List<CharSequence> appFormats) {
        super(appFormats);
    }

    public CharSequence getASAPOwner(Activity activity) {
        return OwnerStorageAndroid.getIdentityStorage(activity).getDisplayName();
    }

    public static SharkNetApp getSharkNetApp() {
        if(SharkNetApp.singleton == null) {
            // SN supports the following applications
            List<CharSequence> appFormats = new ArrayList<>();
            appFormats.add(PersonsAppAndroid.APP_NAME);
            appFormats.add(MakanApp.APP_NAME);

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

    /*
    public void sendASAPMessage(Context ctx, CharSequence uri, CharSequence aaspMessage) {
        Message msg = Message.obtain(null, ASAPServiceMethods.ADD_MESSAGE, 0, 0);
        super.sendMessage2Service(msg);
    }
     */

    public CharSequence getOwnerID() {
        return OwnerStorageAndroid.getIdentityStorage(this.getActivity()).getUUID();
    }

    public boolean isOwnerSet() {
        return OwnerStorageAndroid.getIdentityStorage(this.getActivity()).isOwnerSet();
    }
}
