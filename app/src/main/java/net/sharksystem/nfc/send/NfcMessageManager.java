package net.sharksystem.nfc.send;

import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;

public class NfcMessageManager implements NfcAdapter.OnNdefPushCompleteCallback, NfcAdapter.CreateNdefMessageCallback {

    private byte[] mimeType;
    private byte[] dataToSend;

    public NfcMessageManager(byte[] mimeType, byte[] dataToSend) {

        this.mimeType = mimeType;
        this.dataToSend = dataToSend;
    }

    @Override
    public NdefMessage createNdefMessage(NfcEvent event) {

        NdefRecord certificationMimeRecord = NdefRecord.createMime(new String(mimeType), dataToSend);
        NdefMessage nDefMessage = new NdefMessage(certificationMimeRecord);

        return nDefMessage;
    }

    @Override
    public void onNdefPushComplete(NfcEvent event) {
        // think of a smart event, close activity?
    }
}


