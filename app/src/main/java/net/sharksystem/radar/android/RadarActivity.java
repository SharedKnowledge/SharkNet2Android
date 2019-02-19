package net.sharksystem.radar.android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import net.sharksystem.R;
import net.sharksystem.sharknet.SharkNetApp;

public class RadarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.radar_drawer_layout);
        SharkNetApp.getSharkNetApp().setupDrawerLayout(this);
    }
}
