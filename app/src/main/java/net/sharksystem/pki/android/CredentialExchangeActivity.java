package net.sharksystem.pki.android;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import net.sharksystem.R;
import net.sharksystem.SharkException;
import net.sharksystem.android.util.ObjectHolder;
import net.sharksystem.pki.CredentialMessage;
import net.sharksystem.sharknet.android.SharkNetActivity;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class CredentialExchangeActivity extends SharkNetActivity {
    private static final String CREDENTIAL_MESSAGE_KEY = "credentialMessageKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.credential_exchange_drawer_layout);

        this.getSharkNetApp().setupDrawerLayout(this);
    }

    static void addCredentialMessageToObjectHolder(CredentialMessage credentialMessage,
                                                   boolean viewOnly) {
        /*
         * tag (defined by ViewActivity) -> key (defined here); key -> value (actual value)
         */

        ObjectHolder objectHolder = ObjectHolder.getObjectHolder();
        // credential message stored under a key
        objectHolder.setObject(CredentialViewActivity.CREDENTIAL_MESSAGE_TAG, credentialMessage);

        // behaviour
        objectHolder.setObject(CredentialViewActivity.CREDENTIAL_VIEW_ONLY_TAG, Boolean.TRUE);
    }

    public void onSendCredentialsClick(View view) {
        EditText cicEditText = findViewById(R.id.editTextCic);
        byte[] cic = cicEditText.getText().toString().getBytes(StandardCharsets.UTF_8);

        try {
            CredentialMessage credentialMessage =
                    this.getSharkNetApp().getSharkPKI().createCredentialMessage(cic);

            CredentialExchangeActivity.addCredentialMessageToObjectHolder(credentialMessage, true);

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

    public void onReceiveCredentialsClick(View view) {
        Intent intent = new Intent(this, CredentialReceiveActivity.class);
        this.startActivity(intent);
        this.finish();
    }
}
