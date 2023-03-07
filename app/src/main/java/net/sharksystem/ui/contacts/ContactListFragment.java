package net.sharksystem.ui.contacts;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.selection.ItemDetailsLookup;
import androidx.recyclerview.selection.OnItemActivatedListener;
import androidx.recyclerview.selection.SelectionPredicates;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.selection.StableIdKeyProvider;
import androidx.recyclerview.selection.StorageStrategy;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import net.sharksystem.R;
import net.sharksystem.asap.ASAPSecurityException;
import net.sharksystem.databinding.FragmentContactListBinding;
import net.sharksystem.pki.android.PersonStatusHelper;
import net.sharksystem.pki.android.SelectableListContentAdapterHelper;
import net.sharksystem.sharknet.android.SharkNetApp;
import net.sharksystem.asap.persons.PersonValues;

import java.util.Set;

/**
 * Fragment for displaying all contacts in a list
 */
public class ContactListFragment extends Fragment implements OnItemActivatedListener<Long> {

    /**
     * Binding to access elements from the layout
     */
    private FragmentContactListBinding binding;

    private ContactViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        this.binding = FragmentContactListBinding.inflate(inflater, container, false);
        this.viewModel = new ViewModelProvider(this.requireActivity()).get(ContactViewModel.class);

        PersonStatusHelper personsApp =
                PersonStatusHelper.getPersonsStorage();

        Set<CharSequence> preselectionSet = personsApp.getPreselectionSet();
        //Log.d(this.getLogStart(), "got preselectedset: " + preselectionSet);
        if (preselectionSet != null && !preselectionSet.isEmpty()) {
            SelectableListContentAdapterHelper selectableContentSource = new SelectableListContentAdapterHelper();
            selectableContentSource.setPreselection(preselectionSet);
            personsApp.setPreselectionSet(null);
        }


        //Set-Up RecyclerView
        RecyclerView recyclerView = this.binding.fragmentContactsRecyclerView;

        ContactListContentAdapter adapter = new ContactListContentAdapter();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getContext());

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        SelectionTracker<Long> tracker = new SelectionTracker.Builder<>(
                "contact-selection",
                recyclerView,
                new StableIdKeyProvider(recyclerView),
                new ContactDetailsLookup(recyclerView),
                StorageStrategy.createLongStorage()).
                withSelectionPredicate(SelectionPredicates.createSelectAnything()).
                withOnItemActivatedListener(this).
                build();

        adapter.setTracker(tracker);


        //add onClickListener when the user clicks the button to add a contact
        this.binding.fragmentContactsAddContactButton.setOnClickListener(view ->
                Navigation.findNavController(view).navigate(R.id.action_nav_contacts_to_add_contact)
        );

        return this.binding.getRoot();
    }

    @Override
    public boolean onItemActivated(@NonNull ItemDetailsLookup.ItemDetails<Long> item, @NonNull MotionEvent e) {
        try {
            PersonValues values = SharkNetApp.getSharkNetApp().getSharkPKI().
                    getPersonValuesByPosition(item.getPosition());

            this.viewModel.setPerson(values);

            Navigation.findNavController(this.requireView()).
                    navigate(R.id.action_nav_contacts_to_nav_contact_view);

            return true;
        } catch (ASAPSecurityException ex) {
            return false;
        }


    }
}