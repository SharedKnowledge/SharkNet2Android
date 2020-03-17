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
import net.sharksystem.android.util.DateTimeHelper;
import net.sharksystem.asap.android.Util;
import net.sharksystem.crypto.SharkCryptoException;
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
        TextView userIDTV = this.findViewById(R.id.ownerID);

        try {
            userNameView.setText(OwnerStorageAndroid.getOwnerStorageAndroid().getDisplayName());
            userIDTV.setText(OwnerStorageAndroid.getOwnerStorageAndroid().getUUID());
            this.setKeyCreationDate();
        } catch (UnrecoverableKeyException | NoSuchAlgorithmException | KeyStoreException | SharkCryptoException e) {
            Log.e(this.getLogStart(), "serious problem: " + e.getLocalizedMessage());
            this.finish();
        }

        this.getSharkNetApp().setupDrawerLayout(this);
    }

    private void setKeyCreationDate() throws SharkCryptoException {
        TextView creationTime = this.findViewById(R.id.ownerCreationTimeKeys);
        try {
            // that's funny because long is a homonym: data type but also means a long periode of time... ok, this is not funny at all... :/
            long longTime =
                    OwnerStorageAndroid.getOwnerStorageAndroid().getASAPKeyStorage().getCreationTime();

            if(longTime == DateTimeHelper.TIME_NOT_SET) {
                creationTime.setText("please create a key pair");
            } else {
                creationTime.setText("keys created at: " + DateTimeHelper.long2DateString(longTime));
            }
        } catch (UnrecoverableKeyException | NoSuchAlgorithmException | KeyStoreException e) {
            throw new SharkCryptoException(e.getLocalizedMessage());
        }
    }

    private void notifyKeyPairCreated() throws SharkCryptoException {
        // sync
        PersonsStorageAndroid.getPersonsApp().syncNewReceivedCertificates();

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
            try {
                identityStorage = OwnerStorageAndroid.getIdentityStorage(this);
                identityStorage.setDisplayName(userNameString);
            } catch (UnrecoverableKeyException | NoSuchAlgorithmException | KeyStoreException e) {
                Log.e(this.getLogStart(), "serious problem: " + e.getLocalizedMessage());
            }
            // re-launch
            this.finish();
            this.startActivity(new Intent(this, OwnerActivity.class));
        }
    }

    public void onShowOwnerAsSubjectCertificates(View view) {
        Log.d(Util.getLogStart(this), "onShowOwnerAsSubjectCertificates");
        Intent intent = null;
        try {
            intent = new PersonIntent(this,
                    OwnerStorageAndroid.getOwnerStorageAndroid().getUUID(),
                    CertificateListActivity.class);
            this.startActivity(intent);
        } catch (UnrecoverableKeyException | NoSuchAlgorithmException | KeyStoreException e) {
            Log.d(this.getLogStart(), "problems spawning certificate list: "
                    + e.getLocalizedMessage());
        }
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
                OwnerStorageAndroid o = OwnerStorageAndroid.getOwnerStorageAndroid(OwnerActivity.this);
                o.generateKeyPair();

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
            } catch (UnrecoverableKeyException | NoSuchAlgorithmException | KeyStoreException e) {
                Log.e(OwnerActivity.this.getLogStart(), e.getLocalizedMessage());
            }
        }
    }

    protected String getLogStart() {
        return this.getClass().getSimpleName();
    }

}
