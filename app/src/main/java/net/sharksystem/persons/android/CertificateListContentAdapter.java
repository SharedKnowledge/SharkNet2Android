package net.sharksystem.persons.android;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.sharksystem.R;
import net.sharksystem.SharkException;
import net.sharksystem.crypto.ASAPCertificate;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

class CertificateListContentAdapter extends
        RecyclerView.Adapter<CertificateListContentAdapter.MyViewHolder>
        implements View.OnClickListener /*, View.OnLongClickListener */ {

    private final Context ctx;
    private final List<ASAPCertificate> certList;
    private View.OnClickListener clickListener;
//    private View.OnLongClickListener longClickListener;

//    private Set<CharSequence> selectedUserIDs = new HashSet<>();

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView ownerName, ownerNameCopy, signerName,
                signerNameCopy, identityAssurance, validUntil, validSince,
                caSigner;

        public MyViewHolder(View view) {
            super(view);
            validSince = view.findViewById(R.id.certificate_list_row_valid_since);
            ownerName = view.findViewById(R.id.certificate_list_row_owner);
            ownerNameCopy = view.findViewById(R.id.certificate_list_row_owner_copy);
            signerName = view.findViewById(R.id.certificate_list_row_signer);
            signerNameCopy = view.findViewById(R.id.certificate_list_row_signer_copy);
            caSigner = view.findViewById(R.id.certificate_list_row_cf_signer);
            identityAssurance = view.findViewById(R.id.cert_exchange_failure);
            validUntil = view.findViewById(R.id.certificate_list_row_valid_until);
            view.setOnClickListener(clickListener);
//            view.setOnLongClickListener(longClickListener);
        }
    }

    public CertificateListContentAdapter(Context ctx, List<ASAPCertificate> certList) throws SharkException {
        Log.d(this.getLogStart(), "constructor");
        this.ctx = ctx;
        this.clickListener = this;
        this.certList = certList;
        //this.longClickListener = this;
    }

    @Override
    public CertificateListContentAdapter.MyViewHolder onCreateViewHolder(
            ViewGroup parent, int viewType) {
        Log.d(this.getLogStart(), "onCreateViewHolder");

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.certificate_list_row, parent, false);

        return new CertificateListContentAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CertificateListContentAdapter.MyViewHolder holder, int position) {
        Log.d(this.getLogStart(), "onBindViewHolder with position: " + position);



        try {
            ASAPCertificate asapCertificate = this.certList.get(position);

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd., yyyy");
            holder.validSince.setText(simpleDateFormat.format(
                    new Date(asapCertificate.getValidSince().getTimeInMillis())));

            holder.validUntil.setText(simpleDateFormat.format(
                    new Date(asapCertificate.getValidUntil().getTimeInMillis())));

            CharSequence ownerName;
            if(asapCertificate.getOwnerID().toString().
                    equalsIgnoreCase(PersonsStorageAndroid.getPersonsApp().getOwnerID().toString())) {
                ownerName = "you";
            } else {
                ownerName = asapCertificate.getOwnerName();
            }

            holder.ownerName.setText(ownerName);
            holder.ownerNameCopy.setText(ownerName);

            CharSequence signerName;
            if(asapCertificate.getSignerID().toString().
                    equalsIgnoreCase(PersonsStorageAndroid.getPersonsApp().getOwnerID().toString())) {
                signerName = "you";
            } else {
                signerName = asapCertificate.getSignerName();
            }

            holder.signerName.setText(signerName);
            holder.signerNameCopy.setText(signerName);

            int cef = PersonsStorageAndroid.getPersonsApp().
                    getCertificateExchangeFailure(asapCertificate.getSignerID());
            holder.caSigner.setText(String.valueOf(cef));

            int ia = PersonsStorageAndroid.getPersonsApp().
                    getIdentityAssurance(asapCertificate.getSignerID());
            holder.identityAssurance.setText(String.valueOf(ia));

            holder.itemView.setTag(R.id.certificate_list_owner_tag, asapCertificate.getOwnerID());
            holder.itemView.setTag(R.id.certificate_list_signer_tag, asapCertificate.getSignerID());
        } catch (SharkException e) {
            Log.e(this.getLogStart(), "failure: " + e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        Log.d(this.getLogStart(), "called getItemCount");

        return this.certList.size();
    }

    /*
    @Override
    public boolean onLongClick(View view) {
        CharSequence userID = (CharSequence)view.getTag();
        Intent intent = new PersonIntent(this.ctx, userID, PersonEditActivity.class);

        this.ctx.startActivity(intent);

        return true;
    }
*/

    @Override
    public void onClick(View view) {
        CharSequence owner = (CharSequence)view.getTag(R.id.certificate_list_owner_tag);
        CharSequence signer = (CharSequence)view.getTag(R.id.certificate_list_signer_tag);
        PersonIntent personIntent =
                new PersonIntent(this.ctx, owner, signer, CertificateViewActivity.class);

        this.ctx.startActivity(personIntent);
    }

    protected String getLogStart() {
        return this.getClass().getSimpleName();
    }
}
