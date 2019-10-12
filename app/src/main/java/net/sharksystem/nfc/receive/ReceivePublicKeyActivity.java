package net.sharksystem.nfc.receive;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import net.sharksystem.R;
import net.sharksystem.android.util.NfcChecks;
import static net.sharksystem.android.util.SerializationHelper.byteToObj;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.cert.Certificate;

public class ReceivePublicKeyActivity extends AppCompatActivity {

    private NfcAdapter nfcAdapter = null;
    private final String TAG = this.getClass().getName();


    // Todo https://developer.android.com/guide/navigation/navigation-getting-started navigation editor einrichten und in der Arbeit beschreiben
    // Todo design pattern und best practise https://developer.android.com/jetpack/docs/guide & https://medium.com/@pszklarska/android-design-patterns-in-practice-builder-6b044f83e6e9
    // Todo eventuell in verbindung mit nfc https://developer.android.com/things/training/first-device
    // Todo user interface testing https://developer.android.com/training/testing/ui-testing
    // Todo Orginize layout files: https://stackoverflow.com/questions/4930398/can-the-android-layout-folder-contain-subfolders
    // Todo fuer die BA https://www.google.com/search?q=ipfs&oq=ipfs&aqs=chrome..69i57j69i60j0l4.1049j0j7&sourceid=chrome&ie=UTF-8
    // Todo https://macwright.org/2019/06/08/ipfs-again.html
    // Todo https://github.com/pingcap/talent-plan

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_public_key);


        this.nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        NfcChecks.preliminaryNfcChecks(nfcAdapter, this);
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

    // Todo beschreiben in der Arbeit: https://stackoverflow.com/questions/26943935/what-does-enableforegrounddispatch-and-disableforegrounddispatch-do
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
            Certificate certificate = null;

            if (rawMessages != null) {
                NdefMessage[] messages = new NdefMessage[rawMessages.length];
                for (int i = 0; i < rawMessages.length; i++) {
                    messages[i] = (NdefMessage) rawMessages[i];
                }

                Log.d(TAG, "messages: " + messages[0] + messages.length);
                Log.d(TAG, "getRecord: " + messages[0].getRecords());
                byte[] payload = messages[0].getRecords()[0].getPayload();
                try {
                    certificate = (Certificate) byteToObj(payload);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

                Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

                String certificateToString = (certificate != null) ? certificate.toString() : "cert is null";

                Log.d(TAG, "Tag: " + tag.toString());
                Log.d(TAG, "Tag: " + certificateToString);

                Toast.makeText(this, "beam successfull", Toast.LENGTH_LONG).show();
                showAlert();


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
    private void showAlert() {
//        PublicKeyPackage publicKeyPackage = gson.fromJson(msg, PublicKeyPackage.class);
//        PublicKeyRepository publicKeyRepository = new PublicKeyRepository(this.getApplication());
//        ArrayList<PublicKeyPackage> allPublicKeys = publicKeyRepository.getAllPublicKeys();
//        boolean duplicate = false;

//        for (PublicKeyPackage key : allPublicKeys) {
//            if (key.getPublicKeyInBase64().equals(publicKeyPackage.getPublicKeyInBase64())) {
//                duplicate = true;
//                break;
//            }
//        }


        if (true) {

            new AlertDialog.Builder(this)
                    .setTitle("Are you sure you want to save this Key?")
                    .setMessage("Owner: " + "Me :D" + "\n" + " UUID: 1234567")
                    .setCancelable(false)
                    .setPositiveButton("Yes", (DialogInterface.OnClickListener) (arg0, arg1) -> {
//                        publicKeyRepository.insertPublicKey(publicKeyPackage);
                        finish();
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    }).create().show();
        } else {
            Toast.makeText(this, "Key already in Database", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
