package net.sharksystem.pki.android;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import net.sharksystem.EncounterConnectionType;
import net.sharksystem.R;
import net.sharksystem.asap.ASAPMessageReceivedListener;
import net.sharksystem.asap.ASAPMessages;
import net.sharksystem.sharknet.android.SharkNetActivity;
import net.sharksystem.sharknet.android.SharkNetApp;

public class OwnerCredentialSendActivity extends SharkNetActivity {
    private boolean sended = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.owner_send_credential_layout);
//        this.getSharkNetApp().setupDrawerLayout(this);

        // set user name in layout
        TextView tv = this.findViewById(R.id.ownerDisplayName);
        tv.setText(SharkNetApp.getSharkNetApp().getDisplayName());

        // set control number
        tv = this.findViewById(R.id.credentialsControlNumber);
        tv.setText(String.valueOf(-1));

        Toast.makeText(this, "implementation is obsolete", Toast.LENGTH_LONG).show();
        //this.getSharkNetApp().getSharkPKI().setSharkCredentialReceivedListener(this);
    }

    public void onSendClick(View v) {
        /*
        if(!sended) {
            // send credential message
            try {
                sended = true;

                PersonsStorageAndroidComponent personsAppAndroid =
                        PersonsStorageAndroidComponent.getPersonsStorage();

                CredentialMessage credentialMessage = personsAppAndroid.createCredentialMessage();

                // set control number
                TextView tv = this.findViewById(R.id.credentialsControlNumber);
                tv.setText(CredentialMessage.sixDigitsToString(credentialMessage.getRandomInt()));

                Log.d(this.getLogStart(), "send credentials: " + credentialMessage);
                this.sendASAPMessage(ASAPPKI.CREDENTIAL_APP_NAME,
                        ASAPPKI.CREDENTIAL_URI,
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

         */
        Toast.makeText(this, "implementation removed", Toast.LENGTH_LONG).show();
    }

    private void doHandleCertificateMessage(ASAPMessages asapMessages) {
        Toast.makeText(this, "implementation removed", Toast.LENGTH_LONG).show();
        /*
        // something was received - integrate
        Log.d(this.getLogStart(), "reached doHandleCertificateMessage");
        PersonsStorageAndroidComponent personsApp = PersonsStorageAndroidComponent.getPersonsStorage();
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
         */
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
        public void asapMessagesReceived(ASAPMessages asapMessages,
                                         String senderE2E, // E2E part
                                         String senderPoint2Point, boolean verified, boolean encrypted, // Point2Point part
                                         EncounterConnectionType connectionType) {
            Log.d(getLogStart(), "asapMessageReceived");
            this.ownerCredentialSendActivity.doHandleCertificateMessage(asapMessages);
        }
    }

}
