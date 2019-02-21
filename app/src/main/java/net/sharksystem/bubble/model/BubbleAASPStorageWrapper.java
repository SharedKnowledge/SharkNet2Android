package net.sharksystem.bubble.model;

import android.content.Context;
import android.util.Log;

import net.sharksystem.aasp.AASPChunk;
import net.sharksystem.aasp.AASPChunkCache;
import net.sharksystem.aasp.AASPChunkStorage;
import net.sharksystem.aasp.AASPEngineFS;
import net.sharksystem.aasp.AASPException;
import net.sharksystem.aasp.AASPStorage;
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
    private AASPChunkCache chunkCache = null;
    private CharSequence topic;
    private AASPStorage aaspStorage = null;
    private boolean anyTopic;

    private List<AASPChunk> chunkList = new ArrayList<>();

    private int size = 0;

    public BubbleAASPStorageWrapper(Context ctx) throws IOException, AASPException {
        this(ctx, BubbleMessageImpl.ANY_TOPIC);
    }

    public BubbleAASPStorageWrapper(Context ctx, CharSequence topic) throws IOException, AASPException {
        this.topic = topic;

        String dirName = BubbleAppAndroid.getAASPRootDirectory(ctx).getAbsolutePath();

        if(this.aaspStorage == null) {
            // initialize
            this.aaspStorage = AASPEngineFS.getAASPChunkStorage(dirName);
            Log.d("BubbleStorageWrapper", "use AASPStorage with root: " + dirName);

            // setup translation of asp3 messages to bubble messages

            this.anyTopic = BubbleApp.isAnyTopic(topic);
        }
    }

    private AASPChunkCache getChunkCache() throws IOException {
        if(this.chunkCache == null) {
            int fromEra = this.aaspStorage.getOldestEra();
            int toEra = this.aaspStorage.getEra();
            this.chunkCache =
                    this.aaspStorage.getChunkStorage().getAASPChunkCache(topic, fromEra, toEra);
        }

        return this.chunkCache;
    }

    @Override
    public BubbleMessage getMessage(int position) throws IOException, AASPException {

        // get message at position in inverse chronological order
        CharSequence message = this.getChunkCache().getMessage(position, false);

        // parse it - TODO: maybe there is a bubble message cache in order as well?
        return new BubbleMessageInMemo(this.topic, message);
    }

    @Override
    public void addMessage(CharSequence topic, CharSequence userID, CharSequence message) throws IOException, AASPException {
        // date and time
        Date now = new Date();

        // create a bubble message object
        BubbleMessageInMemo bubbleMessage = new BubbleMessageInMemo(this.topic, userID, message, now);

        // serialize
        CharSequence serializedBubbleMessage = bubbleMessage.getSerializedBubbleMessage();

        // save as aasp message
        this.aaspStorage.add(this.topic, serializedBubbleMessage);

        if(this.chunkCache != null) {
            this.chunkCache.sync();
        }
    }

    @Override
    public void removeAllMessages() throws IOException, AASPException {
        int era = this.aaspStorage.getOldestEra();
        AASPChunkStorage chunkStorage = this.aaspStorage.getChunkStorage();

        boolean again = false;
        do {
            chunkStorage.dropChunks(era);

            // another one?
            if(era != this.aaspStorage.getEra()) {
                era = this.aaspStorage.getNextEra(era);
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
        AASPChunkCache c = this.getChunkCache();
        return c.getNumberMessage();
    }
}
