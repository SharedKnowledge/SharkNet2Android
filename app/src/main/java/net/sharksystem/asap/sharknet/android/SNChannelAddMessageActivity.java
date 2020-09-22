package net.sharksystem.asap.sharknet.android;

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
import net.sharksystem.asap.sharknet.InMemoSNMessage;
import net.sharksystem.crypto.BasicKeyStore;
import net.sharksystem.persons.android.PersonListSelectionActivity;
import net.sharksystem.persons.android.PersonsStorageAndroidComponent;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class SNChannelAddMessageActivity extends SNChannelsActivity {
    private CharSequence name = null;
    private CharSequence uri;
    private Set<CharSequence> selectedRecipients = null;

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

        TextView topicTextView = (TextView) findViewById(R.id.snChannelName);
        topicTextView.setText(this.name);
    }

    public void onSelectRecipients(View view) {
        PersonsStorageAndroidComponent.getPersonsStorage().setPreselectionSet(this.selectedRecipients);
        //Log.d(this.getLogStart(), "setPreselected: " + this.selectedRecipients);
        Intent intent = new Intent(this, PersonListSelectionActivity.class);
        this.startActivity(intent);
    }

    public void onEncryptedClick(View view) {
        this.redrawComments();
    }

    private void redrawComments() {
        TextView commentsTextView = findViewById(R.id.comments);
        commentsTextView.setText("");

        CheckBox encryptedCheckBox = findViewById(R.id.snEncrypted);

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
        PersonsStorageAndroidComponent.getPersonsStorage().setPreselectionSet(null);
        this.redrawRecipientList();
    }

    public void onSendClick(View view) {
        // get new message
        EditText messageTextView = (EditText) findViewById(R.id.snMessage);

        String messageText = messageTextView.getText().toString();

        if (messageText == null || messageText.isEmpty()) {
            Toast.makeText(this, "message is empty", Toast.LENGTH_SHORT).show();
        } else {
            try {
                // let's sort things out.
                CheckBox signCheckBox = findViewById(R.id.snSigned);
                boolean sign = signCheckBox.isChecked();

                CheckBox encryptedCheckBox = findViewById(R.id.snEncrypted);
                boolean encrypt = encryptedCheckBox.isChecked();

                byte[] content = messageText.getBytes();

                // lets produce and send asap messages
                // 1) no recipients
                if(this.selectedRecipients == null || this.selectedRecipients.isEmpty()) {
                    this.compileAndSendASAPMessage(content, null, sign, encrypt);
                } else {
                    // 2) no encryption or just a single recipient
                    if(!encrypt || this.selectedRecipients.size() == 1) {
                        this.compileAndSendASAPMessage(content, sign, encrypt);
                    } else {
                        // 3) encrypt more than one recipient - each gets its own message
                        for(CharSequence receiver : this.selectedRecipients) {
                            this.compileAndSendASAPMessage(content, receiver, sign, encrypt);
                        }
                    }
                }
            } catch (ASAPException | IOException e) {
                Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                Log.d(this.getLogStart(), "problems when sending message in SNChannel: "
                        + e.getLocalizedMessage());
                e.printStackTrace();
            }
        }

        // done here
        this.finish();
    }

    private void compileAndSendASAPMessage(byte[] content, boolean sign, boolean encrypt)
            throws IOException, ASAPException {
        CharSequence sender =
                SNChannelsComponent.getSharkNetChannelComponent().getOwnerID();
        BasicKeyStore basicKeyStore =
                SNChannelsComponent.getSharkNetChannelComponent().getBasicKeyStore();
        byte[] serializedMessage = InMemoSNMessage.serializeMessage(
                content, sender, this.selectedRecipients, sign, encrypt, basicKeyStore);
        // deliver as asap message
        this.sendASAPMessage(SNChannelsComponent.APP_NAME, this.uri,
                serializedMessage, true);
    }

    private void compileAndSendASAPMessage(byte[] content, CharSequence recipient,
                           boolean sign, boolean encrypt)
            throws IOException, ASAPException {
        CharSequence sender =
                SNChannelsComponent.getSharkNetChannelComponent().getOwnerID();
        BasicKeyStore basicKeyStore =
                SNChannelsComponent.getSharkNetChannelComponent().getBasicKeyStore();
        byte[] serializedMessage = InMemoSNMessage.serializeMessage(
                content, sender, recipient, sign, encrypt, basicKeyStore);
        // deliver as asap message
        this.sendASAPMessage(SNChannelsComponent.APP_NAME, this.uri,
                serializedMessage, true);
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
                    recipientName = SNChannelsComponent.getSharkNetChannelComponent().
                            getPersonName(recipient);
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

        TextView tv = this.findViewById(R.id.snMessageRecipients);
        tv.setText(recipientsString);

        this.redrawComments();
    }

    @Override
    protected void onResume() {
        super.onResume();

        this.selectedRecipients =
                PersonsStorageAndroidComponent.getPersonsStorage().getLastPersonsSelection();

        this.redrawRecipientList();
    }
}
