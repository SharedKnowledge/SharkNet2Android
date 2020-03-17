package net.sharksystem.persons.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import net.sharksystem.R;
import net.sharksystem.SharkException;
import net.sharksystem.asap.android.Util;
import net.sharksystem.persons.PersonValuesImpl;

public class PersonEditActivity extends AppCompatActivity {
    private CharSequence userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.person_edit_layout);

        try {
            PersonIntent personIntent = new PersonIntent(this.getIntent());

            this.userID = personIntent.getSubjectID();

            PersonValuesImpl personValues =
                    PersonsStorageAndroid.getPersonsApp().getPersonValues(this.userID);

            TextView userIDView = findViewById(R.id.personEditUserID);
            userIDView.setText(String.valueOf(userID));

            TextView tv = findViewById(R.id.personName);
            tv.setText(personValues.getName());

            SeekBar certExchangeFailureBar =
                    findViewById(R.id.personEditSigningFailureRateSeekBar);

            certExchangeFailureBar.setProgress(personValues.getSigningFailureRate());

            tv = findViewById(R.id.personEditIdentityAssuranceLevel);
            tv.setText(String.valueOf(personValues.getIdentityAssurance()));

        } catch (SharkException e) {
            Log.e(Util.getLogStart(this), "fatal: " + e.getLocalizedMessage());
            Toast.makeText(
                    this, "fatal: " + e.getLocalizedMessage(),
                    Toast.LENGTH_SHORT).show();

            this.finish();
        }
    }

    public void onSaveClick(View view) {
        SeekBar certExchangeFailureSeekBar =
                findViewById(R.id.personEditSigningFailureRateSeekBar);

        int certExchangeFailure = certExchangeFailureSeekBar.getProgress();

        try {
            PersonsStorageAndroid.getPersonsApp().setSigningFailureRate(this.userID, certExchangeFailure);
        } catch (SharkException e) {
            Log.e(this.getLogStart(), "couldn't save data: " + e.getLocalizedMessage());
            Toast.makeText(this, "couldn't save data", Toast.LENGTH_SHORT).show();
        }

        this.finish();
    }

    public void onAbortClick(View view) {
        this.finish();
    }

    public void onShowOwnCertificatesClick(View view) {
        Log.d(Util.getLogStart(this), "onShowOwnCertificatesClick");
        Intent intent = new PersonIntent(this, this.userID, CertificateListActivity.class);
        this.startActivity(intent);
    }

    public void onShowSignedCertificatesClick(View view) {
        Log.d(Util.getLogStart(this), "onShowSignedCertificatesClick");
        Intent intent =
                new PersonIntent(this, null, this.userID, CertificateListActivity.class);
        this.startActivity(intent);
        this.finish();
    }

    public void onShowExplainIdentityAssuranceClick(View view) {
        Log.d(Util.getLogStart(this), "onShowExplainIdentityAssuranceClick");
        Intent intent =
                new PersonIntent(this, this.userID, true,
                        CertificateListActivity.class);
        this.startActivity(intent);
        this.finish();
    }

    protected String getLogStart() {
        return this.getClass().getSimpleName();
    }
}
