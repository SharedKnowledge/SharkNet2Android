package net.sharksystem.pki.android;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import net.sharksystem.R;
import net.sharksystem.SharkException;
import net.sharksystem.android.util.ObjectHolder;
import net.sharksystem.pki.CredentialMessage;
import net.sharksystem.sharknet.android.SharkNetActivity;

import java.io.IOException;

public class CredentialExchangeActivity extends SharkNetActivity {
    private static final String CREDENTIAL_MESSAGE_KEY = "credentialMessageKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.credential_exchange_drawer_layout);

        this.getSharkNetApp().setupDrawerLayout(this);
    }

    public void onSendCredentialsClick(View view) {
        try {
            CredentialMessage credentialMessage =
                    this.getSharkNetApp().getSharkPKI().createCredentialMessage();

            /**
             * tag (defined by ViewActivity) -> key (defined here); key -> value (actual value)
             */

            // credential message stored under a key
            ObjectHolder.getObjectHolder().setObject(
                    CREDENTIAL_MESSAGE_KEY, credentialMessage);

            // key can be found with that taG
            ObjectHolder.getObjectHolder().setObject(
                    CredentialViewActivity.CREDENTIAL_MESSAGE_KEY_TAG, CREDENTIAL_MESSAGE_KEY);

            // view only
            String key = CredentialViewActivity.CREDENTIAL_VIEW_ONLY_TAG
                    + Long.toString(System.currentTimeMillis());

            ObjectHolder.getObjectHolder().setObject(
                    key, Boolean.TRUE);

            ObjectHolder.getObjectHolder().setObject(
                    CredentialViewActivity.CREDENTIAL_VIEW_ONLY_TAG, key);

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
        Toast.makeText(this, "receive credentials - TODO", Toast.LENGTH_SHORT).show();
    }

}
