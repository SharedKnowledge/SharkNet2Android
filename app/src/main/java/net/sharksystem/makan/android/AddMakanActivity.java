package net.sharksystem.makan.android;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import net.sharksystem.R;
import net.sharksystem.asap.ASAPException;
import net.sharksystem.makan.Makan;
import net.sharksystem.sharknet.android.SharkNetActivity;
import net.sharksystem.sharknet.android.SharkNetApp;

import java.io.IOException;

public class AddMakanActivity extends SharkNetActivity {

    public AddMakanActivity() {
        super(SharkNetApp.getSharkNetApp());
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(this.getLogStart(), "onCreate");

        setContentView(R.layout.makan_add_makan_drawer_layout);

        this.getSharkNetApp().setupDrawerLayout(this);

        // initialize MakanApp
        MakanApp.getMakanApp(this);
    }

    public void onClick(View view) {
        if(view == this.findViewById(R.id.makanAddMakanDoAdd)) {
            EditText uriText = this.findViewById(R.id.makanAddMakanURI);
            EditText nameText = this.findViewById(R.id.makanAddMakanName);

            try {
                MakanApp.getMakanStorage().createMakan(
                        uriText.getText(),
                        nameText.getText(),
                        SharkNetApp.getSharkNetApp().getOwnerID()
                );

            } catch (IOException e) {
                String text = "failure: " + e.getLocalizedMessage();
                Log.e(this.getLogStart(), text);
                Toast.makeText(this, "IO error - that's serious", Toast.LENGTH_SHORT).show();
            } catch (ASAPException e) {
                String text = "failure: " + e.getLocalizedMessage();
                Log.w(this.getLogStart(), text);
                Toast.makeText(this, "already exists(?)", Toast.LENGTH_SHORT).show();
            }
        }

        Intent intent = new Intent(this, MakanListActivity.class);
        this.startActivity(intent);
    }
}