package net.sharksystem.pki.android;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import net.sharksystem.R;
import net.sharksystem.sharknet.android.SharkNetActivity;

public class CredentialExchangeActivity extends SharkNetActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.credential_exchange_drawer_layout);

        this.getSharkNetApp().setupDrawerLayout(this);
    }

    public void onSendCredentialsClick(View view) {
        Toast.makeText(this, "send credentials - TODO", Toast.LENGTH_SHORT).show();
    }

    public void onReceiveCredentialsClick(View view) {
        Toast.makeText(this, "receive credentials - TODO", Toast.LENGTH_SHORT).show();
    }

}
