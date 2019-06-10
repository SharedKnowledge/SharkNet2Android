package net.sharksystem.nfc.receive;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Parcelable;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import net.sharksystem.R;
import net.sharksystem.android.util.NfcChecks;

import java.util.ArrayList;

public class ReceivePublicKeyActivity extends AppCompatActivity {

    private NfcAdapter nfcAdapter = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_public_key);


        this.nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        NfcChecks.preliminaryNfcChecks(nfcAdapter,this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (nfcAdapter != null) {
            enableForegroundDispatch();
        }
        processIntent(this.getIntent());
    }

    @Override
    protected void onPause() {
        super.onPause();
        disableForeground();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        processIntent(intent);
    }

    // enableForegroundDispatch gives your current foreground activity priority in receiving NFC events over all other actvities.
    private void enableForegroundDispatch() {
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
                getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null);
    }

    private void disableForeground() {
        if (this.nfcAdapter != null) {
            nfcAdapter.disableForegroundDispatch(this);
        }
    }


    private void processIntent(Intent intent) {

//        if (intent.getAction() != null) {
//            boolean equals = intent.getAction().equals(NfcAdapter.ACTION_NDEF_DISCOVERED);
//            if (equals) {
//                Parcelable[] rawMsgs = intent.getParcelableArrayExtra(
//                        NfcAdapter.EXTRA_NDEF_MESSAGES);
//                NdefMessage msg = (NdefMessage) rawMsgs[0];
//                processNdefMessages(msg);
//            }
//        }

        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            Parcelable[] rawMessages = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);

            if (rawMessages != null) {
                NdefMessage[] messages = new NdefMessage[rawMessages.length];
                for (int i = 0; i < rawMessages.length; i++) {
                    messages[i] = (NdefMessage) rawMessages[i];
                }

                // Process the messages array.
            }
        }
    }

//    private void processNdefMessages(NdefMessage msg) {
//
//        String message = getKeyFromNdefMessage(msg);
//        showAlert(message);
//
//    }
//
//    private String getKeyFromNdefMessage(NdefMessage msg) {
//        String result = "";
//        if (msg != null) {
//            if (msg.getRecords() != null) {
//                for (NdefRecord records : msg.getRecords()) {
//                    result += new String(records.getPayload());
//                }
//            }
//        }
//        return result;
//    }
//
//    private void showAlert(String msg) {
//        PublicKeyPackage publicKeyPackage = gson.fromJson(msg, PublicKeyPackage.class);
//        PublicKeyRepository publicKeyRepository = new PublicKeyRepository(this.getApplication());
//        ArrayList<PublicKeyPackage> allPublicKeys = publicKeyRepository.getAllPublicKeys();
//        boolean duplicate = false;
//
//        for (PublicKeyPackage key : allPublicKeys) {
//            if (key.getPublicKeyInBase64().equals(publicKeyPackage.getPublicKeyInBase64())) {
//                duplicate = true;
//                break;
//            }
//        }
//
//
//        if (!duplicate) {
//
//            new AlertDialog.Builder(this)
//                    .setTitle("Are you sure you want to save this Key?")
//                    .setMessage("Owner: " + publicKeyPackage.getPublicKeyOwnerAlias() + "\n"
//                            + " UUID: " + publicKeyPackage.getPublicKeyOwnerUUID())
//                    .setCancelable(false)
//                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface arg0, int arg1) {
//                            publicKeyRepository.insertPublicKey(publicKeyPackage);
//                            finish();
//                        }
//
//                    })
//                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            finish();
//                        }
//                    }).create().show();
//        } else {
//            Toast.makeText(this, "Key already in Database", Toast.LENGTH_SHORT).show();
//            finish();
//        }
//    }
}
