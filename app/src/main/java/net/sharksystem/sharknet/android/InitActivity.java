package net.sharksystem.sharknet.android;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import net.sharksystem.R;
import net.sharksystem.eID.IdentificationActivity;
import net.sharksystem.persons.android.OwnerActivity;

public class InitActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.init);

    }

    public void onSaveClick(View view) {
        EditText ownerNameEditText = this.findViewById(R.id.initOwnerName);

        String ownerName = ownerNameEditText.getText().toString();

        if(ownerName.equalsIgnoreCase(SharkNetApp.DEFAULT_OWNER_NAME)) {
            Toast.makeText(this, "you must define another name",
                    Toast.LENGTH_SHORT).show();
        } else {
            SharkNetApp.getSharkNetApp().getOwnerStorage().setDisplayName(ownerName);
            this.finish();
            Intent intent = new Intent(this, OwnerActivity.class);
            this.startActivity(intent);
        }
    }

    public void switchToEIdentification(View view) {
        this.finish();
        Intent intent = new Intent(this, IdentificationActivity.class);
        this.startActivity(intent);
    }
}
