package net.sharksystem.persons.android;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import net.sharksystem.R;
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
    }

    public void onDoneClick(View v) {
        this.finish();
    }
}
