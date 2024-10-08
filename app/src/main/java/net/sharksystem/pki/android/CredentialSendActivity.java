package net.sharksystem.pki.android;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import net.sharksystem.R;
import net.sharksystem.SharkException;
import net.sharksystem.android.util.ObjectHolder;
import net.sharksystem.asap.ASAPHop;
import net.sharksystem.asap.ASAPMessageReceivedListener;
import net.sharksystem.asap.ASAPMessages;
import net.sharksystem.pki.CredentialMessage;
import net.sharksystem.sharknet.android.SharkNetActivity;
import net.sharksystem.sharknet.android.SharkNetApp;

import java.io.IOException;
import java.util.List;

@Deprecated
public class CredentialSendActivity extends SharkNetActivity {
    private static final String CREDENTIAL_MESSAGE_KEY = "credentialMessageKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.owner_send_credential_layout);
//        this.getSharkNetApp().setupDrawerLayout(this);

        // set user name in layout
        TextView tv = this.findViewById(R.id.ownerDisplayName);
        tv.setText(SharkNetApp.getSharkNetApp().getOwnerName());

        // set control number
        tv = this.findViewById(R.id.credentialsControlNumber);
        tv.setText(String.valueOf(-1));

        Toast.makeText(this, "implementation is obsolete", Toast.LENGTH_LONG).show();
        //this.getSharkNetApp().getSharkPKI().setSharkCredentialReceivedListener(this);
    }

    /*
    public void onSendClick(View v) {
        try {
            CredentialMessage credentialMessage =
                    this.getSharkNetApp().getSharkPKI().createCredentialMessage();

            // credential message stored under a key
            ObjectHolder.getObjectHolder().setObject(
                    CREDENTIAL_MESSAGE_KEY, credentialMessage);

            // key can be found with that taG
            ObjectHolder.getObjectHolder().setObject(
                    CredentialViewActivity.CREDENTIAL_MESSAGE_TAG, CREDENTIAL_MESSAGE_KEY);

            // view only
            String key = CredentialViewActivity.CREDENTIAL_VIEW_ONLY_TAG
                    + Long.toString(System.currentTimeMillis());

            ObjectHolder.getObjectHolder().setObject(
                    key, Boolean.TRUE);

            ObjectHolder.getObjectHolder().setObject(
                    CredentialViewActivity.CREDENTIAL_VIEW_ONLY_TAG, key);

            this.getSharkNetApp().getSharkPKI().sendOnlineCredentialMessage(credentialMessage);

            Intent intent = new Intent(this, CredentialViewActivity.class);

            this.startActivity(intent);

            this.finish();

        } catch (SharkException | IOException e) {
            Log.d(this.getLogStart(), "could not send credential: " + e.getLocalizedMessage());
            Toast.makeText(this, "could not send credential: " + e.getLocalizedMessage(),
                    Toast.LENGTH_SHORT).show();
        }
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
    }

    public void onDoneClick(View v) {
        this.finish();
    }

    private class CertificateMessageReceivedListener implements ASAPMessageReceivedListener {
        private final CredentialSendActivity credentialSendActivity;

        public CertificateMessageReceivedListener(
                CredentialSendActivity credentialSendActivity) {

            this.credentialSendActivity = credentialSendActivity;
        }

        @Override
        public void asapMessagesReceived(ASAPMessages asapMessages,
                                         String senderE2E, // E2E part
                                         List<ASAPHop> asapHops) {
            Log.d(getLogStart(), "asapMessageReceived");
            this.credentialSendActivity.doHandleCertificateMessage(asapMessages);
        }
    }
     */
}
