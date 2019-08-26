package net.sharksystem.makan.android;

import android.Manifest;
import android.app.Activity;
import android.util.Log;

import net.sharksystem.android.util.PermissionCheck;
import net.sharksystem.asap.ASAPEngineFS;
import net.sharksystem.asap.ASAPException;
import net.sharksystem.asap.ASAPStorage;
import net.sharksystem.makan.MakanStorage;
import net.sharksystem.makan.MakanStorage_Impl;
import net.sharksystem.sharknet.android.SharkNetApp;

import java.io.IOException;
import java.util.HashMap;

public class MakanApp {
    public static final String MAKAN_FOLDER_NAME = "makan";
    public static final String FORMAT = "application/x-sn2-makan";

    private static final String LOGSTART = "MakanApp";
    private static MakanApp singleton = null;
    private MakanStorage makanStorage;

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


    public MakanStorage getMakanStorage() throws IOException, ASAPException {
        if(this.makanStorage == null) {

            this.makanStorage = new MakanStorage_Impl(this.getASAPMakanStorage());
        }

        return this.makanStorage;
    }

    /////////////////////////////////////////////////////////////////////////////////////////
    //                            ASAP Wrapper / Utils / Decorators                        //
    /////////////////////////////////////////////////////////////////////////////////////////

    private ASAPStorage asapMakanStorage = null;

    public ASAPStorage getASAPMakanStorage() throws IOException, ASAPException {
        SharkNetApp sharkNetApp = SharkNetApp.getSharkNetApp(currentActivity);
        if(asapMakanStorage == null) {
            this.asapMakanStorage = ASAPEngineFS.getASAPStorage(
                    sharkNetApp.getOwnerName().toString(),
                    sharkNetApp.getASAPAppRootFolderName(MakanApp.MAKAN_FOLDER_NAME),
                    MakanApp.FORMAT
            );
        }

        return asapMakanStorage;
    }

    public CharSequence getExampleMakanURI() {
        return "sn://makan/Example";
    }

    public CharSequence getExampleMakanName() {
        return "User Friendly Makan Name";
    }

    private HashMap<String, MakanViewActivity> makanLister = new HashMap<>();

    public void handleAASPBroadcast(String uri, int era, String user, String folder) {
        MakanViewActivity makanChangeListener = this.makanLister.get(uri);
        if(makanChangeListener != null) {
            Log.d(LOGSTART, "notify makan about external changes");
            Log.d(LOGSTART, "uri: " + uri);
            makanChangeListener.doExternalChange(era, user, folder);
        }
    }
}
