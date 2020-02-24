package net.sharksystem.persons.android;

import android.content.Intent;
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
import net.sharksystem.asap.android.Util;

public class PersonEditActivity extends AppCompatActivity {
    private CharSequence userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.person_edit_layout);

        try {
            PersonIntent personIntent = new PersonIntent(this.getIntent());

            this.userID = personIntent.getOwnerID();

            TextView userIDView = findViewById(R.id.personEditUserID);
            userIDView.setText(String.valueOf(userID));

            EditText userNameView = findViewById(R.id.personEditName);

            switch(userID.toString()) {
                case "0": userNameView.setText("Alice"); break;
                case "1": userNameView.setText("Bob"); break;
                default: userNameView.setText("Unknown");
            }

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
                new PersonIntent(this, this.userID, IdentityAssuranceExplainActivity.class);
        this.startActivity(intent);
        this.finish();
    }
}
