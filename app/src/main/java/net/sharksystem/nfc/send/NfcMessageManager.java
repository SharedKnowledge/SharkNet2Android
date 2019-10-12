package net.sharksystem.nfc.send;

import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;

public class NfcMessageManager implements NfcAdapter.OnNdefPushCompleteCallback, NfcAdapter.CreateNdefMessageCallback {

    private byte[] mimeType;
    private byte[] pushMessages;

    public NfcMessageManager(byte[] mimeType, byte[] pushMessages) {

        this.mimeType = mimeType;
        this.pushMessages = pushMessages;

    }

    @Override
    public NdefMessage createNdefMessage(NfcEvent event) {

        NdefRecord mimeRecord = NdefRecord.createMime(new String(mimeType), pushMessages);

//                new NdefRecord(NdefRecord.TNF_MIME_MEDIA ,
//                this.mimeType,
//                new byte[0], this.pushMessages);

        // mehrere Records mögleich, einfach mehrere Records wie oben erstellen und an message ranhaengen
        NdefMessage nDefMessage = new NdefMessage(mimeRecord);

        return nDefMessage;
    }

    @Override
    public void onNdefPushComplete(NfcEvent event) {
        // think of a smart event, close activity?
    }
}


