package net.sharksystem.radar.android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import net.sharksystem.R;
import net.sharksystem.sharknet.android.SharkNetActivity;
import net.sharksystem.sharknet.android.SharkNetApp;

import java.util.List;

public class RadarActivity extends SharkNetActivity {

    public RadarActivity() {
        super(SharkNetApp.getSharkNetApp());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.radar_drawer_layout);
        SharkNetApp.getSharkNetApp().setupDrawerLayout(this);

        this.setOnlinePeerText();
    }

    private void setOnlinePeerText() {
        TextView peerListTextView = this.findViewById(R.id.radarOnlinePeersList);

        List<CharSequence> onlinePeerList = this.getSharkNetApp().getOnlinePeerList();
        if(onlinePeerList == null || onlinePeerList.size() < 1) {
            peerListTextView.setText("no peer online");
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("peers online;");
            sb.append("\n");
            for(CharSequence peerName : onlinePeerList) {
                sb.append(peerName);
                sb.append("\n");
            }
            peerListTextView.setText(sb.toString());
        }

        peerListTextView.refreshDrawableState();
    }

    public void asapNotifyOnlinePeersChanged(List<CharSequence> peerList) {
        super.asapNotifyOnlinePeersChanged(peerList);
        this.setOnlinePeerText();
    }
}
