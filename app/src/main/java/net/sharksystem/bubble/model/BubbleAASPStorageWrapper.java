package net.sharksystem.bubble.model;

import android.content.Context;
import android.util.Log;

import net.sharksystem.aasp.AASPChunk;
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
    private CharSequence topic;
    private AASPStorage aaspStorage;
    private boolean anyTopic;

    private List<AASPChunk> chunkList = new ArrayList<>();

    private int size = 0;

    public BubbleAASPStorageWrapper(Context ctx) throws IOException, AASPException {
        this(ctx, BubbleMessageImpl.ANY_TOPIC);
    }

    public BubbleAASPStorageWrapper(Context ctx, CharSequence topic) throws IOException, AASPException {
        this.topic = topic;

        String dirName = BubbleApp.getAASPRootDirectory(ctx).getAbsolutePath();

        this.aaspStorage = AASPEngineFS.getAASPChunkStorage(dirName);
        Log.d("BubbleStorageWrapper", "use AASPStorage with root: " + dirName);

        // setup translation of asp3 messages to bubble messages

        // get all available chunks
        int oldestEra = this.aaspStorage.getOldestEra();
        int era = this.aaspStorage.getEra();

        this.anyTopic = BubbleApp.isAnyTopic(topic);

        // get all chunks in reverse temporal order
        int thisEra = era;
        boolean anotherRound = era != oldestEra;
        boolean lastRound = !anotherRound;
        List<AASPChunk> chunks = null;

        do {
            if(this.anyTopic) {
                chunks = this.aaspStorage.getChunkStorage().getChunks(thisEra);

                for(AASPChunk chunk : chunks) {
                    this.chunkList.add(chunk);
                    // count messages
                    this.size += chunk.getNumberMessage();
                }

            } else {
                AASPChunk chunk = this.aaspStorage.getChunkStorage().getChunk(topic, thisEra);
                this.chunkList.add(chunk);
                this.size += chunk.getNumberMessage();
            }

            if(anotherRound) {
                if(lastRound) {
                    anotherRound = false;
                } else {
                    thisEra = this.aaspStorage.getPreviousEra(thisEra);
                    lastRound = thisEra == oldestEra;
                }
            }
        } while(anotherRound);
    }

    // cache
    private List<BubbleMessage> bubbleList;
    private int cachedFirstIndex = -1;
    private int cachedLastIndex = -1;

    @Override
    public BubbleMessage getMessage(int position) throws IOException {
        if(position > this.size) throw new IOException("Position exceeds number of message");

        // we want to turn around message list - newest first
        position = this.size-1 - position; //

        if(bubbleList != null && position >= cachedFirstIndex && position <= cachedLastIndex) {
            return this.bubbleList.get(position - cachedFirstIndex); // TODO calculation correct?
        }

        // not yet in cache - find chunk
        int cachedFirstIndex = 0;

        // TODO assumed temporal sorted list
        boolean found = false;
        AASPChunk fittingChunk = null;
        for(AASPChunk chunk : this.chunkList) {
            this.cachedLastIndex = this.cachedFirstIndex + chunk.getNumberMessage() - 1;

            if(position >= cachedFirstIndex && position <= cachedLastIndex) {
                fittingChunk = chunk;
                break;
            }

            this.cachedFirstIndex += chunk.getNumberMessage() - 1;
        }

        // chunk found - copy to memory
        this.bubbleList = new ArrayList<>();
        Iterator<CharSequence> messageIter = fittingChunk.getMessages();
        while(messageIter.hasNext()) {
            CharSequence message = messageIter.next();

            BubbleMessageInMemo bubbleMessage = new BubbleMessageInMemo(this.topic, message);
            this.bubbleList.add(bubbleMessage);
        }

        // cache filled - try again
        return this.getMessage(position);

        // if missed  get from asp3storage
//        return new BubbleMessageInMemo(this.topic, "userID", "position #" + position, null);
    }

    @Override
    public void addMessage(CharSequence topic, CharSequence userID, CharSequence message) throws IOException {
        // date and time
        Date now = new Date();

        // create a bubble message object
        BubbleMessageInMemo bubbleMessage = new BubbleMessageInMemo(this.topic, userID, message, now);

        // serialize
        CharSequence serializedBubbleMessage = bubbleMessage.getSerializedBubbleMessage();

        // save as asp3 message
        this.aaspStorage.add(this.topic, serializedBubbleMessage);

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
    public int size() {
        return this.size;
    }
}
