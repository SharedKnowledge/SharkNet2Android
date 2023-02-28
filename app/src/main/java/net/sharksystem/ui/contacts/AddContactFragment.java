package net.sharksystem.ui.contacts;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.sharksystem.R;
import net.sharksystem.databinding.FragmentAddContactBinding;

/**
 * Fragment for adding a new contact
 */
public class AddContactFragment extends Fragment {

    /**
     * Binding to access elements from the layout
     */
    private FragmentAddContactBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.binding = FragmentAddContactBinding.inflate(inflater, container, false);

        //setting up onClickListeners
        //..when the user aborts
        this.binding.fragmentAddContactAbortButton.setOnClickListener(view ->
            Navigation.findNavController(view).navigate(R.id.action_nav_add_contact_to_nav_contacts)
        );

        //...when the user want's to continue the addition of a new contact.
        this.binding.fragmentAddContactContinueButton.setOnClickListener(view ->
            Navigation.findNavController(view).navigate(R.id.action_nav_add_contact_to_nav_receive_credentials)
        );

        this.binding.fragmentAddContactRadarButton.setOnClickListener(view ->
            Navigation.findNavController(view).navigate(R.id.action_nav_add_contact_to_nav_radar)
        );

        //...for navigating to the network
        this.binding.fragmentAddContactNetworkButton.setOnClickListener(view ->
            Navigation.findNavController(view).navigate(R.id.action_nav_add_contact_to_nav_network)
        );

        // Inflate the layout for this fragment
        return this.binding.getRoot();
    }
}