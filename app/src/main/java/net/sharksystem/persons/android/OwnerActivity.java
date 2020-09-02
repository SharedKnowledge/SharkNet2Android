package net.sharksystem.persons.android;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import net.sharksystem.R;
import net.sharksystem.SharkException;
import net.sharksystem.asap.util.DateTimeHelper;
import net.sharksystem.asap.android.Util;
import net.sharksystem.crypto.SharkCryptoException;
import net.sharksystem.sharknet.android.Owner;
import net.sharksystem.sharknet.android.SharkNetApp;

public class OwnerActivity extends PersonAppActivity {
    public OwnerActivity() {
        //super(SharkNetApp.getSharkNetApp());
        // just make a debug break.
        super();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.owner_drawer_layout);

        // set user name in layout
        EditText userNameView = this.findViewById(R.id.ownerDisplayName);
        TextView userIDTV = this.findViewById(R.id.ownerID);

        try {
            userNameView.setText(this.getOwnerStorage().getDisplayName());
            userIDTV.setText(this.getOwnerStorage().getUUID());
            this.setKeyCreationDateView();
        } catch (SharkCryptoException e) {
            Log.e(this.getLogStart(), "serious problem: " + e.getLocalizedMessage());
            this.finish();
        }

        this.getASAPApplication().setupDrawerLayout(this);
    }

    private void setKeyCreationDateView() throws SharkCryptoException {
        TextView creationTime = this.findViewById(R.id.ownerCreationTimeKeys);
        // that's funny because long is a homonym: data type but also means a long periode of time... ok, this is not funny at all... :/
        long longTime =
                AndroidASAPKeyStorage.getASAPKeyStorage().getCreationTime();

        if(longTime == DateTimeHelper.TIME_NOT_SET) {
            creationTime.setText("please create a key pair");
        } else {
            creationTime.setText("keys created at: " + DateTimeHelper.long2DateString(longTime));
        }
    }

    private void notifyKeyPairCreated() throws SharkCryptoException {
        // sync
        PersonsStorageAndroidComponent.getPersonsStorage().syncNewReceivedCertificates();

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
            Owner identityStorage = null;
            SharkNetApp.getSharkNetApp().getOwner().setDisplayName(userNameString);

            // re-launch
            this.finish();
            this.startActivity(new Intent(this, OwnerActivity.class));
        }
    }

    public void onShowOwnerAsSubjectCertificates(View view) {
        Log.d(Util.getLogStart(this), "onShowOwnerAsSubjectCertificates");
        Intent intent = null;
        intent = new PersonIntent(this,
                SharkNetApp.getSharkNetApp().getOwnerID(),
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
                AndroidASAPKeyStorage.getASAPKeyStorage().generateKeyPair();

                // debugging
                /*
                ASAPKeyStorage asapKeyStorage = o.getASAPKeyStorage();
                PrivateKey privateKey = asapKeyStorage.getPrivateKey();
                PublicKey publicKey = asapKeyStorage.getPublicKey();
                 */

                text = "new keypair created";
                Log.d(OwnerActivity.this.getLogStart(), text);
                this.ownerActivity.notifyKeyPairCreated();
            } catch (SharkException e) {
                text = e.getLocalizedMessage();
                Log.e(OwnerActivity.this.getLogStart(), text);
            }
        }
    }

    protected String getLogStart() {
        return this.getClass().getSimpleName();
    }

}
