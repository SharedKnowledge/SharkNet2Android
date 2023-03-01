package net.sharksystem.ui.contacts;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.Snackbar;

import net.sharksystem.R;
import net.sharksystem.databinding.FragmentContactViewBinding;

/**
 * Fragment for showing contact data
 */
public class ContactViewFragment extends Fragment {

    /**
     * Binding to access elements from the layout
     */
    private FragmentContactViewBinding binding;

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.binding = FragmentContactViewBinding.inflate(inflater, container, false);

        //Set-Up OnClickListeners
        this.binding.fragmentContactViewUserIDDescriptionButton.setOnClickListener(view ->
                Snackbar.make(view, R.string.explainUserIDText, Snackbar.LENGTH_LONG).show());

        this.binding.fragmentContactViewNameDescriptionButton.setOnClickListener(view ->
                Snackbar.make(view, R.string.explainUserNameText, Snackbar.LENGTH_LONG).show());

        this.binding.fragmentContactViewIdentityAssuranceDescriptionButton.setOnClickListener(view ->
            Snackbar.make(view, R.string.explainIdentityAssuranceText, Snackbar.LENGTH_LONG).show()
        );

        this.binding.fragmentContactViewSigningFailureDescriptionButton.setOnClickListener(view ->
            Snackbar.make(view, R.string.explainSigningFailureText, Snackbar.LENGTH_LONG).show()
        );

        this.binding.fragmentReceiveCredentialsCertificateDescriptionButton.setOnClickListener(view ->
            Snackbar.make(view, R.string.explainWasCertifiedText, Snackbar.LENGTH_LONG).show()
        );

        this.binding.fragmentReceiveCredentialsShowOwnCertificateButton.setOnClickListener(view -> {
            //TODO: display certificates that where certified by the user
        });

        this.binding.fragmentReceiveCredentialsIssueCertificateDescriptionButton.setOnClickListener(view ->
            Snackbar.make(view, R.string.explainCertifiedText, Snackbar.LENGTH_LONG).show()
        );

        this.binding.fragmentReceiveCredentialsShowIssuedCertificatesButton.setOnClickListener(view -> {
            //TODO: display certificates that where issued by the user
        });

        this.binding.fragmentReceiveCredentialsSaveButton.setOnClickListener(view ->
            Navigation.findNavController(view).navigate(R.id.action_nav_contact_view_to_nav_contacts)
        );

        this.binding.fragmentReceiveCredentialsAbortButton.setOnClickListener(view ->
            Navigation.findNavController(view).navigate(R.id.action_nav_contact_view_to_nav_contacts)
        );

        return this.binding.getRoot();
    }
}