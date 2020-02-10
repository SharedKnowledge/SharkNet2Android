package net.sharksystem.persons.android;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import net.sharksystem.R;
import net.sharksystem.asap.android.apps.ASAPMessageReceivedListener;
import net.sharksystem.asap.apps.ASAPMessages;
import net.sharksystem.sharknet.android.SharkNetActivity;
import net.sharksystem.sharknet.android.SharkNetApp;

public class PersonReceiveCredentialsActivity extends SharkNetActivity {
    public PersonReceiveCredentialsActivity() {
        super(SharkNetApp.getSharkNetApp());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.person_receive_credential_drawer_layout);
        this.getSharkNetApp().setupDrawerLayout(this);

        // TODO: set asap message listener
        this.getSharkNetApp().addASAPMessageReceivedListener(OwnerApp.CREDENTIAL_URI,
                new CredentialMessageReceivedListener(this));
    }


    private void doHandleCredentialMessage(ASAPMessages asapMessages) {
        Log.d(getLogStart(), "doHandleCredentialMessage: TODO");

        Toast.makeText(this, "doHandleCredentialMessage: TODO", Toast.LENGTH_SHORT).show();
    }

    public void onDoneClick(View v) {
        this.finish();
    }

    private class CredentialMessageReceivedListener implements ASAPMessageReceivedListener {

        private final PersonReceiveCredentialsActivity personReceiveCredentialsActivity;

        public CredentialMessageReceivedListener(
                PersonReceiveCredentialsActivity personReceiveCredentialsActivity) {

            this.personReceiveCredentialsActivity = personReceiveCredentialsActivity;

        }

        @Override
        public void asapMessagesReceived(ASAPMessages asapMessages) {
            Log.d(getLogStart(), "asapMessageReceived");
            this.personReceiveCredentialsActivity.doHandleCredentialMessage(asapMessages);
        }
    }
}
