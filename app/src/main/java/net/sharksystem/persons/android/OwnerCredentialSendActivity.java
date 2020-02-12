package net.sharksystem.persons.android;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import net.sharksystem.R;
import net.sharksystem.sharknet.android.SharkNetActivity;
import net.sharksystem.sharknet.android.SharkNetApp;

import java.util.Random;

public class OwnerCredentialSendActivity extends SharkNetActivity {
    private boolean sended = false;

    public OwnerCredentialSendActivity() {
        super(SharkNetApp.getSharkNetApp());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.owner_send_credential_layout);
//        this.getSharkNetApp().setupDrawerLayout(this);

        // set user name in layout
        TextView tv = this.findViewById(R.id.ownerDisplayName);
        tv.setText(OwnerStorageAndroid.getIdentityStorage(this).getDisplayName());

        // set control number
        tv = this.findViewById(R.id.ownerSendCredentialsControlNumber);
        tv.setText(String.valueOf(-1));
    }

    public void onSendClick(View v) {
        if(!sended) {
            sended = true;
            // 1st: produce random number with 6 digits
            int randomInt = ((new Random(System.currentTimeMillis())).nextInt());

            Log.d(this.getLogStart(), "randomInt: " + randomInt);

            // make it positiv
            if(randomInt < 0) randomInt = 0-randomInt;

            // take 6 digits
            int sixDigitsInt = 0;
            for(int i = 0; i < 6; i++) {
                sixDigitsInt += randomInt % 10;
                sixDigitsInt *= 10;
                randomInt /= 10;
            }

            sixDigitsInt /= 10;

            // set control number
            TextView tv = this.findViewById(R.id.ownerSendCredentialsControlNumber);
            tv.setText(CredentialMessage.sixDigitsToString(sixDigitsInt));

            // send credential message
            try {
                PersonsAppAndroid personsAppAndroid = PersonsAppAndroid.getPersonsApp();
                personsAppAndroid.sendCredentialMessage(
                        this, sixDigitsInt, personsAppAndroid.getOwnerUserID());

            } catch (Exception e) {
                Log.d(this.getLogStart(), "Exception when sending credential: "
                        + e.getLocalizedMessage());

                Toast.makeText(this, "Exception when sending credential: "
                        + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();

                this.finish();
                return;
            }
        } else {
            Toast.makeText(this, "credential already sent", Toast.LENGTH_SHORT).show();
        }
    }

    public void onDoneClick(View v) {
        this.finish();
    }
}
