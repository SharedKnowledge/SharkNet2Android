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

import java.util.HashSet;
import java.util.Set;

public class PersonListContentAdapter extends
        RecyclerView.Adapter<PersonListContentAdapter.MyViewHolder>
        implements View.OnClickListener, View.OnLongClickListener {

    private final Context ctx;
    private View.OnClickListener clickListener;
    private View.OnLongClickListener longClickListener;

    private Set<CharSequence> selectedUserIDs = new HashSet<>();

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView personName, personIdentityAssurance,
                personSelected, personCertificateExchangeFailure;

        public MyViewHolder(View view) {
            super(view);
            personName = view.findViewById(R.id.person_list_row_name);
            personIdentityAssurance = view.findViewById(R.id.person_list_row_identity_assurance_level);
            personSelected = view.findViewById(R.id.person_list_row_selected);
            personCertificateExchangeFailure =
                    view.findViewById(R.id.person_list_row_identityAssurance);

            view.setOnClickListener(clickListener);
            view.setOnLongClickListener(longClickListener);
        }
    }

    public PersonListContentAdapter(Context ctx) throws SharkException {
        Log.d(this.getLogStart(), "constructor");
        this.ctx = ctx;
        this.clickListener = this;
        this.longClickListener = this;
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

        /*
        I assume a bug or more probably - I'm to dull to understand recycler view at all.
        Here it comes: this method is called even with position 0
        But that position is never displayed.

        Only happens if we have a toolbar on top of a recycler view, though.

        So: I'm going to fake it until I understand the problem
        Fix: When position 0 called - I return a dummy message

        the other calls are handled as they should but with a decreased position
         */

        String name = "DummyName";
        holder.personName.setText(name);
        holder.personIdentityAssurance.setText("identityAssured: 4");
        holder.personCertificateExchangeFailure.setText("certExchangeFailure: 9");

        if(position == 0) return;

        // fake position - see comments above
        position--;

        try {
            PersonValues personValues =
                    PersonsStorageAndroid.getPersonsApp().getPersonValuesByPosition(position);

            CharSequence userID = personValues.getUserID();

            holder.itemView.setTag(userID);

            holder.personName.setText(personValues.getName());

            holder.personIdentityAssurance.setText("iAssured: "
                    + personValues.getIdentityAssurance());

            holder.personCertificateExchangeFailure.setText("certFailure: "
                    + personValues.getCertificateExchangeFailure());

            String selectedString = this.selectedUserIDs.contains(userID) ? "SELECTED" : "";
            holder.personSelected.setText(selectedString);

        } catch (SharkException e) {
            Toast.makeText(this.ctx, "error finding person information: ", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    @Override
    public int getItemCount() {
        Log.d(this.getLogStart(), "called getItemCount");

        return PersonsStorageAndroid.getPersonsApp().getNumberOfPersons() + 1;
    }

    @Override
    public boolean onLongClick(View view) {
        CharSequence userID = (CharSequence)view.getTag();
        Intent intent = new PersonIntent(this.ctx, userID, PersonEditActivity.class);

        this.ctx.startActivity(intent);

        return true;
    }

    @Override
    public void onClick(View view) {
        CharSequence userID = (CharSequence)view.getTag();

        if(this.selectedUserIDs.contains(userID)) {
            this.selectedUserIDs.remove(userID);
        } else {
            this.selectedUserIDs.add(userID);
        }

        this.notifyDataSetChanged();
    }

    protected String getLogStart() {
        return this.getClass().getSimpleName();
    }
}
