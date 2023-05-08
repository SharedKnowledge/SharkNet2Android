package net.sharksystem.ui.network.hub;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import net.sharksystem.R;
import net.sharksystem.SharkPeer;
import net.sharksystem.databinding.FragmentHubViewBinding;
import net.sharksystem.hub.ASAPHubException;
import net.sharksystem.hub.peerside.HubConnectorDescription;
import net.sharksystem.hub.peerside.TCPHubConnectorDescriptionImpl;
import net.sharksystem.sharknet.android.SharkNetApp;

import java.io.IOException;

/**
 * Fragment for configuration of a hub
 */
public class HubViewFragment extends Fragment {

    /**
     * Binding to access elements from the layout
     */
    private FragmentHubViewBinding binding;

    private HubViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.binding = FragmentHubViewBinding.inflate(inflater, container, false);
        this.viewModel = new ViewModelProvider(this.requireActivity()).get(HubViewModel.class);

        //Set-Up OnClickListeners
        //...when default values should be inserted to editable fields
        this.binding.fragmentHubViewDefaultValuesButton.setOnClickListener(view -> {
            this.binding.fragmentHubViewHostNameInput.
                    setText(this.viewModel.getDefaultHostName());

            this.binding.fragmentHubViewPortInput.
                    setText(String.valueOf(this.viewModel.getDefaultPortNumber()));

            this.binding.fragmentHubViewMultiChannelButton.
                    setChecked(this.viewModel.isMultiChannelEnabledByDefault());
        });

        //initialize
        if(this.viewModel.getState() == HubViewState.CREATE) {
            //if a new hub should be added, the default values are initialized by simulating a click
            //  on the default values button
            this.binding.fragmentHubViewDefaultValuesButton.callOnClick();

        } else {
            //else wise the values from the selected hub are loaded
            try {
                HubConnectorDescription hcd = this.viewModel.getSelectedHub().getValue();
                this.binding.fragmentHubViewHostNameInput.
                        setText(hcd.getHostName());

                this.binding.fragmentHubViewPortInput.
                        setText(String.valueOf(hcd.getPortNumber()));

                this.binding.fragmentHubViewMultiChannelButton.
                        setChecked(hcd.canMultiChannel());
            } catch (ASAPHubException | NullPointerException e) {
                throw new RuntimeException();
            }

        }

        //..when the configured hub should be saved
        this.binding.fragmentHubViewSaveButton.setOnClickListener(view -> {

            String hostName = this.binding.fragmentHubViewHostNameInput.getText().toString();
            String portString = this.binding.fragmentHubViewPortInput.getText().toString();
            boolean multiChannelEnabled = this.binding.fragmentHubViewMultiChannelButton.isChecked();
            System.out.println(multiChannelEnabled);

            try {
                int portNumber = Integer.parseInt(portString);

                HubConnectorDescription newHcd =
                        new TCPHubConnectorDescriptionImpl(hostName, portNumber, multiChannelEnabled);

                SharkPeer peer = SharkNetApp.getSharkNetApp().getSharkPeer();

                //if(view == this.findViewById(R.id.deleteButton)) {
                //    sPeer.removeHubDescription(descriptionFromGUI);
                //}
                // remove old one - it was most probably changed
                if(this.viewModel.getState() == HubViewState.EDIT) {
                    HubConnectorDescription hcd = this.viewModel.getSelectedHub().getValue();
                    peer.removeHubDescription(hcd);
                }
                // add data from GUI
                peer.addHubDescription(newHcd);


                Navigation.findNavController(view).
                        navigate(R.id.action_nav_hub_view_to_nav_hub_list);

            } catch (NumberFormatException e) {
                Toast.makeText(this.getContext(), "port must be a number: " + portString,
                                Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                Toast.makeText(this.getContext(), "port must be a number: " + portString,
                        Toast.LENGTH_SHORT).show();
                Log.e(this.getClass().getSimpleName(), "not good: " + e.getLocalizedMessage());
            }
        });

        //...when the configuration is aborted
        this.binding.fragmentHubViewAbortButton.setOnClickListener(view ->
            Navigation.findNavController(view).navigate(R.id.action_nav_hub_view_to_nav_hub_list)
        );

        return this.binding.getRoot();
    }
}