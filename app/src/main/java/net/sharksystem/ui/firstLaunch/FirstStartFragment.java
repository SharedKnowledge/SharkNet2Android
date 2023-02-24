package net.sharksystem.ui.firstLaunch;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import net.sharksystem.R;
import net.sharksystem.SharkException;
import net.sharksystem.databinding.FragmentFirstStartBinding;
import net.sharksystem.sharknet.android.SharkNetApp;

import java.io.IOException;
import java.util.Objects;

public class FirstStartFragment extends Fragment {

    private FragmentFirstStartBinding binding;
    private FirstStartViewModel mViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentFirstStartBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //add onClickListener for the save button
        binding.save.setOnClickListener(view -> {
            try {
                SharkNetApp.initializeSystem(this.getContext(), binding.fragmentFirstStartNameInput.getText().toString());
                Navigation.findNavController(view).navigate(R.id.nav_channels);
            } catch (SharkException se) {
                Toast.makeText(this.getContext(), se.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });

        return root;
    }

}