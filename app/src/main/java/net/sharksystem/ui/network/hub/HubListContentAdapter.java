package net.sharksystem.ui.network.hub;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.widget.RecyclerView;

import net.sharksystem.R;
import net.sharksystem.SharkException;
import net.sharksystem.hub.peerside.HubConnectorDescription;
import net.sharksystem.sharknet.android.SharkNetApp;

/**
 * ContentAdapter for items in the recycler view of the HubListFragment
 */
public class HubListContentAdapter extends RecyclerView.Adapter<HubListContentAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView connectorType;
        public TextView connectorDescriptionString;
        public TextView multiChannel;

        public ViewHolder(View view) {
            super(view);
            this.connectorType = (TextView)
                    view.findViewById(R.id.settings_hub_descriptions_row_connectorType);
            this.connectorDescriptionString = (TextView)
                    view.findViewById(R.id.settings_hub_descriptions_row_connectorDescriptionString);
            this.multiChannel = (TextView)
                    view.findViewById(R.id.settings_hub_descriptions_row_connectorMultiChannel);
        }
    }



    private SelectionTracker<Long> tracker;

    public HubListContentAdapter() {
        this.setHasStableIds(true);
    }

    public void setTracker(SelectionTracker<Long> tracker) {
        this.tracker = tracker;
    }


    @NonNull
    @Override
    public HubListContentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.settings_hub_descriptions_row, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HubListContentAdapter.ViewHolder holder, int position) {
        try {
            HubConnectorDescription hubDescriptions =
                    SharkNetApp.getSharkNetApp().getSharkPeer().getHubDescription(position);

            String typeString = "unknown type";
            String multiChannelString = "shared connection";
            if (hubDescriptions.getType() == HubConnectorDescription.TCP) {
                typeString = "TCP";
                if (hubDescriptions.canMultiChannel()) {
                    multiChannelString = "multi channel";
                }
            }

            holder.connectorType.setText(typeString);
            holder.connectorDescriptionString.setText(hubDescriptions.toString());
            holder.multiChannel.setText(multiChannelString);

            holder.itemView.setActivated(this.tracker.
                    isSelected((long) holder.getBindingAdapterPosition()));

        } catch (SharkException e) {
            Log.e(this.getClass().getSimpleName(), "problems while showing channel information: "
                    + e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return SharkNetApp.getSharkNetApp().getSharkPeer().getHubDescriptions().size();
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

}
