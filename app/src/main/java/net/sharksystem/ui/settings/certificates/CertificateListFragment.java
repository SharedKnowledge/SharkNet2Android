package net.sharksystem.ui.settings.certificates;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.sharksystem.databinding.FragmentCertificateListBinding;

public class CertificateListFragment extends Fragment {

    private FragmentCertificateListBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.binding = FragmentCertificateListBinding.inflate(this.getLayoutInflater());

        return this.binding.getRoot();
    }
}