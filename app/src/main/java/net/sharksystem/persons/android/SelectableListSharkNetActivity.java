package net.sharksystem.persons.android;

import net.sharksystem.sharknet.android.SharkNetActivity;
import net.sharksystem.sharknet.android.SharkNetApp;

import java.util.Set;

abstract class SelectableListSharkNetActivity extends SharkNetActivity {
    protected SelectableListContentAdapterHelper selectableContentSource
            = new SelectableListContentAdapterHelper();

    SelectableListSharkNetActivity() {
        super(SharkNetApp.getSharkNetApp());
    }

    Set<CharSequence> getSelectedItemIDs() {
        return this.selectableContentSource.getSelectedUIDs();
    }

}
