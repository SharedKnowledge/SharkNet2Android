package net.sharksystem.bubble.model;

import net.sharksystem.bubble.BubbleMessage;

import java.io.IOException;

public interface BubbleMessageStorage {
    BubbleMessage getMessage(int position) throws IOException;

    void addMessage(CharSequence topic, CharSequence userID, CharSequence message) throws IOException;

    int size();
}
