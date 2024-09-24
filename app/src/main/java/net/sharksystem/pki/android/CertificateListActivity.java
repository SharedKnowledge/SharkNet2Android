package net.sharksystem.pki.android;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import net.sharksystem.R;
import net.sharksystem.asap.ASAPSecurityException;
import net.sharksystem.asap.android.Util;
import net.sharksystem.asap.pki.ASAPCertificate;
import net.sharksystem.sharknet.android.SharkNetActivity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CertificateListActivity extends SharkNetActivity {
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private CertificateListContentAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(this.getLogStart(), "onCreate");

        setContentView(R.layout.certificate_list_drawer_layout);

        List<ASAPCertificate> certList = null;

        // find out what to do
        try {
            PersonIntent intent = new PersonIntent(this.getIntent());
            if(intent.isOwnerIDSet()) {
                if(intent.explainIdentityAssurance()) {
                    certList = this.produceListToExplain(intent.getSubjectID());
                } else {
                    certList = this.produceListBySubject(intent.getSubjectID());
                }
            } else {
                if(intent.isSignerIDSet()) {
                    certList = this.produceListByIssuer(intent.getIssuerID());
                } else {
                    Toast.makeText(this,
                            "internal failure: neither owner nor signer id set",
                            Toast.LENGTH_SHORT).show();
                    this.finish();
                    return;
                }
            }

            ////////////////////////////////////////////////////////////////////////
            //                         prepare action bar                         //
            ////////////////////////////////////////////////////////////////////////
            // setup toolbar
            Toolbar myToolbar = (Toolbar) findViewById(R.id.certificate_list_with_toolbar);
            setSupportActionBar(myToolbar);

            ////////////////////////////////////////////////////////////////////////
            //                         prepare recycler view                      //
            ////////////////////////////////////////////////////////////////////////
            mRecyclerView = (RecyclerView) findViewById(R.id.certificate_list_recycler_view);

            mAdapter = new CertificateListContentAdapter(this, certList);
            RecyclerView.LayoutManager mLayoutManager =
                    new LinearLayoutManager(getApplicationContext());

            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            mRecyclerView.setAdapter(mAdapter);
            Log.d(this.getLogStart(), "attached content adapter");
        }
        catch(Exception e) {
            Log.d(this.getLogStart(), "problems while setting up activity and content adapter: "
                    + e.getLocalizedMessage());
            // debug break
            int i = 42;
        }
    }

    private List<ASAPCertificate> produceCertList(Collection<ASAPCertificate> certColl) {
        return new ArrayList<>(certColl);
    }

    private List<ASAPCertificate> produceListBySubject(CharSequence userID) throws ASAPSecurityException {
        Collection<ASAPCertificate> certColl =
                this.getSharkNetApp().getSharkPKI().getCertificatesBySubject(userID);

        return this.produceCertList(certColl);
    }

    private List<ASAPCertificate> produceListByIssuer(CharSequence userID) throws ASAPSecurityException {
        Collection<ASAPCertificate> certColl =
                this.getSharkNetApp().getSharkPKI().getCertificatesByIssuer(userID);

        return this.produceCertList(certColl);
    }

    private List<ASAPCertificate> produceListToExplain(CharSequence userID)
            throws ASAPSecurityException {

        List<ASAPCertificate> certList = new ArrayList<>();
        List<CharSequence> idPath =
                this.getSharkNetApp().getSharkPKI().getIdentityAssurancesCertificationPath(userID);

        if(idPath.isEmpty()) {
            Toast.makeText(this, "Person can not be verified", Toast.LENGTH_LONG).show();
            this.finish();
            return null;
        }

        if(idPath.get(0).toString().equalsIgnoreCase(this.getSharkNetApp().getOwnerID().toString())) {
            // direct certificate
            Toast.makeText(this, "You met this person and signed a certificate",
                    Toast.LENGTH_LONG).show();
            this.finish();
            return null;
        }

        for(CharSequence id : idPath) {
            certList.add(this.getSharkNetApp().getSharkPKI().
                    getCertificatesBySubject(id).iterator().next());
        }

        return certList;
    }

    /////////////////////////////////////////////////////////////////////////////////
    //                              toolbar methods                                //
    /////////////////////////////////////////////////////////////////////////////////

    /**
     * connect menu with menu items and make them visible
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(this.getLogStart(), "onCreateOptionsMenu called");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.person_list_selection_action_buttons, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        try {
            if(item.getItemId() == R.id.personListSelectionDoneButton) {
                Toast.makeText(this, "something clicked", Toast.LENGTH_SHORT).show();
                return true;
            } else if(item.getItemId() == R.id.abortButton) {
                this.finish();
                return true;
            } else {
                return super.onOptionsItemSelected(item);
            }
        }
        catch(Exception e) {
            Log.d(this.getLogStart(), e.getLocalizedMessage());
        }

        return false;
    }

    protected void onResume() {
        super.onResume();
        Log.d(Util.getLogStart(this), "onResume: assume data set changed.");
        this.mAdapter.notifyDataSetChanged();
    }
}
