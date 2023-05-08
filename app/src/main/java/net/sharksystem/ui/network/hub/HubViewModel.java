package net.sharksystem.ui.network.hub;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import net.sharksystem.hub.peerside.HubConnectorDescription;

public class HubViewModel extends ViewModel {

    private static final String DEFAULT_HOST_NAME = "asaphub.f4.htw-berlin.de";
    private static final int DEFAULT_PORT_NUMBER = 6910;

    private static final boolean STANDARD_MULTI_CHANNEL_ENABLED_STATE = false;

    private final MutableLiveData<HubConnectorDescription> selectedHub;


    public HubViewModel() {
        this.selectedHub = new MutableLiveData<>();
    }

    protected LiveData<HubConnectorDescription> getSelectedHub() throws IllegalStateException {
        if(this.selectedHub != null) {
            return this.selectedHub;
        } else {
            throw new IllegalStateException();
        }
    }

    public void setSelectedHub(HubConnectorDescription hub) {
        this.selectedHub.setValue(hub);
    }


    protected HubViewState getState() {
        if(this.selectedHub.getValue() != null) {
            return HubViewState.EDIT;
        } else {
            return HubViewState.CREATE;
        }
    }

    protected String getDefaultHostName() {
        return DEFAULT_HOST_NAME;
    }

    protected int getDefaultPortNumber() {
        return DEFAULT_PORT_NUMBER;
    }

    protected boolean isMultiChannelEnabledByDefault() {
        return STANDARD_MULTI_CHANNEL_ENABLED_STATE;
    }
}
