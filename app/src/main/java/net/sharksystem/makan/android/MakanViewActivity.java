package net.sharksystem.makan.android;

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
import net.sharksystem.aasp.AASPException;
import net.sharksystem.bubble.BubbleMessage;
import net.sharksystem.bubble.android.BubbleAppAndroid;
import net.sharksystem.bubble.android.BubbleMessageContentAdapter;
import net.sharksystem.makan.MakanException;
import net.sharksystem.sharknet.android.SharkNetApp;

import java.io.IOException;

/**
 * View a single makan -- TODO
 */
public class MakanViewActivity extends AppCompatActivity {
    private static final String LOGSTART = "MakanView";
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;

    private BubbleMessageContentAdapter mAdapter;

    private CharSequence topic = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOGSTART, "onCreate");

        // get parameters
        try {
            new MakanViewIntent(this.getIntent());
        } catch (MakanException e) {
            Log.d(LOGSTART, "cannot create makan view intent from intent (fatal): "
                    + e.getLocalizedMessage());

            return;
        }

        // check permissions
        BubbleAppAndroid.askForPermissions(this);

        // activate broadcast receiver for imcomming messages
        SharkNetApp.getSharkNetApp(this).startAASPBroadcastReceiver();


        this.topic = BubbleAppAndroid.getTopicNameFromIntentExtras(this.getIntent());

        if(this.topic == null) {
            this.topic = BubbleMessage.ANY_TOPIC;
        }

        try {
//        setContentView(R.layout.activity_main);
//            setContentView(R.layout.bubble_with_toolbar);

            setContentView(R.layout.makan_list_drawer_layout);

            SharkNetApp.getSharkNetApp(this).setupDrawerLayout(this);

            ////////////////////////////////////////////////////////////////////////
            //                         prepare action bar                         //
            ////////////////////////////////////////////////////////////////////////
            // setup toolbar
            Toolbar myToolbar = (Toolbar) findViewById(R.id.makan_list_toolbar);
            setSupportActionBar(myToolbar);

            ////////////////////////////////////////////////////////////////////////
            //                         prepare recycler view                      //
            ////////////////////////////////////////////////////////////////////////

            mRecyclerView = (RecyclerView) findViewById(R.id.bubble_list_recycler_view);

            mAdapter = new BubbleMessageContentAdapter(this, this.topic);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            mRecyclerView.setAdapter(mAdapter);
        }
        catch(Exception e) {
            int i = 42;
        }

    }

    /**
     * Activity is resumed. Assume changes in data set.
     */
    protected void onResume() {
        super.onResume();
        Log.d(LOGSTART, "onResume");
/*
        //mAdapter.notifyItemInserted(1);
        if(mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }

        SharkNetApp.getSharkNetApp(this).startAASPBroadcastReceiver();
        */
    }

    protected void onPause() {
        super.onPause();
        Log.d(LOGSTART, "onPause");
//        SharkNetApp.getSharkNetApp(this).stopAASPBroadcastReceiver();
    }

    protected void onDestroy() {
        super.onDestroy();
        Log.d(LOGSTART, "onDestroy");
//        SharkNetApp.getSharkNetApp(this).stopAASPBroadcastReceiver();
    }

    /////////////////////////////////////////////////////////////////////////////////
    //                              bubble toolbar methods                         //
    /////////////////////////////////////////////////////////////////////////////////

    /**
     * connect menu with menu items and make them visible
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.makan_list_action_buttons, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        try {
            switch (item.getItemId()) {
                case R.id.makanMenuAddButton:
                    this.doAddMakan();
                    return true;

                case R.id.makanMenuRemoveAllButton:
                    this.doRemoveAll();
                    // force adapter to refresh ui
                    this.mAdapter.notifyDataSetChanged();
                    return true;

                default:
                    // If we got here, the user's action was not recognized.
                    // Invoke the superclass to handle it.
                    return super.onOptionsItemSelected(item);
            }
        }
        catch(Exception e) {
            Log.d(LOGSTART, e.getLocalizedMessage());
        }

        return false;
    }

    private void doAddMakan() {
        String sampleLine = Long.toString(System.currentTimeMillis());
        Log.d("debugLog", "doBubbleCalled");

        Toast.makeText(this, "add makan called - NYI", Toast.LENGTH_SHORT);
/*
        Intent intent = new Intent(this, BubbleCreateActivity.class);

        intent.putExtra(BubbleAppAndroid.EXTRA_TOPIC_KEY, this.uriTextView);

        startActivity(intent);
*/
        // this.chat.addLine(sampleLine);
    }

    private void doRemoveAll() throws IOException, AASPException {
        String sampleLine = Long.toString(System.currentTimeMillis());
        Log.d(LOGSTART, "doRemoveAll called");

        Toast.makeText(this, "remove all makan called - NYI", Toast.LENGTH_SHORT);
    }
}
