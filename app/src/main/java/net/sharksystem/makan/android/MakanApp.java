package net.sharksystem.makan.android;

import android.Manifest;
import android.app.Activity;
import android.util.Log;

import net.sharksystem.android.util.PermissionCheck;
import net.sharksystem.makan.android.model.MakanListStorage;
import net.sharksystem.sharknet.android.SharkNetApp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MakanApp {
    private static final String LOGSTART = "MakanApp";
    private static MakanApp singleton = null;
    private MakanListStorage makanStorage;

    public static final String URI_START = "sn://makan/";

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


    public MakanListStorage getMakanListStorage() {
        if(this.makanStorage == null) {
            this.makanStorage = new MakanListStorage();
        }

        return this.makanStorage;
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
