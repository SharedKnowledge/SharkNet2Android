package net.sharksystem.ui.channels.massage;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import net.sharksystem.R;
import net.sharksystem.messenger.SharkMessage;
import net.sharksystem.messenger.SharkMessengerComponent;
import net.sharksystem.messenger.SharkMessengerException;
import net.sharksystem.messenger.android.SNMessageViewHelper;
import net.sharksystem.sharknet.android.SharkNetApp;

import java.io.IOException;

/**
 * ContentAdapter for the message list in the channel view
 */
public class ChannelViewContentAdapter extends RecyclerView.Adapter<ChannelViewContentAdapter.ViewHolder> {

    /**
     * ViewHolder for message items in the RecyclerView
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView encryptedTextView;
        private final TextView verifiedTextView;
        private final TextView dateTextView;
        private final TextView messageTextView;
        private final TextView senderTextView;

        public ViewHolder(View view) {
            super(view);

            this.dateTextView = view.findViewById(R.id.sn_channel_message_row_date);
            this.messageTextView = view.findViewById(R.id.sn_channel_message_row_message);
            this.senderTextView = view.findViewById(R.id.sn_channel_message_row_sender);
            this.encryptedTextView = view.findViewById(R.id.sn_channel_message_row_encrypted);
            this.verifiedTextView = view.findViewById(R.id.sn_channel_message_row_verified);

            //TODO: message view should slide in and out from the right side of the screen
            view.setOnClickListener(itemView -> {
                Navigation.findNavController(itemView)
                        .navigate(R.id.action_nav_channel_view_to_nav_message_view);
            });
        }
    }

    /**
     * The uri of the selected channel
     */
    private final CharSequence uri;

    /**
     * Constructor of the ContentAdapter
     * @param uri the uri of the selected channel
     */
    public ChannelViewContentAdapter(CharSequence uri) {
        this.uri = uri;
    }

    @NonNull
    @Override
    public ChannelViewContentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sn_channel_message_row, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ChannelViewContentAdapter.ViewHolder holder, int position) {
        try {
            //byte[] asapMessage = this.asapChannel.getMessages().getMessage(position, false);
            SharkMessengerComponent sharkMessenger =
                    SharkNetApp.getSharkNetApp().getSharkMessenger();

            SharkMessage sharkMessage = sharkMessenger.getChannel(this.uri)
                    .getMessages(false, true)
                    .getSharkMessage(position, false);

            CharSequence encrypted2View = SNMessageViewHelper.getEncryptedCharSequence(sharkMessage);
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
            //TODO: "" was LOGSTART before. see what can be done
            Log.e("", "cannot access message storage (yet?)");
            Log.e("", "position == " + position);
            Log.e("", e.getClass().getName());
            Log.e("", e.getLocalizedMessage());
        }
    }

    @Override
    public int getItemCount() {
        try {
            int size = SharkNetApp.getSharkNetApp()
                    .getSharkMessenger()
                    .getChannel(this.uri)
                    .getMessages()
                    .size();
            return size;

        } catch (IOException | SharkMessengerException e) {
            Log.e("", "cannot access message storage (yet?)");
            Log.e("", "got exception class: " + e.getClass().getSimpleName()
                    + "message: " + e.getLocalizedMessage());
            return 0;
        }
    }
}