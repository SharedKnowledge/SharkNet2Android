package net.sharksystem.persons.android;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import net.sharksystem.R;
import net.sharksystem.SharkException;
import net.sharksystem.persons.Owner;
import net.sharksystem.sharknet.android.SharkNetActivity;
import net.sharksystem.sharknet.android.SharkNetApp;

public class OwnerActivity extends SharkNetActivity {
    public OwnerActivity() {
        super(SharkNetApp.getSharkNetApp());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.owner_drawer_layout);

        // set user name in layout
        EditText userNameView = this.findViewById(R.id.ownerDisplayName);

        userNameView.setText(OwnerStorageAndroid.getIdentityStorage(this).getDisplayName());

        this.getSharkNetApp().setupDrawerLayout(this);
    }

    public void onSendCredentials(View view) {
        this.finish();
        this.startActivity(new Intent(this, OwnerCredentialSendActivity.class));
    }

    public void onChangeClick(View view) throws SharkException {
        EditText userNameView = (EditText) findViewById(R.id.ownerDisplayName);

        String userNameString = userNameView.getText().toString();

        if(userNameString == null || userNameString.isEmpty()) {
            Toast.makeText(this, "user name is", Toast.LENGTH_SHORT).show();
        } else {
            Log.d(this.getLogStart(), "set new user name: " + userNameString);
            Owner identityStorage = OwnerStorageAndroid.getIdentityStorage(this);
            identityStorage.setDisplayName(userNameString);
            super.onBackPressed();
        }
    }

    public void onAbortClick(View view) {
        // go back to previous activity
        super.onBackPressed();
    }
}
