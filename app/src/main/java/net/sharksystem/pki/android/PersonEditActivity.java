package net.sharksystem.pki.android;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import net.sharksystem.R;
import net.sharksystem.SharkException;
import net.sharksystem.asap.ASAPSecurityException;
import net.sharksystem.asap.android.Util;
import net.sharksystem.asap.persons.PersonValues;
import net.sharksystem.sharknet.android.SharkNetActivity;

public class PersonEditActivity extends SharkNetActivity
        implements AdapterView.OnItemSelectedListener {

    private CharSequence userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.person_edit_layout);

        try {
            PersonIntent personIntent = new PersonIntent(this.getIntent());

            this.userID = personIntent.getSubjectID();

            if(this.userID == null || this.userID.length() == 0) {
                Toast.makeText(this, "no user id - fatal", Toast.LENGTH_LONG).show();
                this.finish();
            }

            PersonValues personValues =
                    this.getSharkNetApp().getSharkPKI().getPersonValuesByID(this.userID);

            TextView userIDView = findViewById(R.id.personEditUserID);
            userIDView.setText(String.valueOf(userID));

            TextView tv = findViewById(R.id.personName);
            tv.setText(personValues.getName());

            Spinner sfSpinner = (Spinner) this.findViewById(R.id.signingFailureValue);
            ArrayAdapter<CharSequence> sfAdapter = ArrayAdapter.createFromResource(this,
                    R.array.identityAssuranceValues, android.R.layout.simple_spinner_item);
            sfAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sfSpinner.setAdapter(sfAdapter);
            sfSpinner.setOnItemSelectedListener(this);

            // set current value
            sfSpinner.setSelection(personValues.getSigningFailureRate());

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
        // nothing to do..
        this.finish();
    }

    public void onAbortClick(View view) {
        this.finish();
    }

    public void onExplainUserIDClick(View view) {
        Snackbar.make(view, R.string.explainUserIDText, Snackbar.LENGTH_LONG).show();
//        Toast.makeText(this, R.string.explainUserIDText, Toast.LENGTH_SHORT).show();
    }

    public void onExplainUserNameClick(View view) {
        Snackbar.make(view, R.string.explainUserNameText, Snackbar.LENGTH_LONG).show();
//        Toast.makeText(this, R.string.explainUserNameText, Toast.LENGTH_SHORT).show();
    }

    public void onExplainIdentityAssuranceClick(View view) {
        Snackbar.make(view, R.string.explainIdentityAssuranceText, Snackbar.LENGTH_LONG).show();
//        Toast.makeText(this, R.string.explainIdentityAssuranceText, Toast.LENGTH_SHORT).show();
    }

    public void onExplainSigningFailureClick(View view) {
        Snackbar.make(view, R.string.explainSigningFailureText, Snackbar.LENGTH_LONG).show();
//        Toast.makeText(this, R.string.explainSigningFailureText, Toast.LENGTH_SHORT).show();
    }

    public void onExplainWasCertifiedClick(View view) {
        Snackbar.make(view, R.string.explainWasCertifiedText, Snackbar.LENGTH_LONG).show();
//        Toast.makeText(this, R.string.explainWasCertifiedText, Toast.LENGTH_SHORT).show();
    }

    public void onExplainCertifiedClick(View view) {
        Snackbar.make(view, R.string.explainCertifiedText, Snackbar.LENGTH_LONG).show();
//        Toast.makeText(this, R.string.explainCertifiedText, Toast.LENGTH_SHORT).show();
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

    ///////////////////////////////////////////////////////////////////////////////////////
    //                             identity assurance spinner                            //
    ///////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int newSigningFailure, long id) {
        Log.d(this.getLogStart(), "signing failure set: " + newSigningFailure);

        try {
            this.getSharkNetApp().getSharkPKI().setSigningFailureRate(this.userID,newSigningFailure);
        } catch (ASAPSecurityException e) {
            Log.e(this.getLogStart(), "couldn't save data: " + e.getLocalizedMessage());
            Toast.makeText(this, "couldn't save data", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
