package net.sharksystem.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
/**
 * ViewModel saving any data related for the app
 */
public class MainAppViewModel extends ViewModel {

    /**
     * The name inputted by the user on initial start of the app
     */
    private final MutableLiveData<String> inputName;

    private final MutableLiveData<Boolean> bluetoothEnabled;

    private final MutableLiveData<Boolean> drawerShouldBeLocked;

    /**
     * Constructor for ViewModel
     */
    public MainAppViewModel() {
        this.inputName = new MutableLiveData<>();
        this.bluetoothEnabled = new MutableLiveData<>();
        this.drawerShouldBeLocked = new MutableLiveData<>();
        this.drawerShouldBeLocked.setValue(false);
    }

    /**
     * Get the name of the user as LiveData object
     * @return the name of the user
     */
    public LiveData<String> getName() {
        return this.inputName;
    }

    /**
     * Set the name of the user. Observers will be informed
     * @param name the new name of the user
     */
    public void setName(String name) {
        this.inputName.setValue(name);
    }

    public LiveData<Boolean> isBluetoothEnabled() {
        return this.bluetoothEnabled;
    }

    public void setBluetoothEnabled(boolean enable) {
        this.bluetoothEnabled.setValue(enable);
    }

    public LiveData<Boolean> shouldDrawerBeLocked() {
        return this.drawerShouldBeLocked;
    }

    public void requestLockDrawer() {
        this.drawerShouldBeLocked.setValue(true);
    }

    public void requestUnlockDrawer() {
        this.drawerShouldBeLocked.setValue(false);
    }


}
