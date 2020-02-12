package net.sharksystem.persons.android;

import net.sharksystem.SharkException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

class CredentialMessage {
    private int userID;
    private CharSequence ownerName;
    private int randomInt;
    private PublicKey publicKey;


    public int getUserID() { return this.userID; }
    CharSequence getOwnerName() { return this.ownerName; }
    int getRandomInt() { return this.randomInt; }
    PublicKey getPublicKey() { return this.publicKey; }

    CredentialMessage(int randomInt, int userID, CharSequence ownerName, PublicKey publicKey) {
        this.userID = userID;
        this.randomInt = randomInt;
        this.ownerName = ownerName;
        this.publicKey = publicKey;
    }

    CredentialMessage(byte[] serializedMessage) throws IOException, SharkException {
        ByteArrayInputStream bais = new ByteArrayInputStream(serializedMessage);
        DataInputStream dis = new DataInputStream(bais);

        this.ownerName = dis.readUTF();
        this.userID = dis.readInt();
        this.randomInt = dis.readInt();

        // public key
        String algorithm = dis.readUTF(); // read public key algorithm
        int length = dis.readInt(); // read public key length
        byte[] publicKeyBytes = new byte[length];
        dis.read(publicKeyBytes); // read public key bytes

        KeyFactory keyFactory = null;
        try {
            keyFactory = KeyFactory.getInstance(algorithm);
            this.publicKey = keyFactory.generatePublic(new X509EncodedKeySpec(publicKeyBytes));
        } catch (Exception e) {
            throw new SharkException(e.getLocalizedMessage());
        }
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

        // public key
        dos.writeUTF(this.publicKey.getAlgorithm()); // write public key algorithm

        byte[] publicKeyBytes = this.publicKey.getEncoded();
        dos.writeInt(publicKeyBytes.length); // write public key length
        dos.write(publicKeyBytes); // write public key bytes

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
