package net.sharksystem.key_administration.fragments.certifications;

import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.sharksystem.R;
import net.sharksystem.android.util.Constants;
import net.sharksystem.android.util.NfcChecks;
import net.sharksystem.identity.android.IdentityStorageAndroid;
import net.sharksystem.identity.android.SharkIdentityStorage;
import net.sharksystem.key_administration.fragments.publicKey.ReceiveKeyPojo;
import net.sharksystem.nfc.NfcMessageManager;
import net.sharksystem.storage.SharedPreferencesHandler;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.ArrayList;

import static net.sharksystem.android.util.SerializationHelper.objToByte;

public class DetailViewCertificationActivity extends AppCompatActivity {

    private Button sendCertBtn;
    private final String TAG = this.getClass().getName();
    private SharkIdentityStorage storage;
    private NfcAdapter nfcAdapter = null;
    private SharedPreferencesHandler sharedPreferencesHandler;
    private int itemPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_view_certification);

        // NFC setup
        this.nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        NfcChecks.preliminaryNfcChecks(nfcAdapter, this);

        storage = IdentityStorageAndroid.getIdentityStorage(this.getApplicationContext());
        sharedPreferencesHandler = new SharedPreferencesHandler(this.getApplicationContext());

        getItemPos(savedInstanceState);

        sendCertBtn = findViewById(R.id.sendCertificate);
        sendCertBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    setPushMessage();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setPushMessage() throws IOException {
        String keyListJson = sharedPreferencesHandler.getValue(Constants.KEY_LIST);

        if (keyListJson != null) {
            Gson gson = new Gson();
            Type keyListType = new TypeToken<ArrayList<ReceiveKeyPojo>>() {
            }.getType();
            ArrayList<ReceiveKeyPojo> keyList = gson.fromJson(keyListJson, keyListType);

            ReceiveKeyPojo receiveKeyPojo = keyList.get(itemPos);

            initNfcMessageManager(objToByte(receiveKeyPojo));

        }
    }

    private void initNfcMessageManager(byte[] dataToSend) {
        if (dataToSend != null) {
            NfcMessageManager outcomingNfcCallback = new NfcMessageManager("application/net.sharksystem.send.certificates".getBytes(Charset.forName("US-ASCII")), dataToSend);
            this.nfcAdapter.setOnNdefPushCompleteCallback(outcomingNfcCallback, this);
            this.nfcAdapter.setNdefPushMessageCallback(outcomingNfcCallback, this);
            this.nfcAdapter.invokeBeam(this);
        } else {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
        }
    }

    private void getItemPos(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                this.itemPos = 0;
            } else {
                this.itemPos = extras.getInt("ITEM_POS");
            }
        } else {
            this.itemPos = (int) savedInstanceState.getSerializable("ITEM_POS");
        }
    }
}
