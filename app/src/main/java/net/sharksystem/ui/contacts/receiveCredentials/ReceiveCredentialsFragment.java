package net.sharksystem.ui.contacts.receiveCredentials;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.sharksystem.R;
import net.sharksystem.databinding.FragmentReceiveCredentialsBinding;

/**
 * Fragment displaying the received credential or a waiting message until the credential is received
 */
public class ReceiveCredentialsFragment extends Fragment {

    /**
     * Binding to access elements from the layout
     */
    private FragmentReceiveCredentialsBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.binding = FragmentReceiveCredentialsBinding.inflate(inflater, container, false);

        //add onClickListener for aborting the receiving
        this.binding.fragmentReceiveCredentialsAbortButton.setOnClickListener(view ->
            Navigation.findNavController(view).navigate(R.id.action_nav_receive_credentials_to_nav_add_contact)
        );

        // Inflate the layout for this fragment
        return this.binding.getRoot();
    }
}