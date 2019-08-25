package net.sharksystem.sharknet.android.settings;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import net.sharksystem.R;
import net.sharksystem.sharknet.android.SharkNetApp;

public class SettingsActivity extends AppCompatActivity {
    private static final String LOGSTART = "SNSettings";

    private Activity thisActivity;

    public SettingsActivity() {
        this.thisActivity = this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.settings_drawer_layout);

        SharkNetApp.getSharkNetApp(this).setupDrawerLayout(this);

        // add listener for each setting

        ////////////////////////////////////////////////////////////////////////////
        //                          protocol switch on/off                        //
        ////////////////////////////////////////////////////////////////////////////

        ///////////////// BLUETOOTH
        ToggleButton toggle = (ToggleButton) findViewById(R.id.settingsBluetoothToggleButton);
        // set initial status
        toggle.setChecked(SharkNetApp.getSharkNetApp(this).isBluetoothEnvironmentOn());

        // add behaviour
        toggle.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        SharkNetApp sharkNetApp = SharkNetApp.getSharkNetApp(thisActivity);
                        if (isChecked) {
                            Log.d(LOGSTART, "ui said: switch on BT");
                            sharkNetApp.startBluetooth();
                        } else {
                            Log.d(LOGSTART, "ui said: switch off BT");
                            sharkNetApp.stopBluetooth();
                        }
                    }
                });

        ///////////////// BLUETOOTH DISCOVERABLE
        toggle = (ToggleButton) findViewById(R.id.settingsBluetoothDiscoverableToggleButton);
        // set initial status
        toggle.setChecked(SharkNetApp.getSharkNetApp(this).isBluetoothDiscoverable());

        // add behaviour
        toggle.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        SharkNetApp sharkNetApp = SharkNetApp.getSharkNetApp(thisActivity);
                        if (isChecked) {
                            Log.d(LOGSTART, "ui said: switch on BT Discoverable");
                            sharkNetApp.startBluetoothDiscoverable();
                        } else {
                            Log.d(LOGSTART, "ui said: switch off BT - there is no switch off - yet TODO");
//                            sharkNetApp.stopBluetooth();
                        }
                    }
                });

        ///////////////// BLUETOOTH DISCOVERY
        toggle = (ToggleButton) findViewById(R.id.settingsBluetoothDiscoveryToggleButton);
        // set initial status
        toggle.setChecked(SharkNetApp.getSharkNetApp(this).isBluetoothDiscovery());

        // add behaviour
        toggle.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        SharkNetApp sharkNetApp = SharkNetApp.getSharkNetApp(thisActivity);
                        if (isChecked) {
                            Log.d(LOGSTART, "ui said: switch on BT Discovery");
                            sharkNetApp.startBluetoothDiscovery();
                        } else {
                            Log.d(LOGSTART, "ui said: switch off BT - TODO nyi");
                            //sharkNetApp.stopBluetooth();
                        }
                    }
                });
    }

    protected void onPause() {
        super.onPause();
        Log.d(LOGSTART, "onPause");
        SharkNetApp.getSharkNetApp(thisActivity).onPause();
    }

    protected void onDestroy() {
        super.onDestroy();
        Log.d(LOGSTART, "onDestroy");
        SharkNetApp.getSharkNetApp(thisActivity).onDestroy();
    }
/*
    private class AASPToggleListener implements CompoundButton.OnCheckedChangeListener {
        private final Activity activity;

        AASPToggleListener(Activity activity) {
            this.activity = activity;
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            SharkNetApp sharkNetApp = SharkNetApp.getSharkNetApp(activity);
            if(isChecked) {
                Log.d(LOGSTART, "ui said: switch on aasp");
                sharkNetApp.startAASP(activity);
            } else {
                Log.d(LOGSTART, "ui said: switch off aasp");
                sharkNetApp.stopAASP(activity);
            }
        }
    }
    */
}
