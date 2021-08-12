package net.sharksystem.messenger.android;

import android.content.Context;
import android.content.Intent;

import net.sharksystem.SharkException;

public class SNMessageIntent extends Intent {
    private static final String KEY_URI = "uriextra";
    private static final String KEY_MESSAGE_POSITION = "key_msg_position";
    private final CharSequence uri;
    private final int position;

    public SNMessageIntent(Context ctx, CharSequence uri, int position, Class activityClass) {
        super(ctx, activityClass);

        this.uri = uri;
        this.position = position;

        this.putExtra(KEY_URI, uri);
        this.putExtra(KEY_MESSAGE_POSITION, position);
    }

    public SNMessageIntent(Intent intent) throws SharkException {
        super();

        if(!intent.hasExtra(KEY_URI) || !intent.hasExtra(KEY_MESSAGE_POSITION)) {
            throw new SharkException("missing extra parameters");
        }

        this.uri = intent.getStringExtra(KEY_URI);
        this.position = intent.getIntExtra(KEY_MESSAGE_POSITION, 0);
    }

    public CharSequence getUri() {
        return this.uri;
    }

    public int getPosition() {
        return this.position;
    }
}
