package net.sharksystem.eID;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import net.sharksystem.R;
import net.sharksystem.persons.android.OwnerActivity;
import net.sharksystem.sharknet.android.SharkNetApp;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class IdentificationActivity extends AppCompatActivity {
    ForegroundDispatcher foregroundDispatcher;
    Connector extSdk;

    TextView passwordTextView;
    TextView replyTextView;
    ProgressBar progressBar;

    ExecutorService es;
    Future task;

    Intent nfcIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.identification);

        passwordTextView = findViewById(R.id.password);
        replyTextView = findViewById(R.id.reply);

        progressBar = findViewById(R.id.progress_loader);
        progressBar.setVisibility(View.INVISIBLE);

        es = Executors.newSingleThreadExecutor();

        try {
            extSdk = Connector.start(this);
        } catch (Connector.ServiceException e) {
            Log.e("sdk", "", e);
        }

        foregroundDispatcher = new ForegroundDispatcher(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        nfcIntent = intent;
//        handleIntent(intent);
    }

    void handleIntent(Intent intent) {
        final Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

        if (tag != null) {
            try {
                if (task != null) {
                    task.get(100, TimeUnit.MILLISECONDS);
                }
            } catch (InterruptedException ignored) {
            } catch (ExecutionException | TimeoutException e) {
                e.printStackTrace();
            }

            progressBar.setVisibility(View.VISIBLE);
            passwordTextView.setEnabled(false);
            task = es.submit(() -> {
                try {
                    UserData user = extSdk.runWorkflow(tag, new SelfAuthWorkflow(passwordTextView.getText().toString()));
                    Log.i("eID user data", user.toString());

                    runOnUiThread(() -> replyTextView.setText(user.toString()));

                    String ownerName = user.uuid().toString();
                    SharkNetApp.getSharkNetApp().getOwnerStorage().setDisplayName(ownerName);

                    Toast.makeText(IdentificationActivity.this,
                            "Username is set:" + ownerName, Toast.LENGTH_LONG).show();

                    this.finish();
                    Intent nextIntent = new Intent(this, OwnerActivity.class);
                    this.startActivity(nextIntent);

                } catch (SelfAuthWorkflow.WorkflowException e) {
                    runOnUiThread(() -> Toast.makeText(IdentificationActivity.this,
                            e.getMessage(), Toast.LENGTH_LONG).show());
                    Log.w("sdk", "runSelfAuth failed", e);
                } catch (InterruptedException ignored) {
                } finally {
                    runOnUiThread(() -> {
                        progressBar.setVisibility(View.INVISIBLE);
                        passwordTextView.setEnabled(true);
                    });
                }
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        foregroundDispatcher.enable();
    }

    @Override
    public void onPause() {
        super.onPause();
        foregroundDispatcher.disable();

        if (task != null) {
            task.cancel(true);
            task = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        extSdk.stop();
    }

    public void doEIdentification(View view) {
        if (nfcIntent != null) {
            handleIntent(nfcIntent);
        } else {
            Toast.makeText(IdentificationActivity.this,
                "NO ID DETECTED", Toast.LENGTH_SHORT).show();
        }
    }
}

