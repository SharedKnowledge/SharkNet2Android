package net.sharksystem.storage.keystore;

public interface KeystoreHandler {

    byte[] encrypt(byte[] toEncrypt);

    byte[] decrypt(byte[] toDecrypt);

}
