package net.sharksystem.persons;

import net.sharksystem.SharkException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

public class CredentialMessage {
    private final long validSince;
    private CharSequence ownerID;
    private CharSequence ownerName;
    private int randomInt;
    private PublicKey publicKey;


    public CharSequence getOwnerID() { return this.ownerID; }
    public CharSequence getOwnerName() { return this.ownerName; }
    public int getRandomInt() { return this.randomInt; }
    public long getValidSince() { return this.validSince; }
    public PublicKey getPublicKey() { return this.publicKey; }

    public CredentialMessage(int randomInt, CharSequence ownerID, CharSequence ownerName,
                             long validSince, PublicKey publicKey) {
        this.ownerName = ownerName;
        this.ownerID = ownerID;
        this.randomInt = randomInt;
        this.validSince = validSince;
        this.publicKey = publicKey;
    }

    public CredentialMessage(byte[] serializedMessage) throws IOException, SharkException {
        ByteArrayInputStream bais = new ByteArrayInputStream(serializedMessage);
        DataInputStream dis = new DataInputStream(bais);

        this.ownerName = dis.readUTF();
        this.ownerID = dis.readUTF();
        this.randomInt = dis.readInt();
        this.validSince = dis.readLong();

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
        dos.writeUTF(this.ownerID.toString());
        dos.writeInt(this.randomInt);
        dos.writeLong(this.validSince);

        // public key
        dos.writeUTF(this.publicKey.getAlgorithm()); // write public key algorithm

        byte[] publicKeyBytes = this.publicKey.getEncoded();
        dos.writeInt(publicKeyBytes.length); // write public key length
        dos.write(publicKeyBytes); // write public key bytes

        return baos.toByteArray();
    }

    public static String sixDigitsToString(int sixDigitsInt) {
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
