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
import net.sharksystem.asap.android.Util;

public class PersonEditActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.person_edit_layout);

        try {
            PersonEditIntent personEditIntent = new PersonEditIntent(this.getIntent());

            TextView userIDView = findViewById(R.id.personEditUserID);
            userIDView.setText(String.valueOf(personEditIntent.getUserID()));

            EditText userNameView = findViewById(R.id.personEditName);

            switch(personEditIntent.getUserID()) {
                case 0: userNameView.setText("Alice"); break;
                case 1: userNameView.setText("Bob"); break;
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
}
