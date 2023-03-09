package net.sharksystem.ui.channels.massage;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import net.sharksystem.R;
import net.sharksystem.databinding.FragmentAddMassageBinding;
import net.sharksystem.messenger.SharkMessengerException;
import net.sharksystem.sharknet.android.SharkNetApp;
import net.sharksystem.ui.SelectionMode;
import net.sharksystem.ui.contacts.ContactViewModel;

import java.io.IOException;

/**
 * Fragment for adding a message to a channel
 */
public class AddMessageFragment extends Fragment {

    /**
     * Binding to access elements from the layout
     */
    private FragmentAddMassageBinding binding;

    /**
     * The ViewModel containing the needed uri
     */
    private AddMessageViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.viewModel = new ViewModelProvider(this.requireActivity()).get(AddMessageViewModel.class);

        this.binding = FragmentAddMassageBinding.inflate(inflater, container, false);

        //Set-Up onClickListeners
        this.binding.fragmentAddMessageSelectRecipientsButton.setOnClickListener(view -> {
            //TODO: select recipients
            new ViewModelProvider(this.requireActivity()).get(ContactViewModel.class).
                    setSelectionMode(SelectionMode.SELECT);
            Navigation.findNavController(view).navigate(R.id.action_nav_add_message_to_nav_contacts);
        });

        this.binding.fragmentAddMessageSelectAnybodyButton.setOnClickListener(view -> {
            //TODO: select any recipient functionality
        });

        this.binding.fragmentAddMessageSendMessageButton.setOnClickListener(view -> {
            // get new message
            EditText messageTextView = binding.fragmentAddMessageMessageInput;
            String messageText = messageTextView.getText().toString();

            if (messageText.isEmpty()) {
                Toast.makeText(this.getContext(), "message is empty", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    // let's sort things out.
                    byte[] content = messageText.getBytes();

                    CheckBox signCheckBox = binding.fragmentAddMessageSignedCheckbox;
                    boolean sign = signCheckBox.isChecked();

                    CheckBox encryptedCheckBox = binding.fragmentAddMessageEncryptedCheckbox;
                    boolean encrypt = encryptedCheckBox.isChecked();

                    this.viewModel.getUri().observe(this.getViewLifecycleOwner(), uriState -> {
                        this.binding.fragmentAddMessageChannelName.setText(uriState);
                    });

                    CharSequence uri = this.binding.fragmentAddMessageChannelName.getText();

                    // send with shark messenger
                    SharkNetApp.getSharkNetApp().getSharkMessenger().sendSharkMessage(
                            content, uri, sign, encrypt);

                } catch (IOException | SharkMessengerException e) {
                    Toast.makeText(this.getContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }

            //navigate back to the ChannelViewFragment
            Navigation.findNavController(view).navigate(R.id.action_nav_add_message_to_nav_channel_view);
        });

        this.binding.fragmentAddMessageAbortButton.setOnClickListener(view -> {
            Navigation.findNavController(view).navigate(R.id.action_nav_add_message_to_nav_channel_view);
        });

        return this.binding.getRoot();
    }
}