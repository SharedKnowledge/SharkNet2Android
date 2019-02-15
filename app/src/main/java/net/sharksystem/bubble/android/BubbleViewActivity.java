package net.sharksystem.bubble.android;

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

public class BubbleViewActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;

    private BubbleMessageContentAdapter mAdapter;

    private CharSequence topic = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // check permissions
        BubbleApp.askForPermissions(this);

        this.topic = BubbleApp.getTopicNameFromIntentExtras(this.getIntent());

        try {
//        setContentView(R.layout.activity_main);
//            setContentView(R.layout.bubble_with_toolbar);
            setContentView(R.layout.sharknet_drawer);

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

            mAdapter = new BubbleMessageContentAdapter(this,topic);
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

        //mAdapter.notifyItemInserted(1);
        mAdapter.notifyDataSetChanged();
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

        intent.putExtra(BubbleApp.EXTRA_TOPIC_KEY, this.topic);

        startActivity(intent);

        // this.chat.addLine(sampleLine);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.bubble:
                this.doAddBubble();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }
}
