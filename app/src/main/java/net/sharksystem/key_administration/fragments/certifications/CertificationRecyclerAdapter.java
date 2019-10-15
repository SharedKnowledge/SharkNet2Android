package net.sharksystem.key_administration.fragments.certifications;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.sharksystem.R;

import java.util.ArrayList;


public class CertificationRecyclerAdapter extends RecyclerView.Adapter<CertificationRecyclerAdapter.CustomViewHolder> {

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        TextView alias;

        OnKeyListener onKeyListener;

        public CustomViewHolder(View itemView, OnKeyListener onKeyListener) {
            super(itemView);
            this.alias = itemView.findViewById(R.id.tvKeyAlias);
            this.onKeyListener = onKeyListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onKeyListener.onKeyClick(getAdapterPosition());
        }
    }

    private ArrayList<ReceiveCertificationPojo> data;
    private OnKeyListener onKeyListener;

    // Provide a suitable constructor (depends on the kind of dataset)
    public CertificationRecyclerAdapter(ArrayList<ReceiveCertificationPojo> data, OnKeyListener onKeyListener) {
        this.data = data;
        this.onKeyListener = onKeyListener;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CustomViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_layout_public_key_fragment, parent, false), onKeyListener);
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

    public interface OnKeyListener {
        void onKeyClick(int position);
    }
}
