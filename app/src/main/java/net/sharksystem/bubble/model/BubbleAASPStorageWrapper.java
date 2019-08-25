package net.sharksystem.bubble.model;

import android.content.Context;
import android.util.Log;

import net.sharksystem.asap.ASAPChunk;
import net.sharksystem.asap.ASAPChunkCache;
import net.sharksystem.asap.ASAPChunkStorage;
import net.sharksystem.asap.ASAPEngineFS;
import net.sharksystem.asap.ASAPException;
import net.sharksystem.asap.ASAPStorage;
import net.sharksystem.bubble.BubbleApp;
import net.sharksystem.bubble.BubbleMessage;
import net.sharksystem.bubble.android.BubbleAppAndroid;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * That class provides a bubble message storage which is
 * actually a wrapper around an ASP3 file system implementation
 */
class BubbleAASPStorageWrapper implements BubbleMessageStorage {
    private static final String LOGSTART = "AASPStorageWrapper";
    private ASAPChunkCache chunkCache = null;
    private CharSequence topic;
    private ASAPStorage asapStorage = null;
    private boolean anyTopic;

    private List<ASAPChunk> chunkList = new ArrayList<>();

    private int size = 0;

    public BubbleAASPStorageWrapper(Context ctx) throws IOException, ASAPException {
        this(ctx, BubbleMessageImpl.ANY_TOPIC);
    }

    public BubbleAASPStorageWrapper(Context ctx, CharSequence topic) throws IOException, ASAPException {
        this.topic = topic;

        String dirName = BubbleAppAndroid.getAASPRootDirectory(ctx).getAbsolutePath();

        if(this.asapStorage == null) {
            // initialize
            this.asapStorage = ASAPEngineFS.getExistingASAPEngineFS(dirName);
            Log.d("BubbleStorageWrapper", "use AASPStorage with root: " + dirName);

            // setup translation of asp3 messages to bubble messages

            this.anyTopic = BubbleApp.isAnyTopic(topic);
        }
    }

    private ASAPChunkCache getChunkCache() throws IOException {
        if(this.chunkCache == null) {
            Log.d(LOGSTART, "fill cache");
            int fromEra = this.asapStorage.getOldestEra();
            Log.d(LOGSTART, "oldest era: " + fromEra);
            int toEra = this.asapStorage.getEra();
            Log.d(LOGSTART, "current era: " + toEra);
            this.chunkCache =
                    this.asapStorage.getChunkStorage().getASAPChunkCache(topic, toEra);
        }

        return this.chunkCache;
    }

    @Override
    public BubbleMessage getMessage(int position) throws IOException, ASAPException {
        // check if something new has arrived from outside
        if(BubbleApp.newDataReset()) {
            Log.d(LOGSTART, "new data arrived - clear cache");
            // refresh chunk cache
            this.chunkCache = null;
        }

        // get message at position in inverse chronological order
        CharSequence message = this.getChunkCache().getMessage(position, false);

        // parse it - TODO: maybe there is a bubble message cache in order as well?
        return new BubbleMessageInMemo(this.topic, message);
    }

    @Override
    public void addMessage(CharSequence topic, CharSequence userID, CharSequence message)
            throws IOException, ASAPException {
        // date and time
        Date now = new Date();

        // create a bubble message object
        BubbleMessageInMemo bubbleMessage = new BubbleMessageInMemo(this.topic, userID, message, now);

        // serialize
        CharSequence serializedBubbleMessage = bubbleMessage.getSerializedBubbleMessage();

        // save as aasp message
        this.asapStorage.add(this.topic, serializedBubbleMessage);

        if(this.chunkCache != null) {
            this.chunkCache.sync();
        }
    }

    @Override
    public void removeAllMessages() throws IOException, ASAPException {
        int era = this.asapStorage.getOldestEra();
        ASAPChunkStorage chunkStorage = this.asapStorage.getChunkStorage();

        boolean again = false;
        do {
            chunkStorage.dropChunks(era);

            // another one?
            if(era != this.asapStorage.getEra()) {
                era = this.asapStorage.getNextEra(era);
                again = true;
            }
        } while(again);

        if(this.chunkCache != null) {
            this.chunkCache.sync();
        }
    }

    /**
     *
     * @return number of all messages.
     * Note not all messages are actually in cache.
     */
    @Override
    public int size() throws IOException {
        ASAPChunkCache c = this.getChunkCache();
        return c.getNumberMessage();
    }
}
