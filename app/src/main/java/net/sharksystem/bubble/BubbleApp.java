package net.sharksystem.bubble;

import android.content.Context;
import android.util.Log;

import net.sharksystem.bubble.android.BubbleAppAndroid;
import net.sharksystem.bubble.model.BubbleMessageInMemo;
import net.sharksystem.bubble.model.BubbleMessageStorage;

import java.io.IOException;

public class BubbleApp {
    private static final String LOGSTART = "BubbleApp";
    private static boolean newData = false;
    private final Context ctx;

    public BubbleApp(Context ctx) {
        this.ctx = ctx;
    }

    /**
     * ask if new data arrived since last call
     * @return
     */
    public static boolean newDataReset() {
        boolean arrived = BubbleApp.newData;
        BubbleApp.newData = false;
        return arrived;
    }

    /**
     * New data arrived since whatever..
     * @return
     */
    public static boolean newData() {
        return BubbleApp.newData;
    }

    public static boolean isAnyTopic(CharSequence topic) {
        if(topic == null) {
            return true;
        }

        String tString = topic.toString();
        return tString.equalsIgnoreCase(BubbleMessage.ANY_TOPIC);
    }

    public void handleAASPMessage(CharSequence aaspMessage) {
        try {
            Log.d(LOGSTART, "handle aasp message: " + aaspMessage);
            BubbleMessageInMemo bubbleMessage = new BubbleMessageInMemo(aaspMessage);

            BubbleMessageStorage storage =
                    BubbleAppAndroid.getBubbleMessageStorage(this.ctx, bubbleMessage.getTopic());

            Log.d(LOGSTART, "parsed message");
            Log.d(LOGSTART, bubbleMessage.getTopic().toString());
            Log.d(LOGSTART, bubbleMessage.getUserID().toString());
            Log.d(LOGSTART, bubbleMessage.getMessage().toString());

            storage.addMessage(
                    bubbleMessage.getTopic(),
                    bubbleMessage.getUserID(),
                    bubbleMessage.getMessage());

            BubbleApp.newData = true;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
