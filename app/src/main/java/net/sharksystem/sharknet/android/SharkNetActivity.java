package net.sharksystem.sharknet.android;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

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
        // check if owner is set - if not - force to set
        if(!this.sharkNetApp.getOwnerStorage(this).isOwnerSet()) {
            Log.d(this.getLogStart(), "owner not set - force setting");
            // make clear nothing else should happen
            this.finish();
            // because calling SharkNetActivity onCreate will proceed
            this.setInitASAPApplication(false);
            Intent intent = new Intent(this, InitActivity.class);
            this.startActivity(intent);
        } else {
            this.setInitASAPApplication(true);
        }

        // setup asap app only after proper user account creation
        super.onCreate(savedInstanceState);
    }

    public void onAbortClick (View view) {
        this.finish();
    }
}
