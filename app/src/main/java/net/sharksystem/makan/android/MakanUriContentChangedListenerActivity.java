package net.sharksystem.makan.android;

import android.util.Log;

import net.sharksystem.asap.android.apps.ASAPUriContentChangedListener;
import net.sharksystem.sharknet.android.SharkNetActivity;
import net.sharksystem.sharknet.android.SharkNetApp;

public abstract class MakanUriContentChangedListenerActivity extends SharkNetActivity
        implements ASAPUriContentChangedListener {

    private boolean listenerSet = false;

    private void stopUriListening() {
        // stop listening to chunk receiver
        if(!listenerSet) {
            Log.d(this.getLogStart(), "remove uri listener");
            this.getSharkNetApp().removeASAPUriContentChangedListener(MakanApp.APP_NAME, this);
            this.listenerSet = false;
        }
    }

    private void startUriListening() {
        // listen to chunk receiver
        if(!this.listenerSet) {
            Log.d(this.getLogStart(), "add uri listener");
            this.getSharkNetApp().addASAPUriContentChangedListener(MakanApp.APP_NAME, this);
            this.listenerSet = true;
        }
    }

    protected void onStart() {
        super.onStart();
        this.startUriListening();
    }

    protected void onResume() {
        super.onResume();
        this.startUriListening();
    }

    protected void onPause() {
        super.onPause();
        this.stopUriListening();
    }

    protected void onStop() {
        // Unbind from the service
        super.onStop();
        this.stopUriListening();
    }

    protected void onDestroy() {
        super.onDestroy();
        this.stopUriListening();
    }
}
