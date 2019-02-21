package net.sharksystem.sharknet.android;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import net.sharksystem.aasp.AASPEngineFS;
import net.sharksystem.aasp.AASPException;
import net.sharksystem.aasp.AASPStorage;
import net.sharksystem.aasp.android.AASP;

import java.io.IOException;

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

        // create access to that chunk storage
        try {
            AASPStorage chunkStorage = AASPEngineFS.getAASPChunkStorage(folder);
            Log.d(LOGSTART, "got storage on client side");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (AASPException e) {
            e.printStackTrace();
        }
    }
}

