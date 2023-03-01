package net.sharksystem.ui.contacts;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import net.sharksystem.R;

/**
 * ContentAdapter for items in the contact list fragment
 * //TODO: implement
 */
public class ContactsContentAdapter extends RecyclerView.Adapter<ContactsContentAdapter.ViewHolder> {


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

            view.setOnClickListener(itemView -> {
                Navigation.findNavController(itemView).navigate(R.id.action_nav_contacts_to_nav_contact_view);
            });

            //TODO: add OnLongClickListener to delete contact(s)
            //view.setOnLongClickListener(contactView -> {
            //});
        }
    }


    @NonNull
    @Override
    public ContactsContentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ContactsContentAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
