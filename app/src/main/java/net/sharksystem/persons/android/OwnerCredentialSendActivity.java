package net.sharksystem.persons.android;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import net.sharksystem.R;
import net.sharksystem.asap.android.apps.ASAPMessageReceivedListener;
import net.sharksystem.asap.ASAPMessages;
import net.sharksystem.crypto.ASAPCertificate;
import net.sharksystem.crypto.ASAPCertificateStorage;
import net.sharksystem.persons.CredentialMessage;
import net.sharksystem.persons.PersonsStorage;
import net.sharksystem.sharknet.android.SharkNetActivity;
import net.sharksystem.sharknet.android.SharkNetApp;

import java.util.Random;

public class OwnerCredentialSendActivity extends SharkNetActivity {
    private boolean sended = false;

    public OwnerCredentialSendActivity() {
        super(SharkNetApp.getSharkNetApp());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.owner_send_credential_layout);
//        this.getSharkNetApp().setupDrawerLayout(this);

        // set user name in layout
        TextView tv = this.findViewById(R.id.ownerDisplayName);
        tv.setText(SharkNetApp.getSharkNetApp().getOwnerStorage().getDisplayName());

        // set control number
        tv = this.findViewById(R.id.credentialsControlNumber);
        tv.setText(String.valueOf(-1));

        this.getSharkNetApp().addASAPMessageReceivedListener(ASAPCertificate.ASAP_CERTIFICATE_URI,
                new OwnerCredentialSendActivity
                        .CertificateMessageReceivedListener(this));
    }

    public void onSendClick(View v) {
        if(!sended) {
            // send credential message
            try {
                sended = true;

                PersonsStorageAndroid personsAppAndroid = PersonsStorageAndroid.getPersonsApp();
                CredentialMessage credentialMessage = personsAppAndroid.createCredentialMessage();

                // set control number
                TextView tv = this.findViewById(R.id.credentialsControlNumber);
                tv.setText(CredentialMessage.sixDigitsToString(credentialMessage.getRandomInt()));

                Log.d(this.getLogStart(), "send credentials: " + credentialMessage);
                this.sendASAPMessage(PersonsStorage.CREDENTIAL_APP_NAME,
                        PersonsStorage.CREDENTIAL_URI,
                        credentialMessage.getMessageAsBytes(),
                        true);

            } catch (Exception e) {
                Log.d(this.getLogStart(), "Exception when sending credential: "
                        + e.getLocalizedMessage());

                Toast.makeText(this, "Exception when sending credential: "
                        + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();

                this.finish();
                return;
            }
        } else {
            Toast.makeText(this, "credential already sent", Toast.LENGTH_SHORT).show();
        }
    }

    private void doHandleCertificateMessage(ASAPMessages asapMessages) {
        // something was received - integrate
        Log.d(this.getLogStart(), "reached doHandleCertificateMessage");
        PersonsStorageAndroid personsApp = PersonsStorageAndroid.getPersonsApp();
        Log.d(this.getLogStart(), "calling syncNewReceivedCertificates");
        if(personsApp.syncNewReceivedCertificates()) {
            Log.d(this.getLogStart(), "calling save");
            personsApp.save();
        }

        Log.d(this.getLogStart(), "certificates added");

        // do we have issuers public key?
        Toast.makeText(this,
                "You received a signed certificate. Do you already have public key of the signer?"
                , Toast.LENGTH_LONG).show();
    }

    public void onDoneClick(View v) {
        this.finish();
    }

    private class CertificateMessageReceivedListener implements ASAPMessageReceivedListener {
        private final OwnerCredentialSendActivity ownerCredentialSendActivity;

        public CertificateMessageReceivedListener(
                OwnerCredentialSendActivity ownerCredentialSendActivity) {

            this.ownerCredentialSendActivity = ownerCredentialSendActivity;
        }

        @Override
        public void asapMessagesReceived(ASAPMessages asapMessages) {
            Log.d(getLogStart(), "asapMessageReceived");
            this.ownerCredentialSendActivity.doHandleCertificateMessage(asapMessages);
        }
    }

}
