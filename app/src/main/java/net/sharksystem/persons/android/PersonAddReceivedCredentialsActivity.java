package net.sharksystem.persons.android;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import net.sharksystem.R;
import net.sharksystem.SharkException;
import net.sharksystem.asap.util.DateTimeHelper;
import net.sharksystem.asap.ASAPException;
import net.sharksystem.crypto.ASAPCertificate;
import net.sharksystem.crypto.ASAPCertificateStorage;
import net.sharksystem.persons.CredentialMessage;
import net.sharksystem.sharknet.android.SharkNetActivity;
import net.sharksystem.sharknet.android.SharkNetApp;

import java.io.IOException;

public class PersonAddReceivedCredentialsActivity extends SharkNetActivity {
    private CredentialMessage credential;

    public PersonAddReceivedCredentialsActivity() {
        super(SharkNetApp.getSharkNetApp());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.person_add_received_credential_layout);

        Log.d(this.getLogStart(), "onCreate");
        this.credential = PersonsStorageAndroid.getPersonsApp().getReceivedCredential();
        Log.d(this.getLogStart(), "got credential: " + this.credential);

        TextView tv = this.findViewById(R.id.credentialDisplayName);
        tv.setText(credential.getOwnerName());

        tv = this.findViewById(R.id.credentialID);
        tv.setText(credential.getOwnerID());

        tv = this.findViewById(R.id.credentialValidSince);
        tv.setText("valid since: " + DateTimeHelper.long2DateString(credential.getValidSince()));

        tv = this.findViewById(R.id.credentialsControlNumber);
        tv.setText(CredentialMessage.sixDigitsToString(credential.getRandomInt()));
    }

    public void onAddClick(View v) {
        try {
            // sign and create certificate
            Log.d(this.getLogStart(), "before call addAndSignPerson()");
            ASAPCertificate newCert = PersonsStorageAndroid.getPersonsApp().addAndSignPerson(
                    this.credential.getOwnerID(),
                    this.credential.getOwnerName(),
                    this.credential.getPublicKey(),
                    this.credential.getValidSince());

            // return newly created certificate
            Log.d(this.getLogStart(), "right before sending certificate as ASAP Message");
            this.sendASAPMessage(ASAPCertificateStorage.CERTIFICATE_APP_NAME,
                    ASAPCertificate.ASAP_CERTIFICATE_URI,
                    newCert.asBytes(), true);
            Log.d(this.getLogStart(), ".. sent certificate message");

        } catch (ASAPException | SharkException | IOException e) {
            Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }

        Log.d(this.getLogStart(), "finished sending freshly signed certificate back");
        this.finish();
    }
}
