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

public class AddChannelFragment extends Fragment {

    private FragmentAddChannelBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.binding = FragmentAddChannelBinding.inflate(inflater, container, false);

        String uniqueID = PeerIDHelper.createUniqueID();
        String channelUri = "sn2://" + uniqueID;
        this.binding.fragmentAddChannelChannelUri.setText(channelUri);

        this.binding.fragmentAddChannelAddButton.setOnClickListener(view ->  {
            EditText nameText = this.binding.fragmentAddChannelAddNameInputField;
            String channelNameString = nameText.getEditableText().toString();
            TextView tvUri = this.binding.fragmentAddChannelChannelUri;
            String uriString = tvUri.getText().toString();

            try {
                SharkNetApp.getSharkNetApp().getSharkMessenger().createChannel(
                        uriString, channelNameString
                );
                Navigation.findNavController(view).navigate(R.id.nav_channels);

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

        this.binding.fragmentAddChannelExitButton.setOnClickListener(view -> {
            Navigation.findNavController(view).navigate(R.id.nav_channels);
        });

        return binding.getRoot();
    }

    private String getLogStart() {
        return this.getClass().getSimpleName();
    }
}