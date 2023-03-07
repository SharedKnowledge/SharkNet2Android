package net.sharksystem.ui.network.hub;

import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.selection.ItemDetailsLookup;
import androidx.recyclerview.widget.RecyclerView;

public class HubDetailsLookup extends ItemDetailsLookup<Long> {

    private final RecyclerView recyclerView;

    public HubDetailsLookup(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    @Nullable
    @Override
    public ItemDetailsLookup.ItemDetails<Long> getItemDetails(@NonNull MotionEvent e) {
        View view = this.recyclerView.findChildViewUnder(e.getX(), e.getY());
        RecyclerView.ViewHolder viewHolder = this.recyclerView.getChildViewHolder(view);

        if (viewHolder instanceof HubListContentAdapter.ViewHolder) {
            HubListContentAdapter.ViewHolder specificViewHolder =
                    (HubListContentAdapter.ViewHolder) viewHolder;

            return new ItemDetailsLookup.ItemDetails<Long>() {
                @Override
                public int getPosition() {
                    return viewHolder.getBindingAdapterPosition();
                }
                @Override
                public Long getSelectionKey() {
                    return (long) specificViewHolder.getBindingAdapterPosition();
                }
            };
        } else return null;
    }
}
