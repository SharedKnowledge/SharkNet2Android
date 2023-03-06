package net.sharksystem.ui.settings.personal;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import net.sharksystem.R;
import net.sharksystem.SharkException;
import net.sharksystem.databinding.FragmentPersonalDataBinding;
import net.sharksystem.sharknet.android.SharkNetApp;

public class PersonalDataFragment extends Fragment {

    private FragmentPersonalDataBinding binding;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.binding = FragmentPersonalDataBinding.inflate(this.getLayoutInflater());

        this.binding.fragmentPersonalDataNameInput.
                setText(SharkNetApp.getSharkNetApp().getOwnerName());

        this.binding.fragmentPersonalDataSaveButton.setOnClickListener(view ->  {

            String ownerNameString = this.binding.fragmentPersonalDataNameInput.getText().toString();

            SharkNetApp app = SharkNetApp.getSharkNetApp();
            try {
                // TODO should this be initialize system? that would change ownerID
                app.changeOwnerName(this.requireActivity(), ownerNameString);
            } catch (SharkException e) {
                Toast.makeText(this.getContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }

            Navigation.findNavController(view).
                    navigate(R.id.action_nav_personal_data_to_nav_settings);
        });

        return this.binding.getRoot();
    }
}