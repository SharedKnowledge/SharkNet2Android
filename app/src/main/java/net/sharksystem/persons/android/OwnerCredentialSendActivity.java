package net.sharksystem.persons.android;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import net.sharksystem.R;
import net.sharksystem.SharkException;
import net.sharksystem.asap.android.apps.ASAPMessageReceivedListener;
import net.sharksystem.asap.apps.ASAPMessages;
import net.sharksystem.crypto.ASAPCertificate;
import net.sharksystem.crypto.ASAPCertificateImpl;
import net.sharksystem.persons.CredentialMessage;
import net.sharksystem.persons.PersonsStorage;
import net.sharksystem.sharknet.android.SharkNetActivity;
import net.sharksystem.sharknet.android.SharkNetApp;

import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.spec.InvalidKeySpecException;
import java.util.Iterator;
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
        try {
            tv.setText(OwnerStorageAndroid.getIdentityStorage(this).getDisplayName());
        } catch (UnrecoverableKeyException | NoSuchAlgorithmException | KeyStoreException e) {
            Log.e(this.getLogStart(), "serious problem: " + e.getLocalizedMessage());
        }

        // set control number
        tv = this.findViewById(R.id.ownerSendCredentialsControlNumber);
        tv.setText(String.valueOf(-1));

        this.getSharkNetApp().addASAPMessageReceivedListener(ASAPCertificate.ASAP_CERTIFICATE,
                new OwnerCredentialSendActivity.CertificateMessageReceivedListener(this));
    }

    public void onSendClick(View v) {
        if(!sended) {
            sended = true;
            // 1st: produce random number with 6 digits
            int randomInt = ((new Random(System.currentTimeMillis())).nextInt());

            Log.d(this.getLogStart(), "randomInt: " + randomInt);

            // make it positiv
            if(randomInt < 0) randomInt = 0-randomInt;

            // take 6 digits
            int sixDigitsInt = 0;
            for(int i = 0; i < 6; i++) {
                sixDigitsInt += randomInt % 10;
                sixDigitsInt *= 10;
                randomInt /= 10;
            }

            sixDigitsInt /= 10;

            // set control number
            TextView tv = this.findViewById(R.id.ownerSendCredentialsControlNumber);
            tv.setText(CredentialMessage.sixDigitsToString(sixDigitsInt));

            // send credential message
            try {
                PersonsStorageAndroid personsAppAndroid = PersonsStorageAndroid.getPersonsApp();
                personsAppAndroid.sendCredentialMessage(
                        this, sixDigitsInt, personsAppAndroid.getOwnerID());

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
        try {
            for (Iterator<byte[]> it = asapMessages.getMessages(); it.hasNext(); ) {
                byte[] serializedCertificate = it.next();

                ASAPCertificateImpl receivedCertificate =
                        ASAPCertificateImpl.produceCertificateFromBytes(serializedCertificate);

                // do it locally - bypass online exchange which would return this certificate
                PersonsStorageAndroid.getPersonsApp().addCertificate(receivedCertificate);

                Toast.makeText(this, "certificate added", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException | SharkException e) {
            Log.e(this.getLogStart(), "problems when handling received certificate");
        }
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
