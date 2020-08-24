package net.sharksystem.persons.android;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import net.sharksystem.R;
import net.sharksystem.asap.android.apps.ASAPMessageReceivedListener;
import net.sharksystem.asap.ASAPMessages;
import net.sharksystem.crypto.ASAPCertificate;
import net.sharksystem.persons.CredentialMessage;
import net.sharksystem.sharknet.android.SharkNetActivity;
import net.sharksystem.sharknet.android.SharkNetApp;

import java.util.Iterator;

public class PersonWaitForCredentialActivity extends SharkNetActivity {
    public PersonWaitForCredentialActivity() {
        super(SharkNetApp.getSharkNetApp());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.person_wait_for_credential_layout);

        this.getSharkNetApp().addASAPMessageReceivedListener(
                PersonsStorageAndroid.CREDENTIAL_APP_NAME,
                new CredentialMessageReceivedListener());

        Log.d(getLogStart(), "asap Listener registered for "
                + PersonsStorageAndroid.CREDENTIAL_APP_NAME);
    }

    public void onAbortClick(View v) {
        this.finish();
    }

    private void doHandleCredentialMessage(ASAPMessages asapMessages) {
        Log.d(getLogStart(), "doHandleCredentialMessage");

        try {
            Iterator<byte[]> messages = asapMessages.getMessages();
            Log.d(getLogStart(), "#asap messages: " + asapMessages.size());
            if(messages.hasNext()) {
                Log.d(getLogStart(), "create credential message object..");
                CredentialMessage credentialMessage = new CredentialMessage(messages.next());
                Log.d(getLogStart(), "..created: " + credentialMessage);
                PersonsStorageAndroid.getPersonsApp().setReceivedCredential(credentialMessage);
                Log.d(getLogStart(), "credential message saved with persons storage");

                this.finish();
                this.startActivity(new Intent(this,
                        PersonAddReceivedCredentialsActivity.class));
            }
        } catch (Exception e) {
            Log.d(this.getLogStart(), "problems when handling incoming credential: "
                    + e.getLocalizedMessage());
        }
    }

    private class CredentialMessageReceivedListener implements ASAPMessageReceivedListener {
        @Override
        public void asapMessagesReceived(ASAPMessages asapMessages) {
            Log.d(getLogStart(), "asapMessageReceived");
            PersonWaitForCredentialActivity.this.
                    getSharkNetApp().removeASAPMessageReceivedListener(
                            PersonsStorageAndroid.CREDENTIAL_APP_NAME, this);

            Log.d(getLogStart(), "removed listener, handle credential");
            PersonWaitForCredentialActivity.this.doHandleCredentialMessage(asapMessages);
        }
    }
}

