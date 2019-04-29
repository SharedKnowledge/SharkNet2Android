package nfc.send;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import net.sharksystem.R;
import net.sharksystem.storage.keystore.RSAKeystoreHandler;

import java.nio.charset.Charset;
import java.security.cert.CertificateEncodingException;

import nfc.NfcMessageManager;

public class SendPublicKeyActivity extends AppCompatActivity  {

    private NfcAdapter nfcAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_public_key);
        this.nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        preliminaryNfcChecks();

        Button sendCertificate = findViewById(R.id.send_certificate);
        sendCertificate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPushMessage();
            }
        });
    }

    private void setPushMessage() {
        byte[] encodedCertificate = new byte[0];
        try {
            encodedCertificate = RSAKeystoreHandler.getInstance().getCertificate().getEncoded();
        } catch (CertificateEncodingException e) {
            e.printStackTrace();
        }
        NfcCallbackHelperImpl nfcCallbackHelper = new NfcCallbackHelperImpl(this.getApplicationContext(), encodedCertificate);
        NfcMessageManager manager = new NfcMessageManager(nfcCallbackHelper,
                "application/net.sharksystem.send.public.key".getBytes(Charset.forName("US-ASCII")));

        // Todo manager.NdefOnPushCompleteCallback
        this.nfcAdapter.setOnNdefPushCompleteCallback(manager, this);
        // Todo manager.NdefOnPushMessageCallback
        this.nfcAdapter.setNdefPushMessageCallback(manager, this);
    }

    private void preliminaryNfcChecks() {
        isNfcEnabled();
        isNfcSupported();
    }

    private void isNfcEnabled() {
        if (!nfcAdapter.isEnabled()) {
            Toast.makeText(
                    this,
                    "NFC disabled on this device. Turn on to proceed",
                    Toast.LENGTH_SHORT
            ).show();
            startActivity(new Intent(Settings.ACTION_NFC_SETTINGS));
        }
    }

    private void isNfcSupported() {
        if (this.nfcAdapter == null) {
            Toast.makeText(this, "Nfc is not supported on this device", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
