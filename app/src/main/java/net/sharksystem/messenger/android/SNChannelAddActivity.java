package net.sharksystem.messenger.android;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import net.sharksystem.R;
import net.sharksystem.android.ASAPChannelIntent;
import net.sharksystem.asap.utils.PeerIDHelper;
import net.sharksystem.messenger.SharkMessengerException;
import net.sharksystem.sharknet.android.SharkNetActivity;
import net.sharksystem.sharknet.android.SharkNetApp;

import java.io.IOException;

public class SNChannelAddActivity extends SharkNetActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(this.getLogStart(), "onCreate");

        setContentView(R.layout.sn_channel_add_drawer_layout);

        this.getSharkNetApp().setupDrawerLayout(this);

        TextView tvUri = this.findViewById(R.id.snChannelAddURI);

        String uniqueID = PeerIDHelper.createUniqueID();
        String channelUri = "sn2://" + uniqueID;
        tvUri.setText(channelUri);
    }

    public void onClick(View view) {
        if(view == this.findViewById(R.id.snChannelAddDoAdd)) {
            EditText nameText = this.findViewById(R.id.snChannelAddName);
            String channelNameString = nameText.getEditableText().toString();
            TextView tvUri = this.findViewById(R.id.snChannelAddURI);
            String uriString = tvUri.getText().toString();

            try {
                SharkNetApp.getSharkNetApp().getSharkMessenger().createChannel(
                        uriString, channelNameString
                );

                // view new channel
                Intent intent = new ASAPChannelIntent(this, nameText.toString(),
                        uriString, SNChannelViewActivity.class);

                this.startActivity(intent);

            } catch (IOException e) {
                String text = "failure: " + e.getLocalizedMessage();
                Log.e(this.getLogStart(), text);
                Toast.makeText(this, "IO error - that's serious", Toast.LENGTH_SHORT).show();
            } catch (SharkMessengerException e) {
                String text = "failure: " + e.getLocalizedMessage();
                Log.w(this.getLogStart(), text);
                Toast.makeText(this, "already exists(?)", Toast.LENGTH_SHORT).show();
            }
            finally {
                this.finish();
            }
        }

        // go back after catched exceptions
        Intent intent = new Intent(this, SNChannelsListActivity.class);
        this.startActivity(intent);
    }
}
