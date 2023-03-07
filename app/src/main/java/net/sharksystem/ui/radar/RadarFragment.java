package net.sharksystem.ui.radar;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.sharksystem.databinding.FragmentRadarBinding;


public class RadarFragment extends Fragment {

    /**
     * Binding to access elements from the layout
     */
    private FragmentRadarBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        this.binding = FragmentRadarBinding.inflate(inflater, container, false);

        return this.binding.getRoot();
    }
}