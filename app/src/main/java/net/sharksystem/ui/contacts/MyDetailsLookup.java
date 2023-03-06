package net.sharksystem.ui.contacts;

import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.selection.ItemDetailsLookup;
import androidx.recyclerview.widget.RecyclerView;

public class MyDetailsLookup extends ItemDetailsLookup<Long> {

    private final RecyclerView recyclerView;

    public MyDetailsLookup(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    @Nullable
    @Override
    public ItemDetails<Long> getItemDetails(@NonNull MotionEvent e) {
        View view = this.recyclerView.findChildViewUnder(e.getX(), e.getY());
        RecyclerView.ViewHolder viewHolder = this.recyclerView.getChildViewHolder(view);

        if (viewHolder instanceof ContactsContentAdapter.ViewHolder) {
            ContactsContentAdapter.ViewHolder specificViewHolder =
                    (ContactsContentAdapter.ViewHolder) viewHolder;

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
