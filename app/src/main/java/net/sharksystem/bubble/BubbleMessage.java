package net.sharksystem.bubble;

public interface BubbleMessage {
    String ANY_TOPIC = "everthing";

    public CharSequence getMessageID();

    public CharSequence getUserID();

    public CharSequence getTopic();

    public CharSequence getMessage();
}
