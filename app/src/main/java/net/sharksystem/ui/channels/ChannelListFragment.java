package net.sharksystem.ui.channels;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.sharksystem.R;
import net.sharksystem.databinding.FragmentChannelListBinding;

public class ChannelListFragment extends Fragment {
    private FragmentChannelListBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        this.binding = FragmentChannelListBinding.inflate(inflater, container, false);

        RecyclerView recyclerView = binding.fragmentChannelsRecyclerView;

        ChannelListContentAdapter adapter = new ChannelListContentAdapter();
        RecyclerView.LayoutManager layoutManager =
                new LinearLayoutManager(this.getContext());

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        this.binding.floatingActionButton.setOnClickListener(view -> {
            Navigation.findNavController(view).navigate(R.id.nav_add_channel);
        });

        return binding.getRoot();
    }
}