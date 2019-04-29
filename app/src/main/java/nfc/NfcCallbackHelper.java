package nfc;

public interface NfcCallbackHelper {

    byte[] getPushMessage();

    void signalResult();
}
