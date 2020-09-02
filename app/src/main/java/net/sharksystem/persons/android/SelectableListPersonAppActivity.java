package net.sharksystem.persons.android;

import java.util.Set;

abstract class SelectableListPersonAppActivity extends PersonAppActivity {
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
