package net.sharksystem.persons.android;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import net.sharksystem.R;
import net.sharksystem.SharkException;
import net.sharksystem.crypto.ASAPCertificate;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

public class CertificateViewActivity extends AppCompatActivity {
    private CharSequence ownerID;
    private CharSequence signerID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.certificate_view_layout);

        try {
            PersonIntent personIntent = new PersonIntent(this.getIntent());

            this.ownerID = personIntent.getOwnerID();
            this.signerID = personIntent.getSignerID();

            Collection<ASAPCertificate> certificateByOwner =
                    PersonsStorageAndroid.getPersonsApp().getCertificateBySubject(this.ownerID);
            ASAPCertificate cert = null;
            for(ASAPCertificate c : certificateByOwner) {
                if(c.getIssuerID().toString().equalsIgnoreCase(this.signerID.toString())) {
                    // got it
                    cert = c;
                }
            }

            if(cert == null) {
                Log.e(this.getLogStart(), "internal failure: no cert found");
                Toast.makeText(this,
                        "internal failure: no cert found", Toast.LENGTH_SHORT).show();
                this.finish();
                return;
            }

            TextView tv = findViewById(R.id.certificate_view_owner_id);
            tv.setText(cert.getSubjectID());

            tv = findViewById(R.id.certificate_view_owner_name);
            tv.setText(cert.getSubjectName());

            tv = findViewById(R.id.certificate_view_signer_id);
            tv.setText(cert.getIssuerID());

            tv = findViewById(R.id.certificate_view_signer_name);
            tv.setText(cert.getIssuerName());

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd., yyyy");

            tv = findViewById(R.id.certificate_view_valid_since);
            tv.setText(simpleDateFormat.format(
                    new Date(cert.getValidSince().getTimeInMillis())));

            tv = findViewById(R.id.certificate_view_valid_until);
            tv.setText(simpleDateFormat.format(
                    new Date(cert.getValidUntil().getTimeInMillis())));

        } catch (SharkException e) {
            Log.d(this.getLogStart(),
                    "problems when setting up certificate view: " + e.getLocalizedMessage());
        }
    }

    public void onAbortClick(View view) {
        this.finish();
    }

    public void onDeleteClick(View view) {
        Toast.makeText(this, "TODO: doDeleteClick", Toast.LENGTH_SHORT).show();
        this.finish();
    }

    private String getLogStart() {
        return net.sharksystem.asap.util.Log.startLog(this).toString();
    }
}
