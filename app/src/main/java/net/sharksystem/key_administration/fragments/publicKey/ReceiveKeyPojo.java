package net.sharksystem.key_administration.fragments.publicKey;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ReceiveKeyPojo implements Serializable, Comparable<ReceiveKeyPojo> {

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

    public String getUuid() {
        return uuid;
    }

    public String getCertInBase64() {
        return certInBase64;
    }

    @Override
    public int compareTo(@NonNull ReceiveKeyPojo o) {
        return this.uuid.compareTo(o.uuid);
    }


    @Override
    public boolean equals(Object obj) {
        boolean result = false;

        if (obj instanceof ReceiveKeyPojo) {
            ReceiveKeyPojo pojo = (ReceiveKeyPojo) obj;
            result = pojo.uuid.equals(this.uuid);
        }
        return result;
    }
}
