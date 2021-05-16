package net.sharksystem.pki.android;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import net.sharksystem.R;
import net.sharksystem.asap.ASAPMessages;
import net.sharksystem.pki.CredentialMessage;
import net.sharksystem.pki.SharkCredentialReceivedListener;
import net.sharksystem.sharknet.android.SharkNetActivity;

import java.util.Iterator;

public class PersonWaitForCredentialActivity extends SharkNetActivity implements SharkCredentialReceivedListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.person_wait_for_credential_layout);

        this.getSharkNetApp().getSharkPKI().setSharkCredentialReceivedListener(this);

        Log.d(getLogStart(), "credential message Listener registered");
    }

    public void onAbortClick(View v) {
        this.finish();
    }

    private void doHandleCredentialMessage(ASAPMessages asapMessages) {
        /*
        Log.d(getLogStart(), "doHandleCredentialMessage");

        try {
            Iterator<byte[]> messages = asapMessages.getMessages();
            Log.d(getLogStart(), "#asap messages: " + asapMessages.size());
            if(messages.hasNext()) {
                Log.d(getLogStart(), "create credential message object..");
                CredentialMessage credentialMessage = new CredentialMessage(messages.next());
                Log.d(getLogStart(), "..created: " + credentialMessage);
                PersonsStorageAndroidComponent.getPersonsStorage().setReceivedCredential(credentialMessage);
                Log.d(getLogStart(), "credential message saved with persons storage");

                this.finish();
                this.startActivity(new Intent(this,
                        PersonAddReceivedCredentialsActivity.class));
            }
        } catch (Exception e) {
            Log.d(this.getLogStart(), "problems when handling incoming credential: "
                    + e.getLocalizedMessage());
        }
         */
    }

    @Override
    public void credentialReceived(CredentialMessage credentialMessage) {
        Toast.makeText(this, this.getLogStart() + ": implementation requires",
                Toast.LENGTH_LONG).show();

    }
}

