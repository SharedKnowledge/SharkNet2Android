package net.sharksystem.makan.android.viewadapter;

import android.content.Context;
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
import net.sharksystem.makan.MakanStorage;
import net.sharksystem.makan.android.MakanApp;
import net.sharksystem.makan.android.MakanIntent;
import net.sharksystem.makan.android.MakanViewActivity;

import java.io.IOException;

public class MakanListContentAdapter extends
        RecyclerView.Adapter<MakanListContentAdapter.MyViewHolder> implements View.OnClickListener {

    private final Context ctx;
    private View.OnClickListener clickListener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView uriTextView, nameTextView;

        public MyViewHolder(View view) {
            super(view);
            uriTextView = (TextView) view.findViewById(R.id.makan_list_row_uid);
            nameTextView = (TextView) view.findViewById(R.id.makan_list_row_name);
            view.setOnClickListener(clickListener);
        }
    }

    public MakanListContentAdapter(Context ctx) throws SharkException {
        Log.d(this.getLogStart(), "constructor");
        this.ctx = ctx;
        this.clickListener = this;
    }

    @Override
    public MakanListContentAdapter.MyViewHolder onCreateViewHolder(
            ViewGroup parent, int viewType) {
        Log.d(this.getLogStart(), "onCreateViewHolder");

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.makan_list_row, parent, false);

        return new MakanListContentAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MakanListContentAdapter.MyViewHolder holder, int position) {
        Log.d(this.getLogStart(), "onBindViewHolder with position: " + position);

        // go ahead
        try {
            MakanStorage makanStorage = MakanApp.getMakanApp().getMakanStorage();
            if(makanStorage == null) {
                Log.d(this.getLogStart(), "fatal: no makan storage");
                return;
            }

            Makan makan = makanStorage.getMakan(position);
            if(makan == null) {
                Log.d(this.getLogStart(), "fatal: no makan");
                return;
            }

            holder.uriTextView.setText(makan.getURI());
            holder.nameTextView.setText(makan.getName());
        } catch (IOException | ASAPException e) {
            Log.e(this.getLogStart(), "problems while showing mana entries: " + e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        Log.d(this.getLogStart(), "called getItemCount");

        int realSize = 0;
        try {
            realSize = MakanApp.getMakanApp().getMakanStorage().size();
            Log.d(this.getLogStart(), "count is: " + realSize);
        } catch (Exception e) {
            Log.e(this.getLogStart(), "cannot access message storage (yet?)");
            return 0;
        }
        return realSize;
    }

    @Override
    public void onClick(View view) {
        Log.d(this.getLogStart(), "click on view recognized");

        TextView uriTextView = (TextView) view.findViewById(R.id.makan_list_row_uid);
        Log.d(this.getLogStart(), "uri: " + uriTextView.getText());

        TextView nameTextView = (TextView) view.findViewById(R.id.makan_list_row_name);
        Log.d(this.getLogStart(), "name: " + nameTextView.getText());

        MakanIntent intent =
                new MakanIntent(
                        ctx,
                        nameTextView.getText(),
                        uriTextView.getText(),
                        MakanViewActivity.class);

        ctx.startActivity(intent);
    }

    private String getLogStart() {
        return net.sharksystem.asap.util.Log.startLog(this).toString();
    }
}
