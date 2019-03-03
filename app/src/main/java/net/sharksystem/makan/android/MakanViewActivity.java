package net.sharksystem.makan.android;

import android.content.Intent;
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

import net.sharksystem.R;
import net.sharksystem.aasp.AASPException;
import net.sharksystem.bubble.BubbleMessage;
import net.sharksystem.bubble.android.BubbleAppAndroid;
import net.sharksystem.bubble.android.BubbleCreateActivity;
import net.sharksystem.bubble.android.BubbleMessageContentAdapter;
import net.sharksystem.bubble.model.BubbleMessageStorage;
import net.sharksystem.makan.MakanException;
import net.sharksystem.sharknet.android.SharkNetApp;

import java.io.IOException;

public class MakanViewActivity extends AppCompatActivity {
    private static final String LOGSTART = "BubbleView";
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

        // HIER WEITERMACHEN...

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

            setContentView(R.layout.bubble_drawer_layout);

            SharkNetApp.getSharkNetApp(this).setupDrawerLayout(this);

            ////////////////////////////////////////////////////////////////////////
            //                         prepare action bar                         //
            ////////////////////////////////////////////////////////////////////////
            // setup toolbar
            Toolbar myToolbar = (Toolbar) findViewById(R.id.bubble_main_actionbar);
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

        /*
        // debugging
        BubbleMessageStorage storage = null;
        try {
            storage = BubbleApp.getBubbleMessageStorage(this, this.topic);
            storage.addMessage(this.topic, "DummyUser", "text 0"); // got place for async task ?!

//            mAdapter.notifyItemInserted(0);

            // debug: do it twice
            storage.addMessage(this.topic, "DummyUser", "text 1"); // got place for async task ?!
//            mAdapter.notifyItemInserted(1);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ASP3Exception e) {
            e.printStackTrace();
        }
*/

    }

    /**
     * Activity is resumed. Assume changes in data set.
     */
    protected void onResume() {
        super.onResume();
        Log.d(LOGSTART, "onResume");

        //mAdapter.notifyItemInserted(1);
        if(mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }

        SharkNetApp.getSharkNetApp(this).startAASPBroadcastReceiver();
    }

    protected void onPause() {
        super.onPause();
        Log.d(LOGSTART, "onPause");
        SharkNetApp.getSharkNetApp(this).stopAASPBroadcastReceiver();
    }

    protected void onDestroy() {
        super.onDestroy();
        Log.d(LOGSTART, "onDestroy");
        SharkNetApp.getSharkNetApp(this).stopAASPBroadcastReceiver();
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
        inflater.inflate(R.menu.bubble_action_buttons, menu);
        return true;
    }

    private void doAddBubble() {
        String sampleLine = Long.toString(System.currentTimeMillis());
        Log.d("debugLog", "doBubbleCalled");

        Intent intent = new Intent(this, BubbleCreateActivity.class);

        intent.putExtra(BubbleAppAndroid.EXTRA_TOPIC_KEY, this.topic);

        startActivity(intent);

        // this.chat.addLine(sampleLine);
    }

    private void doRemoveAll() throws IOException, AASPException {
        String sampleLine = Long.toString(System.currentTimeMillis());
        Log.d(LOGSTART, "doRemoveAll called");

        BubbleMessageStorage bubbleStorage = BubbleAppAndroid.getBubbleMessageStorage(this);
        bubbleStorage.removeAllMessages();
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        try {
            switch (item.getItemId()) {
                case R.id.bubbleAddBubble:
                    Log.d(LOGSTART, "going to add bubble");
                    this.doAddBubble();
                    return true;

                case R.id.bubbleRemoveAll:
                    Log.d(LOGSTART, "going to remove all");
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
}
