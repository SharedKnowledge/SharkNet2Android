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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.settings_drawer_layout);

        SharkNetApp.getSharkNetApp(this).setupDrawerLayout(this);

        // add listener for each setting

        // AASP service switch on/off
        ToggleButton toggle = (ToggleButton) findViewById(R.id.settingsAASPToggleButton);
        toggle.setOnCheckedChangeListener(new AASPToggleListener(this));


    }

    private class AASPToggleListener implements  CompoundButton.OnCheckedChangeListener {
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
}
