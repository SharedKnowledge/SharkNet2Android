package net.sharksystem.persons.android;

import android.content.Context;
import android.content.Intent;

import net.sharksystem.SharkException;

class PersonEditIntent extends Intent {
    private static final String USER_ID = "userID";
    private int userID;

    public PersonEditIntent(Context ctx, int userID, Class activityClass) {
        super(ctx, activityClass);
        this.userID = userID;
        this.putExtra(USER_ID, userID);
    }

    public PersonEditIntent(Intent intent) throws SharkException {
        super();

        if(!intent.hasExtra(USER_ID)) {
            throw new SharkException("missing extra parameters - unable read MakanViewIntent");
        }

        this.userID = intent.getIntExtra(USER_ID, -1);
    }

    public int getUserID() {
        return this.userID;
    }
}
