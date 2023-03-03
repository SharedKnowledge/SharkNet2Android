package net.sharksystem.ui.settings.rsa;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.sharksystem.R;
import net.sharksystem.databinding.FragmentRsaBinding;

public class RsaFragment extends Fragment {

    private FragmentRsaBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.binding = FragmentRsaBinding.inflate(this.getLayoutInflater());

        //TODO: display RSA values and make it possible to generate new ones

        this.binding.fragmentRsaGenerateButton.setOnClickListener(view ->
                Navigation.findNavController(view).navigate(R.id.action_nav_rsa_to_nav_settings));

        return this.binding.getRoot();
    }
}