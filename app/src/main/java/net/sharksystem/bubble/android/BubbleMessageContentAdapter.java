package net.sharksystem.bubble.android;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import net.sharksystem.bubble.BubbleMessage;
import net.sharksystem.R;
import net.sharksystem.bubble.model.BubbleMessageImpl;
import net.sharksystem.bubble.model.BubbleMessageStorage;

public class BubbleMessageContentAdapter extends RecyclerView.Adapter<BubbleMessageContentAdapter.MyViewHolder> {
    private final Context ctx;
    private BubbleMessageStorage bubbleStorage;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView topic, message, userID;

        public MyViewHolder(View view) {
            super(view);
            topic = (TextView) view.findViewById(R.id.tv_bubble_topic);
            userID = (TextView) view.findViewById(R.id.tv_bubble_userID);
            message = (TextView) view.findViewById(R.id.tv_bubble_message);
        }
    }

    public BubbleMessageContentAdapter(Context ctx, CharSequence topic) {
        this.ctx = ctx;
        try {
//            this.bubbleStorage = BubbleMessageStorageFactory.getStorageByTopic(topic);
            if(BubbleApp.isAnyTopic(topic)) {
                this.bubbleStorage = BubbleApp.getBubbleMessageStorage(ctx);
            } else {
                this.bubbleStorage = BubbleApp.getBubbleMessageStorage(ctx, topic);
            }
        }
        catch(Exception ioe) {
            Log.e("ContentAdapter", "cannot open bubble persistent storage");
            Toast.makeText(this.ctx, "cannot open bubble storage", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bubble_message_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        /*
        I assume a bug or more probably - I'm too dull to understand recycler view at all.
        Here it comes: this method is called with position 0
        But that position is never displayed.

        So: I'm going to fake it until I understand the problem
        Fix: When position 0 called - I return a dummy message back

        the other calls are handled as they should but with a decreased position
         */

        if(position == 0) {
            // dummy message
            holder.topic.setText("dummy-topic");
            holder.userID.setText("dummy-user");
            holder.message.setText("dummy-message");

            return;
        }

        // else: position > 0

        // now fake position
        position--;

        // go ahead
        try {
            BubbleMessage bubbleMessage = this.bubbleStorage.getMessage(position);

            holder.topic.setText(bubbleMessage.getTopic());
            holder.userID.setText(bubbleMessage.getUserID());
            holder.message.setText(bubbleMessage.getMessage());
        } catch (IOException e) {
            // TODO
            Log.e("error: ", "couldn't get message in position: " + position);
        }

    }

    @Override
    public int getItemCount() {
        int realSize = this.bubbleStorage.size();
        int fakeSize = realSize+1;
        return fakeSize;
    }
}