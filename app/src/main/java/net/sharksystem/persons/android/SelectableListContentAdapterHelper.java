package net.sharksystem.persons.android;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.HashSet;
import java.util.Set;

class SelectableListContentAdapterHelper {
    private Set<CharSequence> selectedItemIDs = new HashSet<>();
    private Set<CharSequence> uidSet = new HashSet<>();
    private Set<CharSequence> preselectedUIDSet = null;

    void setPreselection(Set<CharSequence> preselected) {
        this.preselectedUIDSet = preselected;
    }

    void setSelectedText(CharSequence itemID, View selectableItemView, TextView selectedTextView) {
        selectableItemView.setTag(itemID);
        selectedTextView.setText(
                this.selectedItemIDs.contains(itemID) ? "SELECTED" : "");
    }

    void setSelectedText(CharSequence itemID, CharSequence uid,
                         View selectableItemView, TextView selectedTextView) {

        if(this.preselectedUIDSet != null && !preselectedUIDSet.isEmpty()) {
            if(this.preselectedUIDSet.remove(uid)) { // pre-select once
                this.selectedItemIDs.add(itemID);
                this.uidSet.add(uid);
            }
        }

        this.setSelectedText(itemID, selectableItemView, selectedTextView);
    }

    void onAction(RecyclerView.Adapter adapter, View view, CharSequence uid) {
        CharSequence itemID = (CharSequence)view.getTag();

        if(this.selectedItemIDs.contains(itemID)) {
            this.selectedItemIDs.remove(itemID);
            this.uidSet.remove(uid);
        } else {
            this.selectedItemIDs.add(itemID);
            this.uidSet.add(uid);
        }

        adapter.notifyDataSetChanged();
    }

    Set<CharSequence> getSelectedUIDs() {
        return this.uidSet;
    }
}
