package net.sharksystem.makan.android;

import android.content.Context;
import android.content.Intent;

import net.sharksystem.SharkException;
import net.sharksystem.asap.ASAPException;
import net.sharksystem.asap.util.Helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

class OpenMakanIntent extends Intent {
    private static final String MAKAN_NAME_PARAMETER = "makanName";
    private static final String OWNER_ID_PARAMETER = "ownerID";
    private static final String URI_PARAMETER = "uri";
    private static final String RECIPIENTS_PARAMETER = "recipients";

    private String makanName;
    private String ownerID;
    private String uri;
    private Set<CharSequence> recipients;


    OpenMakanIntent(Intent intent) throws SharkException {
        this.ownerID = intent.getStringExtra(OWNER_ID_PARAMETER);
        this.makanName = intent.getStringExtra(MAKAN_NAME_PARAMETER);
        this.uri = intent.getStringExtra(URI_PARAMETER);

        if(
            this.ownerID == null || this.ownerID.length() < 1
            || this.makanName == null || this.makanName.length() < 1
            || this.uri == null || this.uri.length() < 1
        ) {
            throw new SharkException("ownerID, name and uri must not be null/empty when creating makan");
        }

        String recipientsString = intent.getStringExtra(RECIPIENTS_PARAMETER);
        this.recipients = Helper.string2CharSequenceSet(recipientsString);
    }

    OpenMakanIntent(Context ctx, CharSequence ownerID, CharSequence readableName,
                    CharSequence uri, Set<CharSequence> recipients, Class activityClass) throws SharkException {

        super(ctx, activityClass);
        if(
                this.ownerID == null || this.ownerID.length() < 1
                        || this.makanName == null || this.makanName.length() < 1
                        || this.uri == null || this.uri.length() < 1
        ) {
            throw new SharkException("ownerID, name and uri must not be null/empty when creating makan");
        }

        this.putExtra(OWNER_ID_PARAMETER, ownerID);
        this.putExtra(MAKAN_NAME_PARAMETER, readableName);
        this.putExtra(URI_PARAMETER, uri);

        String recipientsString = Helper.collection2String(recipients);

        this.putExtra(RECIPIENTS_PARAMETER, recipientsString);
    }
}
