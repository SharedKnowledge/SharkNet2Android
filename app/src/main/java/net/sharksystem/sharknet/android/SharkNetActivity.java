package net.sharksystem.sharknet.android;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import net.sharksystem.asap.android.apps.ASAPActivity;

public abstract class SharkNetActivity extends ASAPActivity {
    public SharkNetActivity() {
        super(SharkNetApp.getSharkNetApp());
    }

    protected SharkNetApp getSharkNetApp() {
        return SharkNetApp.getSharkNetApp();
    }

    protected String getLogStart() {
        return this.getClass().getSimpleName();
    }
}
