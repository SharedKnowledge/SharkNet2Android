package net.sharksystem.ui.channels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * ViewModel for the ChannelListFragment. Separation of storing and displaying
 */
public class ChannelListViewModel extends ViewModel {

    /**
     * The mode in which the fragment currently is
     */
    private final MutableLiveData<SelectionMode> mode;

    /**
     * The list of channels that are selected to be deleted
     */
    private final MutableLiveData<List<ChannelListContentAdapter.ViewHolder>> deletionList;

    /**
     * Constructor for ViewModel
     */
    public ChannelListViewModel() {
        this.mode = new MutableLiveData<>();
        this.deletionList = new MutableLiveData<>();
        this.deletionList.setValue(new ArrayList<>());
    }

    /**
     * Returns the mode as LiveData object
     * @return the mode
     */
    public LiveData<SelectionMode> getMode() {
        return this.mode;
    }

    /**
     * Set the mode of the fragment.
     * As this is a ViewModel, observers will be informed.
     * @param mode the new mode
     */
    public void setMode(SelectionMode mode) {
        this.mode.setValue(mode);
    }

    /**
     * Returns the list of channels that should be removed als live data.
     * @return the list of channels
     */
    public LiveData<List<ChannelListContentAdapter.ViewHolder>> getDeletionList() {
        return this.deletionList;
    }
}
