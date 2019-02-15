package net.sharksystem.bubble.android;

import android.Manifest;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import net.sharksystem.R;
import net.sharksystem.aasp.AASPException;
import net.sharksystem.android.util.PermissionCheck;
import net.sharksystem.bubble.model.BubbleMessageStorage;

import java.io.IOException;

public class BubbleCreateActivity extends AppCompatActivity {
    private CharSequence topic = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // check permissions
        BubbleApp.askForPermissions(this);

        setContentView(R.layout.bubble_create);

        this.topic = BubbleApp.getTopicNameFromIntentExtras(this.getIntent());

        TextView topicTextView = (TextView) findViewById(R.id.bubbleCreateTopicName);
        topicTextView.setText(topic);
    }

    public void onAddClick(View view) throws IOException, AASPException {
        // get new message
        EditText messageTextView = (EditText) findViewById(R.id.bubbleCreateMessageText);

        String messageText = messageTextView.getText().toString();

        if(messageText == null || messageText.isEmpty()) {
            Toast.makeText(this, "message is empty", Toast.LENGTH_SHORT).show();
        } else {
            BubbleMessageStorage storage = BubbleApp.getBubbleMessageStorage(this, this.topic);
            storage.addMessage(this.topic, "DummyUser", messageText); // got place for async task ?!
        }

        Intent intent = new Intent(this, BubbleViewActivity.class);
        intent.putExtra(BubbleApp.EXTRA_TOPIC_KEY, this.topic);
        startActivity(intent);
    }

    public void onAbortClick(View view) {
        Intent intent = new Intent(this, BubbleViewActivity.class);
        intent.putExtra(BubbleApp.EXTRA_TOPIC_KEY, this.topic);
        startActivity(intent);
    }
}
