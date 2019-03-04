package net.sharksystem.makan.android.viewadapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.sharksystem.R;
import net.sharksystem.SharkException;
import net.sharksystem.makan.MakanException;
import net.sharksystem.makan.android.MakanApp;
import net.sharksystem.makan.android.model.MakanListStorage;

public class MakanViewContentAdapter extends
    RecyclerView.Adapter<MakanViewContentAdapter.MyViewHolder>  {

        private static final String LOGSTART = "MakanViewContentAdapter";
        private final Context ctx;
        private CharSequence topic = null;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView dateTextView, messageTextView, senderTextView;

            public MyViewHolder(View view) {
                super(view);
                dateTextView = (TextView) view.findViewById(R.id.makan_message_row_date);
                messageTextView = (TextView) view.findViewById(R.id.makan_message_row_message);
                senderTextView = (TextView) view.findViewById(R.id.makan_message_row_sender);
            }
        }

        public MakanViewContentAdapter(Context ctx, CharSequence uri)
                throws SharkException {
            Log.d(LOGSTART, "constructor");
            this.ctx = ctx;
            this.topic = uri;
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

            // go ahead
//            try {
                // TODO - use Makan library here

                // dummy
                holder.dateTextView.setText("1.1.1970 12:12");
                holder.messageTextView.setText("message");
                holder.senderTextView.setText("sender");
//            } catch (SharkException e) {
//                Log.e(LOGSTART, "cannot access message storage (yet?)");
//            }
        }

        @Override
        public int getItemCount() {
            Log.d(LOGSTART, "getItemCount");

            return 5;
/*
            int realSize = 0;
            try {
                realSize = MakanApp.getMakanApp().getMakanListStorage().size();
            } catch (MakanException e) {
                Log.e(LOGSTART, "cannot access message storage (yet?)");
                return 0;
            }
            int fakeSize = realSize+1;
            return fakeSize;
        */
        }
}
