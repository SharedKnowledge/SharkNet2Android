package net.sharksystem.makan.android;

import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import net.sharksystem.R;
import net.sharksystem.SharkException;
import net.sharksystem.asap.android.apps.ASAPUriContentChangedListener;
import net.sharksystem.makan.android.viewadapter.MakanViewContentAdapter;
import net.sharksystem.sharknet.android.SharkNetActivity;
import net.sharksystem.sharknet.android.SharkNetApp;

/**
 * View a single makan
 */
public class MakanViewActivity extends SharkNetActivity implements ASAPUriContentChangedListener {
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;

    private MakanViewContentAdapter mAdapter;

    private CharSequence topicUri = null;
    private CharSequence name;

    public MakanViewActivity() {
        super(SharkNetApp.getSharkNetApp());
    }

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
                    this.topicUri, this.name, this.getSharkNetApp().getASAPOwnerID());

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

        MakanIntent intent = new MakanIntent(this,
                this.name, this.topicUri,
                MakanAddMessageActivity.class);


        startActivity(intent);
    }

    private boolean listenerSet = false;
    protected void onStart() {
        super.onStart();

        // listen to chunk receiver
        if(!this.listenerSet) {
            this.getSharkNetApp().addASAPUriContentChangedListener(MakanApp.APP_NAME, this);
            this.listenerSet = true;
        }
    }

    protected void onResume() {
        super.onResume();
        Log.d(this.getLogStart(), "onResume");

        this.resetAdapter(); // that pretty resource intensive but works
        //mAdapter.notifyDataSetChanged(); // simple refresh

        // listen to chunk receiver
        if(!this.listenerSet) {
            this.getSharkNetApp().addASAPUriContentChangedListener(MakanApp.APP_NAME, this);
            this.listenerSet = true;
        }
    }

    protected void onPause() {
        super.onPause();

        // stop listening to chunk receiver
        this.getSharkNetApp().removeASAPUriContentChangedListener(MakanApp.APP_NAME, this);
        this.listenerSet = false;
    }

    protected void onStop() {
        // Unbind from the service
        super.onStop();

        // stop listening to chunk receiver
        this.getSharkNetApp().removeASAPUriContentChangedListener(MakanApp.APP_NAME, this);
        this.listenerSet = false;
    }

    protected void onDestroy() {
        super.onDestroy();

        // stop listening to chunk receiver
        this.getSharkNetApp().removeASAPUriContentChangedListener(MakanApp.APP_NAME, this);
        this.listenerSet = false;
    }

    private void resetAdapter() {
        // reset adapter to get access to new data
        try {
            mAdapter = new MakanViewContentAdapter(this,
                    this.topicUri, this.name, this.getSharkNetApp().getASAPOwnerID());
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
