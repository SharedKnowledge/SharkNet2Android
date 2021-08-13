package net.sharksystem.android;

import android.content.Context;
import android.content.Intent;

import net.sharksystem.SharkException;

public class IntentWithPosition extends Intent {
    private static final String KEY_POSITION = "position";
    private int position = -1;

    public IntentWithPosition(Context ctx, int position, Class activityClass) {
        super(ctx, activityClass);

        this.position = position;

        this.putExtra(KEY_POSITION, position);
    }

    public IntentWithPosition(Intent intent) {
        super();
        if(intent.hasExtra(KEY_POSITION)) {
            this.position = intent.getIntExtra(KEY_POSITION, 0);
        }
    }

    public int getPosition() throws SharkException {
        if(this.position == -1) throw new SharkException("no position set");
        return this.position;
    }
}
