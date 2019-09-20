package net.sharksystem.sharknet.android;

import android.content.Intent;
import android.os.Bundle;

import net.sharksystem.asap.android.apps.ASAPActivity;
import net.sharksystem.identity.android.IdentityActivity;
import net.sharksystem.makan.android.AddMakanActivity;

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
        if(!this.sharkNetApp.isOwnerSet() && this.getClass() != IdentityActivity.class) {
            Intent intent = new Intent(this, IdentityActivity.class);
            this.startActivity(intent);
        }
    }
}
