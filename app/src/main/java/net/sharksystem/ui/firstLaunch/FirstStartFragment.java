package net.sharksystem.ui.firstLaunch;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import net.sharksystem.R;
import net.sharksystem.SharkException;
import net.sharksystem.databinding.FragmentFirstStartBinding;
import net.sharksystem.sharknet.android.SharkNetApp;
import net.sharksystem.ui.MainAppViewModel;


/**
 * Fragment showed at the first start of the app. The user must set his name.
 */
public class FirstStartFragment extends Fragment {

    /**
     * Binding to access elements from the layout
     */
    private FragmentFirstStartBinding binding;

    private MainAppViewModel mainAppViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        this.binding = FragmentFirstStartBinding.inflate(inflater, container, false);
        this.mainAppViewModel = new ViewModelProvider(this.requireActivity()).
                get(MainAppViewModel.class);

        mainAppViewModel.requestLockDrawer();

        //add onClickListener for the save button
        binding.fragmentFirstStartSaveButton.setOnClickListener(view -> {
            try {
                mainAppViewModel.requestUnlockDrawer();
                String inputName = this.binding.fragmentFirstStartNameInput.getText().toString();
                SharkNetApp.initializeSystem(this.requireActivity(), inputName);
                new ViewModelProvider(this.requireActivity()).get(MainAppViewModel.class)
                        .setName(SharkNetApp.getOwnerID(this.requireActivity()));

                Navigation.findNavController(view).navigate(R.id.action_nav_firstStart_to_nav_channels);
            } catch (SharkException se) {
                Toast.makeText(this.getContext(), se.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });

        return binding.getRoot();
    }
}