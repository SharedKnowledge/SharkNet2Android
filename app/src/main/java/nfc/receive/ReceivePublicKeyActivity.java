package nfc.receive;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import net.sharksystem.R;

public class ReceivePublicKeyActivity extends AppCompatActivity {

    private NfcAdapter nfcAdapter = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_public_key);
        this.nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        preliminaryNfcChecks();
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
