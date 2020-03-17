package net.sharksystem.persons.android;

import android.content.Context;
import android.content.Intent;

import net.sharksystem.SharkException;

class PersonIntent extends Intent {
    private static final String SUBJECT_ID = "subjectID";
    private static final String ISSUER_ID = "issuerID";
    private static final String EXPLAIN_IDENTITY_ASSURANCE = "explainIA";
    private final boolean explainIdentityAssurance;
    private CharSequence subjectID;
    private CharSequence issuerID;

    public PersonIntent(Context ctx, CharSequence subjectID, Class activityClass) {
        this(ctx, subjectID, null, false, activityClass);
    }

    public PersonIntent(Context ctx, CharSequence subjectID, boolean explainIAOfOwner,
                        Class activityClass) {
        this(ctx, subjectID, null, explainIAOfOwner, activityClass);
    }

    public PersonIntent(Context ctx, CharSequence subjectID, CharSequence issuerID,
                        Class activityClass) {

        this(ctx, subjectID, issuerID, false, activityClass);
    }

    public PersonIntent(Context ctx, CharSequence subjectID, CharSequence issuerID,
                        boolean explainIAOfSubject, Class activityClass) {
        super(ctx, activityClass);
        this.subjectID = subjectID;
        this.putExtra(SUBJECT_ID, subjectID);
        this.issuerID = issuerID;
        this.putExtra(ISSUER_ID, issuerID);
        this.explainIdentityAssurance = explainIAOfSubject;
        this.putExtra(EXPLAIN_IDENTITY_ASSURANCE, explainIAOfSubject);
    }

    public PersonIntent(Intent intent) throws SharkException {
        super();

        this.subjectID = intent.getStringExtra(SUBJECT_ID);
        this.issuerID = intent.getStringExtra(ISSUER_ID);
        this.explainIdentityAssurance =
                intent.getBooleanExtra(EXPLAIN_IDENTITY_ASSURANCE, false);
    }

    public CharSequence getSubjectID() { return this.subjectID; }
    public CharSequence getIssuerID() { return this.issuerID; }
    public boolean isOwnerIDSet() { return this.subjectID != null; }
    public boolean isSignerIDSet() { return this.issuerID != null; }
    public boolean explainIdentityAssurance() { return this.explainIdentityAssurance; }
}
