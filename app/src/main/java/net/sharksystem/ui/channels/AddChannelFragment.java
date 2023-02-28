package net.sharksystem.ui.channels;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import net.sharksystem.R;
import net.sharksystem.android.ASAPChannelIntent;
import net.sharksystem.asap.utils.PeerIDHelper;
import net.sharksystem.databinding.FragmentAddChannelBinding;
import net.sharksystem.messenger.SharkMessengerException;
import net.sharksystem.messenger.android.SNChannelViewActivity;
import net.sharksystem.sharknet.android.SharkNetApp;

import java.io.IOException;

/**
 * Fragment for adding a new Channel
 */
public class AddChannelFragment extends Fragment {

    /**
     * Binding to access elements from the layout
     */
    private FragmentAddChannelBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.binding = FragmentAddChannelBinding.inflate(inflater, container, false);

        //Create a new unique id for the channel and set it in the view
        String uniqueID = PeerIDHelper.createUniqueID();
        String channelUri = "sn2://" + uniqueID;
        this.binding.fragmentAddChannelChannelUri.setText(channelUri);

        //add an onClickListener when the user wants to create the specified channel
        this.binding.fragmentAddChannelAddButton.setOnClickListener(view ->  {

            //get the needed data from the view
            EditText nameText = this.binding.fragmentAddChannelAddNameInputField;
            String channelNameString = nameText.getEditableText().toString();
            TextView tvUri = this.binding.fragmentAddChannelChannelUri;
            String uriString = tvUri.getText().toString();

            try {
                //create the channel
                SharkNetApp.getSharkNetApp().getSharkMessenger().createChannel(
                        uriString, channelNameString
                );

                //navigate back
                Navigation.findNavController(view).navigate(R.id.action_nav_add_channel_to_nav_channels);

            } catch (IOException e) {
                String text = "failure: " + e.getLocalizedMessage();
                Log.e(this.getLogStart(), text);
                Toast.makeText(this.getContext(), "IO error - that's serious", Toast.LENGTH_SHORT).show();
            } catch (SharkMessengerException e) {
                String text = "failure: " + e.getLocalizedMessage();
                Log.w(this.getLogStart(), text);
                Toast.makeText(this.getContext(), "already exists(?)", Toast.LENGTH_SHORT).show();
            }
        });

        //add onClickListener for aborting the addition of a new channel
        this.binding.fragmentAddChannelExitButton.setOnClickListener(view -> {
            Navigation.findNavController(view).navigate(R.id.nav_channels);
        });

        return binding.getRoot();
    }

    private String getLogStart() {
        return this.getClass().getSimpleName();
    }
}