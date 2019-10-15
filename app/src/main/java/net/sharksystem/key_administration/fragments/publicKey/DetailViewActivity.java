package net.sharksystem.key_administration.fragments.publicKey;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.sharksystem.R;
import net.sharksystem.android.util.Constants;
import net.sharksystem.key_administration.KeyAdministrationActivity;
import net.sharksystem.storage.SharedPreferencesHandler;

import java.io.IOException;
import java.lang.reflect.Type;
import java.security.cert.Certificate;
import java.util.ArrayList;

import static net.sharksystem.android.util.SerializationHelper.byteToObj;

public class DetailViewActivity extends AppCompatActivity {

    private int itemPos;

    private TextView alias;
    private TextView publicKey;
    private TextView uuid;
    private TextView validityPeriod;
    private TextView algo;

    private SharedPreferencesHandler sharedPreferencesHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_view);

        sharedPreferencesHandler = new SharedPreferencesHandler(getApplicationContext());
        getItemPos(savedInstanceState);
        initViews();
    }

    private void initViews() {
        this.alias = findViewById(R.id.dv_alias);
        this.publicKey = findViewById(R.id.dv_textView_public_key);
        this.uuid = findViewById(R.id.dv_textView_uuid);
        this.validityPeriod = findViewById(R.id.dv_textView_validity_period);
        this.algo = findViewById(R.id.dv_textView_algo);

        // set Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_detail_view);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Key Detail");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        fillWithData();
    }

    private void fillWithData() {

        ReceiveKeyPojo key = getKey();
        Certificate certificate = null;
        byte[] decodeBase64Cert = Base64.decode(key.getCertInBase64(), Base64.DEFAULT);
        try {
            certificate = (Certificate) byteToObj(decodeBase64Cert);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        this.alias.setText(key.getAlias());

        if(certificate != null) {
            this.publicKey.setText(Base64.encodeToString(certificate.getPublicKey().getEncoded(), Base64.DEFAULT));
        }else {
            this.publicKey.setText("n/a");
        }

        this.uuid.setText(key.getUuid());
        this.validityPeriod.setText("n/a");
        this.algo.setText(certificate.getType());

    }


    private ReceiveKeyPojo getKey() {
        Gson gson = new Gson();
        Type typeOfKeylist = new TypeToken<ArrayList<ReceiveKeyPojo>>() {
        }.getType();

        String keylistInJson = sharedPreferencesHandler.getValue(Constants.KEY_LIST);

        if (keylistInJson != null) {
            ArrayList<ReceiveKeyPojo> keylist = gson.fromJson(keylistInJson, typeOfKeylist);
            return keylist.get(itemPos);
        } else {
            return null;
        }
    }

    private void getItemPos(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                itemPos = 0;
            } else {
                itemPos = extras.getInt("ITEM_POS");
            }
        } else {
            itemPos = (int) savedInstanceState.getSerializable("ITEM_POS");
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), KeyAdministrationActivity.class);
        startActivityForResult(myIntent, 0);
        finish();
        return true;
    }
}
