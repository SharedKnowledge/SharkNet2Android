package net.sharksystem.makan.android;

import android.Manifest;
import android.app.Activity;
import android.util.Log;

import net.sharksystem.SharkException;
import net.sharksystem.asap.ASAPException;
import net.sharksystem.asap.android.apps.ASAPApplicationComponentHelper;
import net.sharksystem.makan.MakanStorage;
import net.sharksystem.makan.MakanStorage_Impl;
import net.sharksystem.sharknet.android.SharkNetApp;
import net.sharksystem.android.util.PermissionCheck;

import java.io.IOException;

public class MakanApp {
    public static final CharSequence APP_NAME = "Makan";

    private static final String LOGSTART = "MakanApp";
    private static MakanApp singleton = null;
    private MakanStorage makanStorage;

    public static MakanApp getMakanApp() {
        if(MakanApp.singleton == null) {
            MakanApp.singleton = new MakanApp();
        }

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


    public MakanStorage getMakanStorage() throws IOException, ASAPException, SharkException {
        Log.d(LOGSTART, "ALWAYS SET MAKAN STORAGE TO NULL FOR DEBUGGING");
        this.makanStorage = null; // TODO: DEBUGGING
        if(this.makanStorage == null) {
            Log.d(LOGSTART, "makanStorage is null - create on");

            this.makanStorage = new MakanStorage_Impl(
                SharkNetApp.getSharkNetApp().getSharkPeer().
                        getASAPPeer().getASAPStorage(MakanApp.APP_NAME));

            //MakanApp.makanStorage = new MakanStorage_Impl(MakanApp.getASAPMakanStorage());
        } else {
            Log.d(LOGSTART, "makan storage already created");
        }

        Log.d(LOGSTART, "return makan storage");
        return this.makanStorage;
    }

    /////////////////////////////////////////////////////////////////////////////////////////
    //                            ASAP Wrapper / Utils / Decorators                        //
    /////////////////////////////////////////////////////////////////////////////////////////
/*
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
        ASAPOnlineMessageSenderAndroidUserSide asapOnlineMessageSenderUserSide =
                new ASAPOnlineMessageSenderAndroidUserSide(sharkNetApp);

        asapOnlineMessageSenderUserSide.attachToSource(asapStorage);

        return asapStorage;
    }
*/
}
