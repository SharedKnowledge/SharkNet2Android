package net.sharksystem.sharknet.android.settings;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import net.sharksystem.R;
import net.sharksystem.sharknet.android.SharkNetActivity;
import net.sharksystem.sharknet.android.SharkNetApp;

public class SettingsActivity extends SharkNetActivity {

    public SettingsActivity() {
        super(SharkNetApp.getSharkNetApp());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.settings_drawer_layout);

        this.getSharkNetApp().setupDrawerLayout(this);

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
                        if (isChecked) {
                            Log.d(getLogStart(), "ui said: switch on BT");
                            startBluetooth();
                        } else {
                            Log.d(getLogStart(), "ui said: switch off BT");
                            stopBluetooth();
                        }
                    }
                });

        ///////////////// BLUETOOTH DISCOVERABLE
        toggle = (ToggleButton) findViewById(R.id.settingsBluetoothDiscoverableToggleButton);
        // set initial status
        toggle.setChecked(isBluetoothDiscoverable());

        // add behaviour
        toggle.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            Log.d(getLogStart(), "ui said: switch on BT Discoverable");
                            startBluetoothDiscoverable();
                        } else {
                            Log.d(getLogStart(), "ui said: switch off BT - there is no switch off - yet TODO");
                            stopBluetooth();
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
                        if (isChecked) {
                            Log.d(getLogStart(), "ui said: switch on BT Discovery");
                            startBluetoothDiscovery();
                        } else {
                            Log.d(getLogStart(), "ui said: switch off BT - TODO nyi");
                            //sharkNetApp.stopBluetooth();
                        }
                    }
                });
    }
}
