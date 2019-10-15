package net.sharksystem.key_administration.fragments.certifications;

public class Signer {

    private String signaturInBase64;
    private String alias;
    private String uuid;

    public Signer(String signaturInBase64, String alias, String uuid) {
        this.signaturInBase64 = signaturInBase64;
        this.alias = alias;
        this.uuid = uuid;
    }

    public String getSignaturInBase64() {
        return signaturInBase64;
    }

    public void setSignaturInBase64(String signaturInBase64) {
        this.signaturInBase64 = signaturInBase64;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public boolean equals(Object obj) {
        boolean result = false;

        if (obj instanceof Signer) {
            Signer signer = (Signer) obj;
            result = signer.uuid.equals(this.uuid);
        }
        return result;
    }
}
