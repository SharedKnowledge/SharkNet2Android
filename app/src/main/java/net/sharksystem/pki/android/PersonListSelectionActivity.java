package net.sharksystem.pki.android;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import net.sharksystem.R;

public class PersonListSelectionActivity extends PersonListActivity {
    /////////////////////////////////////////////////////////////////////////////////
    //                              toolbar methods                                //
    /////////////////////////////////////////////////////////////////////////////////

    /**
     * connect menu with menu items and make them visible
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(this.getLogStart(), "onCreateOptionsMenu called");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.person_list_selection_action_buttons, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        try {
            if(item.getItemId() == R.id.personListSelectionDoneButton) {
                this.doDone();
                return true;
            } else if(item.getItemId() == R.id.abortButton) {
                this.finish();
                return true;
            } else {
                return super.onOptionsItemSelected(item);
            }
        }
        catch(Exception e) {
            Log.d(this.getLogStart(), e.getLocalizedMessage());
        }

        return false;
    }

    /////////////////////////////////////////////////////////////////////////////////////////////
    //                                action implementations                                   //
    /////////////////////////////////////////////////////////////////////////////////////////////

    private void doDone() {
        Log.d(this.getLogStart(), "doDone");
        PersonStatusHelper personsApp = PersonStatusHelper.getPersonsStorage();
        personsApp.setLastPersonsSelection(this.getSelectedItemIDs());
        this.finish();
    }
}
