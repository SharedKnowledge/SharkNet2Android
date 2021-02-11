package net.sharksystem.messenger.android;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import net.sharksystem.R;
import net.sharksystem.android.ASAPChannelIntent;
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
    }

    public void onClick(View view) {
        if(view == this.findViewById(R.id.snChannelAddDoAdd)) {
            EditText uriText = this.findViewById(R.id.snChannelAddURI);
            EditText nameText = this.findViewById(R.id.snChannelAddName);

            try {
                SharkNetApp.getSharkNetApp().getSharkMessenger().createChannel(
                        uriText.getText(),
                        nameText.getText()
                );

                // view new channel
                Intent intent = new ASAPChannelIntent(this, nameText.toString(),
                        uriText.toString(), SNChannelViewActivity.class);

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
