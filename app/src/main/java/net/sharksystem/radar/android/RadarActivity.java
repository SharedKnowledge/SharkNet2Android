package net.sharksystem.radar.android;

import android.os.Bundle;
import android.widget.TextView;

import net.sharksystem.R;
import net.sharksystem.SharkException;
import net.sharksystem.persons.android.PersonsStorageAndroid;
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
            PersonsStorageAndroid personsApp = PersonsStorageAndroid.getPersonsApp();
            StringBuilder sb = new StringBuilder();
            sb.append("peers online;");
            sb.append("\n");
            for(CharSequence peerID : onlinePeerList) {
                String peerName = "unknown";
                try {
                    peerName = personsApp.getPersonValues(peerID).getName().toString();
                } catch (SharkException e) {
                    e.printStackTrace();
                }
                sb.append("name: ");
                sb.append(peerName);
                sb.append(" | ");
                sb.append("id: ");
                sb.append(peerID);
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
