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

            TextView tv = findViewById(R.id.certificate_view_owner_id);
            tv.setText(this.ownerID);

            tv = findViewById(R.id.certificate_view_owner_name);
            tv.setText("ownerName");

            tv = findViewById(R.id.certificate_view_signer_id);
            tv.setText(this.signerID);

            tv = findViewById(R.id.certificate_view_signer_name);
            tv.setText("signerName");

            tv = findViewById(R.id.certificate_view_valid_since);
            tv.setText("since");

            tv = findViewById(R.id.certificate_view_valid_until);
            tv.setText("until");

        } catch (SharkException e) {
            Log.d(this.getLogStart(),
                    "problems when setting up certificate view: " + e.getLocalizedMessage());
        }
    }

    public void onSaveClick(View view) {
        SeekBar certExchangeFailureSeekBar =
                findViewById(R.id.personEditCertificateExchangeFailureRateSeekBar);

        int certExchangeFailure = certExchangeFailureSeekBar.getProgress();

        EditText userNameEditText = findViewById(R.id.personEditName);
        String userName = userNameEditText.getText().toString();

        Toast.makeText(this, "save: " +
                userName + " / " + certExchangeFailure, Toast.LENGTH_LONG).show();
        this.finish();
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
