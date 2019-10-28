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
import net.sharksystem.key_administration.fragments.certifications.ReceiveCertificationPojo;
import net.sharksystem.key_administration.fragments.publicKey.ReceiveKeyPojo;
import net.sharksystem.storage.SharedPreferencesHandler;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import static net.sharksystem.android.util.SerializationHelper.byteToObj;

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
                    Log.d(TAG, "processIntent: Something went wrong");
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

    private void persistData(ReceiveKeyPojo receivedData) {
        Gson gson = new Gson();
        Type keyListType = new TypeToken<ArrayList<ReceiveKeyPojo>>() {
        }.getType();

        String keyListJson = sharedPreferencesHandler.getValue(Constants.KEY_LIST);
        ArrayList<ReceiveKeyPojo> keyList = gson.fromJson(keyListJson, keyListType);

        if (keyList == null) {
            ArrayList<ReceiveKeyPojo> initialKeyList = new ArrayList<>();
            initialKeyList.add(receivedData);
            String newKeyListJson = gson.toJson(initialKeyList);
            sharedPreferencesHandler.writeValue(Constants.KEY_LIST, newKeyListJson);
        } else {
            if (!keyList.contains(receivedData)) {
                keyList.add(receivedData);
                String newKeyListJson = gson.toJson(keyList);
                sharedPreferencesHandler.writeValue(Constants.KEY_LIST, newKeyListJson);
            }
        }

    }

    private void showAlert(ReceiveKeyPojo receivedData) {


        if (receivedData != null) {

            new AlertDialog.Builder(this)
                    .setTitle("Are you sure you want to save this Key?")
                    .setMessage(receivedData.getAlias())
                    .setCancelable(false)
                    .setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> {
                        persistData(receivedData);
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


    private void processSendCertificates(Intent intent) {
        // Todo check in Keylist if public already exist in direct trust, if so, check signer, if signer is not the same at to signers listc


        Parcelable[] rawMessages = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        ReceiveCertificationPojo receiveData = null;

        if (rawMessages != null) {
            NdefMessage[] messages = new NdefMessage[rawMessages.length];
            for (int i = 0; i < rawMessages.length; i++) {
                messages[i] = (NdefMessage) rawMessages[i];
            }

            byte[] receiveDataPayload = messages[0].getRecords()[0].getPayload();

            try {
                receiveData = (ReceiveCertificationPojo) byteToObj(receiveDataPayload);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            Toast.makeText(this, "beam successful", Toast.LENGTH_LONG).show();
            showAlertCert(receiveData);
        }

    }

    public void persistCertificate(ReceiveCertificationPojo receivedData) {
        Gson gson = new Gson();
        Type certificateListType = new TypeToken<ArrayList<ReceiveCertificationPojo>>() {
        }.getType();

        String certificateListJson = sharedPreferencesHandler.getValue(Constants.CERTIFICATE_LIST);
        ArrayList<ReceiveCertificationPojo> certificateList = gson.fromJson(certificateListJson, certificateListType);

        if (certificateList == null) {
            ArrayList<ReceiveCertificationPojo> initialCertificateList = new ArrayList<>();
            initialCertificateList.add(receivedData);
            String newKeyListJson = gson.toJson(initialCertificateList);
            sharedPreferencesHandler.writeValue(Constants.CERTIFICATE_LIST, newKeyListJson);
        } else {
            if (!certificateList.contains(receivedData)) {
                certificateList.add(receivedData);
                String newKeyListJson = gson.toJson(certificateList);
                sharedPreferencesHandler.writeValue(Constants.CERTIFICATE_LIST, newKeyListJson);
            }
        }
    }

    private void showAlertCert(ReceiveCertificationPojo receiveCertificationPojo) {


        if (receiveCertificationPojo != null) {

            new AlertDialog.Builder(this)
                    .setTitle("Are you sure you want to save this Key?")
                    .setMessage(receiveCertificationPojo.getAlias())
                    .setCancelable(false)
                    .setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> {
                        persistCertificate(receiveCertificationPojo);
                        finish();
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    }).create().show();
        } else {
            Toast.makeText(this, "Cert already in Database", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

}
