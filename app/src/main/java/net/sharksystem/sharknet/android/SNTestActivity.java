package net.sharksystem.sharknet.android;

import android.os.Bundle;
import android.view.View;

import net.sharksystem.R;
import net.sharksystem.asap.ASAPSecurityException;
import net.sharksystem.pki.CredentialMessage;
import net.sharksystem.pki.android.SharkPKIReceivedCredentialMessageHandler;

public class SNTestActivity extends SharkNetActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sntest2);
    }

    public void onClick(View view) {

        // create test credential message
        try {
            CredentialMessage credentialMessage =
                    SharkNetApp.getSharkNetApp().getSharkPKI().createCredentialMessage();

            SharkPKIReceivedCredentialMessageHandler receivedCredentialListener =
                    SharkNetApp.getSharkNetApp().getReceivedCredentialListener();

            receivedCredentialListener.credentialReceived(credentialMessage);
        } catch (ASAPSecurityException e) {
            e.printStackTrace();
        }
    }
}