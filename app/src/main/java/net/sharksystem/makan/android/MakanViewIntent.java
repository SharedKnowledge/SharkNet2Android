package net.sharksystem.makan.android;

import android.content.Context;
import android.content.Intent;

import net.sharksystem.aasp.AASPStorage;
import net.sharksystem.makan.MakanException;

public class MakanViewIntent extends Intent {
    private static final String USER_FRIENDLY_NAME = "userfriendlyname";
    private static final String URI_EXTRA = "uriextra";
    private final CharSequence userFriendlyName;
    private final CharSequence uri;

    public MakanViewIntent(Context ctx, CharSequence userFriendlyName, CharSequence uri) {
        super(ctx, MakanViewActivity.class);

        this.userFriendlyName = userFriendlyName;
        this.uri = uri;

        this.putExtra(USER_FRIENDLY_NAME, userFriendlyName);
        this.putExtra(URI_EXTRA, uri);
    }

    public MakanViewIntent(Intent intent) throws MakanException {
        super();

        if(!intent.hasExtra(USER_FRIENDLY_NAME) || !intent.hasExtra(URI_EXTRA))
            throw new MakanException("missing extra parameters - unable read MakanViewIntent");

        this.userFriendlyName = intent.getStringExtra(USER_FRIENDLY_NAME);
        this.uri = intent.getStringExtra(URI_EXTRA);
    }

    public CharSequence getUserFriendlyName() {
        return this.userFriendlyName;
    }

    public CharSequence getUri() {
        return this.uri;
    }
}
