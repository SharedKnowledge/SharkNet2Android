package net.sharksystem.ui.channels.massage;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.sharksystem.R;
import net.sharksystem.databinding.FragmentMessageViewBinding;

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



        return this.binding.getRoot();
    }
}