package net.sharksystem.radar.android;

import android.os.Bundle;
import android.widget.TextView;

import net.sharksystem.R;
import net.sharksystem.asap.ASAPSecurityException;
import net.sharksystem.persons.android.PersonStatusHelper;
import net.sharksystem.pki.SharkPKIComponent;
import net.sharksystem.sharknet.android.SharkNetActivity;
import net.sharksystem.sharknet.android.SharkNetApp;

import java.util.Set;

public class RadarActivity extends SharkNetActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.radar_drawer_layout);
        SharkNetApp.getSharkNetApp().setupDrawerLayout(this);

        this.setOnlinePeerText();
    }

    private void setOnlinePeerText() {
        TextView peerListTextView = this.findViewById(R.id.radarOnlinePeersList);

        Set<CharSequence> onlinePeerList = this.getASAPAndroidPeer().getOnlinePeerList();
        if(onlinePeerList == null || onlinePeerList.size() < 1) {
            peerListTextView.setText("no peer(s) connected");
        } else {
            PersonStatusHelper personsApp = PersonStatusHelper.getPersonsStorage();
            SharkPKIComponent sharkPKIComponent = this.getSharkNetApp().getSharkPKI();

            StringBuilder sb = new StringBuilder();
            sb.append("peers connected:");
            sb.append("\n");
            for(CharSequence peerID : onlinePeerList) {
                String peerName = "unknown";
                try {
                    peerName = sharkPKIComponent.getPersonValuesByID(peerID).getName().toString();
                } catch (ASAPSecurityException e) {
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

    // TODO: use environment listener concept!
    public void asapNotifyOnlinePeersChanged(Set<CharSequence> peerList) {
        super.asapNotifyOnlinePeersChanged(peerList);
        this.setOnlinePeerText();
    }
}
