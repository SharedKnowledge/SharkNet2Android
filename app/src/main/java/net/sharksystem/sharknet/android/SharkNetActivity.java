package net.sharksystem.sharknet.android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public abstract class SharkNetActivity extends AppCompatActivity {
    private SharkNetApp sharkNetApp;

    protected SharkNetApp getSharkNetApp() {
        if(sharkNetApp == null) {
            this.sharkNetApp = SharkNetApp.getSharkNetApp(this);
        }
        return this.sharkNetApp;
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(this.getLogStart(), "onStart");
        this.getSharkNetApp().onStart();
    }

    protected void onResume() {
        super.onResume();
        Log.d(this.getLogStart(), "onResume");
        this.getSharkNetApp().onResume();
    }

    protected void onPause() {
        super.onPause();
        Log.d(this.getLogStart(), "onPause");
        this.getSharkNetApp().onPause();
    }

    protected void onDestroy() {
        super.onDestroy();
        Log.d(this.getLogStart(), "onDestroy");
        this.getSharkNetApp().onDestroy();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        this.getSharkNetApp().onActivityResult(requestCode, resultCode, data);
    }

    protected String getLogStart() {
        return this.getClass().getSimpleName();
    }

}
