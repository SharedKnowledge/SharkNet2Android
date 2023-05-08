package net.sharksystem.ui.network.hub;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.selection.ItemDetailsLookup;
import androidx.recyclerview.selection.OnItemActivatedListener;
import androidx.recyclerview.selection.SelectionPredicates;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.selection.StableIdKeyProvider;
import androidx.recyclerview.selection.StorageStrategy;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import net.sharksystem.R;
import net.sharksystem.SharkException;
import net.sharksystem.databinding.FragmentHubListBinding;
import net.sharksystem.hub.peerside.HubConnectorDescription;
import net.sharksystem.sharknet.android.SharkNetApp;

/**
 * Fragment for displaying a list of all configured hubs
 */
public class HubListFragment extends Fragment implements OnItemActivatedListener<Long> {

    /**
     * Binding to access elements from the layout
     */
    private FragmentHubListBinding binding;

    private HubViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.binding = FragmentHubListBinding.inflate(inflater, container, false);
        this.viewModel = new ViewModelProvider(this.requireActivity()).get(HubViewModel.class);

        //Set-Up Recycler View
        RecyclerView recyclerView = this.binding.fragmentHubListRecyclerView;

        HubListContentAdapter adapter = new HubListContentAdapter();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getContext());

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        SelectionTracker<Long> tracker = new SelectionTracker.Builder<>(
                "contact-selection",
                recyclerView,
                new StableIdKeyProvider(recyclerView),
                new HubDetailsLookup(recyclerView),
                StorageStrategy.createLongStorage()).
                withSelectionPredicate(SelectionPredicates.createSelectAnything()).
                withOnItemActivatedListener(this).
                build();

        adapter.setTracker(tracker);


        //Set OnClickListener when the user clicks the add button
        this.binding.fragmentHubListAddHubButton.setOnClickListener(view -> {
            this.viewModel.setSelectedHub(null);
            Navigation.findNavController(view)
                    .navigate(R.id.action_nav_hub_list_to_nav_hub_view);
        });


        return this.binding.getRoot();
    }

    @Override
    public boolean onItemActivated(@NonNull ItemDetailsLookup.ItemDetails<Long> item, @NonNull MotionEvent e) {
        try {
            HubConnectorDescription hcd = SharkNetApp.getSharkNetApp().getSharkPeer().
                    getHubDescription(item.getPosition());

            this.viewModel.setSelectedHub(hcd);

            Navigation.findNavController(this.requireView()).
                    navigate(R.id.action_nav_hub_list_to_nav_hub_view);

            return true;
        } catch (SharkException ex) {
            return false;
        }
    }
}