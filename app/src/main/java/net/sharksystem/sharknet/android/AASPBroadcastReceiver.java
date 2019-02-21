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
    private static final String LOGSTART = "AASP_BCR";

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
            (new BroadcastHandlerThread(
                    aaspIntent.getUser().toString(),
                    aaspIntent.getFoldername().toString(),
                    aaspIntent.getUri().toString(),
                    aaspIntent.getEra())
            ).start();
        } catch (AASPException e) {
            e.printStackTrace();
        }
    }

    private class BroadcastHandlerThread extends Thread {
        private final String folder;
        private final String uri;
        private final int era;
        private final String user;

        BroadcastHandlerThread(String user, String folder, String uri, int era) {
            this.user = user;
            this.folder = folder;
            this.uri = uri;
            this.era = era;
        }

        public void run() {
            Log.d(LOGSTART,"handler thread started");
            // create access to that chunk storage
            try {
                BubbleApp bubbleApp = SharkNetApp.getBubbleApp();
                if(bubbleApp == null) {
                    Log.d(LOGSTART, "failed to get bubble app object - break");
                    return;
                }

                AASPStorage chunkStorage = AASPEngineFS.getAASPChunkStorage(folder);
                Log.d(LOGSTART, "got chunk Storage to read from");

                AASPChunkStorage receivedChunksStorage = chunkStorage.getReceivedChunkStorage(user);

                AASPChunkCache aaspChunkCache =
                        receivedChunksStorage.getAASPChunkCache(uri, era, era);

                Log.d(LOGSTART, "start iterating received messages");
                Iterator<CharSequence> messages = aaspChunkCache.getMessages();

                if(messages == null) {
                    Log.d(LOGSTART, "couldn't create message iterator - fatal");
                    return;
                }

                Log.d(LOGSTART, "got message iterator");
                if(!messages.hasNext()) {
                    Log.d(LOGSTART, "no messages in iterator - most probably a failure");
                }
                while(messages.hasNext()) {
                    bubbleApp.handleAASPMessage(messages.next());
                }

            } catch (Exception e) {
                Log.d(LOGSTART, e.getLocalizedMessage());
                e.printStackTrace();
            }
        }
    }
}

