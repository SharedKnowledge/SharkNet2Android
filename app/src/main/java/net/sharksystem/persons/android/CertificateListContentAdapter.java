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
        public TextView subjectName, subjectNameCopy, issuerName,
                identityAssurance, validUntil, validSince,
                caIssuer;

        public MyViewHolder(View view) {
            super(view);
            validSince = view.findViewById(R.id.certificate_list_row_valid_since);
            subjectName = view.findViewById(R.id.certificate_list_row_subject);
            subjectNameCopy = view.findViewById(R.id.certificate_list_row_subject_copy);
            issuerName = view.findViewById(R.id.certificate_list_row_issuer);
            caIssuer = view.findViewById(R.id.certificate_list_row_cf_issuer);
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

        ASAPCertificate asapCertificate = this.certList.get(position);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd., yyyy");
        holder.validSince.setText(simpleDateFormat.format(
                new Date(asapCertificate.getValidSince().getTimeInMillis())));

        holder.validUntil.setText(simpleDateFormat.format(
                new Date(asapCertificate.getValidUntil().getTimeInMillis())));

        CharSequence ownerName = PersonsStorageAndroidComponent.getPersonsStorage().
                getPersonName(asapCertificate.getSubjectID());

        holder.subjectName.setText(ownerName);

        holder.subjectNameCopy.setText(" " + ownerName + ": ");

        CharSequence signerName;
        if(asapCertificate.getIssuerID().toString().
                equalsIgnoreCase(PersonsStorageAndroidComponent.getPersonsStorage().getOwnerID().toString())) {
            signerName = "You";
        } else {
            signerName = asapCertificate.getIssuerName();
        }

        holder.issuerName.setText(signerName);

        int cef = PersonsStorageAndroidComponent.getPersonsStorage().
                getSigningFailureRate(asapCertificate.getIssuerID());

        holder.caIssuer.setText(String.valueOf(cef));

        int identityAssurance = 0;
        try {
            identityAssurance = PersonsStorageAndroidComponent.getPersonsStorage().
                    getIdentityAssurance(asapCertificate.getSubjectID());
        } catch (SharkException e) {
            Log.d(this.getLogStart(),
                    "issuer in certificate but not found in person storage " +
                            "- can happen if persons are manually removed from list");
        }

        holder.identityAssurance.setText(String.valueOf(identityAssurance));

        holder.itemView.setTag(R.id.certificate_list_subject_tag, asapCertificate.getSubjectID());
        holder.itemView.setTag(R.id.certificate_list_issuer_tag, asapCertificate.getIssuerID());
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
        CharSequence owner = (CharSequence)view.getTag(R.id.certificate_list_subject_tag);
        CharSequence signer = (CharSequence)view.getTag(R.id.certificate_list_issuer_tag);
        PersonIntent personIntent =
                new PersonIntent(this.ctx, owner, signer, CertificateViewActivity.class);

        this.ctx.startActivity(personIntent);
    }

    protected String getLogStart() {
        return this.getClass().getSimpleName();
    }
}
