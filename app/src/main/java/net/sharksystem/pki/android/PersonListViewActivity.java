package net.sharksystem.pki.android;

import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import net.sharksystem.R;

public class PersonListViewActivity extends PersonListActivity {

    /////////////////////////////////////////////////////////////////////////////////
    //                              toolbar methods                                //
    /////////////////////////////////////////////////////////////////////////////////

    /**
     * connect menu with menu items and make them visible
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(this.getLogStart(), "onCreateOptionsMenu called");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.person_list_view_action_buttons, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(this.getLogStart(), "onOptionsItemSelected");

        try {
            if(item.getItemId() == R.id.personListViewRemoveButton) {
                Log.d(this.getLogStart(), "personListViewRemoveButton");
                this.doRemovePersons();
                return true;
            } else if(item.getItemId() == R.id.personListViewAddButton) {
                Log.d(this.getLogStart(), "personListViewAddButton");
                this.doAddPerson();
                return true;
            } else if(item.getItemId() == R.id.abortButton) {
                this.finish();
                return true;
            } else {
                return super.onOptionsItemSelected(item);
            }
        } catch (Exception e) {
            Log.d(this.getLogStart(), e.getLocalizedMessage());
        }

        return false;
    }

    /////////////////////////////////////////////////////////////////////////////////////////////
    //                                action implementations                                   //
    /////////////////////////////////////////////////////////////////////////////////////////////

    private void doRemovePersons() {
        // TODO
        Log.d(this.getLogStart(), "doRemovePersons: TODO");

        Toast.makeText(this, "NYI: remove persons: " + this.getSelectedItemIDs(),
                Toast.LENGTH_SHORT).show();
    }

    private void doAddPerson() {
        Log.d(this.getLogStart(), "doAddPerson");
        //try {
            /*
            if(!this.getSharkNetApp().getSharkPKI().secureKeyAvailable()) {
                Toast.makeText(this,
                        "setup your secure keys first", Toast.LENGTH_SHORT).show();
                this.startActivity(new Intent(this, OwnerActivity.class));
            } else {

             */
                this.startActivity(new Intent(this,
                        PersonAddExplanationActivity.class));
            //}
        /*
        } catch (ASAPSecurityException e) {
            Toast.makeText(this,
                    "serious problems: " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }
         */
    }
}
