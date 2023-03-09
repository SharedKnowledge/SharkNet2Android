package net.sharksystem.ui.contacts;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import net.sharksystem.asap.persons.PersonValues;
import net.sharksystem.ui.SelectionMode;

public class ContactViewModel extends ViewModel {

    private PersonValues personValues;

    private final MutableLiveData<SelectionMode> selectionMode;

    public ContactViewModel() {
        this.selectionMode = new MutableLiveData<>();
    }


    PersonValues getPersonValues() {
        return this.personValues;
    }

    void setPerson(PersonValues values) {
        this.personValues = values;
    }

    public LiveData<SelectionMode> getSelectionMode() {
        return this.selectionMode;
    }

    public void setSelectionMode(SelectionMode mode) {
        this.selectionMode.setValue(mode);
    }
}
