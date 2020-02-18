package net.sharksystem.crypto;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Calendar;

public class SharkCertificateImpl implements SharkCertificate {
    public static final int DEFAULT_CERTIFICATE_VALIDITY_IN_YEARS = 1;

    private final PublicKey publicKey;
    private final CharSequence ownerName;
    private final int ownerID;
    private final CharSequence signerName;
    private final int signerID;
    private Calendar validSince;
    private Calendar validUntil;

    public SharkCertificateImpl(int signerID,
                                CharSequence signerName,
                                PrivateKey privateKey,
                                int ownerID, CharSequence ownerName,
                                PublicKey publicKey) {

        this.signerID = signerID;
        this.signerName = signerName;
        this.ownerID = ownerID;
        this.ownerName = ownerName;
        this.publicKey = publicKey;

        this.validSince = Calendar.getInstance();
        this.validUntil = Calendar.getInstance();
        this.validUntil.add(Calendar.YEAR, DEFAULT_CERTIFICATE_VALIDITY_IN_YEARS);

        // now actually create certificate - TODO

    }

    @Override
    public int getOwnerID() { return this.ownerID; }

    @Override
    public CharSequence getOwnerName() { return this.ownerName; }

    @Override
    public CharSequence getSignerName() {  return this.signerName;  }

    @Override
    public int getSignerID() { return this.signerID; }

    @Override
    public Calendar getValidSince() { return this.validSince; }

    @Override
    public Calendar getValidUntil() { return this.validUntil; }
}
