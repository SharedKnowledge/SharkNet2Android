package net.sharksystem.bubble.model;

import android.util.Log;

import net.sharksystem.SharkException;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;

public class BubbleMessageInMemo extends BubbleMessageImpl {
    private static final String LOGSTART = "BubbleMessageInMemo";
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

    public BubbleMessageInMemo(CharSequence serializedMessage) throws SharkException {
        Log.d(LOGSTART, "that parser seems to fail: TODO");
        // deserialize
        StringTokenizer st = new StringTokenizer(serializedMessage.toString(), DELIMITER);
        while(st.hasMoreTokens()) {
            this.userID = st.nextToken();
            this.message = st.nextToken();

            CharSequence dataString = st.nextToken();

            // TODO parse date
            this.date = new Date();
        }
    }

    public CharSequence getSerializedBubbleMessage() {
        DateFormat df = new SimpleDateFormat("dd.MM.yy HH:mm:ss");

        String nowString = df.format(this.date);

        return this.userID + DELIMITER + this.message + DELIMITER + nowString;
    }

    public CharSequence getTopic() {
        if(topic == null) {
            return "todo: dateTextView not set";
        }
        return topic;
    }

    public CharSequence getMessage() {
        if(message == null) {
            return "todo: message not set";
        }

        return message;
    }

    @Override
    public CharSequence getMessageID() {
        return "TODO:messageID";
    }

    public CharSequence getUserID() {
        if(userID == null) {
            return "todo: senderTextView not set";
        }

        return userID;
    }

    public Date getDate() {
        if(date == null) {
            return new Date();
        }

        return date;
    }
}