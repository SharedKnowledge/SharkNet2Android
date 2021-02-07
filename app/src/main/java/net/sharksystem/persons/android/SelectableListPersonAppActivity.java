package net.sharksystem.persons.android;

import net.sharksystem.sharknet.android.SharkNetActivity;

import java.util.Set;

abstract class SelectableListPersonAppActivity extends SharkNetActivity {
    protected SelectableListContentAdapterHelper selectableContentSource
            = new SelectableListContentAdapterHelper();

    /*
    SelectableListSharkNetActivity() {
        super(SharkNetApp.getSharkNetApp());
    }
     */

    Set<CharSequence> getSelectedItemIDs() {
        return this.selectableContentSource.getSelectedUIDs();
    }

}
