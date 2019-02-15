package net.sharksystem.bubble.model;

import net.sharksystem.aasp.AASPException;
import net.sharksystem.bubble.BubbleMessage;

import java.io.IOException;

public interface BubbleMessageStorage {
    BubbleMessage getMessage(int position) throws IOException, AASPException;

    void addMessage(CharSequence topic, CharSequence userID, CharSequence message) throws IOException, AASPException;

    int size() throws IOException;
}
