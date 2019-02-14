package net.sharksystem.bubble.model;

import android.content.Context;

import net.sharksystem.aasp.AASPException;

import java.io.IOException;

public class BubbleMessageStorageFactory {
    public static BubbleMessageStorage getStorage(Context ctx) throws IOException, AASPException {
        return new BubbleAASPStorageWrapper(ctx);
    }

    public static BubbleMessageStorage getStorage(Context ctx, CharSequence topic)
            throws IOException, AASPException {

        return new BubbleAASPStorageWrapper(ctx, topic);
    }
}
