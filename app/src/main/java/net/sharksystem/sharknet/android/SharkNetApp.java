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
    private AASPBroadcastReceiver aaspBroadcastReceiver;

    private static final String LOGSTART = "SNApp";

    private Activity currentActivity;


    /////////////// service management
    private ServiceConnection mConnection;
    /** Messenger for communicating with the service. */
    Messenger mService = null;

    private boolean aaspBroadcastReceiverRegistered = false;

    private SharkNetApp(Activity activity) {
        this.currentActivity = activity;

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
        PermissionCheck.askForPermissions(this.currentActivity, permissions);

        // create broadcast receiver dealing with AASP service
        this.aaspBroadcastReceiver = new AASPBroadcastReceiver();
    }

    private void setCurrentActivity(Activity activity) {
        this.currentActivity = activity;
    }

    public Activity getCurrentActivity() {
        return this.currentActivity;
    }

    public static SharkNetApp getSharkNetApp(Activity activity) {
        if(SharkNetApp.singleton == null) {
            SharkNetApp.singleton = new SharkNetApp(activity);
        }

        SharkNetApp.singleton.setCurrentActivity(activity);

        return SharkNetApp.singleton;
    }

    public static BubbleApp getBubbleApp() {
        return BubbleAppAndroid.getBubbleApp(SharkNetApp.singleton.currentActivity);
    }

    public void setupDrawerLayout(Activity activity) {
        DrawerLayout mDrawerLayout = activity.findViewById(R.id.sharknet_drawer_layout);

        // add listener to drawer items
        NavigationView navigationView = activity.findViewById(R.id.sharknet_drawer_navigation_view);

        navigationView.setNavigationItemSelectedListener(
                new DrawerOnNavigationItemListener(activity, mDrawerLayout));
    }

    boolean aaspStarted = false;

    ////////////////////////////////////////////////////////////////////////////////////////
    //                                   AASP management                                  //
    ////////////////////////////////////////////////////////////////////////////////////////

    public boolean isAASPOn() {
        return this.aaspStarted;
    }

    /**
     * Bind (and start) AASP service and start listening to AASP broadcasts
     */
    public void startAASP() {
        Log.d(LOGSTART, "start aasp");

        if(this.aaspStarted) {
            Log.d(LOGSTART, "aasp already started");
            return;
        }

        // start service - which allows service to outlive unbind
        Intent aaspServiceCreationIntent = new Intent(this.currentActivity, AASPService.class);
        Log.d(LOGSTART, "TODO: use user alice for aasp service creation");
        aaspServiceCreationIntent.putExtra(AASP.USER, "alice");

        Log.d(LOGSTART, "start aasp android service");
        this.currentActivity.startService(aaspServiceCreationIntent);

        this.startWifiDirect();

        this.aaspStarted = true;
    }

    /**
     * Unbind (and stop) AASP service and stop listening to AASP broadcasts
     */
    public void stopAASP() {
        Log.d(LOGSTART, "going to stop aasp service");

        if(!this.aaspStarted) {
            Log.d(LOGSTART, "aasp not running");
            return;
        }

        // TODO: shouldn't we let the service decide what protocols to shutdown?
        Log.d(LOGSTART, "ask aasp service to stop wifi direct");
        Log.d(LOGSTART, "shouldn't we let the service decide what protocols to shutdown?");

        Log.d(LOGSTART, "stop wifi");
        this.stopWifiDirect();

        // stop service
        this.unbindServices();

        Intent aaspServiceIntent = new Intent(this.currentActivity, AASPService.class);
        Log.d(LOGSTART, "stop aasp service");
        this.currentActivity.stopService(aaspServiceIntent);

        this.aaspStarted = false;
    }

    public void startWifiDirect() {
        // are we already bound to the service
        Log.d(LOGSTART, "startWifi called");
        this.sendMessage2Service(this.currentActivity, AASPServiceMethods.START_WIFI_DIRECT);
    }

    public void stopWifiDirect() {
        // are we already bound to the service
        Log.d(LOGSTART, "stopWifi called");
        this.sendMessage2Service(this.currentActivity, AASPServiceMethods.STOP_WIFI_DIRECT);
    }

    public void startAASPBroadcastReceiver() {
        // create broadcast receiver
        if(aaspBroadcastReceiverRegistered) {
            Log.d(LOGSTART, "broadcast receiver already registered");
            return;
        }

        Log.d(LOGSTART, "register broadcast receiver");
        IntentFilter filter = new IntentFilter();
        filter.addAction(AASP.BROADCAST_ACTION);
        this.currentActivity.registerReceiver(this.aaspBroadcastReceiver, filter);

        aaspBroadcastReceiverRegistered = true;

        // make service broadcast
        this.sendMessage2Service(this.currentActivity, AASPServiceMethods.START_BROADCASTS);
        this.unbindServices();
    }

    public void stopAASPBroadcastReceiver() {
        // make service stop broadcasting
        this.sendMessage2Service(this.currentActivity, AASPServiceMethods.STOP_BROADCASTS);
        this.unbindServices();

        // unregister broadcast receiver
        if(!aaspBroadcastReceiverRegistered) {
            Log.d(LOGSTART, "broadcast receiver not registered");
            return;
        }
        Log.d(LOGSTART, "unregister broadcast receiver");
        this.currentActivity.unregisterReceiver(this.aaspBroadcastReceiver);

        aaspBroadcastReceiverRegistered = false;
    }

    private void sendMessage2Service(Context ctx, int messageNumber) {
        Log.d(LOGSTART, "send message 2 aasp service called");
        if(this.mService != null) {
            Log.d(LOGSTART, "already bound to aasp service - send message");
            Message msg = Message.obtain(null, messageNumber, 0, 0);
            try {
                mService.send(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            Log.d(LOGSTART, "not bound to aasp service, bind and call after binding");
            this.mConnection = new SNServiceConnection(messageNumber);
            ctx.bindService(new Intent(ctx, AASPService.class),
                    mConnection,
                    Context.BIND_AUTO_CREATE);
        }
    }

    public void unbindServices() {
        if(this.mService != null && this.mConnection != null) {
            Log.d(LOGSTART, "unbind services");
            this.currentActivity.unbindService(this.mConnection);
            this.mConnection = null;
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////
    //                               android service management                            //
    /////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Class for interacting with the main interface of the service.
     */
    private class SNServiceConnection implements ServiceConnection {

        private final int messageNumber;

        SNServiceConnection(int messageNumber) {
            this.messageNumber = messageNumber;
        }

        public void onServiceConnected(ComponentName className, IBinder service) {
            // This is called when the connection with the service has been
            // established, giving us the object we can use to
            // interact with the service.  We are communicating with the
            // service using a Messenger, so here we get a client-side
            // representation of that from the raw IBinder object.
            Log.d(LOGSTART, "connected to aasp service");
            mService = new Messenger(service);

            Log.d(LOGSTART, "initiate sending delayed message");
            Message msg = Message.obtain(null, messageNumber, 0, 0);
            try {
                mService.send(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        public void onServiceDisconnected(ComponentName className) {
            // This is called when the connection with the service has been
            // unexpectedly disconnected -- that is, its process crashed.
            Log.d(LOGSTART, "disconnected from aasp service");
            mService = null;
        }
    };
}
