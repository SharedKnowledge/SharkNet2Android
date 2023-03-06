package net.sharksystem.sharknet.android.settings;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import net.sharksystem.R;
import net.sharksystem.SharkException;
import net.sharksystem.SharkPeer;
import net.sharksystem.android.IntentWithPosition;
import net.sharksystem.asap.android.apps.ASAPActivity;
import net.sharksystem.hub.hubside.Hub;
import net.sharksystem.hub.peerside.HubConnectorDescription;
import net.sharksystem.hub.peerside.TCPHubConnectorDescriptionImpl;
import net.sharksystem.sharknet.android.SharkNetApp;

import java.io.IOException;

// TCP only
public class HubDescriptionEditActivity extends ASAPActivity {
    private HubConnectorDescription origHubDescription;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = this.getIntent();

        // prepare defaults if a new entry is created
        CharSequence hostNameString = "hostName";
        CharSequence portString = String.valueOf(Hub.DEFAULT_PORT);
        int layout = R.layout.settings_hub_description_editor_drawer_layout;

        // there could be a position in intent
        try {
            IntentWithPosition intentWithPosition = new IntentWithPosition(intent);

            // this can fail - and end in catch
            int position = intentWithPosition.getPosition();

            // we have got a position
            this.origHubDescription =
                    SharkNetApp.getSharkNetApp().getSharkPeer().getHubDescription(position);

            hostNameString = origHubDescription.getHostName();
            portString = Integer.toString(origHubDescription.getPortNumber());

            layout = R.layout.settings_hub_description_editor_with_delete_drawer_layout;

        } catch (SharkException e) {
            // take defaults - a new description is created
        }

        setContentView(layout);

        SharkNetApp.getSharkNetApp().setupDrawerLayout(this);

        EditText etHostName = this.findViewById(R.id.fragment_hub_view_host_name_input);
        EditText etPort = this.findViewById(R.id.fragment_hub_view_port_input);

        etHostName.setText(hostNameString);
        etPort.setText(portString);
    }

    public void onClick(View view) {
        if(view == this.findViewById(R.id.fragment_receive_credentials_abort_button)) {
            this.finish();
            return;
        }

        if(view == this.findViewById(R.id.fragment_hub_view_default_values_button)) {
            EditText etHostName = this.findViewById(R.id.fragment_hub_view_host_name_input);
            EditText etPort = this.findViewById(R.id.fragment_hub_view_port_input);
            ToggleButton tbMultiChannel = this.findViewById(R.id.fragment_hub_view_multi_channel_button);

            etHostName.setText("asaphub.f4.htw-berlin.de");
            etPort.setText("6910");
            tbMultiChannel.setChecked(false);
            return;
        }

        // remove or add - in any case create a description object from GUI entries

        // add new one
        EditText etHostName = this.findViewById(R.id.fragment_hub_view_host_name_input);
        EditText etPort = this.findViewById(R.id.fragment_hub_view_port_input);
        ToggleButton tbMultiChannel = this.findViewById(R.id.fragment_hub_view_multi_channel_button);

        String hostNameString = etHostName.getEditableText().toString();
        String portString = etPort.getEditableText().toString();
        boolean multiChannel = tbMultiChannel.isChecked();

        int port = Hub.DEFAULT_PORT;
        try {
             port = Integer.parseInt(portString);
        }
        catch (NumberFormatException e) {
            Toast.makeText(this, "port must be a number: " + portString, Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            HubConnectorDescription descriptionFromGUI =
                    new TCPHubConnectorDescriptionImpl(hostNameString, port, multiChannel);

            SharkPeer sPeer = SharkNetApp.getSharkNetApp().getSharkPeer();

            if(view == this.findViewById(R.id.deleteButton)) {
                sPeer.removeHubDescription(descriptionFromGUI);
            }
            else if(view == this.findViewById(R.id.fragment_hub_view_save_button)) {
                // remove old one - it was most probably changed
                if(this.origHubDescription != null) {
                    sPeer.removeHubDescription(this.origHubDescription);
                }

                // add data from GUI
                sPeer.addHubDescription(descriptionFromGUI);
            }
        } catch (IOException e) {
            Log.e(this.getLogStart(), "not good: " + e.getLocalizedMessage());
        }

        this.finish();
    }
}
