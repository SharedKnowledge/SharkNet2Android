package net.sharksystem.persons.android;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.HashSet;
import java.util.Set;

class SelectableListContentAdapterHelper {
    private Set<CharSequence> selectedItemIDs = new HashSet<>();

    void setSelectedText(CharSequence itemID, View selectableItemView, TextView selectedTextView) {
        selectableItemView.setTag(itemID);
        selectedTextView.setText(
                this.selectedItemIDs.contains(itemID) ? "SELECTED" : "");

    }

    void onAction(RecyclerView.Adapter adapter, View view) {
        CharSequence itemID = (CharSequence)view.getTag();

        if(this.selectedItemIDs.contains(itemID)) {
            this.selectedItemIDs.remove(itemID);
        } else {
            this.selectedItemIDs.add(itemID);
        }

        adapter.notifyDataSetChanged();
    }

    Set<CharSequence> getSelected() {
        return this.selectedItemIDs;
    }
}
