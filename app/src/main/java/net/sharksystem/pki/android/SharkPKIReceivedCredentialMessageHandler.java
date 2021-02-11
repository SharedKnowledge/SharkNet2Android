package net.sharksystem.pki.android;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import net.sharksystem.R;
import net.sharksystem.asap.persons.CredentialMessage;
import net.sharksystem.pki.SharkCredentialReceivedListener;
import net.sharksystem.sharknet.android.SharkNetApp;

public class SharkPKIReceivedCredentialMessageHandler implements SharkCredentialReceivedListener {
    private static final String NOTIFICATION_CHANNEL_SN2_CREDENTIAL_RECEIVED =
            "SN2CredentialReceived";

    private static boolean notificationInitialized = false;
    private static int currentNotificationID = -1;

    public SharkPKIReceivedCredentialMessageHandler(Activity initialActivity) {
        if(!notificationInitialized) {
            createCredentialReceivedNotificationChannel(initialActivity);
        }
    }

    private int nextNotificationID() {
        currentNotificationID++;
        return currentNotificationID;
    }

    // https://developer.android.com/training/notify-user/build-notification#java
    public static void createCredentialReceivedNotificationChannel(Activity initialActivity) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = initialActivity.getString(R.string.channel_name);
            String description = initialActivity.getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel =
                    new NotificationChannel(NOTIFICATION_CHANNEL_SN2_CREDENTIAL_RECEIVED,
                            name, importance);

            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager =
                    initialActivity.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    /**
     * Credential received listener
     * @param credentialMessage
     */
    @Override
    public void credentialReceived(CredentialMessage credentialMessage) {
        Log.d(getLogStart(), "credential message received: " + credentialMessage);
        Context context = SharkNetApp.getSharkNetApp().getASAPAndroidPeer().getActivity();

        // store message to be retrievable from processing activity
        PersonStatusHelper.getPersonsStorage().setReceivedCredential(credentialMessage);

        // Create an explicit intent for an Activity in your app
        Intent intent = new Intent(context, PersonAddReceivedCredentialsActivity.class);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                0, // ??
                intent,
                0 // ??
        );

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_SN2_CREDENTIAL_RECEIVED)
                .setSmallIcon(R.drawable.shark_red_lowerres)
                .setContentTitle("Credential received")
                .setContentText("A peer wants you to issue a certificate ...")
//            .setStyle(new NotificationCompat.BigTextStyle()
//                    .bigText("Much longer text that cannot fit one line..."))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(nextNotificationID(), builder.build());
    }

    private static String getLogStart() {
        return SharkNetApp.class.getSimpleName();
    }
}
