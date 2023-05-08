package net.sharksystem.ui.channels;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import net.sharksystem.R;
import net.sharksystem.SharkNotSupportedException;
import net.sharksystem.databinding.FragmentChannelListBinding;
import net.sharksystem.messenger.SharkMessengerException;
import net.sharksystem.sharknet.android.SharkNetApp;
import net.sharksystem.ui.SelectionMode;
import net.sharksystem.ui.channels.massage.AddMessageViewModel;

import java.io.IOException;

/**
 * Fragment for displaying a list of all channels
 */
public class ChannelListFragment extends Fragment implements ChannelSelectedListener {

    /**
     * Binding to access elements from the layout
     */
    private FragmentChannelListBinding binding;

    /**
     * The ViewModel for saving related data
     */
    private ChannelListViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        this.binding = FragmentChannelListBinding.inflate(inflater, container, false);

        //Get the ViewModel from the provider which has the functionality of a singleton implemented
        //  if the specified owner (this.requireActivity()) is always the same. Otherwise a new
        //  instance is returned, so always passing the same owner is necessary if a connection
        //  between the fragments should be established
        this.viewModel = new ViewModelProvider(this.requireActivity())
                .get(ChannelListViewModel.class);
        this.viewModel.setMode(SelectionMode.SELECT);

        //Set-Up RecyclerView
        RecyclerView recyclerView = binding.fragmentChannelListRecyclerView;

        ChannelListContentAdapter adapter = new ChannelListContentAdapter();
        //add this fragment as a listener to the adapter
        adapter.addChannelSelectedListener(this);

        RecyclerView.LayoutManager layoutManager =
                new LinearLayoutManager(this.getContext());

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        //add the onClickListener for the add button to add a new channel
        this.binding.fragmentChannelListAddChannelButton.setOnClickListener(view ->
                Navigation.findNavController(view)
                        .navigate(R.id.action_nav_channels_to_nav_add_channel));

        return binding.getRoot();
    }

    /**
     * Implementation of the listener interface method.
     * Called when a item of the recycler view was shortly clicked.
     * @param viewHolder the ViewHolder object of the channel item
     */
    @Override
    public void channelSelectedWithShortClick(ChannelListContentAdapter.ViewHolder viewHolder) {
        //get the uri from the ViewHolder object, as this is needed to identify the channel
        String uri = viewHolder.getUriTextView().getText().toString();


        //if in deletion mode (= some channel was long-clicked before) a short click on another
        //  channel will mark this newly selected channel also to be deleted.
        //if not, the normal behavior of showing the message in more detail is executed
        if(this.viewModel.getMode().getValue() == SelectionMode.SELECT) {
            //set the uri in the view model from the AddMessageFragment. This is NOT the same as
            //  the ViewModel that is saved as an attribute in this class. It is a completely
            //  different one. The uri ist needed in order to write a message into that channel, and
            //  as the uri can't be contained later, it is passed on here, where the channel is
            //  selected.
            new ViewModelProvider(this.requireActivity()).get(AddMessageViewModel.class).setUri(uri);

            Navigation.findNavController(this.binding.fragmentChannelListRecyclerView)
                    .navigate(R.id.action_nav_channels_to_nav_channel_view);

        } else {
            //if the deletion mode is on, there are two different cases:
            //1) the view isn't already selected to be deleted, so this needs to be done now
            //2) the view was already selected and needs to be unselected

            if(!this.viewModel.getDeletionList().getValue().contains(viewHolder)) {
                //Case 1: add the channel to the deletion list and change the background color,
                //  so the user sees that the channel was selected
                this.viewModel.getDeletionList().getValue().add(viewHolder);
                viewHolder.itemView.setBackgroundColor(0x88888888);
            } else {
                //Case 2: remove the channel from the deletion list and change the background color
                //  back to normal, so it appears to be unselected
                //TODO: the background color is not pure white. Change to real back ground color
                this.viewModel.getDeletionList().getValue().remove(viewHolder);
                viewHolder.itemView.setBackgroundColor(0xFFFFFFFF);
            }
        }


    }

    /**
     * Implementation of the listener interface method.
     * Called when a item of the recycler view was clicked for a longer period.
     * @param viewHolder the ViewHolder object of the item
     */
    @Override
    public void channelSelectedWithLongClick(ChannelListContentAdapter.ViewHolder viewHolder) {
        //If the fragment is currently in the selection state, a long click changes the state
        //  to the deletion state.
        //If the fragment was already in the deletion state, nothing happens
        if(this.viewModel.getMode().getValue() == SelectionMode.SELECT) {
            //First a menu provider needs to be created, as in the deletion state a new app bar icon
            //  is displayed. That's the trash-bin which can be clicked to finally delete the
            //  selected items.
            MenuProvider menuProvider = new MenuProvider() {
                @Override
                public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                    menuInflater.inflate(R.menu.menu_channel_list_delete, menu);
                }

                /**
                 * When an item is click, this method is called.
                 * @param menuItem the menu item that was selected
                 * @return i have no idea, seems to change noting if true or false
                 */
                @Override
                public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                    //TODO: delete selected channels
                    //go through each channel in the saved list from the ViewModel
                    for (ChannelListContentAdapter.ViewHolder channel :
                            viewModel.getDeletionList().getValue()) {
                        try {
                            SharkNetApp.getSharkNetApp().getSharkMessenger()
                                    .removeChannel(channel.getUriTextView().getText());
                        } catch (IOException | SharkMessengerException |
                                 SharkNotSupportedException ignored) {
                            //Functionality not yet implemented
                            Toast.makeText(getContext(), "Not yet implemented functionality",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                    //after deletion, the state is set back to the normal selection state
                    viewModel.setMode(SelectionMode.SELECT);

                    //any selected items are unselected from the list and from the view by removing
                    //  the highlighting
                    for(ChannelListContentAdapter.ViewHolder channel : viewModel.getDeletionList()
                            .getValue()) {
                        channel.itemView.setBackgroundColor(0xFFFFFFFF);
                    }
                    viewModel.getDeletionList().getValue().clear();
                    return false;
                }
            };

            //Add the menu to the activity app bar. Only as long as the fragment is in the resumed
            //  state.
            //TODO: pressing the up button in deletion state finishes the app, as we are already in
            //  at the root fragment. This needs to be changed. the back button should do the same
            //  as if the delete icon was pressed but without deletion.
            this.requireActivity().addMenuProvider(menuProvider, this.getViewLifecycleOwner(),
                    Lifecycle.State.RESUMED);

            this.viewModel.setMode(SelectionMode.DELETE);
        }
    }
}