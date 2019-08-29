package net.sharksystem.sharknet.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import net.sharksystem.asap.android.ASAPActivity;

public abstract class SharkNetActivity extends ASAPActivity {
    private SharkNetApp sharkNetApp;

    public SharkNetActivity(SharkNetApp sharkNetApp) {
        super(sharkNetApp);
        this.sharkNetApp = sharkNetApp;
    }

    protected SharkNetApp getSharkNetApp() {
        return this.sharkNetApp;
    }

    protected String getLogStart() {
        return this.getClass().getSimpleName();
    }
}
