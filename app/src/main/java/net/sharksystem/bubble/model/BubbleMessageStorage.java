package net.sharksystem.bubble.model;

import net.sharksystem.aasp.AASPException;
import net.sharksystem.bubble.BubbleMessage;

import java.io.IOException;

public interface BubbleMessageStorage {
    BubbleMessage getMessage(int position) throws IOException, AASPException;

    /**
     * Add a message to a defined uriTextView
     * @param topic
     * @param userID
     * @param message
     * @throws IOException
     * @throws AASPException
     */
    void addMessage(CharSequence topic,
                    CharSequence userID,
                    CharSequence message) throws IOException, AASPException;

    /**
     * Remove all messages of a uriTextView
     * @throws IOException
     * @throws AASPException
     */
    void removeAllMessages() throws IOException, AASPException;

    int size() throws IOException;
}
