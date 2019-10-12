package net.sharksystem.android.util;

import android.app.Activity;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.provider.Settings;
import android.widget.Toast;

/**
 * This class implement all necessary checks for the nfc communication
 */
public class NfcChecks {

    public static void preliminaryNfcChecks(NfcAdapter nfcAdapter, Activity activity) {
        // Todo doppelte Parameter Uebergabe versuch es besser zu machen globale variable?
        isNfcEnabled(nfcAdapter,activity);
        isNfcSupported(nfcAdapter,activity);
    }


    private static void isNfcSupported(NfcAdapter nfcAdapter, Activity activity) {
        if (nfcAdapter == null) {
            Toast.makeText(activity, "Nfc is not supported on this device", Toast.LENGTH_SHORT).show();
            activity.finish();
        }
    }

    private static void isNfcEnabled(NfcAdapter nfcAdapter, Activity activity) {
        if (!nfcAdapter.isEnabled()) {
            Toast.makeText(
                    activity,
                    "NFC disabled on this device. Turn on to proceed",
                    Toast.LENGTH_SHORT
            ).show();
            activity.startActivity(new Intent(Settings.ACTION_NFC_SETTINGS));
        }
    }
}
