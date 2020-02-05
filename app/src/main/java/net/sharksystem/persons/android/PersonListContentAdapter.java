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

import java.util.HashSet;
import java.util.Set;

public class PersonListContentAdapter extends
        RecyclerView.Adapter<PersonListContentAdapter.MyViewHolder> implements View.OnClickListener {

    private static final String LOGSTART = "PersonListContentAdapter";
    private final Context ctx;
    private CharSequence topic = null;
    private View.OnClickListener clickListener;

    private Set<CharSequence> selectedName = new HashSet<>();

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView personName, personTrustLevel, personSelected;

        public MyViewHolder(View view) {
            super(view);
            personName = (TextView) view.findViewById(R.id.person_list_row_name);
            personTrustLevel = (TextView) view.findViewById(R.id.person_list_row_trustlevel);
            personSelected = (TextView) view.findViewById(R.id.person_list_row_selected);
            view.setOnClickListener(clickListener);
        }
    }

    public PersonListContentAdapter(Context ctx) throws SharkException {
        Log.d(this.getLogStart(), "constructor");
        this.ctx = ctx;
        this.clickListener = this;
    }

    @Override
    public net.sharksystem.persons.android.PersonListContentAdapter.MyViewHolder onCreateViewHolder(
            ViewGroup parent, int viewType) {
        Log.d(this.getLogStart(), "onCreateViewHolder");

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.person_list_row, parent, false);

        return new net.sharksystem.persons.android.PersonListContentAdapter.MyViewHolder(itemView);
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
        holder.personTrustLevel.setText("trust: 4");

        if(position == 0) return;

        // fake position - see comments above
        position--;

        switch(position) {
            case 0: name = "Alice"; break;
            case 1: name = "Bob"; break;
        }

        holder.personName.setText(name);
        String selectedString = this.selectedName.contains(name) ? "SELECTED" : "";
        holder.personSelected.setText(selectedString);

        // go ahead
/*
        try {
            MakanStorage makanStorage = MakanApp.getMakanStorage();
            if(makanStorage == null) {
                Log.d(this.getLogStart(), "fatal: no makan storage");
                return;
            }

            Makan makan = makanStorage.getMakan(position);
            if(makan == null) {
                Log.d(this.getLogStart(), "fatal: no makan");
                return;
            }

            holder.personName.setText(makan.getURI());
            holder.personTrustLevel.setText(makan.getDisplayName());
        } catch (IOException | ASAPException e) {
            Log.e(this.getLogStart(), "problems while showing mana entries: " + e.getLocalizedMessage());
            e.printStackTrace();
        }
*/
    }

    @Override
    public int getItemCount() {
        Log.d(this.getLogStart(), "called getItemCount");

        return 2+1; // fake length - see comments above
/*
        int realSize = 0;
        try {
            realSize = MakanApp.getMakanStorage().size();
            Log.d(this.getLogStart(), "count is: " + realSize);
        } catch (Exception e) {
            Log.e(this.getLogStart(), "cannot access message storage (yet?)");
            return 0;
        }
        int fakeSize = realSize+1;
        return fakeSize;

 */
    }

    @Override
    public void onClick(View view) {
        Log.d(this.getLogStart(), "click on view recognized");

        TextView personName = (TextView) view.findViewById(R.id.person_list_row_name);

        CharSequence name = personName.getText();

        if(this.selectedName.contains(name)) {
            this.selectedName.remove(name);
            Log.d(this.getLogStart(), "added: " + name);
        } else {
            this.selectedName.add(name);
            Log.d(this.getLogStart(), "removed: " + name);
        }

        this.notifyDataSetChanged();

        /*
        TextView uriTextView = (TextView) view.findViewById(R.id.makan_list_row_uid);
        Log.d(this.getLogStart(), "uri: " + uriTextView.getText());

        TextView nameTextView = (TextView) view.findViewById(R.id.makan_list_row_name);
        Log.d(this.getLogStart(), "name: " + nameTextView.getText());

        MakanIntent intent =
                new MakanIntent(
                        ctx,
                        nameTextView.getText(),
                        uriTextView.getText(),
                        MakanViewActivity.class);

        ctx.startActivity(intent);
 */
    }

    protected String getLogStart() {
        return this.getClass().getSimpleName();
    }
}
