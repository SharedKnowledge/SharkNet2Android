package net.sharksystem.makan.android.viewadapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.sharksystem.R;
import net.sharksystem.SharkException;
import net.sharksystem.asap.ASAPEngineFS;
import net.sharksystem.asap.ASAPException;
import net.sharksystem.asap.ASAPStorage;
import net.sharksystem.makan.Makan;
import net.sharksystem.makan.MakanMessage;
import net.sharksystem.sharknet.android.SharkNetApp;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;

import identity.IdentityStorage;

public class MakanViewContentAdapter extends
    RecyclerView.Adapter<MakanViewContentAdapter.MyViewHolder>  {

    private static final String LOGSTART = "MakanViewContentAdapter";
    private final Activity activity;

    // parameter to create makan wrapper
    private final CharSequence topic;
    private final CharSequence userFriendlyName;
    private final CharSequence ownerID;
    private final IdentityStorage identityStorage;

    private ASAPStorage aaspStorage;

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
               CharSequence userFriendlyName, CharSequence ownerID,
               IdentityStorage identityStorage) throws SharkException {

        this.activity = activity;
        this.topic = uri;
        this.userFriendlyName = userFriendlyName;
        this.ownerID = ownerID;
        this.identityStorage = identityStorage;
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

        /*
        I assume a bug or more probably - I'm too dull to understand recycler view at all.
        Here it comes: this method is called with position 0
        But that position is never displayed.

        So: I'm going to fake it until I understand the problem
        Fix: When position 0 called - I return a dummy message

        the other calls are handled as they should but with a decreased position
         */
        if(position == 0) {
            // dummy message
            holder.dateTextView.setText("dummy-date");
            holder.messageTextView.setText("dummy-message");
            holder.senderTextView.setText("dummy-sender");
            return;
        }

        // else: position > 0

        // fake position
        position--;

        try {
            MakanMessage message = this.getMakan().getMessage(position, false);
            holder.dateTextView.setText(
                    DateFormat.getInstance().format(message.getSentDate()));

            holder.messageTextView.setText(message.getContentAsString());

            holder.senderTextView.setText(
                    this.identityStorage.getNameByID(message.getSenderID()));

        } catch (Exception e) {
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
            return this.getMakan().size() + 1; // +1 ?? see comments in onBindViewHolder

        } catch (ASAPException | IOException e) {
            Log.e(LOGSTART, "cannot access message storage (yet?)");
            return 0;
        }
    }

    private Makan getMakan() throws IOException, ASAPException {
        if(this.makan == null) {
            File asapRootDirectory =
                    SharkNetApp.getSharkNetApp(this.activity).getAASPRootDirectory();

            this.aaspStorage =
                    ASAPEngineFS.getExistingASAPEngineFS(asapRootDirectory.getAbsolutePath());

            /* TODO
            this.makan = new MakanAndroid(
                    this.userFriendlyName,
                    this.topic,
                    this.aaspStorage,
                    this.identityStorage.getPersonByID(this.ownerID),
                    this.identityStorage
            );
            */
        }

        return this.makan;
    }
    public void sync() {
        this.makan = null;
    }
}
