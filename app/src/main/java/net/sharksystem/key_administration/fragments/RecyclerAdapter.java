package net.sharksystem.key_administration.fragments;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.sharksystem.R;

import java.util.ArrayList;


public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.CustomViewHolder> {

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class CustomViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextView alias;

        public CustomViewHolder(View itemView) {
            super(itemView);
        }
    }

    private ArrayList<ReceiveKeyPojo> data;

    // Provide a suitable constructor (depends on the kind of dataset)
    public RecyclerAdapter(ArrayList<ReceiveKeyPojo> data) {
        this.data = data;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return null;
//        return new CustomViewHolder(
//                LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_layout_public_key_fragment, parent, false));
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.alias.setText(data.get(position).getAlias());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
