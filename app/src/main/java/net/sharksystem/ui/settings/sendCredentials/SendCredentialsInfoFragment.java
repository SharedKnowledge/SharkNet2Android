package net.sharksystem.ui.settings.sendCredentials;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.sharksystem.R;
import net.sharksystem.databinding.FragmentSendCredentialsInfoBinding;

public class SendCredentialsInfoFragment extends Fragment {

   private FragmentSendCredentialsInfoBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.binding = FragmentSendCredentialsInfoBinding.inflate(this.getLayoutInflater());

        this.binding.fragmentSendCredentialsInfoAbortButton.setOnClickListener(view ->
                Navigation.findNavController(view)
                        .navigate(R.id.action_nav_send_credentials_info_to_nav_settings)
        );

        this.binding.fragmentSendCredentialsInfoContinueButton.setOnClickListener(view ->
                Navigation.findNavController(view)
                        .navigate(R.id.action_nav_send_credentials_info_to_nav_send_credentials)
        );

        return this.binding.getRoot();
    }
}