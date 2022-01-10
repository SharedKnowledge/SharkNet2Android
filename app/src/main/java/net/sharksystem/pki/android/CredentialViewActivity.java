package net.sharksystem.pki.android;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import net.sharksystem.R;
import net.sharksystem.SharkException;
import net.sharksystem.android.util.ObjectHolder;
import net.sharksystem.asap.ASAPSecurityException;
import net.sharksystem.asap.utils.DateTimeHelper;
import net.sharksystem.pki.CredentialMessage;
import net.sharksystem.sharknet.android.SharkNetActivity;

import java.io.IOException;

public class CredentialViewActivity extends SharkNetActivity {
    public static final String CREDENTIAL_MESSAGE_KEY_TAG = "credentialMessageKeyTag";
    public static final String CREDENTIAL_VIEW_ONLY_TAG =
        "CREDENTIAL_VIEW_BEHAVIOUR_KEY";

    private CredentialMessage credentialMessage;
    private boolean viewOnly = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.credential_view_layout);

        // get credential message
        try {
            // get Credential message
            ObjectHolder objectHolder = ObjectHolder.getObjectHolder();
            String key = (String) objectHolder.getAndRemoveObject(CREDENTIAL_MESSAGE_KEY_TAG);
            this.credentialMessage = (CredentialMessage) objectHolder.getAndRemoveObject(key);

            // what to do.
            key = (String) objectHolder.getAndRemoveObject(CREDENTIAL_VIEW_ONLY_TAG);
            this.viewOnly = (boolean) objectHolder.getAndRemoveObject(key);

            // set values
            TextView tv = this.findViewById(R.id.credentialSubjectIDValue);
            tv.setText(credentialMessage.getSubjectID());

            tv = this.findViewById(R.id.credentialSubjectNameValue);
            tv.setText(credentialMessage.getSubjectName());

            tv = this.findViewById(R.id.credentialValidSinceValue);
            tv.setText(DateTimeHelper.long2DateString(credentialMessage.getValidSince()));

            tv = this.findViewById(R.id.credentialRandomNumberValue);
            tv.setText(Integer.toString(credentialMessage.getRandomInt()));

            tv = this.findViewById(R.id.credentialHashPublicKeyValue);
            tv.setText("TODO create a hash from key");

            Button button = this.findViewById(R.id.actionButton);
            if(this.viewOnly) button.setText("Dismiss");
            else button.setText("Approve - I certify");

        } catch (SharkException e) {
            Log.e(this.getLogStart(),
                    "fatal: cannot read data from intent: " + e.getLocalizedMessage());
            Toast.makeText(this, "fatal: cannot read data from intent: "
                            + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            this.finish();
        }
    }

    public void onClick(View view) {
        if(viewOnly) {
            this.finish();
            return;
        }

        try {
            this.getSharkNetApp().getSharkPKI().acceptAndSignCredential(this.credentialMessage);
        } catch (IOException | ASAPSecurityException e) {
            String s = "fatal: could not add certificate: " + e.getLocalizedMessage();
            Log.e(this.getLogStart(), s);
            Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
        }

    }
}
