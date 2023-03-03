package net.sharksystem.ui.settings.sendCredentials;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.sharksystem.databinding.FragmentSendCredentialsBinding;

public class SendCredentialsFragment extends Fragment {

    private FragmentSendCredentialsBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.binding = FragmentSendCredentialsBinding.inflate(this.getLayoutInflater());

        //this.binding.

        return this.binding.getRoot();
    }
}