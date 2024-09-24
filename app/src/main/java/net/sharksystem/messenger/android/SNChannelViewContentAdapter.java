package net.sharksystem.messenger.android;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import net.sharksystem.R;
import net.sharksystem.app.messenger.SharkMessage;
import net.sharksystem.app.messenger.SharkMessengerComponent;
import net.sharksystem.app.messenger.SharkMessengerException;
import net.sharksystem.sharknet.android.SharkNetApp;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Set;

public class SNChannelViewContentAdapter extends
        RecyclerView.Adapter<SNChannelViewContentAdapter.MyViewHolder>
        implements View.OnClickListener {

    private static final String LOGSTART = "SNCViewContentAdapter";
    private final Activity activity;

    private final CharSequence channelURI;
    private final CharSequence channelName;

    private View.OnClickListener clickListener;

    @Override
    public SNChannelViewContentAdapter.MyViewHolder onCreateViewHolder(
            ViewGroup parent, int viewType) {
        Log.d(LOGSTART, "onCreateViewHolder");

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sn_channel_message_row, parent, false);

        return new SNChannelViewContentAdapter.MyViewHolder(itemView);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView encryptedTextView;
        private final TextView verifiedTextView;
        public TextView dateTextView, messageTextView, senderTextView;

        public MyViewHolder(View view) {
            super(view);
            dateTextView = (TextView) view.findViewById(R.id.sn_channel_message_row_date);
            messageTextView = (TextView) view.findViewById(R.id.sn_channel_message_row_message);
            senderTextView = (TextView) view.findViewById(R.id.sn_channel_message_row_sender);
            encryptedTextView = (TextView) view.findViewById(R.id.sn_channel_message_row_encrypted);
            verifiedTextView = (TextView) view.findViewById(R.id.sn_channel_message_row_verified);
            view.setOnClickListener(clickListener);
        }
    }

    public SNChannelViewContentAdapter(Activity activity, CharSequence uri, CharSequence name) {
        this.activity = activity;
        this.channelURI = uri;
        this.channelName = name;
        Log.d(LOGSTART, "constructor");
        this.clickListener = this;
    }

    @Override
    public void onBindViewHolder(SNChannelViewContentAdapter.MyViewHolder holder, int position) {
        Log.d(LOGSTART, "onBindViewHolder with position: " + position);

        try {
            //byte[] asapMessage = this.asapChannel.getMessages().getMessage(position, false);
            SharkMessengerComponent sharkMessenger =
                    SharkNetApp.getSharkNetApp().getSharkMessenger();

            SharkMessage sharkMessage = sharkMessenger.getChannel(this.channelURI)
                            .getMessages(false, true)
                            .getSharkMessage(position, false);

            CharSequence encrypted2View =
                    SNMessageViewHelper.getEncryptedCharSequence(sharkMessage);
            CharSequence sender2View = SNMessageViewHelper.getSenderCharSequence(sharkMessage);
            CharSequence content2View = SNMessageViewHelper.getContentCharSequence(sharkMessage);
            CharSequence verified2View = SNMessageViewHelper.getVerifiedCharSequence(sharkMessage);
            CharSequence timestamp2View = SNMessageViewHelper.getCreationTimeCharSequence(sharkMessage);

            holder.dateTextView.setText(timestamp2View);
            holder.messageTextView.setText(content2View);
            holder.senderTextView.setText(sender2View);
            holder.encryptedTextView.setText(encrypted2View);
            holder.verifiedTextView.setText(verified2View);

            holder.itemView.setId(position);

        } catch (Throwable e) {
            Log.e(LOGSTART, "cannot access message storage (yet?)");
            Log.e(LOGSTART, "position == " + position);
            Log.e(LOGSTART, e.getClass().getName());
            Log.e(LOGSTART, e.getLocalizedMessage());
        }
    }

    @Override
    public int getItemCount() {
        try {
            int size = SharkNetApp.getSharkNetApp()
                    .getSharkMessenger()
                    .getChannel(this.channelURI)
                    .getMessages()
                    .size();
            return size;

        } catch (IOException | SharkMessengerException e) {
            Log.e(LOGSTART, "cannot access message storage (yet?)");
            Log.e(LOGSTART, "got exception class: " + e.getClass().getSimpleName()
                    + "message: " + e.getLocalizedMessage());
            return 0;
        }
    }


    @Override
    public void onClick(View view) {
        int position = view.getId();

        SNMessageIntent intent = new SNMessageIntent(
                this.activity, this.channelURI, position, SNMessageViewActivity.class);

        this.activity.startActivity(intent);
    }

    private String getLogStart() {
        return net.sharksystem.utils.Log.startLog(this).toString();
    }

}