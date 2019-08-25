package net.sharksystem.bubble.model;

import net.sharksystem.asap.ASAPException;
import net.sharksystem.bubble.BubbleMessage;

import java.io.IOException;

public interface BubbleMessageStorage {
    BubbleMessage getMessage(int position) throws IOException, ASAPException;

    /**
     * Add a message to a defined dateTextView
     * @param topic
     * @param userID
     * @param message
     * @throws IOException
     * @throws ASAPException
     */
    void addMessage(CharSequence topic,
                    CharSequence userID,
                    CharSequence message) throws IOException, ASAPException;

    /**
     * Remove all messages of a dateTextView
     * @throws IOException
     * @throws ASAPException
     */
    void removeAllMessages() throws IOException, ASAPException;

    int size() throws IOException;
}
