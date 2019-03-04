package net.sharksystem.makan.android;

import android.Manifest;
import android.app.Activity;

import net.sharksystem.android.util.PermissionCheck;
import net.sharksystem.makan.android.model.MakanListStorage;

public class MakanApp {
    private static MakanApp singleton = null;
    private MakanListStorage makanStorage;

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
}
