package net.sharksystem.messenger.android;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import net.sharksystem.R;
import net.sharksystem.app.messenger.SharkMessengerException;
import net.sharksystem.sharknet.android.SharkNetActivity;

import java.io.IOException;

public class SNChannelsListActivity extends SharkNetActivity {
    private RecyclerView mRecyclerView;

    public SNChannelsListActivity() {
        super(); // need a breakpoint
    }
    private SNChannelsListContentAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(this.getLogStart(), "onCreate");

        try {
            setContentView(R.layout.sn_channels_list_drawer_layout);
            this.getSharkNetApp().setupDrawerLayout(this);

            ////////////////////////////////////////////////////////////////////////
            //                         prepare action bar                         //
            ////////////////////////////////////////////////////////////////////////
            // setup toolbar
            Toolbar myToolbar = (Toolbar) findViewById(R.id.sn_channel_list_with_toolbar);
            setSupportActionBar(myToolbar);

            ////////////////////////////////////////////////////////////////////////
            //                         prepare recycler view                      //
            ////////////////////////////////////////////////////////////////////////

            mRecyclerView = (RecyclerView) findViewById(R.id.sn_channel_list_recycler_view);

            mAdapter = new SNChannelsListContentAdapter(this);
            RecyclerView.LayoutManager mLayoutManager =
                    new LinearLayoutManager(getApplicationContext());

            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            mRecyclerView.setAdapter(mAdapter);
            Log.d(this.getLogStart(), "attached content adapter");
        }
        catch(Exception e) {
            Log.d(this.getLogStart(), "problems while attaching content adapter: "
                    + e.getLocalizedMessage());
            // debug break
            int i = 42;
        }

    }

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
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sn_channel_list_action_buttons, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            if(item.getItemId() == R.id.snAddChannelButton) {
                this.doAddChannel();
                return true;
            } else if(item.getItemId() == R.id.snRemoveAllChannelButton) {
                this.doRemoveAll();
                // force adapter to refresh ui
                this.mAdapter.notifyDataSetChanged();
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
    //                                     life cycle                                          //
    /////////////////////////////////////////////////////////////////////////////////////////////

    protected void onResume() {
        super.onResume();
        Log.d(this.getLogStart(), "onResume");

        if(mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////
    //                                call shark messenger app logic                           //
    /////////////////////////////////////////////////////////////////////////////////////////////

    private void doAddChannel() {
        String sampleLine = Long.toString(System.currentTimeMillis());
        Log.d(this.getLogStart(), "doAddChannel");

        Intent intent = new Intent(this, SNChannelAddActivity.class);
        this.startActivity(intent);
    }

    private void doRemoveAll() throws IOException, SharkMessengerException {
        String sampleLine = Long.toString(System.currentTimeMillis());
        Log.e(this.getLogStart(), "doRemoveAll called - TODO - nyi");

        //SharkNetApp.getSharkNetApp().getSharkMessenger().removeAllChannels();

        Toast.makeText(this, "done - removed all channels - TODO", Toast.LENGTH_SHORT).show();
    }

//    @Override
    public void asapUriContentChanged(CharSequence changedUri) {
        // content in a changed - could set something to make it visible - but at least redraw
        Log.d(this.getLogStart(), "content in a uri changed redraw view (could highlight changed makan in some later versions)");
        mAdapter.notifyDataSetChanged();
    }

}
