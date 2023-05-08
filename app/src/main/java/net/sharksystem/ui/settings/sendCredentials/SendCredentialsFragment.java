package net.sharksystem.ui.settings.sendCredentials;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import net.sharksystem.SharkException;
import net.sharksystem.asap.ASAPSecurityException;
import net.sharksystem.asap.utils.DateTimeHelper;
import net.sharksystem.databinding.FragmentSendCredentialsBinding;
import net.sharksystem.pki.CredentialMessage;
import net.sharksystem.sharknet.android.SharkNetApp;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * This fragment shows the entire credential that will be sent to the receiver.
 */
public class SendCredentialsFragment extends Fragment {

    private FragmentSendCredentialsBinding binding;

    private CredentialsViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.binding = FragmentSendCredentialsBinding.inflate(this.getLayoutInflater());

        this.viewModel = new ViewModelProvider(this.requireActivity()).get(CredentialsViewModel.class);

        byte[] cic = this.viewModel.getCIC().toString().getBytes(StandardCharsets.UTF_8);

        try {
            CredentialMessage cm = SharkNetApp.getSharkNetApp().
                    getSharkPKI().createCredentialMessage(cic);

            CharSequence id = cm.getSubjectID();
            CharSequence name = cm.getSubjectName();
            long validSince = cm.getValidSince();
            int randomInt = cm.getRandomInt();
            CharSequence hash = "not yet implemented";
            CharSequence cicCS = new String(cic, StandardCharsets.UTF_8);

            this.binding.credentialSubjectIDValue.setText(id);
            this.binding.credentialSubjectNameValue.setText(name);
            this.binding.credentialValidSinceValue.setText(DateTimeHelper.long2DateString(validSince));
            this.binding.credentialRandomNumberValue.setText(String.valueOf(randomInt));
            this.binding.credentialHashPublicKeyValue.setText(hash);
            this.binding.credentialExtraDataValue.setText(cicCS);

            this.binding.actionButton.setOnClickListener(view -> {
                try {
                    //TODO: why is this not working?
                    SharkNetApp.getSharkNetApp().getSharkPKI().
                            acceptAndSignCredential(cm);

                } catch (IOException | ASAPSecurityException e) {
                    String s = "fatal: could not add certificate: " + e.getLocalizedMessage();
                    Log.e(this.getClass().getSimpleName(), s);
                    Toast.makeText(this.getContext(), s, Toast.LENGTH_SHORT).show();
                }
            });

        } catch (SharkException e) {
            Log.d(this.getClass().getSimpleName(), "could not send credential: " + e.getLocalizedMessage());
            Toast.makeText(this.getContext(), "could not send credential: " + e.getLocalizedMessage(),
                    Toast.LENGTH_SHORT).show();
        }

        return this.binding.getRoot();
    }
}