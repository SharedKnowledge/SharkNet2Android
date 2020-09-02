package net.sharksystem.persons.android;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import net.sharksystem.R;
import net.sharksystem.radar.android.RadarActivity;
import net.sharksystem.sharknet.android.SharkNetActivity;
import net.sharksystem.sharknet.android.SharkNetApp;
import net.sharksystem.sharknet.android.settings.SettingsActivity;


public class PersonAddExplanationActivity extends PersonAppActivity {
/*    public PersonAddExplanationActivity() {
        super(SharkNetApp.getSharkNetApp());
    }

 */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.person_add_explanation_layout);
    }

    public void onContinueClick(View v) {
        this.finish();
        this.startActivity(new Intent(this, PersonWaitForCredentialActivity.class));
    }

    public void onGotoRadarClick(View v) {
        this.finish();
        this.startActivity(new Intent(this, RadarActivity.class));
    }

    public void onGotoNetworksSettingsClick(View v) {
        this.finish();
        this.startActivity(new Intent(this, SettingsActivity.class));
    }
}
