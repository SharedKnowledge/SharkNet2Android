package net.sharksystem.identity.android;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import net.sharksystem.R;
import net.sharksystem.sharknet.android.SharkNetApp;
import net.sharksystem.storage.Storage;
import net.sharksystem.storage.keystore.RSAKeystoreHandler;

public class IdentityActivity extends AppCompatActivity {
    private static final String LOGSTART = "IdentityActivity";

    private Activity thisActivity;

    private Storage storage;
    private RSAKeystoreHandler keystore;


    public IdentityActivity() {
        this.thisActivity = this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.identity_drawer_layout);

        // set user name in layout
//        EditText userNameView = this.findViewById(R.id.identityUserName);

//        userNameView.setText(IdentityStorageAndroid.getIdentityStorage(this).getOwnerName());

        SharkNetApp.getSharkNetApp(this).setupDrawerLayout(this);

        initStorages();
        initViews();
    }

    private void initViews() {
        TextView textViewPublicKey = findViewById(R.id.textView_public_key);
        final TextView textViewPrivateKey = findViewById(R.id.textView_private_key);
        TextView textViewUuid = findViewById(R.id.textView_uuid);
        TextView textViewAlias = findViewById(R.id.textView_alias_identity_activity);

        String publicKeyEncodedToString = Base64.encodeToString(keystore.getPublicKey().getEncoded(), Base64.DEFAULT);
//        String privateKeyEncodedToStringString = Base64.encodeToString(keystore.getPrivateKey().getEncoded(), Base64.DEFAULT);

        textViewPublicKey.setText(publicKeyEncodedToString);
//        textViewPrivateKey.setText(privateKeyEncodedToStringString);
        textViewUuid.setText(storage.getUUID());
        textViewAlias.setText(storage.getAlias());

        ImageButton copyToClipboard = findViewById(R.id.imageButton_copy_icon);

        copyToClipboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Copied Text", textViewPrivateKey.getText().toString());
                assert clipboard != null;
                clipboard.setPrimaryClip(clip);
                Toast.makeText(IdentityActivity.this, "Copy to clickboard!", Toast.LENGTH_SHORT).show();
            }
        });
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

    private void initStorages() {
        keystore = RSAKeystoreHandler.getInstance();
        storage = Storage.getInstance(this.getApplicationContext());
    }
//
//    public void onChangeClick(View view) throws SharkException {
//        EditText userNameView = (EditText) findViewById(R.id.identityUserName);
//
//        String userNameString = userNameView.getText().toString();
//
//        if(userNameString == null || userNameString.isEmpty()) {
//            Toast.makeText(this, "user name is", Toast.LENGTH_SHORT).show();
//        } else {
//            Log.d(LOGSTART, "set new user name: " + userNameString);
//            SharkIdentityStorage identityStorage = IdentityStorageAndroid.getIdentityStorage(this);
//            identityStorage.setOwnerName(userNameString);
//            identityStorage.setNewOwnerUUID(userNameString);
//            super.onBackPressed();
//        }
//    }

    public void onAbortClick(View view) {
        // go back to previous activity
        super.onBackPressed();
    }
}
