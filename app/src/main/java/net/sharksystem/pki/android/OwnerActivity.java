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

public class OwnerActivity extends SharkNetActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.owner_drawer_layout);

        // set user name in layout
        EditText userNameView = this.findViewById(R.id.ownerDisplayName);
        TextView userIDTV = this.findViewById(R.id.ownerID);

        try {
            userNameView.setText(this.getSharkNetApp().getDisplayName());
            userIDTV.setText(this.getSharkNetApp().getUUID());
            this.setKeyCreationDateView();
        } catch (ASAPSecurityException e) {
            Log.e(this.getLogStart(), "serious problem: " + e.getLocalizedMessage());
            this.finish();
        }

        this.getSharkNetApp().setupDrawerLayout(this);
    }

    private void setKeyCreationDateView() throws ASAPSecurityException {
        TextView creationTime = this.findViewById(R.id.ownerCreationTimeKeys);
        // that's funny because long is a homonym: data type but also means a long periode of time... ok, this is not funny at all... :/
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

    public void onSendCredentials(View view) {
        this.finish();
        this.startActivity(new Intent(this, OwnerCredentialSendActivity.class));
    }

    public void onSaveClick(View view) throws SharkException {
        EditText userNameView = (EditText) findViewById(R.id.ownerDisplayName);
        String userNameString = userNameView.getText().toString();

        if(userNameString == null || userNameString.isEmpty()) {
            Toast.makeText(this, "user name is", Toast.LENGTH_SHORT).show();
        } else {
            Log.d(this.getLogStart(), "set new user name: " + userNameString);
            this.getSharkNetApp().initializeSystem(this, userNameString);

            // re-launch
            this.finish();
            this.startActivity(new Intent(this, OwnerActivity.class));
        }
    }

    public void onShowOwnerAsSubjectCertificates(View view) {
        Log.d(Util.getLogStart(this), "onShowOwnerAsSubjectCertificates");
        Intent intent = null;
        intent = new PersonIntent(this,
                this.getSharkNetApp().getID(),
                CertificateListActivity.class);
        this.startActivity(intent);
    }

    public void onCreateNewKeys(View view) {
        (new CreateKeyPairThread(this)).start();
    }

    public void onAbortClick(View view) {
        // go back to previous activity
        this.finish();
    }

    private class CreateKeyPairThread extends Thread {
        private final OwnerActivity ownerActivity;

        CreateKeyPairThread(OwnerActivity ownerActivity) {
            this.ownerActivity = ownerActivity;
        }

        public void run() {
            String text = null;
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
