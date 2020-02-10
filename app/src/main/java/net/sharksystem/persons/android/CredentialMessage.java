package net.sharksystem.persons.android;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

class CredentialMessage {
    private String ownerName;
    private int randomInt;
    private byte[] publicKeyBytes;

    CredentialMessage(int randomInt, String ownerName, byte[] publicKeyBytes) {
        this.randomInt = randomInt;
        this.ownerName = ownerName;
        this.publicKeyBytes = publicKeyBytes;
    }

    CredentialMessage(byte[] serializedMessage) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(serializedMessage);
        DataInputStream dis = new DataInputStream(bais);

        this.ownerName = dis.readUTF();
        this.randomInt = dis.readInt();
        int length = dis.readInt();
        this.publicKeyBytes = new byte[length];
        dis.read(this.publicKeyBytes);
    }

    String getOwnerName() { return this.ownerName; }
    int getRandomInt() { return this.randomInt; }
    byte[] getPublicKeyBytes() { return this.publicKeyBytes; }

    public byte[] getMessageAsBytes() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeUTF(this.ownerName);
        dos.writeInt(randomInt);
        dos.writeInt(this.publicKeyBytes.length);
        dos.write(this.publicKeyBytes);

        return baos.toByteArray();
    }
}
