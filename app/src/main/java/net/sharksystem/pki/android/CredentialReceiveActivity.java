package net.sharksystem.pki.android;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import net.sharksystem.R;
import net.sharksystem.asap.ASAPMessages;
import net.sharksystem.pki.CredentialMessage;
import net.sharksystem.pki.SharkCredentialReceivedListener;
import net.sharksystem.sharknet.android.SharkNetActivity;

import java.util.Iterator;

public class CredentialReceiveActivity extends SharkNetActivity
        implements SharkCredentialReceivedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.person_wait_for_credential_layout);

        this.getSharkNetApp().getSharkPKI().setSharkCredentialReceivedListener(this);

        Log.d(getLogStart(), "credential message Listener registered");
    }

    public void onAbortClick(View v) {
        this.finish();
    }

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

