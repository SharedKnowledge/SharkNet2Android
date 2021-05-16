package net.sharksystem.messenger.android;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import net.sharksystem.R;
import net.sharksystem.asap.ASAPException;
import net.sharksystem.asap.utils.DateTimeHelper;
import net.sharksystem.messenger.SharkMessage;
import net.sharksystem.messenger.SharkMessengerComponent;
import net.sharksystem.messenger.SharkMessengerException;
import net.sharksystem.sharknet.android.SharkNetApp;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Set;

public class SNChannelViewContentAdapter extends
        RecyclerView.Adapter<SNChannelViewContentAdapter.MyViewHolder>  {

    private static final String LOGSTART = "SNCViewContentAdapter";
    private final Activity activity;

    // parameter to create makan wrapper
    private final CharSequence channelURI;
    private final CharSequence channelName;

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
        private final TextView identityAssuranceTextView;
        private final TextView recipientsTextView;
        public TextView dateTextView, messageTextView, senderTextView;

        public MyViewHolder(View view) {
            super(view);
            dateTextView = (TextView) view.findViewById(R.id.sn_channel_message_row_date);
            messageTextView = (TextView) view.findViewById(R.id.sn_channel_message_row_message);
            senderTextView = (TextView) view.findViewById(R.id.sn_channel_message_row_sender);
            encryptedTextView = (TextView) view.findViewById(R.id.sn_channel_message_row_encrypted);
            verifiedTextView = (TextView) view.findViewById(R.id.sn_channel_message_row_verified);
            identityAssuranceTextView = (TextView) view.findViewById(R.id.sn_channel_message_row_identityassurance);
            recipientsTextView = (TextView) view.findViewById(R.id.sn_channel_message_row_recipients);
        }
    }

    public SNChannelViewContentAdapter(Activity activity, CharSequence uri, CharSequence name) {
        this.activity = activity;
        this.channelURI = uri;
        this.channelName = name;
        Log.d(LOGSTART, "constructor");
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

            CharSequence recipients2View;

            Set<CharSequence> recipients = sharkMessage.getRecipients();
            if(recipients == null || recipients.isEmpty()) {
                recipients2View = "anybody";
            } else {
                StringBuilder sb = new StringBuilder();

                boolean firstRound = true;
                for (CharSequence recipientID : recipients) {
                    if (firstRound) {
                        firstRound = false;
                        sb.append("to: ");
                    } else {
                        sb.append("|");
                    }

                    CharSequence recipientName = null;
                    try {
                        recipientName = SharkNetApp.getSharkNetApp().getSharkPKI()
                                .getPersonValuesByID(recipientID).getName();
                        sb.append(recipientName);
                    }
                    catch(ASAPException e) {
                        // no name found
                        sb.append(recipientID);
                    }
                }

                recipients2View = sb.toString();
            }

            CharSequence encrypted2View = "not E2E encrypted";
            if(sharkMessage.encrypted()) {
                encrypted2View = "is E2E encrypted";
            }

            // assume defaults at first
            CharSequence sender2View = "from: unknown";
            CharSequence content2View = "cannot decrypt message";
            CharSequence verified2View = "not verified";
            CharSequence timestamp2View = "time: unknown";
            CharSequence iA2View = "iA: unknown";


            // do we have an decrypted message?
            if(sharkMessage.couldBeDecrypted()) {
                byte[] snContent = sharkMessage.getContent();
                content2View = new String(snContent);

                Timestamp creationTime = sharkMessage.getCreationTime();
                timestamp2View = DateTimeHelper.long2DateString(creationTime.getTime());

                CharSequence senderID = sharkMessage.getSender();
                try {
                    senderID = SharkNetApp.getSharkNetApp().getSharkPKI()
                            .getPersonValuesByID(senderID).getName();
                }
                catch(ASAPException e) {
                    // no name found
                }

                sender2View = "from: " + senderID;

                if(sharkMessage.verified()) {
                    verified2View = "is verified";

                    int identityAssurance =
                            SharkNetApp.getSharkNetApp().getSharkPKI().
                                    getIdentityAssurance(sharkMessage.getSender());

                    iA2View = "iA of " + senderID + " is " + identityAssurance;
                }
            }

            holder.dateTextView.setText(timestamp2View);
            holder.messageTextView.setText(content2View);
            holder.senderTextView.setText(sender2View);
            holder.encryptedTextView.setText(encrypted2View);
            holder.verifiedTextView.setText(verified2View);
            holder.identityAssuranceTextView.setText(iA2View);
            holder.recipientsTextView.setText(recipients2View);

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
}