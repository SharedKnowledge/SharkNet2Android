package net.sharksystem.persons.android;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

class CredentialMessage {
    private int userID;
    private CharSequence ownerName;
    private int randomInt;
    private byte[] publicKeyBytes;


    public int getUserID() { return this.userID; }
    CharSequence getOwnerName() { return this.ownerName; }
    int getRandomInt() { return this.randomInt; }
    byte[] getPublicKeyBytes() { return this.publicKeyBytes; }

    CredentialMessage(int randomInt, int userID, CharSequence ownerName, byte[] publicKeyBytes) {
        this.userID = userID;
        this.randomInt = randomInt;
        this.ownerName = ownerName;
        this.publicKeyBytes = publicKeyBytes;
    }

    CredentialMessage(byte[] serializedMessage) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(serializedMessage);
        DataInputStream dis = new DataInputStream(bais);

        this.ownerName = dis.readUTF();
        this.userID = dis.readInt();
        this.randomInt = dis.readInt();
        int length = dis.readInt();
        this.publicKeyBytes = new byte[length];
        dis.read(this.publicKeyBytes);
    }

    /**
     * Serialize
     * @return
     * @throws IOException
     */
    public byte[] getMessageAsBytes() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeUTF(this.ownerName.toString());
        dos.writeInt(this.userID);
        dos.writeInt(randomInt);
        dos.writeInt(this.publicKeyBytes.length);
        dos.write(this.publicKeyBytes);

        return baos.toByteArray();
    }

    static String sixDigitsToString(int sixDigitsInt) {
        // give it a nice shape
        StringBuilder sb = new StringBuilder();
        for(int i = 5; i > -1; i--) {
            if(i % 2 == 1 && i != 5) sb.append(' ');

            int q = (int) Math.pow(10, i);
            int digit = sixDigitsInt / q;
            sixDigitsInt -= digit * q;

            sb.append(digit);
        }

        return sb.toString();
    }
}
