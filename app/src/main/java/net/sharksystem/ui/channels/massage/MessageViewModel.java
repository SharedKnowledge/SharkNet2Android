package net.sharksystem.ui.channels.massage;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MessageViewModel extends ViewModel {

    private final MutableLiveData<Integer> position;

    public MessageViewModel() {
        this.position = new MutableLiveData<>();
    }

    public void setPosition(int id) {
        this.position.setValue(id);
    }

    public LiveData<Integer> getPosition() {
        return this.position;
    }
}
