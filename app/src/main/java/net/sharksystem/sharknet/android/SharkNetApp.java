package net.sharksystem.sharknet.android;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;

import net.sharksystem.R;
import net.sharksystem.aasp.android.AASP;
import net.sharksystem.aasp.android.AASPService;
import net.sharksystem.aasp.android.AASPServiceMethods;
import net.sharksystem.android.util.PermissionCheck;
import net.sharksystem.bubble.BubbleApp;
import net.sharksystem.bubble.android.BubbleAppAndroid;

public class SharkNetApp {

    private static SharkNetApp singleton;

    private static final String LOGSTART = "SNApp";
    private final Activity activity;

    private AASPBroadcastReceiver aaspBroadcastReceiver;

    /////////////// service management
    private ServiceConnection mConnection;
    /** Messenger for communicating with the service. */
    Messenger mService = null;
    /** Flag indicating whether we have called bind on the service. */
    boolean mBound = false;

    private SharkNetApp(Activity activity) {
        this.activity = activity;

        // required permissions
        String[] permissions = new String[] {
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.CHANGE_WIFI_STATE,
                Manifest.permission.CHANGE_NETWORK_STATE,
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_ADMIN,
                Manifest.permission.ACCESS_COARSE_LOCATION
        };

        // check for write permissions
        PermissionCheck.askForPermissions(activity, permissions);

        // create broadcast receiver dealing with AASP service
        this.aaspBroadcastReceiver = new AASPBroadcastReceiver();
    }

    public static SharkNetApp getSharkNetApp(Activity activity) {
        if(SharkNetApp.singleton == null) {
            SharkNetApp.singleton = new SharkNetApp(activity);

        }

        return SharkNetApp.singleton;
    }

    public static BubbleApp getBubbleApp() {
        return BubbleAppAndroid.getBubbleApp();
    }

    public void setupDrawerLayout(Activity activity) {
        DrawerLayout mDrawerLayout = activity.findViewById(R.id.sharknet_drawer_layout);

        // add listener to drawer items
        NavigationView navigationView = activity.findViewById(R.id.sharknet_drawer_navigation_view);

        navigationView.setNavigationItemSelectedListener(
                new DrawerOnNavigationItemListener(activity, mDrawerLayout));
    }

    boolean aaspStarted = false;

    /**
     * Bind (and start) AASP service and start listening to AASP broadcasts
     * @param ctx
     */
    public void startAASP(Context ctx) {
        Log.d(LOGSTART, "start aasp");

        if(this.aaspStarted) {
            Log.d(LOGSTART, "aasp already started");
        }

        /*
        // start service - which allows service to outlive unbind
        Intent aaspServiceCreationIntent = new Intent(ctx, AASPService.class);
        Log.d(LOGSTART, "TODO: use user alice for aasp service creation");
        aaspServiceCreationIntent.putExtra(AASP.USER, "alice");

        Log.d(LOGSTART, "start aasp android service");
        ctx.startService(aaspServiceCreationIntent);
        */

        // first: bind to (and maybe create) aasp service
        Log.d(LOGSTART, "call bind");
        this.mConnection = new SNServiceConnection();
        ctx.bindService(new Intent(ctx, AASPService.class),
                mConnection,
                Context.BIND_AUTO_CREATE);

        // create broadcast receiver
        Log.d(LOGSTART, "register broadcast receiver");
        IntentFilter filter = new IntentFilter();
        filter.addAction(AASP.BROADCAST_ACTION);
        ctx.registerReceiver(this.aaspBroadcastReceiver, filter);

        this.aaspStarted = true;
    }

    /**
     * Unbind (and stop) AASP service and stop listening to AASP broadcasts
     * @param ctx
     */
    public void stopAASP(Context ctx) {
        Log.d(LOGSTART, "stop aasp");

        if(this.aaspStarted) {
            Log.d(LOGSTART, "aasp already stopped");
        }

        // unregister broadcast receiver
        Log.d(LOGSTART, "unregister broadcast receiver");
        ctx.unregisterReceiver(this.aaspBroadcastReceiver);

        // TODO: shouldn't we let the service decide what protocols to shutdown?
        Log.d(LOGSTART, "ask aasp service to stop wifi direct");
        Log.d(LOGSTART, "shouldn't we let the service decide what protocols to shutdown?");
        this.sendMessage2Service(AASPServiceMethods.STOP_WIFI_DIRECT);

        // stop service
        Log.d(LOGSTART, "call unbind");
        if (mBound) {
            ctx.unbindService(mConnection);
            mBound = false;
        }

        this.aaspStarted = false;
    }

    private void sendMessage2Service(int messageNumber) {
        if(this.mService == null) {
            Log.d(LOGSTART, "sendMessage called but mService null");
            return;
        }

        Message msg = Message.obtain(null, messageNumber, 0, 0);
        try {
            mService.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////
    //                               android service management                            //
    /////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Class for interacting with the main interface of the service.
     */
    private class SNServiceConnection implements ServiceConnection {

        public void onServiceConnected(ComponentName className, IBinder service) {
            // This is called when the connection with the service has been
            // established, giving us the object we can use to
            // interact with the service.  We are communicating with the
            // service using a Messenger, so here we get a client-side
            // representation of that from the raw IBinder object.
            Log.d(LOGSTART, "connected to aasp service");
            mService = new Messenger(service);
            mBound = true;

            Log.d(LOGSTART, "start wifidirect");
            sendMessage2Service(AASPServiceMethods.START_WIFI_DIRECT);
        }

        public void onServiceDisconnected(ComponentName className) {
            // This is called when the connection with the service has been
            // unexpectedly disconnected -- that is, its process crashed.
            Log.d(LOGSTART, "disconnected to aasp service");
            mService = null;
            mBound = false;
        }
    };
}
