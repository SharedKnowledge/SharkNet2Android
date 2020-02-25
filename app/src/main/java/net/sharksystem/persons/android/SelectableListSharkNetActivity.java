package net.sharksystem.persons.android;

import net.sharksystem.sharknet.android.SharkNetActivity;
import net.sharksystem.sharknet.android.SharkNetApp;

import java.util.Collection;

abstract class SelectableListSharkNetActivity extends SharkNetActivity {
    protected SelectableListContentAdapterHelper selectableContentSource
            = new SelectableListContentAdapterHelper();

    SelectableListSharkNetActivity() {
        super(SharkNetApp.getSharkNetApp());
    }

    Collection<CharSequence> getSelectedItemIDs() {
        return this.selectableContentSource.getSelected();
    }

}
