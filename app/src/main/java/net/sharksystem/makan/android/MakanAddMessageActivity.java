package net.sharksystem.makan.android;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import net.sharksystem.R;
import net.sharksystem.SharkException;
import net.sharksystem.makan.InMemoMakanMessage;
import net.sharksystem.sharknet.android.SharkNetApp;

import java.util.Date;

public class MakanAddMessageActivity extends AppCompatActivity {
    private static final String LOGSTART = "MakanAddMessage";
    private CharSequence name = null;
    private CharSequence uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // check permissions
        MakanApp.askForPermissions(this);

        setContentView(R.layout.makan_add_message);

        MakanIntent intent;
        try {
            intent = new MakanIntent(this.getIntent());
            this.name = intent.getUserFriendlyName();
            this.uri = intent.getUri();
        } catch (SharkException e) {
            Log.d(LOGSTART, "cannot create makan intent - fatal");
            return;
        }

        Log.d(LOGSTART, "created with name: " + this.name + " / " + this.uri);

        TextView topicTextView = (TextView) findViewById(R.id.makanName);
        topicTextView.setText(this.name);
    }

    public void onAddClick(View view) {
        // get new message
        EditText messageTextView = (EditText) findViewById(R.id.makanMessage);

        String messageText = messageTextView.getText().toString();

        if(messageText == null || messageText.isEmpty()) {
            Toast.makeText(this, "message is empty", Toast.LENGTH_SHORT).show();
        } else {
            SharkNetApp sharkNetApp = SharkNetApp.getSharkNetApp(this);

            InMemoMakanMessage makanMessage =
                    new InMemoMakanMessage(sharkNetApp.getOwnerID(), messageText, new Date());

            CharSequence serializedMessage = makanMessage.getSerializedMessage();

            Log.d(LOGSTART, "send serialized makan message");
            Log.d(LOGSTART, serializedMessage.toString());

            SharkNetApp.getSharkNetApp(this).sendASAPMessage(this, this.uri, serializedMessage);
        }

        MakanIntent intent = new MakanIntent(this, this.name, this.uri, MakanViewActivity.class);
        startActivity(intent);
    }

    public void onAbortClick(View view) {
        MakanIntent intent = new MakanIntent(this, this.name, this.uri, MakanViewActivity.class);
        startActivity(intent);
    }

    protected void onPause() {
        super.onPause();
        SharkNetApp.getSharkNetApp(this).onPause();
    }

    protected void onDestroy() {
        super.onDestroy();
        SharkNetApp.getSharkNetApp(this).onDestroy();
    }
}

