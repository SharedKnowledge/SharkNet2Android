package net.sharksystem.ui.settings.sendCredentials;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.sharksystem.R;
import net.sharksystem.asap.ASAPSecurityException;
import net.sharksystem.databinding.FragmentSendCredentialsInfoBinding;
import net.sharksystem.pki.CredentialMessage;
import net.sharksystem.sharknet.android.SharkNetApp;

import java.nio.charset.StandardCharsets;

/**
 * This fragment informs the user about the earnestness of exchanging credentials and lets the
 * sender add a Custom Identification Code that the sender told him to add, so he can identify
 * the sender.
 */
public class SendCredentialsInfoFragment extends Fragment {

    /**
     * Binding for easy access to layout elements
     */
   private FragmentSendCredentialsInfoBinding binding;

    /**
     * View Model for saving the credential message for the fragment, where it is really send
     */
   private CredentialsViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.binding = FragmentSendCredentialsInfoBinding.inflate(this.getLayoutInflater());

        this.viewModel = new ViewModelProvider(this.requireActivity()).get(CredentialsViewModel.class);

        //Abort button brings the user back to the settings overview screen
        this.binding.fragmentSendCredentialsInfoAbortButton.setOnClickListener(view ->
                Navigation.findNavController(view)
                        .navigate(R.id.action_nav_send_credentials_info_to_nav_settings)
        );

        //If the user continues, the credentials will be saved including optional CIC
        this.binding.fragmentSendCredentialsInfoContinueButton.setOnClickListener(view -> {
            try {
                byte[] cic = this.binding.fragmentSendCredentialsInfoCicInput.getText().
                        toString().getBytes(StandardCharsets.UTF_8);

                CredentialMessage credentialMessage = SharkNetApp.getSharkNetApp().
                        getSharkPKI().createCredentialMessage(cic);

                this.viewModel.setCredentialMessage(credentialMessage);

                Navigation.findNavController(view)
                        .navigate(R.id.action_nav_send_credentials_info_to_nav_send_credentials);

            } catch (ASAPSecurityException e) {
                throw new RuntimeException(e);
            }
        });

        return this.binding.getRoot();
    }
}