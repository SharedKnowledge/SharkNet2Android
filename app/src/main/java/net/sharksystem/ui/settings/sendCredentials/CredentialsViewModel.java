package net.sharksystem.ui.settings.sendCredentials;

import androidx.lifecycle.ViewModel;

public class CredentialsViewModel extends ViewModel {

    private CharSequence cic;


    public CredentialsViewModel() {
        this.cic = "";
    }

    CharSequence getCIC() {
        return this.cic;
    }

    void setCIC(CharSequence cic) {
        this.cic = cic;
    }
}
