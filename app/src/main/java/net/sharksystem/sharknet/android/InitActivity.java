package net.sharksystem.sharknet.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import net.sharksystem.R;
import net.sharksystem.SharkException;
import net.sharksystem.messenger.android.SNChannelsListActivity;
import net.sharksystem.pki.android.CredentialExchangeActivity;
import net.sharksystem.pki.android.OwnerActivity;
import net.sharksystem.pki.android.PersonListViewActivity;
import net.sharksystem.sharknet.android.settings.SettingsActivity;

import java.io.IOException;

public class InitActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(this.getLogStart(), "Startup SharkNetApplication");

        try {
            // used before? is there an ownerID?
            String ownerID = SharkNetApp.getOwnerID(this);
            // yes - ignition!
            this.initializeSystem(ownerID);
        } catch(SharkException se) {
            Log.d(this.getLogStart(), "most probably first app usage: "
                    + se.getLocalizedMessage());

            // no - ask for name with this activity
            setContentView(R.layout.init);
        }
    }

    private void initializeSystem(String ownerID) {
        try {
            boolean veryFirstLaunch = false;
            if(ownerID == null) {
                veryFirstLaunch = true;
                ownerID = SharkNetApp.getOwnerID(this);
            }

            SharkNetApp.initializeSharkNetApp(this, ownerID);
            Log.d(this.getLogStart(), "shark system is initialized - start first activity");

//            Class firstActivity = veryFirstLaunch ? OwnerActivity.class : SNChannelsListActivity.class;
//            Class firstActivity = veryFirstLaunch ? OwnerActivity.class : SNTestActivity.class;
//            Class firstActivity = veryFirstLaunch ? OwnerActivity.class : PersonListViewActivity.class;
            Class firstActivity = veryFirstLaunch ? OwnerActivity.class : SettingsActivity.class;
//            Class firstActivity = veryFirstLaunch ? OwnerActivity.class : CredentialExchangeActivity.class;


            Log.d(this.getLogStart(), "shark system is initialized - start first activity:"
                    + firstActivity.getClass().getSimpleName());
            this.finish();
            Intent intent = new Intent(this, firstActivity);
            this.startActivity(intent);
        }
        catch(SharkException se) {
            Log.e(this.getLogStart(), "cannot initialized app - fatal: "
                    + se.getLocalizedMessage());
            se.printStackTrace();
            Toast.makeText(this, "internal fatal error: cannot launch system",
                    Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onSaveClick(View view) {
        EditText ownerNameEditText = this.findViewById(R.id.initOwnerName);

        String ownerName = ownerNameEditText.getText().toString();

        try {
            SharkNetApp.initializeSystem(this, ownerName);
            this.initializeSystem(null);
        } catch (SharkException e) {
            Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private String getLogStart() {
        return "SharkNet2 InitActivity";
    }
}
