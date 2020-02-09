package net.sharksystem.sharknet.android;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import net.sharksystem.asap.android.apps.ASAPActivity;
import net.sharksystem.persons.android.OwnerActivity;

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

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // check if owner is set - if not - force to set
        if(!this.sharkNetApp.isOwnerSet() && this.getClass() != OwnerActivity.class) {
            Log.d(this.getLogStart(), "ower not set - force setting");
            Intent intent = new Intent(this, OwnerActivity.class);
            this.startActivity(intent);
        }
    }
}
