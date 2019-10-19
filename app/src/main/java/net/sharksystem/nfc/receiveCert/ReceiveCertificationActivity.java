package net.sharksystem.nfc.receiveCert;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.sharksystem.R;
import net.sharksystem.android.util.Constants;
import net.sharksystem.android.util.NfcChecks;
import net.sharksystem.key_administration.fragments.certifications.ReceiveCertificationPojo;
import net.sharksystem.key_administration.fragments.publicKey.ReceiveKeyPojo;
import net.sharksystem.storage.SharedPreferencesHandler;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import static net.sharksystem.android.util.SerializationHelper.byteToObj;

public class ReceiveCertificationActivity extends AppCompatActivity {

    private NfcAdapter nfcAdapter = null;
    private final String TAG = this.getClass().getName();
    private ProgressBar progressBar;
    private SharedPreferencesHandler sharedPreferencesHandler;

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
            Parcelable[] rawMessages = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            ReceiveCertificationPojo receiveCertificationPojo = null;

            if (rawMessages != null) {
                NdefMessage[] messages = new NdefMessage[rawMessages.length];
                for (int i = 0; i < rawMessages.length; i++) {
                    messages[i] = (NdefMessage) rawMessages[i];
                }

                byte[] receiveDataPayload = messages[0].getRecords()[0].getPayload();

                try {
                    receiveCertificationPojo = (ReceiveCertificationPojo) byteToObj(receiveDataPayload);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

                Toast.makeText(this, "beam successful", Toast.LENGTH_LONG).show();
                showAlert(receiveCertificationPojo);
            }
        }
    }

    private void persistData(ReceiveCertificationPojo receiveCertificationPojo) {
        Gson gson = new Gson();
        Type keyListType = new TypeToken<ArrayList<ReceiveKeyPojo>>() {}.getType();

        String keyListJson = sharedPreferencesHandler.getValue(Constants.KEY_LIST);
        ArrayList<ReceiveCertificationPojo> keyList = gson.fromJson(keyListJson, keyListType);

        if (keyList == null) {
            ArrayList<ReceiveCertificationPojo> initialKeyList = new ArrayList<>();
            initialKeyList.add(receiveCertificationPojo);
            String newKeyListJson = gson.toJson(initialKeyList);
            sharedPreferencesHandler.writeValue(Constants.KEY_LIST, newKeyListJson);
        } else {
            if(!keyList.contains(receiveCertificationPojo)) {
                keyList.add(receiveCertificationPojo);
                String newKeyListJson = gson.toJson(keyList);
                sharedPreferencesHandler.writeValue(Constants.KEY_LIST, newKeyListJson);
            }
        }

    }

    private void showAlert(ReceiveCertificationPojo receiveCertificationPojo) {


        if (receiveCertificationPojo != null) {

            new AlertDialog.Builder(this)
                    .setTitle("Are you sure you want to save this Key?")
                    .setMessage(receiveCertificationPojo.getAlias())
                    .setCancelable(false)
                    .setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> {
                        persistData(receiveCertificationPojo);
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
