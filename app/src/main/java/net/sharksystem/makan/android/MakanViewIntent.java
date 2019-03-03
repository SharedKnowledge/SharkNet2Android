package net.sharksystem.makan.android;

import android.content.Intent;

import net.sharksystem.aasp.AASPStorage;
import net.sharksystem.makan.MakanException;

public class MakanViewIntent extends Intent {
    private static final String USER_FRIENDLY_NAME = "userfriendlyname";
    private static final String URI_EXTRA = "uriextra";
    private static final String OWNER_ID = "ownerid";
    private final CharSequence userFriendlyName;
    private final CharSequence uri;
    private final CharSequence ownerID;

    public MakanViewIntent(CharSequence userFriendlyName, CharSequence uri, CharSequence ownerID) {
        super();

        this.userFriendlyName = userFriendlyName;
        this.uri = uri;
        this.ownerID = ownerID;

        this.putExtra(USER_FRIENDLY_NAME, userFriendlyName);
        this.putExtra(URI_EXTRA, uri);
        this.putExtra(OWNER_ID, ownerID);
    }

    public MakanViewIntent(Intent intent) throws MakanException {
        super();

        if(
                !intent.hasExtra(USER_FRIENDLY_NAME)
                        || !intent.hasExtra(URI_EXTRA)
                        || !intent.hasExtra(OWNER_ID)
        )
            throw new MakanException("missing extra parameters - unable read MakanViewIntent");

        this.userFriendlyName = intent.getStringExtra(USER_FRIENDLY_NAME);
        this.uri = intent.getStringExtra(URI_EXTRA);
        this.ownerID = intent.getStringExtra(OWNER_ID);
    }

    public CharSequence getUserFriendlyName() {
        return this.userFriendlyName;
    }

    public CharSequence getUri() {
        return this.uri;
    }

    public CharSequence getOwnerID() {
        return this.ownerID;
    }
}
