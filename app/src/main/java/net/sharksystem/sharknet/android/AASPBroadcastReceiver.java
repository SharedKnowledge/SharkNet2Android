package net.sharksystem.sharknet.android;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import net.sharksystem.aasp.AASPChunkCache;
import net.sharksystem.aasp.AASPChunkStorage;
import net.sharksystem.aasp.AASPEngineFS;
import net.sharksystem.aasp.AASPException;
import net.sharksystem.aasp.AASPStorage;
import net.sharksystem.aasp.android.AASP;
import net.sharksystem.aasp.android.AASPBroadcastIntent;
import net.sharksystem.bubble.BubbleApp;

import java.io.IOException;
import java.util.Iterator;

class AASPBroadcastReceiver extends BroadcastReceiver {
    private static final String LOGSTART = "AASPBCReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {

        try {
            AASPBroadcastIntent aaspIntent = new AASPBroadcastIntent(intent);

            String text = "AASPService notified: "
                    + aaspIntent.getUser() + " / "
                    + aaspIntent.getFoldername() + " / "
                    + aaspIntent.getUri() + " / "
                    + aaspIntent.getEra();

            Log.d(LOGSTART, text);

            // handle that request
            Log.d(LOGSTART,"going to start handler thread");
            (new BroadcastHandlerThread(aaspIntent)).start();
        } catch (AASPException e) {
            e.printStackTrace();
        }
    }
}

