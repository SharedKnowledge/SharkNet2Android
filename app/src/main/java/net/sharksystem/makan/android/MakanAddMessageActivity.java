package net.sharksystem.makan.android;

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
import net.sharksystem.makan.InMemoMakanMessage;
import net.sharksystem.sharknet.android.SharkNetActivity;
import net.sharksystem.sharknet.android.SharkNetApp;

import java.util.Date;

public class MakanAddMessageActivity extends SharkNetActivity {
    private CharSequence name = null;
    private CharSequence uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // check permissions
        MakanApp.askForPermissions(this);

        setContentView(R.layout.makan_add_message);

        ASAPChannelIntent intent;
        try {
            intent = new ASAPChannelIntent(this.getIntent());
            this.name = intent.getName();
            this.uri = intent.getUri();
        } catch (SharkException e) {
            Log.d(this.getLogStart(), "cannot create makan intent - fatal");
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
                // send message via ASAP Service to leverage online exchange etc. pp.
                InMemoMakanMessage makanMessage = new InMemoMakanMessage(
                        SharkNetApp.getSharkNetApp().getOwnerID(),
                        messageText,
                        new Date()
                );

                this.sendASAPMessage(MakanApp.APP_NAME, this.uri,
                        makanMessage.getSerializedASAPMessageAsBytes(), true);
/*
                Makan makan = MakanApp.getMakanApp().getMakanStorage().getMakan(this.uri);
                Log.d(this.getLogStart(), "store makan message: " + this.uri + " | " + messageText);
                makan.addMessage(messageText);
 */
            } catch (ASAPException e) {
                Log.d(this.getLogStart(), "problems when sending makan message: "
                        + e.getLocalizedMessage());
            }
        }

        // done here
        this.finish();
    }

    public void onAbortClick(View view) {
        ASAPChannelIntent intent = new ASAPChannelIntent(this, this.name, this.uri, MakanViewActivity.class);
        startActivity(intent);
    }
}
