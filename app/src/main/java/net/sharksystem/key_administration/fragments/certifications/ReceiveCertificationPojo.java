package net.sharksystem.key_administration.fragments.certifications;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.security.cert.Certificate;
import java.util.ArrayList;

public class ReceiveCertificationPojo implements Serializable, Comparable<ReceiveCertificationPojo> {

    @SerializedName("alias")
    private String alias;

    @SerializedName("uuid")
    private String uuid;

    @SerializedName("certInBase64")
    private String certInBase64;

    @SerializedName("signer")
    private Signer signer;

//
//    @SerializedName("signers")
//    private ArrayList<Signer> signers;



    public ReceiveCertificationPojo(String alias, String uuid, String certInBase64, Signer signer) {
        this.alias = alias;
        this.uuid = uuid;
        this.certInBase64 = certInBase64;
        this.signer = signer;
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

    public Signer getSigner() {
        return signer;
    }

    //    public void addMeToSignersList(Signer signer) {
//        this.signers.add(signer);
//    }


    @Override
    public int compareTo(@NonNull ReceiveCertificationPojo o) {
        return this.uuid.compareTo(o.uuid);
    }

    @Override
    public boolean equals(Object obj) {
        boolean result = false;

        if (obj instanceof ReceiveCertificationPojo) {
            ReceiveCertificationPojo pojo = (ReceiveCertificationPojo) obj;
            result = pojo.uuid.equals(this.uuid);
        }
        return result;
    }
}
