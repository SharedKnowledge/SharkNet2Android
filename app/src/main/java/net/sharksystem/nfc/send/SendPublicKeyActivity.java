package net.sharksystem.nfc.send;

import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import net.sharksystem.R;
import net.sharksystem.android.util.NfcChecks;
import net.sharksystem.identity.android.IdentityStorageAndroid;
import net.sharksystem.identity.android.SharkIdentityStorage;
import net.sharksystem.key_administration.fragments.ReceiveKeyPojo;
import net.sharksystem.storage.keystore.RSAKeystoreHandler;

import static net.sharksystem.android.util.SerializationHelper.objToByte;

import java.io.IOException;
import java.nio.charset.Charset;

public class SendPublicKeyActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getName();
    private NfcAdapter nfcAdapter = null;
    private Button sendPublicKeyButton;
    private SharkIdentityStorage storage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_public_key);

        this.nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        NfcChecks.preliminaryNfcChecks(nfcAdapter, this);

        sendPublicKeyButton = findViewById(R.id.send_public_key);
        sendPublicKeyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    setPushMessage();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        storage = IdentityStorageAndroid.getIdentityStorage(this.getApplicationContext());

    }

    private void setPushMessage() throws IOException {
        byte[] encodedPublicKeyCert = null;
        try {
            encodedPublicKeyCert = objToByte(RSAKeystoreHandler.getInstance().getCertificate());
        } catch (IOException e) {
            e.printStackTrace();
        }
        String certInBase64 = Base64.encodeToString(encodedPublicKeyCert, Base64.DEFAULT);
        ReceiveKeyPojo dataToSend = new ReceiveKeyPojo(storage.getOwnerName().toString(),storage.getOwnerID().toString(), certInBase64);
        initNfcMessageManager(objToByte(dataToSend));
    }

    private void initNfcMessageManager(byte[] encodedPublicKeyCert) {
        if (encodedPublicKeyCert != null) {
            NfcMessageManager outcomingNfcCallback = new NfcMessageManager("application/net.sharksystem.send.public.key".getBytes(Charset.forName("US-ASCII")), encodedPublicKeyCert);
            this.nfcAdapter.setOnNdefPushCompleteCallback(outcomingNfcCallback, this);
            this.nfcAdapter.setNdefPushMessageCallback(outcomingNfcCallback, this);
            this.nfcAdapter.invokeBeam(this);
        } else {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
        }
    }
}
