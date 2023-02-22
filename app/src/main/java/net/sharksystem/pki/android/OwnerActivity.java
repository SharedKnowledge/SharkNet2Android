package net.sharksystem.pki.android;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import net.sharksystem.R;
import net.sharksystem.SharkException;
import net.sharksystem.asap.ASAPSecurityException;
import net.sharksystem.asap.android.Util;
import net.sharksystem.asap.utils.DateTimeHelper;
import net.sharksystem.sharknet.android.SharkNetActivity;
import net.sharksystem.sharknet.android.SharkNetApp;

/**
 * This activity is responsible for allowing the user to change the previously specified owner name
 * and to initialize the exchange of the pki credentials
 */
public class OwnerActivity extends SharkNetActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.owner_drawer_layout);

        // set user information in layout
        EditText ownerNameEditText = this.findViewById(R.id.ownerDisplayName);
        TextView ownerIdTextView = this.findViewById(R.id.ownerID);

        try {
            ownerNameEditText.setText(this.getSharkNetApp().getOwnerName());
            ownerIdTextView.setText(this.getSharkNetApp().getOwnerID());
            this.setKeyCreationDateView();
        } catch (ASAPSecurityException e) {
            Log.e(this.getLogStart(), "serious problem: " + e.getLocalizedMessage());
            this.finish();
        }

        this.getSharkNetApp().setupDrawerLayout(this);
    }


    private void setKeyCreationDateView() throws ASAPSecurityException {
        TextView creationTime = this.findViewById(R.id.ownerKeysCreationTime);
        // that's funny because long is a homonym: data type but also means a long period of time... ok, this is not funny at all... :/
        long longTime = this.getSharkNetApp().getSharkPKI().getKeysCreationTime();

        if(longTime == DateTimeHelper.TIME_NOT_SET) {
            creationTime.setText("please create a key pair");
        } else {
            creationTime.setText("keys created at: " + DateTimeHelper.long2DateString(longTime));
        }
    }

    private void notifyKeyPairCreated() {
        // sync
        this.getSharkNetApp().getSharkPKI().syncNewReceivedCertificates();

        // re-launch
        this.finish();
        this.startActivity(new Intent(this, OwnerActivity.class));
    }

    /**
     * Called when the user wants to send/receive the credentials to/from another user
     */
    public void onSendOrReceiveCredentials(View view) {
        this.finish();
        this.startActivity(new Intent(this, CredentialExchangeActivity.class));
    }


    /**
     * Called when the user clicks the save button to save the new owner name
     */
    public void onSaveClick(View view) {
        EditText ownerNameEditText = findViewById(R.id.ownerDisplayName);
        String ownerNameString = ownerNameEditText.getText().toString();

        try {
            SharkNetApp app = SharkNetApp.getSharkNetApp();
            app.changeOwnerName(this, ownerNameString); // TODO should this be initialize system? that would change ownerID
        } catch (SharkException e) {
            Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            Log.d(this.getLogStart(), e.getLocalizedMessage());
        } finally {
            // re-launch
            this.finish();
            this.startActivity(new Intent(this, OwnerActivity.class));
        }
    }



    public void onShowOwnerAsSubjectCertificates(View view) {
        Log.d(Util.getLogStart(this), "onShowOwnerAsSubjectCertificates");
        Intent intent;
        intent = new PersonIntent(this,
                this.getSharkNetApp().getOwnerID(),
                CertificateListActivity.class);
        this.startActivity(intent);
    }

    /**
     * Creates a new key pair in a new Thread
     */
    public void onCreateNewKeys(View view) {
        new CreateKeyPairThread(this).start();
    }

    /**
     * Clicked by the user to get back to the last activity
     */
    public void onAbortClick(View view) {
        // go back to previous activity
        this.finish();
    }

    /**
     * Thread for generating key pairs
     */
    private class CreateKeyPairThread extends Thread {
        private final OwnerActivity ownerActivity;

        CreateKeyPairThread(OwnerActivity ownerActivity) {
            this.ownerActivity = ownerActivity;
        }

        public void run() {
            String text;
            try {
                OwnerActivity.this.getSharkNetApp().getSharkPKI().generateKeyPair();

                // debugging
                /*
                ASAPKeyStorage asapKeyStorage = o.getASAPKeyStorage();
                PrivateKey privateKey = asapKeyStorage.getPrivateKey();
                PublicKey publicKey = asapKeyStorage.getPublicKey();
                 */

                text = "new keypair created";
                Log.d(OwnerActivity.this.getLogStart(), text);
                this.ownerActivity.notifyKeyPairCreated();
            } catch (ASAPSecurityException e) {
                text = e.getLocalizedMessage();
                Log.e(OwnerActivity.this.getLogStart(), text);
            }
        }
    }
    protected String getLogStart() {
        return this.getClass().getSimpleName();
    }
}
