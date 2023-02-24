package net.sharksystem.ui.channels;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import net.sharksystem.R;
import net.sharksystem.messenger.SharkMessengerChannel;
import net.sharksystem.messenger.SharkMessengerException;
import net.sharksystem.sharknet.android.SharkNetApp;

import java.io.IOException;

public class ChannelListContentAdapter
        extends RecyclerView.Adapter<ChannelListContentAdapter.ViewHolder> {


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView uriTextView;
        private final TextView nameTextView;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            this.uriTextView = view.findViewById(R.id.sn_channel_list_row_uri);
            this.nameTextView = view.findViewById(R.id.sn_channel_list_row_name);

            view.setOnClickListener(itemView -> {
                Navigation.findNavController(itemView).navigate(R.id.nav_channel_view);
            });
        }

        /**
         * Returns the TextView of the channel uri
         * @return the channel uri TextView
         */
        public TextView getUriTextView() {
            return this.uriTextView;
        }

        /**
         * Returns the TextView of the channel name
         * @return the channel name TextView
         */
        public TextView getNameTextView() {
            return this.nameTextView;
        }
    }


    @NonNull
    @Override
    public ChannelListContentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sn_channels_list_row, parent, false);

        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ChannelListContentAdapter.ViewHolder holder, int position) {
        try {
            SharkMessengerChannel channel =
                    SharkNetApp.getSharkNetApp().getSharkMessenger().getChannel(position);

            holder.uriTextView.setText(channel.getURI());
            holder.nameTextView.setText(channel.getName());

        } catch (IOException | SharkMessengerException e) {
            Log.e(this.getClass().getSimpleName(), "problems while showing channel information: "
                    + e.getLocalizedMessage());
        }
    }

    /**
     * Returns the number of channels in the recycler view
     * @return the number of channels
     */
    @Override
    public int getItemCount() {
        int count = 0;
        try {
            count = SharkNetApp.getSharkNetApp().getSharkMessenger().getChannelUris().size();
        } catch (Exception e) {
            return 0;
        }
        return count;
    }
}
