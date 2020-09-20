package net.sharksystem.asap.sharknet.android;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.sharksystem.R;
import net.sharksystem.asap.ASAPChannel;
import net.sharksystem.asap.ASAPException;
import net.sharksystem.asap.sharknet.SharkNetMessage;
import net.sharksystem.crypto.BasicKeyStore;
import net.sharksystem.makan.Makan;
import net.sharksystem.makan.android.MakanApp;

import java.io.IOException;
import java.text.DateFormat;

public class SNChannelViewContentAdapter extends
        RecyclerView.Adapter<SNChannelViewContentAdapter.MyViewHolder>  {

    private static final String LOGSTART = "SNCViewContentAdapter";
    private final Activity activity;

    // parameter to create makan wrapper
    private final CharSequence channelURI;
    private final CharSequence channelName;
    private final ASAPChannel asapChannel;

    private Makan makan;

    @Override
    public SNChannelViewContentAdapter.MyViewHolder onCreateViewHolder(
            ViewGroup parent, int viewType) {
        Log.d(LOGSTART, "onCreateViewHolder");

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sn_channel_message_row, parent, false);

        return new SNChannelViewContentAdapter.MyViewHolder(itemView);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView dateTextView, messageTextView, senderTextView;

        public MyViewHolder(View view) {
            super(view);
            dateTextView = (TextView) view.findViewById(R.id.sn_channel_message_row_date);
            messageTextView = (TextView) view.findViewById(R.id.sn_channel_message_row_message);
            senderTextView = (TextView) view.findViewById(R.id.sn_channel_message_row_sender);
        }
    }

    public SNChannelViewContentAdapter(Activity activity, ASAPChannel asapChannel,
                                       CharSequence uri, CharSequence name) {
        this.activity = activity;
        this.asapChannel = asapChannel;
        this.channelURI = uri;
        this.channelName = name;
        Log.d(LOGSTART, "constructor");
    }

    @Override
    public void onBindViewHolder(SNChannelViewContentAdapter.MyViewHolder holder, int position) {
        Log.d(LOGSTART, "onBindViewHolder with position: " + position);

        try {
            byte[] asapMessage = this.asapChannel.getMessages().getMessage(position, false);

/*
    public static SharkNetMessage parseMessage(byte[] message, String sender, String uri,
                CharSequence ownerID, BasicKeyStore basicKeyStore) throws IOException, ASAPException {
 */
            CharSequence sender;
            String uri = this.asapChannel.getUri().toString();
            CharSequence ownerID = SNChannelsComponent.getSharkNetChannelComponent().getOwnerID();

            BasicKeyStore basicKeyStore =
                    SNChannelsComponent.getSharkNetChannelComponent().getBasicKeyStore();

            SharkNetMessage snMessage =
                    SharkNetMessage.parseMessage(asapMessage, uri, ownerID, basicKeyStore);

            // produce SNMessage
            String date = "dummy Date";
            String content = "dummy message";
            sender = "dummy Sender";

            holder.dateTextView.setText(date);
            /*
            holder.dateTextView.setText(
                    DateFormat.getInstance().format(message.getSentDate()));
             */

            holder.messageTextView.setText(content);
            holder.senderTextView.setText(sender);

            /*
            holder.messageTextView.setText(message.getContentAsString());
            holder.senderTextView.setText(PersonsStorageAndroidComponent.
                    getPersonsStorage().getPersonName(message.getSenderID()));
             */

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
            int size = this.asapChannel.getMessages().size();
            return size;

        } catch (IOException e) {
            Log.e(LOGSTART, "cannot access message storage (yet?)");
            Log.e(LOGSTART, "got exception class: " + e.getClass().getSimpleName()
                    + "message: " + e.getLocalizedMessage());
            return 0;
        }
    }

    private Makan getMakan() throws IOException, ASAPException {
        if(this.makan == null) {
            this.makan = MakanApp.getMakanApp().getMakanStorage().getMakan(this.channelURI);
        }
        return this.makan;
    }

    public void sync() {
        this.makan = null;
    }
}