package net.sharksystem.storage;

import android.content.Context;

import java.util.UUID;

import de.htw_berlin.s0551733.nfcchat.util.Constants;

public final class Storage {

    private Context context;
    private SharedPreferencesHandler sharedPreferencesHandler;
    private static Storage storage = null;

    private Storage(Context context) {
        this.context = context.getApplicationContext();
        this.sharedPreferencesHandler = new SharedPreferencesHandler(this.context);
    }

    public static Storage getInstance(Context context) {
        if(storage == null) {
            storage = new Storage(context);
        }
        return storage;
    }

    public String getUUID() {
        String myUUID = sharedPreferencesHandler.getValue(Constants.UUID_USER);
        return myUUID;
    }

    public void createUUID() {
        UUID uuid = UUID.randomUUID();
        storeUUID(uuid.toString());
    }

    private void storeUUID(String uuid) {
        sharedPreferencesHandler.writeValue(Constants.UUID_USER, uuid);
    }

    public String getAlias() {
        String myAlias = sharedPreferencesHandler.getValue(Constants.KEY_ALIAS_USER);
        return myAlias;
    }

    public void storeAlias(String alias) {
        sharedPreferencesHandler.writeValue(Constants.KEY_ALIAS_USER, alias);
    }
}
