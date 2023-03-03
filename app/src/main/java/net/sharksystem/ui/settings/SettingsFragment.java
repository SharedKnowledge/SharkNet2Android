package net.sharksystem.ui.settings;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.sharksystem.R;
import net.sharksystem.databinding.FragmentSettingsBinding;

public class SettingsFragment extends Fragment {

    private FragmentSettingsBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        this.binding = FragmentSettingsBinding.inflate(inflater, container, false);

        this.binding.fragmentSettingsPersonalDataButton.setOnClickListener(view ->
            Navigation.findNavController(view).navigate(R.id.action_nav_settings_to_nav_personal_data)
        );

        this.binding.fragmentSettingsRsaKeyButton.setOnClickListener(view ->
            Navigation.findNavController(view).navigate(R.id.action_nav_settings_to_nav_rsa)
        );

        this.binding.fragmentSettingsSendCredentialsButton.setOnClickListener(view ->
            Navigation.findNavController(view).navigate(R.id.action_nav_settings_to_nav_send_credentials_info)
        );

        this.binding.fragmentSettingsCertificatesButton.setOnClickListener(view ->
            Navigation.findNavController(view).navigate(R.id.action_nav_settings_to_nav_certificate_list)
        );

        return this.binding.getRoot();
    }

}