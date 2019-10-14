package net.sharksystem.key_administration.fragments;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ReceiveKeyPojo implements Serializable {

    @SerializedName("alias")
    private String alias;

    @SerializedName("uuid")
    private String uuid;

    @SerializedName("certInBase64")
    private String certInBase64;

    public ReceiveKeyPojo(String alias, String uuid, String certInBase64) {
        this.alias = alias;
        this.uuid = uuid;
        this.certInBase64 = certInBase64;
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

    public String getCertInBase64() {
        return certInBase64;
    }

    public void setCertInBase64(String certInBase64) {
        this.certInBase64 = certInBase64;
    }
}
