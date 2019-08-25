package net.sharksystem.sharknet.android;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import net.sharksystem.asap.ASAPException;
import net.sharksystem.asap.android.ASAPBroadcastIntent;

class AASPBroadcastReceiver extends BroadcastReceiver {
    private static final String LOGSTART = "AASPBCReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {

        try {
            ASAPBroadcastIntent asapIntent = new ASAPBroadcastIntent(intent);

            String text = "AASPService notified: "
                    + asapIntent.getUser() + " / "
                    + asapIntent.getFoldername() + " / "
                    + asapIntent.getUri() + " / "
                    + asapIntent.getEra();

            Log.d(LOGSTART, text);

            // handle that request
            Log.d(LOGSTART,"going to start handler thread");
            (new BroadcastHandlerThread(asapIntent)).start();
        } catch (ASAPException e) {
            e.printStackTrace();
        }
    }
}

