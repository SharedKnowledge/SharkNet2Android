package net.sharksystem.messenger.android;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import net.sharksystem.R;
import net.sharksystem.SharkException;
import net.sharksystem.asap.android.apps.ASAPActivity;
import net.sharksystem.messenger.SharkMessage;
import net.sharksystem.messenger.SharkMessengerComponent;
import net.sharksystem.messenger.SharkMessengerException;
import net.sharksystem.sharknet.android.SharkNetActivity;
import net.sharksystem.sharknet.android.SharkNetApp;

import java.io.IOException;

public class SNMessageViewActivity extends ASAPActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(this.getLogStart(), "onCreate");

        setContentView(R.layout.sn_message_view_drawer_layout);

        SharkNetApp.getSharkNetApp().setupDrawerLayout(this);

        // fill GUI with actual data
        try {
            SNMessageIntent snMessageIntent = new SNMessageIntent(this.getIntent());

            CharSequence uri = snMessageIntent.getUri();
            int position = snMessageIntent.getPosition();

            SharkMessengerComponent sharkMessenger =
                    SharkNetApp.getSharkNetApp().getSharkMessenger();

            SharkMessage sharkMessage = sharkMessenger.getChannel(uri)
                    .getMessages(false, true)
                    .getSharkMessage(position, false);

            // Receivers
            CharSequence receiversCharSequence =
                    SNMessageViewHelper.getReceiversCharSequence(sharkMessage);
            TextView receiversView = this.findViewById(R.id.sn_message_receivers);
            receiversView.setText(receiversCharSequence);

            // encrypted
            CharSequence encryptedCharSequence =
                    SNMessageViewHelper.getEncryptedCharSequence(sharkMessage);
            TextView encryptedView = this.findViewById(R.id.sn_message_encrypted);
            encryptedView.setText(encryptedCharSequence);

            // sender
            CharSequence senderCharSequence =
                    SNMessageViewHelper.getSenderCharSequence(sharkMessage);
            TextView senderView = this.findViewById(R.id.sn_message_sender);
            senderView.setText(senderCharSequence);

            // content
            CharSequence contentCharSequence =
                    SNMessageViewHelper.getContentCharSequence(sharkMessage);
            TextView contentView = this.findViewById(R.id.sn_message_content);
            contentView.setText(contentCharSequence);

            // verified
            CharSequence verifiedCharSequence =
                    SNMessageViewHelper.getVerifiedCharSequence(sharkMessage);
            TextView verifiedView = this.findViewById(R.id.sn_message_verified);
            verifiedView.setText(verifiedCharSequence);

            // time stamp
            CharSequence creationTimeCharSequence =
                    SNMessageViewHelper.getCreationTimeCharSequence(sharkMessage);
            TextView creationTimeView = this.findViewById(R.id.sn_message_creation_time);
            creationTimeView.setText(creationTimeCharSequence);

            // identity assurance
            CharSequence iACharSequence =
                    SNMessageViewHelper.getIdentityAssuranceCharSequence(sharkMessage);
            TextView identityAssuranceView = this.findViewById(R.id.sn_message_identityassurance);
            identityAssuranceView.setText(iACharSequence);

            // asap hops
            CharSequence asapHopsSequence =
                    SNMessageViewHelper.getASAPHopsCharSequence(sharkMessage);
            TextView asapHopsView = this.findViewById(R.id.sn_message_asap_hops);
            asapHopsView.setText(asapHopsSequence);


        } catch (SharkMessengerException e) {
            Log.e(this.getLogStart(), "cannot get message: " + e);
        } catch (IOException e) {
            Log.e(this.getLogStart(), "cannot get message: " + e);
        } catch (SharkException e) {
            Log.e(this.getLogStart(), "cannot get message: " + e);
        }
    }
}
