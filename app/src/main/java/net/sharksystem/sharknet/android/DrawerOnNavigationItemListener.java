package net.sharksystem.sharknet.android;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.MenuItem;

import net.sharksystem.R;
import net.sharksystem.identity.android.IdentityActivity;
import net.sharksystem.makan.android.MakanListActivity;
import net.sharksystem.persons.android.PersonListSelectionActivity;
import net.sharksystem.persons.android.PersonListViewActivity;
import net.sharksystem.radar.android.RadarActivity;
import net.sharksystem.sharknet.android.settings.SettingsActivity;

public class DrawerOnNavigationItemListener implements
        NavigationView.OnNavigationItemSelectedListener {

    private static final String LOGSTART = "SN2 Drawer";

    private final Activity activity;
    private final DrawerLayout mDrawerLayout;

    public DrawerOnNavigationItemListener(Activity activity, DrawerLayout mDrawerLayout) {
        this.activity = activity;
        this.mDrawerLayout = mDrawerLayout;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        try {

            Intent intent = null;
            switch (itemId) {
                case R.id.moduleMakan:
                    intent = new Intent(this.activity, MakanListActivity.class);
                    this.activity.startActivity(intent);
                    break;

                case R.id.moduleIdentity:
                    intent = new Intent(this.activity, IdentityActivity.class);
                    this.activity.startActivity(intent);
                    break;

                case R.id.modulePersons:
                    intent = new Intent(this.activity, PersonListViewActivity.class);
                    this.activity.startActivity(intent);
                    break;

                case R.id.moduleRadar:
                    intent = new Intent(this.activity, RadarActivity.class);
                    this.activity.startActivity(intent);
                    break;

                case R.id.moduleNetworkSettings:
                    intent = new Intent(this.activity, SettingsActivity.class);
                    this.activity.startActivity(intent);
                    break;

            }

            this.mDrawerLayout.closeDrawers();
        }
        catch(Throwable t) {
            Log.d(LOGSTART, "while handling navigation item selected");
            Log.d(LOGSTART, t.getLocalizedMessage());
        }

        return true;
    }
}
