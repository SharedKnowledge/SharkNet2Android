package net.sharksystem.messenger.android;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import net.sharksystem.R;
import net.sharksystem.SharkException;
import net.sharksystem.android.ASAPChannelIntent;
import net.sharksystem.app.messenger.SharkMessengerChannel;
import net.sharksystem.app.messenger.SharkMessengerException;
import net.sharksystem.sharknet.android.SharkNetApp;

import java.io.IOException;

class SNChannelsListContentAdapter extends
        RecyclerView.Adapter<SNChannelsListContentAdapter.MyViewHolder>
                implements View.OnClickListener {

    private final Context ctx;
    private View.OnClickListener clickListener;

    @Override
    public SNChannelsListContentAdapter.MyViewHolder onCreateViewHolder(
            ViewGroup parent, int viewType) {
        Log.d(this.getLogStart(), "onCreateViewHolder");

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sn_channels_list_row, parent, false);

        return new SNChannelsListContentAdapter.MyViewHolder(itemView);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView uriTextView, nameTextView, ageTextView;

        public MyViewHolder(View view) {
            super(view);
            uriTextView = (TextView) view.findViewById(R.id.sn_channel_list_row_uri);
            nameTextView = (TextView) view.findViewById(R.id.sn_channel_list_row_name);
            view.setOnClickListener(clickListener);
        }
    }

    public SNChannelsListContentAdapter(Context ctx) throws SharkException {
        Log.d(this.getLogStart(), "constructor");
        this.ctx = ctx;
        this.clickListener = this;
    }

    @Override
    public void onBindViewHolder(SNChannelsListContentAdapter.MyViewHolder holder, int position) {
        Log.d(this.getLogStart(), "onBindViewHolder with position: " + position);

        try {
            SharkMessengerChannel channel =
                    SharkNetApp.getSharkNetApp().getSharkMessenger().getChannel(position);

            holder.uriTextView.setText(channel.getURI());
            holder.nameTextView.setText(channel.getName());
            /*
            CharSequence ageString = "age: unknown";
            switch(channel.getAge()) {
                case BRONZE_AGE: ageString = "bronze age"; break;
                case STONE_AGE: ageString = "stone age"; break;
                case NETWORK_AGE: ageString = "network age"; break;
            }
            holder.ageTextView.setText(ageString);
             */
        } catch (IOException | SharkMessengerException e) {
            Log.e(this.getLogStart(), "problems while showing channel information: "
                    + e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        Log.d(this.getLogStart(), "called getItemCount");

        int realSize = 0;
        try {
            realSize = SharkNetApp.getSharkNetApp().getSharkMessenger().getChannelUris().size();
            Log.d(this.getLogStart(), "count is: " + realSize);
        } catch (Exception e) {
            Log.e(this.getLogStart(), "cannot find number of open channels: " + e.getLocalizedMessage());
            return 0;
        }
        return realSize;
    }

    @Override
    public void onClick(View view) {
        Log.d(this.getLogStart(), "click on view recognized");

        TextView uriTextView = (TextView) view.findViewById(R.id.sn_channel_list_row_uri);
        Log.d(this.getLogStart(), "uri: " + uriTextView.getText());

        TextView nameTextView = (TextView) view.findViewById(R.id.sn_channel_list_row_name);
        Log.d(this.getLogStart(), "name: " + nameTextView.getText());

        ASAPChannelIntent intent =
                new ASAPChannelIntent(
                        ctx,
                        nameTextView.getText(),
                        uriTextView.getText(),
                        SNChannelViewActivity.class);

        ctx.startActivity(intent);
    }

    private String getLogStart() {
        return net.sharksystem.utils.Log.startLog(this).toString();
    }
}
