package net.sharksystem.ui.network;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.sharksystem.R;
import net.sharksystem.databinding.FragmentNetworkBinding;


public class NetworkFragment extends Fragment {

    /**
     * Binding to access elements from the layout
     */
    private FragmentNetworkBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        this.binding = FragmentNetworkBinding.inflate(inflater,container,false);

        //Set-Up OnClickListeners
        //...when bluetooth is switched on or off
        this.binding.fragmentNetworkBluetoothOnOffButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            //TODO: activate or deactivate bluetooth
        });

        //...when bluetooth scanning should be enabled or disabled
        this.binding.fragmentNetworkBluetoothScanButton.setOnCheckedChangeListener(((buttonView, isChecked) -> {
            //TODO: make visible and scan or stop discovery and itself stop being discoverable
        }));

        //...when connections to hubs should be made or not
        this.binding.fragmentNetworkBluetoothConnectHubsButton.setOnCheckedChangeListener(((buttonView, isChecked) -> {
            //TODO: allow to connect to hubs or not
        }));

        //...when hubs are configured
        this.binding.fragmentNetworkBluetoothConfigureHubsButton.setOnClickListener(view ->
                Navigation.findNavController(view)
                        .navigate(R.id.action_nav_network_to_nav_hub_list));

        return this.binding.getRoot();
    }
}