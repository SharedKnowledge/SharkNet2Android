package net.sharksystem.nfc.receive;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.sharksystem.R;
import net.sharksystem.android.util.Constants;
import net.sharksystem.android.util.NfcChecks;
import net.sharksystem.key_administration.fragments.publicKey.ReceiveKeyPojo;
import net.sharksystem.storage.SharedPreferencesHandler;

import static net.sharksystem.android.util.SerializationHelper.byteToObj;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class ReceiveNFCDataActivity extends AppCompatActivity {

    private NfcAdapter nfcAdapter = null;
    private final String TAG = this.getClass().getName();
    private ProgressBar progressBar;
    private SharedPreferencesHandler sharedPreferencesHandler;

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

        progressBar = findViewById(R.id.receivePublicKeyProgressBar);
        progressBar.setVisibility(ProgressBar.VISIBLE);

        sharedPreferencesHandler = new SharedPreferencesHandler(this.getApplicationContext());
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

        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {

            switch (intent.getType()) {
                case "application/net.sharksystem.send.public.key":
                    processSendPublic(intent);
                    break;
                case "application/net.sharksystem.send.certificates":
                    processSendCertificates(intent);
                    break;
                default:
                    Log.d(TAG, "processIntent: Something went wrong in Receive NFC Data");
            }
        }
    }

    private void processSendPublic(Intent intent) {
        Parcelable[] rawMessages = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        ReceiveKeyPojo receiveData = null;

        if (rawMessages != null) {
            NdefMessage[] messages = new NdefMessage[rawMessages.length];
            for (int i = 0; i < rawMessages.length; i++) {
                messages[i] = (NdefMessage) rawMessages[i];
            }

            byte[] receiveDataPayload = messages[0].getRecords()[0].getPayload();

            try {
                receiveData = (ReceiveKeyPojo) byteToObj(receiveDataPayload);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            Toast.makeText(this, "beam successful", Toast.LENGTH_LONG).show();
            showAlert(receiveData);
        }
    }

    private void processSendCertificates(Intent intent) {

    }

    private void persistData(ReceiveKeyPojo receiveData) {
        Gson gson = new Gson();
        Type keyListType = new TypeToken<ArrayList<ReceiveKeyPojo>>() {
        }.getType();

        String keyListJson = sharedPreferencesHandler.getValue(Constants.KEY_LIST);
        ArrayList<ReceiveKeyPojo> keyList = gson.fromJson(keyListJson, keyListType);

        if (keyList == null) {
            ArrayList<ReceiveKeyPojo> initialKeyList = new ArrayList<>();
            initialKeyList.add(receiveData);
            String newKeyListJson = gson.toJson(initialKeyList);
            sharedPreferencesHandler.writeValue(Constants.KEY_LIST, newKeyListJson);
        } else {
            if (!keyList.contains(receiveData)) {
                keyList.add(receiveData);
                String newKeyListJson = gson.toJson(keyList);
                sharedPreferencesHandler.writeValue(Constants.KEY_LIST, newKeyListJson);
            }
        }

    }

    private void showAlert(ReceiveKeyPojo receiveData) {


        if (receiveData != null) {

            new AlertDialog.Builder(this)
                    .setTitle("Are you sure you want to save this Key?")
                    .setMessage(receiveData.getAlias())
                    .setCancelable(false)
                    .setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> {
                        persistData(receiveData);
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
