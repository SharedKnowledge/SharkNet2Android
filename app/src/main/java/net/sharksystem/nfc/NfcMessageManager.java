package net.sharksystem.nfc;

import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;

public class NfcMessageManager implements NfcAdapter.OnNdefPushCompleteCallback, NfcAdapter.CreateNdefMessageCallback {

    private NfcCallbackHelper nfcCallbackHelper;
    private byte[] mimeType;
    private  byte[] pushMessage;

    public NfcMessageManager(NfcCallbackHelper nfcCallbackHelper, byte[] mimeType) {
        this.nfcCallbackHelper = nfcCallbackHelper;
        this.mimeType = mimeType;
//        this.mimeType = "application/net.sharksystem.send.public.key".getBytes(Charset.forName("US-ASCII"));
        this.pushMessage = this.nfcCallbackHelper.getPushMessage();

    }

    @Override
    public NdefMessage createNdefMessage(NfcEvent event) {

        NdefRecord mimeRecord = NdefRecord.createMime(new String(mimeType), pushMessage);

//                new NdefRecord(NdefRecord.TNF_MIME_MEDIA ,
//                this.mimeType,
//                new byte[0], this.pushMessage);

        // mehrere Records mögleich
        NdefMessage nDefMessage = new NdefMessage(mimeRecord);

        return  nDefMessage;
    }

    @Override
    public void onNdefPushComplete(NfcEvent event) {
        nfcCallbackHelper.signalResult();
    }
}


