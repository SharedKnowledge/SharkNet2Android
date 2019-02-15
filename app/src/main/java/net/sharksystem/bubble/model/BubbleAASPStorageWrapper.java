package net.sharksystem.bubble.model;

import android.content.Context;
import android.util.Log;

import net.sharksystem.aasp.AASPChunk;
import net.sharksystem.aasp.AASPChunkCache;
import net.sharksystem.aasp.AASPEngineFS;
import net.sharksystem.aasp.AASPException;
import net.sharksystem.aasp.AASPStorage;
import net.sharksystem.bubble.BubbleMessage;
import net.sharksystem.bubble.android.BubbleApp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
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

        String dirName = BubbleApp.getAASPRootDirectory(ctx).getAbsolutePath();

        if(this.aaspStorage == null) {
            // initialize
            this.aaspStorage = AASPEngineFS.getAASPChunkStorage(dirName);
            Log.d("BubbleStorageWrapper", "use AASPStorage with root: " + dirName);

            // setup translation of asp3 messages to bubble messages

            // get all available chunks
            int oldestEra = this.aaspStorage.getOldestEra();
            int era = this.aaspStorage.getEra();

            this.anyTopic = BubbleApp.isAnyTopic(topic);

            this.chunkCache =
                    this.aaspStorage.getChunkStorage().getAASPChunkCache(topic, era, oldestEra);
        }
    }

    // cache
    private List<BubbleMessage> bubbleList;
    private int cachedFirstIndex = -1;
    private int cachedLastIndex = -1;

    @Override
    public BubbleMessage getMessage(int position) throws IOException, AASPException {
        // get message at position in inverse chronological order
        CharSequence message = this.chunkCache.getMessage(position, false);

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

        // save as asp3 message
        this.aaspStorage.add(this.topic, serializedBubbleMessage);

        // TODO!! cache handling is in AASPJava module now
        // cache available
        if(this.bubbleList != null) {
            // not empty

            // add message to cache
            this.bubbleList.add(bubbleMessage);

            // increase size
            this.size++;
            this.cachedLastIndex++;
        } else {
            /* can be empty
            a) getMessage was never called - cache wasn't initialized
            b) storage is empty
            we can distinguish both cases
             */

            if(this.size == 0) {
                // storage was empty
                this.bubbleList = new ArrayList<>();
                this.bubbleList.add(bubbleMessage);
                this.size++;
                this.cachedFirstIndex = 0;
                this.cachedLastIndex = 0;
            } else {
                // cache wasn't yet initialized - do it
                this.size++;
                this.getMessage(this.size-1);
            }
        }
    }

    /**
     *
     * @return number of all messages.
     * Note not all messages are actually in cache.
     */
    @Override
    public int size() throws IOException {
        return this.chunkCache.getNumberMessage();
    }
}
