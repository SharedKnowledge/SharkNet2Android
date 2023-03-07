package net.sharksystem.ui.settings.sendCredentials;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import net.sharksystem.pki.CredentialMessage;

public class CredentialsViewModel extends ViewModel {

    private final MutableLiveData<CredentialMessage> credentialMessage;

    public CredentialsViewModel() {
        this.credentialMessage = new MutableLiveData<>();
    }

    LiveData<CredentialMessage> getCredentialMessage() {
        return this.credentialMessage;
    }

    void setCredentialMessage(CredentialMessage credentialMessage) {
        this.credentialMessage.setValue(credentialMessage);
    }
}
