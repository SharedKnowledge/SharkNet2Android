package net.sharksystem.persons.android;

import android.content.Context;
import android.content.Intent;

import net.sharksystem.SharkException;

class PersonIntent extends Intent {
    private static final String OWNER_ID = "ownerID";
    private static final String SIGNER_ID = "signerID";
    private static final String EXPLAIN_IDENTITY_ASSURANCE = "explainIA";
    private final boolean explainIdentityAssurance;
    private CharSequence ownerID;
    private CharSequence signerID;

    public PersonIntent(Context ctx, CharSequence ownerID, Class activityClass) {
        this(ctx, ownerID, null, false, activityClass);
    }

    public PersonIntent(Context ctx, CharSequence ownerID, boolean explainIAOfOwner,
                        Class activityClass) {
        this(ctx, ownerID, null, explainIAOfOwner, activityClass);
    }

    public PersonIntent(Context ctx, CharSequence ownerID, CharSequence signerID,
                        Class activityClass) {

        this(ctx, ownerID, signerID, false, activityClass);
    }

    public PersonIntent(Context ctx, CharSequence ownerID, CharSequence signerID,
        boolean explainIAOfOwner, Class activityClass) {
        super(ctx, activityClass);
        this.ownerID = ownerID;
        this.putExtra(OWNER_ID, ownerID);
        this.signerID = signerID;
        this.putExtra(SIGNER_ID, signerID);
        this.explainIdentityAssurance = explainIAOfOwner;
        this.putExtra(EXPLAIN_IDENTITY_ASSURANCE, explainIAOfOwner);
    }

    public PersonIntent(Intent intent) throws SharkException {
        super();

        this.ownerID = intent.getStringExtra(OWNER_ID);
        this.signerID = intent.getStringExtra(SIGNER_ID);
        this.explainIdentityAssurance =
                intent.getBooleanExtra(EXPLAIN_IDENTITY_ASSURANCE, false);
    }

    public CharSequence getOwnerID() { return this.ownerID; }
    public CharSequence getSignerID() { return this.signerID; }
    public boolean isOwnerIDSet() { return this.ownerID != null; }
    public boolean isSignerIDSet() { return this.signerID != null; }
    public boolean explainIdentityAssurance() { return this.explainIdentityAssurance; }
}
