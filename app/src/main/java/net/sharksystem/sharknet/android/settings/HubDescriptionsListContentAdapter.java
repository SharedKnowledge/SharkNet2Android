package net.sharksystem.sharknet.android.settings;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import net.sharksystem.R;
import net.sharksystem.SharkException;
import net.sharksystem.android.IntentWithPosition;
import net.sharksystem.hub.peerside.HubConnectorDescription;
import net.sharksystem.sharknet.android.SharkNetApp;

import java.io.IOException;

public class HubDescriptionsListContentAdapter extends
        RecyclerView.Adapter<HubDescriptionsListContentAdapter.MyViewHolder>
        implements View.OnClickListener {

    private final Context ctx;
    private View.OnClickListener clickListener;

    public HubDescriptionsListContentAdapter(Context ctx) {
        this.ctx = ctx;
        this.clickListener = this;
    }

    @NonNull
    @Override
    public HubDescriptionsListContentAdapter.
            MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.settings_hub_descriptions_row, parent, false);

        return new HubDescriptionsListContentAdapter.MyViewHolder(itemView);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView connectorType, connectorDescriptionString, multiChannel;

        public MyViewHolder(View view) {
            super(view);
            this.connectorType = (TextView)
                    view.findViewById(R.id.settings_hub_descriptions_row_connectorType);
            this.connectorDescriptionString = (TextView)
                    view.findViewById(R.id.settings_hub_descriptions_row_connectorDescriptionString);
            this.multiChannel = (TextView)
                    view.findViewById(R.id.settings_hub_descriptions_row_connectorMultiChannel);
            view.setOnClickListener(clickListener);
        }
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Log.d(this.getLogStart(), "onBindViewHolder with position: " + position);

        try {
            HubConnectorDescription hubDescriptions =
                    SharkNetApp.getSharkNetApp().getSharkPeer().getHubDescription(position);

            String typeString = "unknown type";
            String multiChannelString = "shared connection";
            switch(hubDescriptions.getType()) {
                case HubConnectorDescription.TCP:
                    typeString = "TCP";
                    if(hubDescriptions.canMultiChannel()) {
                        multiChannelString = "multi channel";
                    }
                    break;
            }

            holder.connectorType.setText(typeString);
            holder.connectorDescriptionString.setText(hubDescriptions.toString());
            holder.multiChannel.setText(multiChannelString);

            holder.itemView.setId(position);

        } catch (SharkException e) {
            Log.e(this.getLogStart(), "problems while showing channel information: "
                    + e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        int i = SharkNetApp.getSharkNetApp().getSharkPeer().getHubDescriptions().size();
        return i;
    }

    @Override
    public void onClick(View v) {
        v.getId();
        IntentWithPosition intent = new IntentWithPosition(this.ctx, v.getId(),
                HubDescriptionEditActivity.class);

        this.ctx.startActivity(intent);
    }

    private String getLogStart() {
        return net.sharksystem.utils.Log.startLog(this).toString();
    }

}
