package net.sharksystem.android.util;

import android.content.Context;
import android.content.Intent;

import net.sharksystem.SharkException;

public class KeysIntent extends Intent {
    public static final String KEY_KEYNAME = "KEY_KEYNAME";
    private final CharSequence[] key;

    public KeysIntent(Context ctx, CharSequence[] key, Class activityClass) {
        super(ctx, activityClass);
        this.key = key;
        this.putExtra(KEY_KEYNAME, key);
    }

    public KeysIntent(Intent intent) throws SharkException {
        super();
        this.key = intent.getStringArrayExtra(KEY_KEYNAME);
    }

    public CharSequence[] getKeys() { return this.key; }
}