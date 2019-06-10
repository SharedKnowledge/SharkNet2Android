package net.sharksystem.nfc.send;

import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import net.sharksystem.R;
import net.sharksystem.android.util.NfcChecks;
import net.sharksystem.nfc.NfcCallbackHelper;
import net.sharksystem.nfc.NfcMessageManager;
import net.sharksystem.storage.keystore.RSAKeystoreHandler;

import java.nio.charset.Charset;
import java.security.cert.CertificateEncodingException;

public class SendPublicKeyActivity extends AppCompatActivity implements NfcCallbackHelper {

    private NfcAdapter nfcAdapter = null;
    private Button sendPublicKeyButton;
    private byte[] encodedPublicKeyCert;


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
                } catch (CertificateEncodingException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setPushMessage() throws CertificateEncodingException {
        this.encodedPublicKeyCert = RSAKeystoreHandler.getInstance().getCertificate().getEncoded();

//        NfcCallbackHelperImpl nfcCallbackHelper = new NfcCallbackHelperImpl(this.getApplicationContext(), encodedCertificate);
//
//        NfcMessageManager manager = new NfcMessageManager(nfcCallbackHelper,
//                "application/net.sharksystem.send.public.key".getBytes(Charset.forName("US-ASCII")));

        initOutcomingNfcManager();

//        // Todo manager.NdefOnPushCompleteCallback
//        this.nfcAdapter.setOnNdefPushCompleteCallback(manager, this);
//        // Todo manager.NdefOnPushMessageCallback
//        this.nfcAdapter.setNdefPushMessageCallback(manager, this);

    }

    private void initOutcomingNfcManager() {
        NfcMessageManager outcomingNfcCallback = new NfcMessageManager(this, "application/net.sharksystem.send.public.key".getBytes(Charset.forName("US-ASCII")));
        this.nfcAdapter.setOnNdefPushCompleteCallback(outcomingNfcCallback, this);
        this.nfcAdapter.setNdefPushMessageCallback(outcomingNfcCallback, this);
    }

    @Override
    public byte[] getPushMessage() {
        return this.encodedPublicKeyCert;
    }

    @Override
    public void signalResult() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), "Sending Complete", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
