package net.sharksystem.pki.android;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import net.sharksystem.R;
import net.sharksystem.pki.CredentialMessage;
import net.sharksystem.pki.SharkCredentialReceivedListener;
import net.sharksystem.sharknet.android.SharkNetActivity;

/**
 * Activity for receiving credentials
 */
public class CredentialReceiveActivity extends SharkNetActivity
        implements SharkCredentialReceivedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.person_wait_for_credential_layout);

        this.getSharkNetApp().getSharkPKI().setSharkCredentialReceivedListener(this);

        Log.d(getLogStart(), "credential message Listener registered");
    }

    /**
     * Called when the users cancels the receiving process
     */
    public void onAbortClick(View v) {
        this.finish();
    }

    /**
     * Called when the credentials were received
     */
    @Override
    public void credentialReceived(CredentialMessage credentialMessage) {
        CredentialExchangeActivity.addCredentialMessageToObjectHolder(credentialMessage, false);
        // TODO shouldn't it be possible to remove the listener
        //  since this is a manual process of receiving credentials?
        Intent intent = new Intent(this, CredentialViewActivity.class);
        this.startActivity(intent);
        this.finish();
    }
}

