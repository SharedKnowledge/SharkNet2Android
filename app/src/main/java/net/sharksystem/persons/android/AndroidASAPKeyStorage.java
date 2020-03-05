package net.sharksystem.persons.android;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import net.sharksystem.crypto.ASAPKeyStorage;
import net.sharksystem.crypto.InMemoASAPKeyStorage;
import net.sharksystem.crypto.KeyHelper;
import net.sharksystem.crypto.SharkCryptoException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;

import static net.sharksystem.persons.android.OwnerStorageAndroid.PREFERENCES_FILE;

/**
 * This class must be reviewed and re-implemented and use a secure key store.
 */
class AndroidASAPKeyStorage extends InMemoASAPKeyStorage implements ASAPKeyStorage {
    private static final String PRIVATE_KEY = "PRIVATE_KEY_ALIAS";
    private static final String PUBLIC_KEY = "PUBLIC_KEY_ALIAS";
    private static final String KEYS_CREATION_TIME = "KEYS_CREATION_TIME";

    private final Context ctx;

    /////////////////////////////////////////////////////////////////////////////////////////////
    //                                    asap key storage                                     //
    /////////////////////////////////////////////////////////////////////////////////////////////

    AndroidASAPKeyStorage(Context ctx) {
        this.ctx = ctx;
    }

    private SharedPreferences getSharedPreferences() {
        return this.ctx.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
    }

    private byte[] string2byteArray(String s) {
        return s.getBytes(Charset.defaultCharset());
    }

    private String byteArray2String(byte[] bArray) {
        return new String(bArray, Charset.defaultCharset());
    }

    private DataInputStream getDIS(byte[] bytes) {
        return new DataInputStream(new ByteArrayInputStream(bytes));
    }

    @Override
    public void storePrivateKey(PrivateKey privateKey) {
        // make peristent
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        try {
            KeyHelper.writePrivateKeyToStream(privateKey, dos);
            String keyString = this.byteArray2String(baos.toByteArray());
            SharedPreferences.Editor edit = this.getSharedPreferences().edit();
            edit.putString(PRIVATE_KEY, keyString);
            edit.commit();
        } catch (IOException e) {
            Log.e(this.getLogStart(), "serious problem: " + e.getLocalizedMessage());
        }
        super.storePrivateKey(privateKey);
    }

    @Override
    public void storePublicKey(PublicKey publicKey) {
        // make peristent
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        try {
            KeyHelper.writePublicKeyToStream(publicKey, dos);
            String keyString = this.byteArray2String(baos.toByteArray());
            SharedPreferences.Editor edit = this.getSharedPreferences().edit();
            edit.putString(PUBLIC_KEY, keyString);
            edit.commit();
        } catch (IOException e) {
            Log.e(this.getLogStart(), "serious problem: " + e.getLocalizedMessage());
        }

        super.storePublicKey(publicKey);
    }

    @Override
    public void setCreationTime(long creationTime) {
        // make persistent
        SharedPreferences.Editor editor = this.getSharedPreferences().edit();
        editor.putLong(KEYS_CREATION_TIME, creationTime);
        editor.commit();

        super.setCreationTime(creationTime);
    }

    @Override
    public long getCreationTime() throws SharkCryptoException {
        try {
            return super.getCreationTime();
        }
        catch(SharkCryptoException e) {
            // restore from external memory
            long storedLong = this.getSharedPreferences().getLong(KEYS_CREATION_TIME, 0);
            if(storedLong != 0) {
                super.setCreationTime(storedLong);
                return storedLong;
            }
            throw e; // cannot recover
        }
    }

    @Override
    public PrivateKey retrievePrivateKey() throws SharkCryptoException {
        try {
            return super.retrievePrivateKey();
        }
        catch(SharkCryptoException e) {
            // restore from external memory
            String keyString = this.getSharedPreferences().getString(PRIVATE_KEY, null);
            if(keyString != null) {
                byte[] keyBytes = this.string2byteArray(keyString);
                try {
                    PrivateKey privateKey = KeyHelper.readPrivateKeyFromStream(
                            new DataInputStream(new ByteArrayInputStream(keyBytes)));

                    super.storePrivateKey(privateKey);
                    return privateKey;
                } catch (NoSuchAlgorithmException | IOException | InvalidKeySpecException ex) {
                    throw new SharkCryptoException(ex.getLocalizedMessage());
                }
            }
            throw e; // cannot recover
        }
    }

    @Override
    public PublicKey retrievePublicKey() throws SharkCryptoException {
        try {
            return super.retrievePublicKey();
        }
        catch(SharkCryptoException e) {
            // restore from external memory
            String keyString = this.getSharedPreferences().getString(PUBLIC_KEY, null);
            if(keyString != null) {
                byte[] keyBytes = this.string2byteArray(keyString);
                try {
                    PublicKey publicKey = KeyHelper.readPublicKeyFromStream(
                            new DataInputStream(new ByteArrayInputStream(keyBytes)));

                    super.storePublicKey(publicKey);
                    return publicKey;
                } catch (NoSuchAlgorithmException | IOException | InvalidKeySpecException ex) {
                    throw new SharkCryptoException(ex.getLocalizedMessage());
                }
            }
            throw e; // cannot recover
        }
    }

    private String getLogStart() {
        return net.sharksystem.asap.util.Log.startLog(this).toString();
    }
}
