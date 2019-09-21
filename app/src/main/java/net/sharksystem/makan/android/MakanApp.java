package net.sharksystem.makan.android;

import android.Manifest;
import android.app.Activity;
import android.util.Log;

import net.sharksystem.android.util.PermissionCheck;
import net.sharksystem.asap.ASAPEngineFS;
import net.sharksystem.asap.ASAPException;
import net.sharksystem.asap.ASAPStorage;
import net.sharksystem.asap.android.apps.ASAPOnlineMessageSenderUserSide;
import net.sharksystem.makan.Makan;
import net.sharksystem.makan.MakanStorage;
import net.sharksystem.makan.MakanStorage_Impl;
import net.sharksystem.sharknet.android.SharkNetApp;

import java.io.IOException;
import java.util.HashMap;

public class MakanApp {
    public static final String MAKAN_FOLDER_NAME = "makan";

    private static final String LOGSTART = "MakanApp";
    private static MakanApp singleton = null;
    private static MakanStorage makanStorage;

    public static final String URI_START = "sn://makan/";
    private Activity currentActivity;

    public static MakanApp getMakanApp(Activity currentActivity) {
        if(MakanApp.singleton == null) {
            MakanApp.singleton = new MakanApp();
        }

        MakanApp.singleton.currentActivity = currentActivity;

        return MakanApp.singleton;
    }

    public static void askForPermissions(Activity activity) {
        // required permissions
        String[] permissions = new String[] {
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
        };

        // check for write permissions
        PermissionCheck.askForPermissions(activity, permissions);
    }


    public static MakanStorage getMakanStorage() throws IOException, ASAPException {
        MakanApp.makanStorage = null; // TODO: DEBUGGING
        if(MakanApp.makanStorage == null) {
            Log.d(LOGSTART, "makanStorage is null - create on");

            MakanApp.makanStorage = new MakanStorage_Impl(MakanApp.getASAPMakanStorage());
        }

        Log.d(LOGSTART, "return makan storage");
        return MakanApp.makanStorage;
    }

    /////////////////////////////////////////////////////////////////////////////////////////
    //                            ASAP Wrapper / Utils / Decorators                        //
    /////////////////////////////////////////////////////////////////////////////////////////

    private static ASAPStorage getASAPMakanStorage() throws IOException, ASAPException {
        Log.d(LOGSTART, "try get ASAP storage");
        if(MakanApp.singleton == null || MakanApp.singleton.currentActivity == null) {
            Log.d(LOGSTART, "MakanApp was initialized - either singled or currentActivity null");
            throw new ASAPException("MakanApp was initialized - either singled or currentActivity null");
        }

        SharkNetApp sharkNetApp = SharkNetApp.getSharkNetApp();
        // always create a new one - to keep track of changes in file system

        String ownerName = sharkNetApp.getASAPOwner().toString();
        String asapMakanRootFolderName =
                sharkNetApp.getApplicationRootFolder(MakanApp.MAKAN_FOLDER_NAME);

        Log.d(LOGSTART, "create makan with owner: " + ownerName + " | folder: " + asapMakanRootFolderName);
        Log.d(LOGSTART, "always create new ASAP storage - maybe we make it more permanent later");

        ASAPStorage asapStorage =
                ASAPEngineFS.getASAPStorage(ownerName, asapMakanRootFolderName, Makan.MAKAN_FORMAT);

        // attach online sender
        ASAPOnlineMessageSenderUserSide asapOnlineMessageSenderUserSide =
                new ASAPOnlineMessageSenderUserSide(sharkNetApp);

        asapOnlineMessageSenderUserSide.attachToSource(asapStorage);

        return asapStorage;
    }

    private HashMap<String, MakanViewActivity> makanLister = new HashMap<>();

    public void handleASAPBroadcast(String uri, int era, String user, String folder) {
        MakanViewActivity makanChangeListener = this.makanLister.get(uri);
        if(makanChangeListener != null) {
            Log.d(LOGSTART, "notify makan about external changes");
            Log.d(LOGSTART, "uri: " + uri);
            makanChangeListener.doExternalChange(era, user, folder);
        }
    }
}
