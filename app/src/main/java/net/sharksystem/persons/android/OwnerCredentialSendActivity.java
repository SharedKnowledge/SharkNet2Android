package net.sharksystem.persons.android;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import net.sharksystem.R;
import net.sharksystem.sharknet.android.SharkNetActivity;
import net.sharksystem.sharknet.android.SharkNetApp;

public class OwnerCredentialSendActivity extends SharkNetActivity {
    public OwnerCredentialSendActivity() {
        super(SharkNetApp.getSharkNetApp());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.owner_credential_send_drawer_layout);
        this.getSharkNetApp().setupDrawerLayout(this);

        // send credentials
        int controlNumber = OwnerApp.getOwnerApp().sendCredentials();

        // set user name in layout
        TextView tv = this.findViewById(R.id.ownerDisplayName);
        tv.setText(OwnerStorageAndroid.getIdentityStorage(this).getDisplayName());

        // set control number
        tv = this.findViewById(R.id.ownerSendCredentialsControlNumber);
        tv.setText(String.valueOf(controlNumber));
    }

    public void onDoneClick(View v) {
        this.finish();
    }
}
