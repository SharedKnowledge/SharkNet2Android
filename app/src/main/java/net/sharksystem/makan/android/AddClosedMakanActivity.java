package net.sharksystem.makan.android;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import net.sharksystem.R;
import net.sharksystem.asap.ASAPException;
import net.sharksystem.asap.android.ASAPServiceMessage;
import net.sharksystem.persons.android.PersonListSelectionActivity;
import net.sharksystem.persons.android.PersonsStorageAndroid;
import net.sharksystem.sharknet.android.SharkNetActivity;
import net.sharksystem.sharknet.android.SharkNetApp;

import java.util.Set;

public class AddClosedMakanActivity extends SharkNetActivity {

    private Set<CharSequence> selectedRecipients = null;

    public AddClosedMakanActivity() {
        super(SharkNetApp.getSharkNetApp());
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(this.getLogStart(), "onCreate");

        setContentView(R.layout.makan_add_closed_makan_drawer_layout);

        this.getSharkNetApp().setupDrawerLayout(this);
    }

    public void onShowPersonListClick(View view) {
        Log.d(this.getLogStart(), "onShowPersonListClick");

        PersonsStorageAndroid.getPersonsStorage().setPreselectionSet(this.selectedRecipients);
        //Log.d(this.getLogStart(), "setPreselected: " + this.selectedRecipients);
        Intent intent = new Intent(this, PersonListSelectionActivity.class);
        this.startActivity(intent);
    }

    public void onClick(View view) {
        // open new closed makan
        if(view == this.findViewById(R.id.makanAddMakanDoAdd)) {
            //EditText uriText = this.findViewById(R.id.makanAddMakanURI);
            EditText nameText = this.findViewById(R.id.makanAddMakanName);
            String name = nameText.getText().toString();
            String uri = "sn2://closedUriHasToBeCreated";
            Set<CharSequence> recipients =
                    PersonsStorageAndroid.getPersonsStorage().getLastPersonsSelection();

            try {
                Log.d(this.getLogStart(), "call create with "
                        + "name: " + name
                        + " | uri: " + uri
                        + " | recipients: " + recipients);
                ASAPServiceMessage serviceMsg =
                        ASAPServiceMessage.createCreateClosedChannelMessage(MakanApp.APP_NAME,
                                uri, name, recipients);

                this.sendMessage2Service(serviceMsg.getMessage());
                this.finish();
            } catch (ASAPException e) {
                String text = "failure: " + e.getLocalizedMessage();
                Log.w(this.getLogStart(), text);
                Toast.makeText(this, "already exists(?)", Toast.LENGTH_SHORT).show();
            }
        }

        Intent intent = new Intent(this, MakanListActivity.class);
        this.startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        this.selectedRecipients = PersonsStorageAndroid.getPersonsStorage().getLastPersonsSelection();
        TextView tv = this.findViewById(R.id.makanCreateSelectedMember);
        tv.setText(selectedRecipients.toString());
    }
}
