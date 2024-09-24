package net.sharksystem.sharknet.android;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import net.sharksystem.R;
import net.sharksystem.asap.ASAPException;
import net.sharksystem.asap.ASAPSecurityException;
import net.sharksystem.pki.CredentialMessage;
import net.sharksystem.pki.android.SharkPKIReceivedCredentialMessageHandler;

import java.io.IOException;

public class SNTestActivity extends SharkNetActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sntest2);
    }

    public void onClick(View view) {

        // create test credential message
        try {
            SharkNetApp.getSharkNetApp().getSharkPKI().sendTransientCredentialMessage();

            SharkPKIReceivedCredentialMessageHandler receivedCredentialListener =
                    SharkNetApp.getSharkNetApp().getReceivedCredentialListener();

            Log.d(this.getLogStart(), "credential sent but nothing else - TODO");

            // receivedCredentialListener.credentialReceived(credentialMessage);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}