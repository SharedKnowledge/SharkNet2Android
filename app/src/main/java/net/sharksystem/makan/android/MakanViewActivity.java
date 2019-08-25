package net.sharksystem.makan.android;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import net.sharksystem.R;
import net.sharksystem.SharkException;
import net.sharksystem.makan.MakanException;
import net.sharksystem.makan.android.viewadapter.MakanViewContentAdapter;
import net.sharksystem.sharknet.android.SharkNetActivity;
import net.sharksystem.sharknet.android.SharkNetApp;

/**
 * View a single makan
 */
public class MakanViewActivity extends SharkNetActivity {
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;

    private MakanViewContentAdapter mAdapter;

    private CharSequence topicUri = null;
    private CharSequence name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(this.getLogStart(), "onCreate");

        // get parameters
        try {
            MakanIntent intent = new MakanIntent(this.getIntent());
            this.topicUri = intent.getUri();
            this.name = intent.getUserFriendlyName();

        } catch (SharkException e) {
            Log.d(this.getLogStart(), "cannot create makan view intent from intent (fatal): "
                    + e.getLocalizedMessage());

            return;
        }

        try {
            setContentView(R.layout.makan_view_drawer_layout);

            SharkNetApp.getSharkNetApp(this).setupDrawerLayout(this);

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
                    this.topicUri, this.name,
                    SharkNetApp.getOwner(), SharkNetApp.getSharkNetApp(this).getIdentityStorage());

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

  //      MakanApp.getMakanApp().startAASPBroadcastReceiver(this, this.topicUri);
    }


    public void doExternalChange(int era, String user, String folder) {
        // external changes happen
        this.mAdapter.setOutdated(era, user, folder);
        // TODO: refresh view
    }

    /**
     * Activity is resumed. Assume changes in data set.
     */
    protected void onResume() {
        super.onResume();
        Log.d(this.getLogStart(), "onResume");
        if(mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }

//        MakanApp.getMakanApp().startAASPBroadcastReceiver(this, this.topicUri);
    }

    private void sync() {
        this.mAdapter.sync();
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

        MakanIntent intent = new MakanIntent(this,
                this.name, this.topicUri,
                MakanAddMessageActivity.class);


        startActivity(intent);
    }
}
