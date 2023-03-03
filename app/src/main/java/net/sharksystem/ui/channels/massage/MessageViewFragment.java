package net.sharksystem.ui.channels.massage;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.sharksystem.databinding.FragmentMessageViewBinding;
import net.sharksystem.messenger.SharkMessage;
import net.sharksystem.messenger.SharkMessengerComponent;
import net.sharksystem.messenger.SharkMessengerException;
import net.sharksystem.messenger.android.SNMessageViewHelper;
import net.sharksystem.sharknet.android.SharkNetApp;

import java.io.IOException;

/**
 * Fragment to display detailed information about a message
 * //TODO: implement
 */
public class MessageViewFragment extends Fragment {

    private FragmentMessageViewBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.binding = FragmentMessageViewBinding.inflate(inflater, container, false);

        MessageViewModel messageViewModel = new ViewModelProvider(this.requireActivity()).
                get(MessageViewModel.class);

        AddMessageViewModel addMessageViewModel = new ViewModelProvider(this.requireActivity()).
                get(AddMessageViewModel.class);

        CharSequence uri = addMessageViewModel.getUri().getValue();
        int position = messageViewModel.getPosition().getValue();

        SharkMessengerComponent sharkMessenger =
                SharkNetApp.getSharkNetApp().getSharkMessenger();

        try {
            SharkMessage sharkMessage = sharkMessenger.getChannel(uri)
                    .getMessages(false, true)
                    .getSharkMessage(position, false);

            // Receivers
            CharSequence receiversCharSequence =
                    SNMessageViewHelper.getReceiversCharSequence(sharkMessage);
            this.binding.fragmentMessageViewRecipients.setText(receiversCharSequence);

            // encrypted
            CharSequence encryptedCharSequence =
                    SNMessageViewHelper.getEncryptedCharSequence(sharkMessage);
            this.binding.fragmentMessageViewEncrypted.setText(encryptedCharSequence);

            // sender
            CharSequence senderCharSequence =
                    SNMessageViewHelper.getSenderCharSequence(sharkMessage);
            this.binding.fragmentMessageViewSender.setText(senderCharSequence);

            // content
            CharSequence contentCharSequence =
                    SNMessageViewHelper.getContentCharSequence(sharkMessage);
            this.binding.fragmentMessageViewMessage.setText(contentCharSequence);

            // verified
            CharSequence verifiedCharSequence =
                    SNMessageViewHelper.getVerifiedCharSequence(sharkMessage);
            this.binding.fragmentMessageViewVerified.setText(verifiedCharSequence);

            // time stamp
            CharSequence creationTimeCharSequence =
                    SNMessageViewHelper.getCreationTimeCharSequence(sharkMessage);
            this.binding.fragmentMessageViewTime.setText(creationTimeCharSequence);

            // identity assurance
            CharSequence iACharSequence =
                    SNMessageViewHelper.getIdentityAssuranceCharSequence(sharkMessage);
            this.binding.fragmentMessageViewIA.setText(iACharSequence);

            // asap hops
            CharSequence asapHopsSequence =
                    SNMessageViewHelper.getASAPHopsCharSequence(sharkMessage);
            this.binding.fragmentMessageViewHops.setText(asapHopsSequence);

        } catch (SharkMessengerException | IOException e) {
            throw new RuntimeException(e);
        }

        return this.binding.getRoot();
    }
}