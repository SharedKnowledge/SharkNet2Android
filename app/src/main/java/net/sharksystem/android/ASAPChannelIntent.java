package net.sharksystem.android;

import android.content.Context;
import android.content.Intent;

import net.sharksystem.SharkException;

public class ASAPChannelIntent extends Intent {
    private static final String KEY_CHANNEL_NAME = "userfriendlyname";
    private static final String KEY_URI = "uriextra";
    private final CharSequence name;
    private final CharSequence uri;

    public ASAPChannelIntent(Context ctx, CharSequence name, CharSequence uri,
                             Class activityClass) {

        super(ctx, activityClass);

        this.name = name;
        this.uri = uri;

        this.putExtra(KEY_CHANNEL_NAME, name);
        this.putExtra(KEY_URI, uri);
    }

    public ASAPChannelIntent(Intent intent) throws SharkException {
        super();

        if(!intent.hasExtra(KEY_CHANNEL_NAME) || !intent.hasExtra(KEY_URI)) {
            throw new SharkException("missing extra parameters - unable read MakanViewIntent");
        }

        this.name = intent.getStringExtra(KEY_CHANNEL_NAME);
        this.uri = intent.getStringExtra(KEY_URI);
    }

    public CharSequence getName() {
        return this.name;
    }

    public CharSequence getUri() {
        return this.uri;
    }
}
