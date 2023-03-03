package net.sharksystem.ui.channels.massage;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.sharksystem.R;
import net.sharksystem.databinding.FragmentChannelViewBinding;

/**
 * Fragment for a specific channel. Send messages into that channel
 */
public class ChannelViewFragment extends Fragment implements MessageSelectedListener {

    /**
     * Binding to access elements from the layout
     */
    private FragmentChannelViewBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.binding = FragmentChannelViewBinding.inflate(inflater, container, false);

        //Set-Up the RecyclerView
        RecyclerView recyclerView = this.binding.fragmentChannelViewRecyclerView;

        AddMessageViewModel viewModel = new ViewModelProvider(this.requireActivity()).get(AddMessageViewModel.class);

        ChannelViewContentAdapter adapter = new ChannelViewContentAdapter(viewModel.getUri().getValue());
        adapter.addListener(this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getContext());

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        this.binding.fragmentChannelViewAddMessageButton.setOnClickListener(view ->
                Navigation.findNavController(view)
                        .navigate(R.id.action_nav_channel_view_to_nav_add_message));

        //TODO: make channel name visible in app bar instead of "Channel"
        // Inflate the layout for this fragment

        return this.binding.getRoot();
    }


    @Override
    public void onMessageSelected(int id) {
        MessageViewModel viewModel = new ViewModelProvider(this.requireActivity()).
                get(MessageViewModel.class);

        viewModel.setPosition(id);

        //TODO: message view should slide in and out from the right side of the screen
        Navigation.findNavController(this.requireView())
                .navigate(R.id.action_nav_channel_view_to_nav_message_view);
    }
}