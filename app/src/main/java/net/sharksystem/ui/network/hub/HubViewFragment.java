package net.sharksystem.ui.network.hub;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.sharksystem.R;
import net.sharksystem.databinding.FragmentHubViewBinding;

/**
 * Fragment for configuration of a hub
 */
public class HubViewFragment extends Fragment {

    /**
     * Binding to access elements from the layout
     */
    private FragmentHubViewBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.binding = FragmentHubViewBinding.inflate(inflater, container, false);

        //Set-Up OnClickListeners
        //...when default values should be inserted to editable fields
        this.binding.fragmentHubViewDefaultValuesButton.setOnClickListener(view -> {
            //TODO: set default values
        });

        //..when the configured hub should be saved
        this.binding.fragmentHubViewSaveButton.setOnClickListener(view -> {
            //TODO: save hub
            Navigation.findNavController(view).navigate(R.id.action_nav_hub_view_to_nav_hub_list);
        });

        //...when the configuration is aborted
        this.binding.fragmentHubViewAbortButton.setOnClickListener(view ->
            Navigation.findNavController(view).navigate(R.id.action_nav_hub_view_to_nav_hub_list)
        );

        return this.binding.getRoot();
    }
}