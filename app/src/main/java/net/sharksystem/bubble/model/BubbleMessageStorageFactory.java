package net.sharksystem.bubble.model;

import android.content.Context;

import net.sharksystem.asap.ASAPException;

import java.io.IOException;

public class BubbleMessageStorageFactory {
    public static BubbleMessageStorage getStorage(Context ctx) throws IOException, ASAPException {
        return new BubbleAASPStorageWrapper(ctx);
    }

    public static BubbleMessageStorage getStorage(Context ctx, CharSequence topic)
            throws IOException, ASAPException {

        return new BubbleAASPStorageWrapper(ctx, topic);
    }
}
