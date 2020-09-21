package net.sharksystem.asap.sharknet.android;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import net.sharksystem.R;
import net.sharksystem.SharkException;
import net.sharksystem.android.ASAPChannelIntent;
import net.sharksystem.asap.ASAPException;
import net.sharksystem.asap.sharknet.InMemoSNMessage;
import net.sharksystem.asap.sharknet.SNMessage;
import net.sharksystem.crypto.BasicKeyStore;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class SNChannelAddMessageActivity extends SNChannelsActivity {
    private CharSequence name = null;
    private CharSequence uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // check permissions
//        MakanApp.askForPermissions(this);

        setContentView(R.layout.sn_channel_add_message);

        ASAPChannelIntent intent;
        try {
            intent = new ASAPChannelIntent(this.getIntent());
            this.name = intent.getName();
            this.uri = intent.getUri();
        } catch (SharkException e) {
            Log.d(this.getLogStart(), "cannot create asap channel intent - fatal");
            return;
        }

        Log.d(this.getLogStart(), "created with name: " + this.name + " / " + this.uri);

        TextView topicTextView = (TextView) findViewById(R.id.makanName);
        topicTextView.setText(this.name);
    }

    public void onAddClick(View view) {
        // get new message
        EditText messageTextView = (EditText) findViewById(R.id.makanMessage);

        String messageText = messageTextView.getText().toString();

        if (messageText == null || messageText.isEmpty()) {
            Toast.makeText(this, "message is empty", Toast.LENGTH_SHORT).show();
        } else {
            try {

                // let's sort things out.
                CharSequence topic = this.uri;
                CharSequence sender =
                        SNChannelsComponent.getSharkNetChannelComponent().getOwnerID();
                Set<CharSequence> recipients = new HashSet<>();
                recipients.add("Alice"); // TODO must come from GUI
                boolean sign = false; // TODO must come from GUI
                boolean encrypt = false;  // TODO must come from GUI
                BasicKeyStore basicKeyStore =
                        SNChannelsComponent.getSharkNetChannelComponent().getBasicKeyStore();

                byte[] content = "TestContent".getBytes();  // TODO must come from GUI

                byte[] serializedMessage = null;
                    serializedMessage = InMemoSNMessage.serializeMessage(
                            content, sender, recipients, sign, encrypt, basicKeyStore);

                // deliver as asap message
                this.sendASAPMessage(SNChannelsComponent.APP_NAME, this.uri,
                        serializedMessage, true);

            } catch (ASAPException | IOException e) {
                Log.d(this.getLogStart(), "problems when sending makan message: "
                        + e.getLocalizedMessage());
            }
        }

        // done here
        this.finish();
    }

    public void onAbortClick(View view) {
        ASAPChannelIntent intent = new ASAPChannelIntent(this, this.name, this.uri,
                SNChannelViewActivity.class);
        startActivity(intent);
    }
}
