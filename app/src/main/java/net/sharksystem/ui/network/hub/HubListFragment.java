package net.sharksystem.ui.network.hub;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.sharksystem.R;
import net.sharksystem.databinding.FragmentHubListBinding;

/**
 * Fragment for displaying a list of all configured hubs
 */
public class HubListFragment extends Fragment {

    /**
     * Binding to access elements from the layout
     */
    private FragmentHubListBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.binding = FragmentHubListBinding.inflate(inflater, container, false);

        //Set-Up Recycler View
        RecyclerView recyclerView = this.binding.fragmentHubListRecyclerView;

        HubListContentAdapter adapter = new HubListContentAdapter();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getContext());

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        //Set OnClickListener when the user clicks the add button
        this.binding.fragmentHubListAddHubButton.setOnClickListener(view ->
                Navigation.findNavController(view)
                        .navigate(R.id.action_nav_hub_list_to_nav_hub_view));

        return this.binding.getRoot();
    }
}