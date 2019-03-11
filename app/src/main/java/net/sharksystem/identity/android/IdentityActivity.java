package net.sharksystem.identity.android;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import net.sharksystem.R;
import net.sharksystem.SharkException;
import net.sharksystem.sharknet.android.SharkNetApp;

import java.util.Date;

public class IdentityActivity extends AppCompatActivity {
    private static final String LOGSTART = "IdentityActivity";

    private Activity thisActivity;

    public IdentityActivity() {
        this.thisActivity = this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.identity_drawer_layout);

        // set user name in layout
        EditText userNameView = this.findViewById(R.id.identityUserName);

        userNameView.setText(IdentityStorageAndroid.getIdentityStorage(this).getOwnerName());

        SharkNetApp.getSharkNetApp(this).setupDrawerLayout(this);
    }

    protected void onPause() {
        super.onPause();
        Log.d(LOGSTART, "onPause");
//        SharkNetApp.getSharkNetApp(thisActivity).unbindServices();
    }

    protected void onDestroy() {
        super.onDestroy();
        Log.d(LOGSTART, "onDestroy");
//        SharkNetApp.getSharkNetApp(thisActivity).unbindServices();
    }

    public void onChangeClick(View view) throws SharkException {
        EditText userNameView = (EditText) findViewById(R.id.identityUserName);

        String userNameString = userNameView.getText().toString();

        if(userNameString == null || userNameString.isEmpty()) {
            Toast.makeText(this, "user name is", Toast.LENGTH_SHORT).show();
        } else {
            Log.d(LOGSTART, "set new user name: " + userNameString);
            IdentityStorageAndroid.getIdentityStorage(this).setOwnerName(userNameString);
            super.onBackPressed();
        }
    }

    public void onAbortClick(View view) {
        // go back to previous activity
        super.onBackPressed();
    }
}
