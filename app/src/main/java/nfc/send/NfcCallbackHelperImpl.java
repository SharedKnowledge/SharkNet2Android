package nfc.send;

import android.content.Context;
import android.widget.Toast;

import nfc.NfcCallbackHelper;

public class NfcCallbackHelperImpl implements NfcCallbackHelper {

    private Context context;
    private byte[] certificate;

    public NfcCallbackHelperImpl(Context context, byte[] certificate) {
        this.certificate = certificate;
        this.context = context;
    }

    @Override
    public byte[] getPushMessage() {
        if (this.certificate != null) {
            return this.certificate;
        } else {
            return null;
        }
    }

    @Override
    public void signalResult() {
        Toast.makeText(context, "Sending Complete", Toast.LENGTH_SHORT).show();
    }
}
