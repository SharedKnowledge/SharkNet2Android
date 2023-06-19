package net.sharksystem.sharknet.android.settings;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.ToggleButton;

import net.sharksystem.R;
import net.sharksystem.SharkPeer;
import net.sharksystem.asap.android.apps.ASAPActivity;
import net.sharksystem.asap.android.apps.ASAPAndroidPeer;
import net.sharksystem.asap.android.apps.HubConnectionManagerApplicationSide;
import net.sharksystem.asap.android.apps.HubManagerStatusChangedListener;
import net.sharksystem.hub.HubConnectionManager;
import net.sharksystem.hub.peerside.HubConnectorDescription;
import net.sharksystem.sharknet.android.SharkNetActivity;
import net.sharksystem.sharknet.android.SharkNetApp;

import java.util.ArrayList;
import java.util.List;

public class SettingsActivity extends ASAPActivity implements HubManagerStatusChangedListener {
    private HubConnectionManagerApplicationSide hubConnectionManager;
    private ListView listViewConnectedHubs;
    private ListView listViewFailedAttempts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.settings_drawer_layout);

        SharkNetApp.getSharkNetApp().setupDrawerLayout(this);

        hubConnectionManager = (HubConnectionManagerApplicationSide) this.getHubConnectionManager();
        listViewConnectedHubs = findViewById(R.id.settingsConnectedHubsList);
        listViewFailedAttempts = findViewById(R.id.settingsFailedAttemptsList);
        hubConnectionManager.addListener(this);
        // add listener for each setting

        ////////////////////////////////////////////////////////////////////////////
        //                          protocol switch on/off                        //
        ////////////////////////////////////////////////////////////////////////////

        ///////////////// BLUETOOTH
        ToggleButton toggle = (ToggleButton) findViewById(R.id.settingsBluetoothToggleButton);
        // set initial status
        toggle.setChecked(this.isBluetoothEnvironmentOn());

        // add behaviour
        toggle.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(!dontDoAnything) {
                            if (isChecked) {
                                Log.d(getLogStart(), "ui said: switch on BT");
                                startBluetooth();
                            } else {
                                Log.d(getLogStart(), "ui said: switch off BT");
                                stopBluetooth();
                            }
                        }
                    }
                });

        ///////////////// BLUETOOTH DISCOVERABLE
/*
        toggle = (ToggleButton) findViewById(R.id.settingsBluetoothDiscoverableToggleButton);
        // set initial status
        toggle.setChecked(isBluetoothDiscoverable());

        // add behaviour
        toggle.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(!dontDoAnything) {
                            if (isChecked) {
                                Log.d(getLogStart(), "ui said: switch on BT Discoverable");
                                startBluetoothDiscoverable();
                            } else {
                                Log.d(getLogStart(), "ui said: switch off BT - there is no switch off - yet TODO");
                            }
                        }
                    }
                });

        ///////////////// BLUETOOTH DISCOVERY
        toggle = (ToggleButton) findViewById(R.id.settingsBluetoothDiscoveryToggleButton);
        // set initial status
        toggle.setChecked(isBluetoothDiscovery());

        // add behaviour
        toggle.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (!dontDoAnything) {
                            if (isChecked) {
                                Log.d(getLogStart(), "ui said: switch on BT Discovery");
                                startBluetoothDiscovery();
                            } else {
                                Log.d(getLogStart(), "ui said: switch off BT - TODO nyi");
                                //sharkNetApp.stopBluetooth();
                            }
                        }
                    }
                });
 */
        ///////////////// BLUETOOTH DISCOVERY AND DISCOVERABLE = SCAN BT
        toggle = (ToggleButton) findViewById(R.id.settingsBluetoothD_And_D_ExplanationButton);
        // set initial status
        toggle.setChecked(isBluetoothDiscovery());

        // add behaviour
        toggle.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (!dontDoAnything) {
                            if (isChecked) {
                                Log.d(getLogStart(), "ui said: switch on BT D and D");
                                startBluetoothDiscovery();
                                startBluetoothDiscoverable();
                            } else {
                                Log.d(getLogStart(), "ui said: switch off BT - TODO nyi");
                                //sharkNetApp.stopBluetooth(); TODO
                            }
                        }
                    }
                });

        ///////////////// ASAP Hub on / off
        toggle = (ToggleButton) findViewById(R.id.settingsASAPHubsExplanationButton);
        // set initial status
        toggle.setChecked(isASAPHubsConnected());

        // add behaviour
        toggle.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (!dontDoAnything) {
                            if (isChecked) {
                                Log.d(getLogStart(), String.format("connect ASAPHubs.. Available " +
                                        "descriptions: %d",SharkNetApp.getSharkNetApp().getSharkPeer().getHubDescriptions().size()));
                                for (HubConnectorDescription hcd: SharkNetApp.getSharkNetApp().getSharkPeer().getHubDescriptions() ) {
                                    connectASAPHubs(hcd);
                                }
                            } else {
                                for (HubConnectorDescription hcd: SharkNetApp.getSharkNetApp().getSharkPeer().getHubDescriptions() ) {
                                    disconnectASAPHubs(hcd);
                                }
                            }
                        }
                    }
                });

        ///////////////// ASAP Hub refresh connected hubs list and failed connection attempts list
        Button refreshButton = findViewById(R.id.settingsRefreshHubListButton);
        refreshButton.setOnClickListener(view -> {
            hubConnectionManager.refreshHubList();
        });
    }

    private boolean dontDoAnything = false;

    private void refreshToggleButtons() {
        this.dontDoAnything = true;

        ToggleButton toggle = (ToggleButton) findViewById(R.id.settingsBluetoothToggleButton);
        toggle.setChecked(this.isBluetoothEnvironmentOn());
        toggle.refreshDrawableState();

        /*
        toggle = (ToggleButton) findViewById(R.id.settingsBluetoothDiscoverableToggleButton);
        toggle.setChecked(this.isBluetoothDiscoverable());
        toggle.refreshDrawableState();

        toggle = (ToggleButton) findViewById(R.id.settingsBluetoothDiscoveryToggleButton);
        toggle.setChecked(this.isBluetoothDiscovery());
        toggle.refreshDrawableState();
         */

        toggle = (ToggleButton) findViewById(R.id.settingsBluetoothD_And_D_ExplanationButton);
        toggle.setChecked(this.isBluetoothDiscovery());
        toggle.refreshDrawableState();

        this.dontDoAnything = false;
    }

    @Override
    protected void onStart() {
        super.onStart();
        this.refreshProtocolStatus();
    }

    public void onConfigASAPHubs(View view) {
        Intent intent = new Intent(this, HubDescriptionsListActivity.class);
        this.startActivity(intent);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////
    //                             keep in sync with protocol changes                         //
    ////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void onResume() {
        super.onResume();
        this.refreshProtocolStatus();
    }

    @Override
    public void asapNotifyBTDiscoverableStarted() {
        super.asapNotifyBTDiscoverableStarted();
        this.refreshToggleButtons();
    }

    @Override
    public void asapNotifyBTDiscoverableStopped() {
        super.asapNotifyBTDiscoverableStopped();
        this.refreshToggleButtons();
    }

    @Override
    public void asapNotifyBTEnvironmentStarted() {
        super.asapNotifyBTEnvironmentStarted();
        this.refreshToggleButtons();
    }

    @Override
    public void asapNotifyBTEnvironmentStopped() {
        super.asapNotifyBTEnvironmentStopped();
        this.refreshToggleButtons();
    }

    @Override
    public void asapNotifyBTDiscoveryStarted() {
        super.asapNotifyBTDiscoveryStarted();
        Log.d(this.getLogStart(), "asapNotifyBTDiscoveryStarted() called");
        this.refreshToggleButtons();
    }

    @Override
    public void asapNotifyBTDiscoveryStopped() {
        super.asapNotifyBTDiscoveryStopped();
        this.refreshToggleButtons();
    }

    @Override
    public void notifyHubListReceived() {
        List<String> connectedHubs = new ArrayList<>();
        for (HubConnectorDescription hcd : hubConnectionManager.getConnectedHubs()) {
            connectedHubs.add(hcd.toString());
        }
        List<String> failedAttempts = new ArrayList<>();
        for (HubConnectionManager.FailedConnectionAttempt attempt : hubConnectionManager.getFailedConnectionAttempts()) {
            failedAttempts.add(attempt.getHubConnectorDescription().toString());
        }
        listViewConnectedHubs.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, connectedHubs.toArray(new String[0])));
        listViewFailedAttempts.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, failedAttempts.toArray(new String[0])));
    }
}
