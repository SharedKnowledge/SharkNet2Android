package net.sharksystem.sharknet.android;

import net.sharksystem.SharkPeer;
import net.sharksystem.asap.android.apps.ASAPActivity;

public abstract class SharkNetActivity extends ASAPActivity {
    protected SharkNetApp getSharkNetApp() {
        return SharkNetApp.getSharkNetApp();
    }

    public SharkNetActivity() {

        int breakpoint = 42;
    }

    protected SharkPeer getSharkPeer() {
        return this.getSharkNetApp().getSharkPeer();
    }

    protected String getLogStart() {
        return this.getClass().getSimpleName();
    }
}
