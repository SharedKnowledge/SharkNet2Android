package net.sharksystem.sharknet.android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import net.sharksystem.R;
import net.sharksystem.asap.sharknet.android.SNChannelsListActivity;
import net.sharksystem.persons.android.OwnerActivity;

public class InitActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boolean launchFirstActivity = true;
        if(!SharkNetApp.isStarted()) {
            Log.d(this.getLogStart(), "Startup SharkNetApplication");
            SharkNetApp sharkNetApp = SharkNetApp.initializeSharkNetApp(this);
            Owner owner = sharkNetApp.getOwnerData();
            if (!owner.isOwnerSet()) launchFirstActivity = false;
        }

        if(launchFirstActivity) {
            // leave - we have no business here
//            Intent intent = new Intent(this, MakanListActivity.class);
            Intent intent = new Intent(this, SNChannelsListActivity.class);
            this.startActivity(intent);
        } else {
            setContentView(R.layout.init);
        }
    }

    public void onSaveClick(View view) {
        EditText ownerNameEditText = this.findViewById(R.id.initOwnerName);

        String ownerName = ownerNameEditText.getText().toString();

        if(ownerName.equalsIgnoreCase(OwnerStorage.DEFAULT_OWNER_NAME)) {
            Toast.makeText(this, "you must define another name",
                    Toast.LENGTH_SHORT).show();
        } else {
            SharkNetApp.getSharkNetApp().getOwnerData().setDisplayName(ownerName);
            this.finish();
            Intent intent = new Intent(this, OwnerActivity.class);
            this.startActivity(intent);
        }
    }

    private String getLogStart() {
        return "SharkNet2 InitActivity";
    }
}
