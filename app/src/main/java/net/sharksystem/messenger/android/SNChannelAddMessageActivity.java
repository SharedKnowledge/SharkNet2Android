package net.sharksystem.messenger.android;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import net.sharksystem.R;
import net.sharksystem.SharkException;
import net.sharksystem.android.ASAPChannelIntent;
import net.sharksystem.asap.ASAPException;
import net.sharksystem.messenger.SharkMessengerException;
import net.sharksystem.pki.android.PersonListSelectionActivity;
import net.sharksystem.pki.android.PersonStatusHelper;
import net.sharksystem.sharknet.android.SharkNetActivity;
import net.sharksystem.sharknet.android.SharkNetApp;

import java.io.IOException;
import java.util.Set;

public class SNChannelAddMessageActivity extends SharkNetActivity {
    private CharSequence name = null;
    private CharSequence uri;
    private Set<CharSequence> selectedRecipients = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

        TextView topicTextView = (TextView) findViewById(R.id.fragment_add_message_channel_name);
        topicTextView.setText(this.name);
    }

    public void onSelectRecipients(View view) {
        PersonStatusHelper.getPersonsStorage().setPreselectionSet(this.selectedRecipients);
        //Log.d(this.getLogStart(), "setPreselected: " + this.selectedRecipients);
        Intent intent = new Intent(this, PersonListSelectionActivity.class);
        this.startActivity(intent);
    }

    public void onEncryptedClick(View view) {
        this.redrawComments();
    }

    private void redrawComments() {
        TextView commentsTextView = findViewById(R.id.fragment_add_message_comments);
        commentsTextView.setText("");

        CheckBox encryptedCheckBox = findViewById(R.id.fragment_add_message_encrypted_checkbox);

        if(this.selectedRecipients != null && this.selectedRecipients.size() > 1) {
            if (encryptedCheckBox.isChecked()) {
                commentsTextView.setText("each recipient gets its own encrypted message");
            }
        }

        if(this.selectedRecipients == null || this.selectedRecipients.isEmpty()) {
            if(encryptedCheckBox.isChecked()) {
                encryptedCheckBox.setChecked(false);
                Toast.makeText(this,
                        "select recipient(s) - cannot encrypt message to anybody",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void onRemoveRecipients(View view) {
        this.selectedRecipients = null;
        PersonStatusHelper.getPersonsStorage().setPreselectionSet(null);
        this.redrawRecipientList();
    }

    public void onSendClick(View view) {
        // get new message
        EditText messageTextView = (EditText) findViewById(R.id.fragment_add_message_message_input);

        String messageText = messageTextView.getText().toString();

        if (messageText == null || messageText.isEmpty()) {
            Toast.makeText(this, "message is empty", Toast.LENGTH_SHORT).show();
        } else {
            try {
                // let's sort things out.
                byte[] content = messageText.getBytes();

                CheckBox signCheckBox = findViewById(R.id.fragment_add_message_signed_checkbox);
                boolean sign = signCheckBox.isChecked();

                CheckBox encryptedCheckBox = findViewById(R.id.fragment_add_message_encrypted_checkbox);
                boolean encrypt = encryptedCheckBox.isChecked();

                // send with shark messenger
                this.getSharkNetApp().getSharkMessenger().sendSharkMessage(
                        content, this.uri, sign, encrypt);

            } catch (IOException | SharkMessengerException e) {
                Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                Log.d(this.getLogStart(), "problems when sending message in SNChannel: "
                        + e.getLocalizedMessage());
                e.printStackTrace();
            }
        }

        // we are done here
        this.finish();
    }

    public void onAbortClick(View view) {
        ASAPChannelIntent intent = new ASAPChannelIntent(this, this.name, this.uri,
                SNChannelViewActivity.class);
        startActivity(intent);
    }

    private void redrawRecipientList() {
        String recipientsString = "anybody";

        if(this.selectedRecipients != null && !this.selectedRecipients.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            boolean first = true;
            for(CharSequence recipient : this.selectedRecipients) {
                CharSequence recipientName = recipient;
                try {
                    recipientName = SharkNetApp.getSharkNetApp().getSharkPKI().
                            getPersonValuesByID(recipient).getName();
                } catch (ASAPException e) {
                    // name not found
                }

                if(first) {
                    first = false;
                } else {
                    sb.append(" | ");
                }
                sb.append(recipientName);
            }
            recipientsString = sb.toString();
        }

        TextView tv = this.findViewById(R.id.fragment_add_message_message_recipients);
        tv.setText(recipientsString);

        this.redrawComments();
    }

    @Override
    protected void onResume() {
        super.onResume();

        this.selectedRecipients =
                PersonStatusHelper.getPersonsStorage().getLastPersonsSelection();

        this.redrawRecipientList();
    }
}
