package net.sharksystem.persons.android;

import android.content.Context;
import android.content.Intent;

import net.sharksystem.SharkException;

class PersonIntent extends Intent {
    private static final String OWNER_ID = "ownerID";
    private static final String SIGNER_ID = "signerID";
    private CharSequence ownerID;
    private CharSequence signerID;

    public PersonIntent(Context ctx, CharSequence ownerID, Class activityClass) {
        this(ctx, ownerID, null, activityClass);
    }

    public PersonIntent(Context ctx, CharSequence ownerID, CharSequence signerID,
                        Class activityClass) {
        super(ctx, activityClass);
        this.ownerID = ownerID;
        this.putExtra(OWNER_ID, ownerID);
        this.signerID =signerID;
        this.putExtra(SIGNER_ID, signerID);
    }

    public PersonIntent(Intent intent) throws SharkException {
        super();

        this.ownerID = intent.getStringExtra(OWNER_ID);
        this.signerID = intent.getStringExtra(SIGNER_ID);
    }

    public CharSequence getOwnerID() {
        return this.ownerID;
    }
    public CharSequence getSignerID() {
        return this.signerID;
    }
}
