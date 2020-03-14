package net.sharksystem.persons.android;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import net.sharksystem.R;
import net.sharksystem.asap.android.Util;

import java.util.Set;

public abstract class PersonListActivity extends SelectableListSharkNetActivity /*SharkNetActivity*/ {
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private PersonListContentAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(this.getLogStart(), "onCreate");

        try {
            setContentView(R.layout.person_list_drawer_layout);

            this.getSharkNetApp().setupDrawerLayout(this);

            PersonsStorageAndroid personsApp = PersonsStorageAndroid.getPersonsApp();
            Set<CharSequence> preselectionSet = personsApp.getPreselectionSet();
            //Log.d(this.getLogStart(), "got preselectedset: " + preselectionSet);
            if(preselectionSet != null && !preselectionSet.isEmpty()) {
                this.selectableContentSource.setPreselection(preselectionSet);
                personsApp.setPreselectionSet(null);
            }

            ////////////////////////////////////////////////////////////////////////
            //                         prepare action bar                         //
            ////////////////////////////////////////////////////////////////////////
            // setup toolbar
            Toolbar myToolbar = (Toolbar) findViewById(R.id.person_list_with_toolbar);
            setSupportActionBar(myToolbar);

            ////////////////////////////////////////////////////////////////////////
            //                         prepare recycler view                      //
            ////////////////////////////////////////////////////////////////////////
            mRecyclerView = (RecyclerView) findViewById(R.id.person_list_recycler_view);

            mAdapter = new PersonListContentAdapter(this, this.selectableContentSource);
            RecyclerView.LayoutManager mLayoutManager =
                    new LinearLayoutManager(getApplicationContext());

            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            mRecyclerView.setAdapter(mAdapter);
            Log.d(this.getLogStart(), "attached content adapter");
        }
        catch(Exception e) {
            Log.d(this.getLogStart(), "problems while setting up activity and content adapter: "
                    + e.getLocalizedMessage());
            // debug break
            int i = 42;
        }
    }

    protected void onResume() {
        super.onResume();
        if(this.mAdapter != null) {
            Log.d(Util.getLogStart(this), "onResume: assume data set changed.");
            this.mAdapter.notifyDataSetChanged();
        } else {
            Log.e(Util.getLogStart(this), "onResume: content adapter not initialized?!");
        }
    }
}
