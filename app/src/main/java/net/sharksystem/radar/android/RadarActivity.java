package net.sharksystem.radar.android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import net.sharksystem.R;
import net.sharksystem.sharknet.android.SharkNetActivity;
import net.sharksystem.sharknet.android.SharkNetApp;

public class RadarActivity extends SharkNetActivity {

    public RadarActivity() {
        super(SharkNetApp.getSharkNetApp());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.radar_drawer_layout);
        SharkNetApp.getSharkNetApp().setupDrawerLayout(this);
    }
}
