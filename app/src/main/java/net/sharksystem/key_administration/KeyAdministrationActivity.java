package net.sharksystem.key_administration;

import android.net.Uri;
import android.nfc.NfcAdapter;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;


import net.sharksystem.R;
import net.sharksystem.key_administration.fragments.CertificationFragment;
import net.sharksystem.key_administration.fragments.PublicKeyTabFragment;
import net.sharksystem.sharknet.android.SharkNetApp;

public class KeyAdministrationActivity extends AppCompatActivity implements PublicKeyTabFragment.OnFragmentInteractionListener, CertificationFragment.OnFragmentInteractionListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.key_administration_drawer_layout);

        SharkNetApp.getSharkNetApp(this).setupDrawerLayout(this);

        // set Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_key_administration);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Keys and Certifications");

        //ViewPager
        ViewPager viewPager = findViewById(R.id.keyAdministrationViewPager);
        PagerAdapter pagerAdapter = new KeyAdministrationPagerAdapter(getSupportFragmentManager(), this.getApplicationContext());
        viewPager.setAdapter(pagerAdapter);

        //tabLayout
        TabLayout tableLayout = findViewById(R.id.keyAdministrationTabLayout);
        tableLayout.setupWithViewPager(viewPager);

    }

    // Menu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.key_administration_toolbar_setttings_menu, menu);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
