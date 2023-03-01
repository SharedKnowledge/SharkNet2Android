package net.sharksystem.ui.channels;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import net.sharksystem.R;
import net.sharksystem.messenger.SharkMessengerChannel;
import net.sharksystem.messenger.SharkMessengerException;
import net.sharksystem.sharknet.android.SharkNetApp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * ContentAdapter for items in the ChannelListFragment
 */
public class ChannelListContentAdapter
        extends RecyclerView.Adapter<ChannelListContentAdapter.ViewHolder> {

    /**
     * Listeners which want to be informed when an item was clicked
     */
    private List<ChannelSelectedListener> listeners;

    /**
     * Constructor of the ContentAdapter
     */
    public ChannelListContentAdapter() {
        this.listeners = new ArrayList<>();
    }

    /**
     * ViewHolder for the items (=channels)
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        /**
         * TextView of the channel uri
         */
        private final TextView uriTextView;

        /**
         * TextView of the channel name
         */
        private final TextView nameTextView;

        /**
         * Constructor of the ViewModel
         * @param view the item as view
         */
        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            this.uriTextView = view.findViewById(R.id.sn_channel_list_row_uri);
            this.nameTextView = view.findViewById(R.id.sn_channel_list_row_name);
        }

        /**
         * Returns the uri as TextView
         * @return the uri as TextView object
         */
        public TextView getUriTextView() {
            return this.uriTextView;
        }
    }


    /**
     * Adds a listener to the list of listeners
     * @param listener the new listener
     */
    public void addChannelSelectedListener(ChannelSelectedListener listener) {
        this.listeners.add(listener);
    }

    /**
     * Called when a new item is created
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return The new ViewHolder of the item
     */
    @NonNull
    @Override
    public ChannelListContentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sn_channels_list_row, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        //inform the listeners when items were clicked.
        view.setOnClickListener(itemView -> {
            for (ChannelSelectedListener listener : this.listeners)
                listener.channelSelectedWithShortClick(viewHolder);

        });

        view.setOnLongClickListener(itemView -> {
            for (ChannelSelectedListener listener : this.listeners)
                listener.channelSelectedWithLongClick(viewHolder);
            return false;
        });

        return viewHolder;
    }

    /**
     * Bind ViewHolder to data
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
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
