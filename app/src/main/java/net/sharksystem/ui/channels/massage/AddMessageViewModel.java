package net.sharksystem.ui.channels.massage;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * ViewModel for sending a new message.
 */
public class AddMessageViewModel extends ViewModel {

    /**
     * The uri of the channel in which the message is send
     */
    private final MutableLiveData<String> uri;

    /**
     * Constructor of the ViewModel
     */
    public AddMessageViewModel() {
        this.uri = new MutableLiveData<>();
    }

    /**
     * Return the uri as observable LiveData object
     * @return
     */
    public LiveData<String> getUri() {
        return this.uri;
    }

    /**
     * Set the uri.
     * This is done in the ChannelListFragment to determine which channel was selected, as it is
     *  impossible to get the uri when in the ChannelViewFragment or AddMessageFragment
     * @param uri the uri
     */
    public void setUri(String uri) {
        this.uri.setValue(uri);
    }
}
