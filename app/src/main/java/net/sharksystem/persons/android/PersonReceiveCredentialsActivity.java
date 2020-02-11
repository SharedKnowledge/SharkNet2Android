package net.sharksystem.persons.android;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import net.sharksystem.R;
import net.sharksystem.asap.android.apps.ASAPMessageReceivedListener;
import net.sharksystem.asap.apps.ASAPMessages;
import net.sharksystem.sharknet.android.SharkNetActivity;
import net.sharksystem.sharknet.android.SharkNetApp;

import java.io.IOException;
import java.util.Iterator;

public class PersonReceiveCredentialsActivity extends SharkNetActivity {
    public PersonReceiveCredentialsActivity() {
        super(SharkNetApp.getSharkNetApp());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.person_receive_credential_layout);
//        this.getSharkNetApp().setupDrawerLayout(this);

        this.getSharkNetApp().addASAPMessageReceivedListener(OwnerApp.CREDENTIAL_URI,
                new CredentialMessageReceivedListener(this));

    }

    private void doHandleCredentialMessage(ASAPMessages asapMessages) {
        Log.d(getLogStart(), "doHandleCredentialMessage");

        try {
            Iterator<byte[]> messages = asapMessages.getMessages();
            Log.d(getLogStart(), "#asap messages: " + asapMessages.size());
            if(messages.hasNext()) {
                Log.d(getLogStart(), "create credential message object");
                CredentialMessage credentialMessage = new CredentialMessage(messages.next());
                TextView tv = this.findViewById(R.id.ownerDisplayName);
                tv.setText(credentialMessage.getOwnerName());

                tv = this.findViewById(R.id.ownerSendCredentialsControlNumber);
                tv.setText(CredentialMessage.sixDigitsToString(credentialMessage.getRandomInt()));
            }
        } catch (IOException e) {
            Log.d(this.getLogStart(), "problems when handling incoming credential: "
                    + e.getLocalizedMessage());
        }
    }

    public void onDoneClick(View v) {
        this.getSharkNetApp().removeChunkReceivedListener(OwnerApp.CREDENTIAL_URI);
        this.finish();
    }

    private class CredentialMessageReceivedListener implements ASAPMessageReceivedListener {

        private final PersonReceiveCredentialsActivity personReceiveCredentialsActivity;

        public CredentialMessageReceivedListener(
                PersonReceiveCredentialsActivity personReceiveCredentialsActivity) {

            this.personReceiveCredentialsActivity = personReceiveCredentialsActivity;

        }

        @Override
        public void asapMessagesReceived(ASAPMessages asapMessages) {
            Log.d(getLogStart(), "asapMessageReceived");
            this.personReceiveCredentialsActivity.doHandleCredentialMessage(asapMessages);
        }
    }
}
