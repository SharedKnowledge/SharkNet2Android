package net.sharksystem.sharknet.android;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import net.sharksystem.aasp.AASPChunkCache;
import net.sharksystem.aasp.AASPEngineFS;
import net.sharksystem.aasp.AASPException;
import net.sharksystem.aasp.AASPStorage;
import net.sharksystem.aasp.android.AASP;
import net.sharksystem.bubble.BubbleApp;

import java.io.IOException;
import java.util.Iterator;

class AASPBroadcastReceiver extends BroadcastReceiver {
    private static final String LOGSTART = "AASP_BCR";

    @Override
    public void onReceive(Context context, Intent intent) {
        String folder = intent.getStringExtra(AASP.FOLDER);
        String uri = intent.getStringExtra(AASP.URI);
        int era = intent.getIntExtra(AASP.ERA, 0);

        String text = "AASPService notified: "
                + folder + " / "
                + uri + " / "
                + era;

        Log.d(LOGSTART, text);

        // handle that request
        (new BroadcastHandlerThread(folder, uri, era)).start();
    }

    private class BroadcastHandlerThread extends Thread {
        private final String folder;
        private final String uri;
        private final int era;

        BroadcastHandlerThread(String folder, String uri, int era) {
            this.folder = folder;
            this.uri = uri;
            this.era = era;
        }

        public void run() {
            // create access to that chunk storage
            try {
                BubbleApp bubbleApp = SharkNetApp.getBubbleApp();

                Log.d(LOGSTART, "got bubble app object");

                AASPStorage chunkStorage = AASPEngineFS.getAASPChunkStorage(folder);
                Log.d(LOGSTART, "got chunk Storage to read from");

                AASPChunkCache aaspChunkCache =
                        chunkStorage.getChunkStorage().getAASPChunkCache(uri, era, era);

                Log.d(LOGSTART, "start iterating received messages");
                Iterator<CharSequence> messages = aaspChunkCache.getMessages();

                if(messages == null) {
                    Log.d(LOGSTART, "couldn't create message iterator - fatal");
                    return;
                }

                Log.d(LOGSTART, "get message iterator");
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

