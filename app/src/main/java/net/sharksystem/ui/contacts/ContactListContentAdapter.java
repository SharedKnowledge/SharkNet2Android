package net.sharksystem.ui.contacts;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.widget.RecyclerView;

import net.sharksystem.R;
import net.sharksystem.asap.ASAPSecurityException;
import net.sharksystem.asap.persons.PersonValues;
import net.sharksystem.sharknet.android.SharkNetApp;

/**
 * ContentAdapter for items in the contact list fragment
 * //TODO: implement
 */
public class ContactListContentAdapter extends RecyclerView.Adapter<ContactListContentAdapter.ViewHolder> {


    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView personName;
        private final TextView personIdentityAssurance;
        private final TextView personSelected;
        private final TextView personCertificateExchangeFailure;

        public ViewHolder(View view) {
            super(view);

            this.personName = view.findViewById(R.id.person_list_row_person_name);
            this.personIdentityAssurance = view.findViewById(R.id.person_list_row_identity_assurance_level);
            this.personSelected = view.findViewById(R.id.cert_exchange_failure);
            this.personCertificateExchangeFailure = view.findViewById(R.id.person_list_row_selected);

            //TODO: add OnLongClickListener to delete contact(s)
            //view.setOnLongClickListener(contactView -> {
            //});
        }
    }

    private SelectionTracker<Long> tracker;

    public ContactListContentAdapter() {
        this.setHasStableIds(true);
    }

    public void setTracker(SelectionTracker<Long> tracker) {
        this.tracker = tracker;
    }


    @NonNull
    @Override
    public ContactListContentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.person_list_row, parent, false);

        return new ContactListContentAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactListContentAdapter.ViewHolder holder, int position) {
        try {
            PersonValues personValues =
                    SharkNetApp.getSharkNetApp().getSharkPKI().getPersonValuesByPosition(position);

            CharSequence userID = personValues.getUserID();
            //this.scs.setSelectedText(Integer.toString(position), userID,
                    //holder.itemView, holder.personSelected);
            int identityAssurance = personValues.getIdentityAssurance();
            int signingFailureRate = personValues.getSigningFailureRate();

            holder.itemView.setTag(R.id.user_id_tag, userID);
            holder.personName.setText(personValues.getName());
            holder.personIdentityAssurance.setText(String.valueOf(identityAssurance));
            holder.personCertificateExchangeFailure.setText(String.valueOf(signingFailureRate));

            holder.itemView.setActivated(this.tracker.
                    isSelected((long) holder.getBindingAdapterPosition()));

        } catch (ASAPSecurityException e) {
            //Toast.makeText(this.ctx, "error finding person information: ", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return SharkNetApp.getSharkNetApp().getSharkPKI().getNumberOfPersons();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
