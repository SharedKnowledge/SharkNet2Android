package net.sharksystem.nfc.send;

import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import net.sharksystem.R;
import net.sharksystem.android.util.NfcChecks;
import net.sharksystem.nfc.NfcCallbackHelper;
import net.sharksystem.nfc.NfcMessageManager;
import net.sharksystem.storage.keystore.RSAKeystoreHandler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.charset.Charset;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;

public class SendPublicKeyActivity extends AppCompatActivity implements NfcCallbackHelper {

    private final String TAG = this.getClass().getName();
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
                    Log.d(TAG, "cert" + encodedPublicKeyCert);

                } catch (CertificateEncodingException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setPushMessage() throws CertificateEncodingException {
//        this.encodedPublicKeyCert = RSAKeystoreHandler.getInstance().getCertificate().getEncoded();

        try {
            this.encodedPublicKeyCert = objToByte(RSAKeystoreHandler.getInstance().getCertificate());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "cert" + encodedPublicKeyCert);
        Certificate certificate = RSAKeystoreHandler.getInstance().getCertificate();
        System.out.println("@@@@@@@@@@@@@" + certificate.toString());

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

    // Serialization
    public byte[] objToByte(Certificate certificate) throws IOException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream objStream = new ObjectOutputStream(byteStream);
        objStream.writeObject(certificate);

        return byteStream.toByteArray();
    }
}
