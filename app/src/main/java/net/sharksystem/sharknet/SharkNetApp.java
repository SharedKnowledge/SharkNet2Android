package net.sharksystem.sharknet;

import android.app.Activity;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import net.sharksystem.R;
import net.sharksystem.bubble.android.BubbleViewActivity;

public class SharkNetApp {

    private static SharkNetApp singleton;

    private SharkNetApp() {  }

    public static final SharkNetApp getSharkNetApp() {
        if(SharkNetApp.singleton == null) {
            SharkNetApp.singleton = new SharkNetApp();
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
}
