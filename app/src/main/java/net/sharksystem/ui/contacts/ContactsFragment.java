package net.sharksystem.ui.contacts;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.sharksystem.R;
import net.sharksystem.databinding.FragmentContactsBinding;

/**
 * Fragment for displaying all contacts in a list
 */
public class ContactsFragment extends Fragment {

    /**
     * Binding to access elements from the layout
     */
    private FragmentContactsBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        this.binding = FragmentContactsBinding.inflate(inflater, container, false);

        //Set-Up RecyclerView
        RecyclerView recyclerView = this.binding.fragmentContactsRecyclerView;

        ContactsContentAdapter adapter = new ContactsContentAdapter();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getContext());

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        //add onClickListener when the user clicks the button to add a contact
        this.binding.fragmentContactsAddContactButton.setOnClickListener(view ->
            Navigation.findNavController(view).navigate(R.id.action_nav_contacts_to_add_contact)
        );

        return this.binding.getRoot();
    }

}