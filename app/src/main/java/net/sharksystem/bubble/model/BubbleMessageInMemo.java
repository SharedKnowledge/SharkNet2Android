package net.sharksystem.bubble.model;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;

class BubbleMessageInMemo extends BubbleMessageImpl {
    private CharSequence topic, userID, message;
    private Date date;

    public BubbleMessageInMemo(CharSequence topic, CharSequence userID, CharSequence message, Date date) {
        this.topic = topic;
        this.userID = userID;
        this.message = message;
        this.date = date;
    }

    public BubbleMessageInMemo(CharSequence topic, CharSequence message) throws IOException {
        this.topic = topic;
        String mString = message.toString();

        try {
            StringTokenizer messageTokenizer = new StringTokenizer(mString, DELIMITER);

            this.userID = messageTokenizer.nextToken();
            this.message = messageTokenizer.nextToken();
            String dateString = messageTokenizer.nextToken();

            this.date = DateFormat.getDateInstance().parse(dateString);
        }
        catch(Throwable t) {
            throw new IOException("malformed message format: " + t.getLocalizedMessage());
        }
    }

    public CharSequence getSerializedBubbleMessage() {
        DateFormat df = new SimpleDateFormat("dd.MM.yy HH:mm:ss");

        String nowString = df.format(this.date);

        return this.userID + DELIMITER + this.message + DELIMITER + nowString;
    }

    public CharSequence getTopic() {
        return topic;
    }

    public CharSequence getMessage() {
        return message;
    }

    @Override
    public CharSequence getMessageID() {
        return "TODO:messageID";
    }

    public CharSequence getUserID() {
        return userID;
    }

    public Date getDate() {
        return date;
    }
}