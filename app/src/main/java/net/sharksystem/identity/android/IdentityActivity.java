package net.sharksystem.identity.android;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import net.sharksystem.R;
import net.sharksystem.android.util.NfcChecks;
import net.sharksystem.nfc.receive.ReceivePublicKeyActivity;
import net.sharksystem.nfc.send.SendPublicKeyActivity;
import net.sharksystem.sharknet.android.SharkNetApp;
import net.sharksystem.storage.keystore.RSAKeystoreHandler;

public class IdentityActivity extends AppCompatActivity {
    private static final String LOGSTART = "IdentityActivity";

    private Activity thisActivity;

    private SharkIdentityStorage storage;
    private RSAKeystoreHandler keystore;

    private TextView textViewAlias;
    private TextView textViewPublicKey;
    private TextView textViewUuid;

    private NfcAdapter nfcAdapter = null;


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

        this.nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        NfcChecks.preliminaryNfcChecks(nfcAdapter, this);

        initStorages();
        initViews();

    }

    private void initViews() {
        // Todo trenne die onClick function
        textViewPublicKey = findViewById(R.id.textView_public_key);
        textViewUuid = findViewById(R.id.textView_uuid);
        textViewAlias = findViewById(R.id.textView_alias_identity_activity);

        String publicKeyEncodedToString = Base64.encodeToString(keystore.getPublicKey().getEncoded(), Base64.DEFAULT);

        textViewPublicKey.setText(publicKeyEncodedToString.substring(24));
        textViewUuid.setText(storage.getOwnerID());
        textViewAlias.setText(storage.getOwnerName());

        ImageButton resetKeypairImageButton = findViewById(R.id.imageButton_reset_public_key);
        ImageButton editKAliasImageButton = findViewById(R.id.imageButton_edit_alias);


        resetKeypairImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                areYouSureDialog();
            }
        });
        editKAliasImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeAliasDialog();
            }
        });

        //set onClick for send public key button
        ImageButton sendPublicKeyButton = findViewById(R.id.imageButton_send_public_key);
        sendPublicKeyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SendPublicKeyActivity.class);
                startActivity(intent);
            }
        });
    }


    // Todo Dialog class impl
    private void changeAliasDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View viewInflated = inflater.inflate(R.layout.dialog_change_alias, null);

        final EditText aliasInput = viewInflated.findViewById(R.id.inputChangeAlias);

        builder.setTitle("Set your Alias");
        builder.setView(viewInflated);
        builder.setCancelable(false);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!aliasInput.getText().toString().equals("")) {
                    dialog.dismiss();
                    storage.setOwnerName(aliasInput.getText().toString());
                    textViewAlias.setText(storage.getOwnerName());
                } else {
                    changeAliasDialog();
                }
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    private void areYouSureDialog() {

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        keystore.resetKeystore();

                        String publicKeyEncodedToString = Base64.encodeToString(keystore.getPublicKey().getEncoded(), Base64.DEFAULT);
                        textViewPublicKey.setText(publicKeyEncodedToString.substring(24));
                        textViewUuid.setText(storage.getOwnerID());
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        dialog.dismiss();
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to reset?")
                .setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
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
        storage = IdentityStorageAndroid.getIdentityStorage(this.getApplicationContext());
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
