package net.sharksystem.android.util;

import android.app.Activity;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

@Deprecated
public class PermissionCheck {
    public static void askForPermissions(Activity activity, String[] permissions) {
        if(permissions == null || permissions.length < 1) return;

        int requestNumber = 0;

        for(String permission : permissions) {
            if (ContextCompat.checkSelfPermission(activity, permission)
                    != PackageManager.PERMISSION_GRANTED) {

                // ask for missing permission
                ActivityCompat.requestPermissions(
                        activity,
                        new String[]{permission},
                        requestNumber++);
            }
        }
    }

}
