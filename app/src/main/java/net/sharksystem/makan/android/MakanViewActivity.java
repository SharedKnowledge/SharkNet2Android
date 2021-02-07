package net.sharksystem.makan.android;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import net.sharksystem.R;
import net.sharksystem.SharkException;
import net.sharksystem.android.ASAPChannelIntent;
import net.sharksystem.makan.android.viewadapter.MakanViewContentAdapter;

/**
 * View a single makan
 */
public class MakanViewActivity extends MakanUriContentChangedListenerActivity {
    private RecyclerView mRecyclerView;

    private MakanViewContentAdapter mAdapter;

    private CharSequence topicUri = null;
    private CharSequence name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(this.getLogStart(), "onCreate");

        // get parameters
        try {
            ASAPChannelIntent intent = new ASAPChannelIntent(this.getIntent());
            this.topicUri = intent.getUri();
            this.name = intent.getName();

        } catch (SharkException e) {
            Log.d(this.getLogStart(), "cannot create makan view intent from intent (fatal): "
                    + e.getLocalizedMessage());

            return;
        }

        try {
            setContentView(R.layout.makan_view_drawer_layout);

            this.getSharkNetApp().setupDrawerLayout(this);

            ////////////////////////////////////////////////////////////////////////
            //                         prepare action bar                         //
            ////////////////////////////////////////////////////////////////////////
            // setup toolbar
            Toolbar myToolbar = (Toolbar) findViewById(R.id.makan_view_with_toolbar);
            setSupportActionBar(myToolbar);

            ////////////////////////////////////////////////////////////////////////
            //                         prepare recycler view                      //
            ////////////////////////////////////////////////////////////////////////

            mRecyclerView = (RecyclerView) findViewById(R.id.makan_view_recycler_view);

            mAdapter = new MakanViewContentAdapter(this,
                    this.topicUri, this.name, this.getSharkNetApp().getID());

            RecyclerView.LayoutManager mLayoutManager =
                    new LinearLayoutManager(getApplicationContext());
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            mRecyclerView.setAdapter(mAdapter);
        }
        catch(Exception e) {
            // debug break
            int i = 42;
        }
    }

    /////////////////////////////////////////////////////////////////////////////////
    //                               makan toolbar methods                         //
    /////////////////////////////////////////////////////////////////////////////////

    /**
     * connect menu with menu items and make them visible
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.makan_view_action_buttons, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        try {
            switch (item.getItemId()) {
                case R.id.makanMenuAddMessage:
                    this.doAddMessage();
                    return true;

                default:
                    // If we got here, the user's action was not recognized.
                    // Invoke the superclass to handle it.
                    return super.onOptionsItemSelected(item);
            }
        }
        catch(Exception e) {
            Log.d(this.getLogStart(), e.getLocalizedMessage());
        }

        return false;
    }

    private void doAddMessage() {
        String sampleLine = Long.toString(System.currentTimeMillis());
        Log.d(this.getLogStart(), "doAddMessageCalled");

        ASAPChannelIntent intent = new ASAPChannelIntent(this,
                this.name, this.topicUri,
                MakanAddMessageActivity.class);


        startActivity(intent);
    }

    protected void onResume() {
        super.onResume();
        this.resetAdapter(); // that pretty resource intensive but works
        //mAdapter.notifyDataSetChanged(); // simple refresh
    }

    private void resetAdapter() {
        // reset adapter to get access to new data
        try {
            mAdapter = new MakanViewContentAdapter(this,
                    this.topicUri, this.name, this.getSharkNetApp().getID());
            Log.d(this.getLogStart(), "recreate adapter");
            this.mRecyclerView.setAdapter(this.mAdapter);
            Log.d(this.getLogStart(), "notify data set changed");
            mAdapter.notifyDataSetChanged();
        } catch (SharkException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void asapUriContentChanged(CharSequence changedUri) {
        Log.d(this.getLogStart(), "uriContentChanged: " + changedUri);

        if(this.topicUri.toString().equalsIgnoreCase(changedUri.toString())) {
            this.resetAdapter();
        } else {
            Log.d(this.getLogStart(), "not my uri: " + this.topicUri);
        }
    }
}
