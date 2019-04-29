package nfc;

import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;

import java.nio.charset.Charset;

public class NfcMessageManager implements NfcAdapter.OnNdefPushCompleteCallback, NfcAdapter.CreateNdefMessageCallback {

    private NfcCallbackHelper nfcCallbackHelper;
    private String mimeType;
    private String pushMessage;

    public NfcMessageManager(NfcCallbackHelper nfcCallbackHelper, String mimeType) {
        this.nfcCallbackHelper = nfcCallbackHelper;
        this.mimeType = mimeType;
        this.pushMessage = this.nfcCallbackHelper.getPushMessage();

    }

    @Override
    public NdefMessage createNdefMessage(NfcEvent event) {

        String publicKey = pushMessage;

        NdefRecord mimeRecord = NdefRecord.createMime(this.mimeType,
                publicKey.getBytes(Charset.forName("US-ASCII")));

        NdefMessage nDefMessage = new NdefMessage(mimeRecord);

        return  nDefMessage;
    }

    @Override
    public void onNdefPushComplete(NfcEvent event) {
        nfcCallbackHelper.signalResult();
    }
}


