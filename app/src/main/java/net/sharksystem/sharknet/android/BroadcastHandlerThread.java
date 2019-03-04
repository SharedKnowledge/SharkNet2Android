package net.sharksystem.sharknet.android;

import android.util.Log;

import net.sharksystem.aasp.AASPChunkCache;
import net.sharksystem.aasp.AASPChunkStorage;
import net.sharksystem.aasp.AASPEngineFS;
import net.sharksystem.aasp.AASPStorage;
import net.sharksystem.aasp.android.AASPBroadcastIntent;
import net.sharksystem.bubble.BubbleApp;

import java.util.Iterator;

class BroadcastHandlerThread extends Thread {
    private static final String LOGSTART = "AASPBCHandler";
    private final String folder;
    private final String uri;
    private final int era;
    private final String user;

    BroadcastHandlerThread(AASPBroadcastIntent aaspIntent) {
        this.user = aaspIntent.getUser().toString();
        this.folder = aaspIntent.getFoldername().toString();
        this.uri = aaspIntent.getUri().toString();
        this.era = aaspIntent.getEra();
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

            AASPChunkStorage receivedChunksStorage = chunkStorage.getIncomingChunkStorage(user);

            AASPChunkCache aaspChunkCache =
                    receivedChunksStorage.getAASPChunkCache(uri, era);

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