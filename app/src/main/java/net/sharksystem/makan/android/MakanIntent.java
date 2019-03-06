package net.sharksystem.makan.android;

import android.content.Context;
import android.content.Intent;

import net.sharksystem.SharkException;
import net.sharksystem.aasp.AASPStorage;
import net.sharksystem.makan.MakanException;

public class MakanIntent extends Intent {
    private static final String USER_FRIENDLY_NAME = "userfriendlyname";
    private static final String URI_EXTRA = "uriextra";
    private final CharSequence userFriendlyName;
    private final CharSequence uri;

    public MakanIntent(Context ctx, CharSequence userFriendlyName, CharSequence uri,
                       Class activityClass) {

        super(ctx, activityClass);

        this.userFriendlyName = userFriendlyName;
        this.uri = uri;

        this.putExtra(USER_FRIENDLY_NAME, userFriendlyName);
        this.putExtra(URI_EXTRA, uri);
    }

    public MakanIntent(Intent intent) throws SharkException {
        super();

        if(!intent.hasExtra(USER_FRIENDLY_NAME) || !intent.hasExtra(URI_EXTRA)) {
            throw new SharkException("missing extra parameters - unable read MakanViewIntent");
        }

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
