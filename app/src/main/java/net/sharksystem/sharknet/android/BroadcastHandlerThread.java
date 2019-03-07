package net.sharksystem.sharknet.android;

import android.util.Log;

import net.sharksystem.aasp.AASPChunkCache;
import net.sharksystem.aasp.AASPChunkStorage;
import net.sharksystem.aasp.AASPEngineFS;
import net.sharksystem.aasp.AASPStorage;
import net.sharksystem.aasp.android.AASPBroadcastIntent;
import net.sharksystem.bubble.BubbleApp;
import net.sharksystem.makan.android.MakanApp;

import java.util.Iterator;

class BroadcastHandlerThread extends Thread {
    private static final String LOGSTART = "Incom. AASP Broadcast";
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
        try {
            if(this.uri.startsWith(MakanApp.URI_START)) {
                Log.d(LOGSTART, "makan uri");
                MakanApp.getMakanApp().handleAASPBroadcast(
                        this.uri, this.era, this.user, this.folder);
            }

        } catch (Exception e) {
            Log.d(LOGSTART, e.getLocalizedMessage());
            e.printStackTrace();
        }
    }
}