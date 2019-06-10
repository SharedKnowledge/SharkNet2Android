package net.sharksystem.nfc;

public interface NfcCallbackHelper {

    byte[] getPushMessage();

    void signalResult();
}
