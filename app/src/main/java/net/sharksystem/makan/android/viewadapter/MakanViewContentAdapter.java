package net.sharksystem.makan.android.viewadapter;

import android.app.Activity;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.sharksystem.R;
import net.sharksystem.SharkException;
import net.sharksystem.asap.ASAPException;
import net.sharksystem.makan.Makan;
import net.sharksystem.makan.MakanMessage;
import net.sharksystem.makan.android.MakanApp;

import java.io.IOException;
import java.text.DateFormat;

public class MakanViewContentAdapter extends
    RecyclerView.Adapter<MakanViewContentAdapter.MyViewHolder>  {

    private static final String LOGSTART = "MakanViewContentAdapter";
    private final Activity activity;

    // parameter to create makan wrapper
    private final CharSequence channelURI;
    private final CharSequence userFriendlyName;
    private final CharSequence ownerID;

    private Makan makan;

    public void setOutdated(int era, String user, String folder) {
        // TODO
        Log.d(LOGSTART, "TODO: got new data from another peer");
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView dateTextView, messageTextView, senderTextView;

        public MyViewHolder(View view) {
            super(view);
            dateTextView = (TextView) view.findViewById(R.id.makan_message_row_date);
            messageTextView = (TextView) view.findViewById(R.id.makan_message_row_message);
            senderTextView = (TextView) view.findViewById(R.id.makan_message_row_sender);
        }
    }

    public MakanViewContentAdapter(Activity activity, CharSequence uri,
               CharSequence userFriendlyName, CharSequence ownerID)
            throws SharkException {

        this.activity = activity;
        this.channelURI = uri;
        this.userFriendlyName = userFriendlyName;
        this.ownerID = ownerID;
        Log.d(LOGSTART, "constructor");
    }

    @Override
    public MakanViewContentAdapter.MyViewHolder onCreateViewHolder(
            ViewGroup parent, int viewType) {
        Log.d(LOGSTART, "onCreateViewHolder");

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.makan_message_row, parent, false);

        return new MakanViewContentAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MakanViewContentAdapter.MyViewHolder holder, int position) {
        Log.d(LOGSTART, "onBindViewHolder with position: " + position);

        try {
            MakanMessage message = this.getMakan().getMessage(position, false);
            holder.dateTextView.setText(
                    DateFormat.getInstance().format(message.getSentDate()));

            holder.messageTextView.setText(message.getContentAsString());

            holder.senderTextView.setText("ID: " + (message.getSenderID()));

        } catch (Throwable e) {
            Log.e(LOGSTART, "cannot access message storage (yet?)");
            Log.e(LOGSTART, "position == " + position);
            Log.e(LOGSTART, e.getClass().getName());
            Log.e(LOGSTART, e.getLocalizedMessage());
        }
    }

    @Override
    public int getItemCount() {
        Log.d(LOGSTART, "getItemCount");

        try {
            int size = this.getMakan().size();
            Log.d(LOGSTART, "got makan size of " + size);
            return this.getMakan().size(); // +1 ?? see comments in onBindViewHolder

        } catch (ASAPException | IOException e) {
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
