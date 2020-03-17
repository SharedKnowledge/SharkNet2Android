package net.sharksystem.persons.android;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import net.sharksystem.R;
import net.sharksystem.SharkException;
import net.sharksystem.asap.ASAPException;
import net.sharksystem.asap.android.apps.ASAPMessageReceivedListener;
import net.sharksystem.asap.apps.ASAPMessages;
import net.sharksystem.crypto.ASAPCertificate;
import net.sharksystem.crypto.ASAPCertificateStorage;
import net.sharksystem.persons.CredentialMessage;
import net.sharksystem.sharknet.android.SharkNetActivity;
import net.sharksystem.sharknet.android.SharkNetApp;

import java.io.IOException;
import java.util.Iterator;

public class PersonReceiveCredentialsActivity extends SharkNetActivity {
    private CredentialMessage credentialMessage;

    public PersonReceiveCredentialsActivity() {
        super(SharkNetApp.getSharkNetApp());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.person_receive_credential_layout);
//        this.getSharkNetApp().setupDrawerLayout(this);

        this.getSharkNetApp().addASAPMessageReceivedListener(PersonsStorageAndroid.CREDENTIAL_URI,
                new CredentialMessageReceivedListener(this));

    }

    private void doHandleCredentialMessage(ASAPMessages asapMessages) {
        Log.d(getLogStart(), "doHandleCredentialMessage");

        try {
            Iterator<byte[]> messages = asapMessages.getMessages();
            Log.d(getLogStart(), "#asap messages: " + asapMessages.size());
            if(messages.hasNext()) {
                Log.d(getLogStart(), "create credential message object");
                this.credentialMessage = new CredentialMessage(messages.next());
                TextView tv = this.findViewById(R.id.ownerDisplayName);
                tv.setText(credentialMessage.getOwnerName());

                tv = this.findViewById(R.id.ownerSendCredentialsControlNumber);
                tv.setText(CredentialMessage.sixDigitsToString(credentialMessage.getRandomInt()));
            }
        } catch (Exception e) {
            Log.d(this.getLogStart(), "problems when handling incoming credential: "
                    + e.getLocalizedMessage());
        }
    }

    public void onDoneClick(View v) {
        try {
            // sign and create certificate
            Log.d(this.getLogStart(), "before call addAndSignPerson()");
            ASAPCertificate newCert = PersonsStorageAndroid.getPersonsApp().addAndSignPerson(
                    this.credentialMessage.getOwnerID(),
                    this.credentialMessage.getOwnerName(),
                    this.credentialMessage.getPublicKey(),
                    this.credentialMessage.getValidSince());

            // return newly created certificate
            Log.d(this.getLogStart(), "before send ASAP Message");
            this.sendASAPMessage(ASAPCertificateStorage.APP_NAME,
                    ASAPCertificate.ASAP_CERTIFICATE_URI,
                    newCert.asBytes(), true);

        } catch (ASAPException | SharkException | IOException e) {
            Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }

        Log.d(this.getLogStart(), "finished sending freshly signed certificate back");
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
