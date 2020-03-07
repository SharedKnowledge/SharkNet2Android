package net.sharksystem.persons.android;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import net.sharksystem.R;
import net.sharksystem.SharkException;
import net.sharksystem.persons.Owner;
import net.sharksystem.sharknet.android.SharkNetActivity;
import net.sharksystem.sharknet.android.SharkNetApp;

import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;

public class OwnerActivity extends SharkNetActivity {
    public OwnerActivity() {
        super(SharkNetApp.getSharkNetApp());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.owner_drawer_layout);

        // set user name in layout
        EditText userNameView = this.findViewById(R.id.ownerDisplayName);

        try {
            userNameView.setText(OwnerStorageAndroid.getIdentityStorage(this).getDisplayName());
        } catch (UnrecoverableKeyException | NoSuchAlgorithmException | KeyStoreException e) {
            Log.e(this.getLogStart(), "serious problem: " + e.getLocalizedMessage());
        }

        this.getSharkNetApp().setupDrawerLayout(this);
    }

    public void onSendCredentials(View view) {
        this.finish();
        this.startActivity(new Intent(this, OwnerCredentialSendActivity.class));
    }

    public void onChangeClick(View view) throws SharkException {
        EditText userNameView = (EditText) findViewById(R.id.ownerDisplayName);

        String userNameString = userNameView.getText().toString();

        if(userNameString == null || userNameString.isEmpty()) {
            Toast.makeText(this, "user name is", Toast.LENGTH_SHORT).show();
        } else {
            Log.d(this.getLogStart(), "set new user name: " + userNameString);
            Owner identityStorage = null;
            try {
                identityStorage = OwnerStorageAndroid.getIdentityStorage(this);
                identityStorage.setDisplayName(userNameString);
            } catch (UnrecoverableKeyException | NoSuchAlgorithmException | KeyStoreException e) {
                Log.e(this.getLogStart(), "serious problem: " + e.getLocalizedMessage());
            }
            super.onBackPressed();
        }
    }

    public void onCreateNewKeys(View view) {
        (new CreateKeyPairThread(this)).start();
        /*
        try {
            PersonsStorageAndroid.getPersonsApp().generateKeyPair();
        } catch (SharkException e) {
            Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }
        */
    }

    public void onAbortClick(View view) {
        // go back to previous activity
        super.onBackPressed();
    }

    private class CreateKeyPairThread extends Thread {
        private final Context ctx;

        CreateKeyPairThread(Context ctx) {
            this.ctx = ctx;
        }

        public void run() {
            String text = null;
            try {
                OwnerStorageAndroid.getOwnerStorageAndroid(OwnerActivity.this).generateKeyPair();
                text = "new keypair created";
            } catch (SharkException e) {
                text = e.getLocalizedMessage();
                Log.e(OwnerActivity.this.getLogStart(), text);
            } catch (UnrecoverableKeyException | NoSuchAlgorithmException | KeyStoreException e) {
                Log.e(OwnerActivity.this.getLogStart(), e.getLocalizedMessage());
            }

//            Toast.makeText(this.ctx, text, Toast.LENGTH_SHORT).show();
        }
    }

    protected String getLogStart() {
        return this.getClass().getSimpleName();
    }

}
