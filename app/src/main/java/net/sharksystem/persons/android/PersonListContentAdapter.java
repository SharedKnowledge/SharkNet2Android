package net.sharksystem.persons.android;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import net.sharksystem.R;
import net.sharksystem.SharkException;
import net.sharksystem.persons.PersonValues;

public class PersonListContentAdapter extends
        RecyclerView.Adapter<PersonListContentAdapter.MyViewHolder>
        implements View.OnClickListener, View.OnLongClickListener {

    private final Context ctx;
    protected final SelectableListContentAdapterHelper scs;
    private View.OnClickListener clickListener;
    private View.OnLongClickListener longClickListener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView personName, personIdentityAssurance,
                personSelected, personCertificateExchangeFailure;

        public MyViewHolder(View view) {
            super(view);
            personName = view.findViewById(R.id.person_list_row_person_name);
            personIdentityAssurance = view.findViewById(R.id.person_list_row_identity_assurance_level);
            personCertificateExchangeFailure = view.findViewById(R.id.cert_exchange_failure);
            personSelected = view.findViewById(R.id.person_list_row_selected);

            view.setOnClickListener(clickListener);
            view.setOnLongClickListener(longClickListener);
        }
    }

    public PersonListContentAdapter(Context ctx, SelectableListContentAdapterHelper scs) throws SharkException {
        Log.d(this.getLogStart(), "constructor");
        this.ctx = ctx;
        this.clickListener = this;
        this.longClickListener = this;
        this.scs = scs;
    }

    @Override
    public PersonListContentAdapter.MyViewHolder onCreateViewHolder(
            ViewGroup parent, int viewType) {
        Log.d(this.getLogStart(), "onCreateViewHolder");

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.person_list_row, parent, false);

        return new PersonListContentAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PersonListContentAdapter.MyViewHolder holder, int position) {
        Log.d(this.getLogStart(), "onBindViewHolder with position: " + position);

        try {
            PersonValues personValues =
                    PersonsStorageAndroidComponent.getPersonsStorage().getPersonValuesByPosition(position);

            CharSequence userID = personValues.getUserID();
            this.scs.setSelectedText(Integer.toString(position), userID,
                    holder.itemView, holder.personSelected);

            holder.itemView.setTag(R.id.user_id_tag, userID);
            holder.personName.setText(personValues.getName());

            int idenityAssurance = personValues.getIdentityAssurance();
            Log.d(this.getLogStart(), "identity Assurance: " + idenityAssurance);
            holder.personIdentityAssurance.setText(String.valueOf(idenityAssurance));

            int signingFailureRate = personValues.getSigningFailureRate();
            Log.d(this.getLogStart(), "signing failure: " + signingFailureRate);
            holder.personCertificateExchangeFailure.setText(String.valueOf(signingFailureRate));

        } catch (SharkException e) {
            Toast.makeText(this.ctx, "error finding person information: ", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    @Override
    public int getItemCount() {
        Log.d(this.getLogStart(), "called getItemCount");
        return PersonsStorageAndroidComponent.getPersonsStorage().getNumberOfPersons();
    }

    @Override
    public boolean onLongClick(View view) {
        CharSequence userID = (CharSequence)view.getTag(R.id.user_id_tag);
        Intent intent = new PersonIntent(this.ctx, userID, PersonEditActivity.class);

        this.ctx.startActivity(intent);

        return true;
    }

    boolean firstClick = true;
    @Override
    public void onClick(View view) {
        if(this.firstClick) {
            this.firstClick = false;
            Toast.makeText(this.ctx, "long click to edit", Toast.LENGTH_SHORT).show();
        }

        CharSequence userID = (CharSequence)view.getTag(R.id.user_id_tag);
        this.scs.onAction(this, view, userID);
    }

    protected String getLogStart() {
        return this.getClass().getSimpleName();
    }
}
