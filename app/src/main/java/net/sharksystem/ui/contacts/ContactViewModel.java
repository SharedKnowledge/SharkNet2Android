package net.sharksystem.ui.contacts;

import androidx.lifecycle.ViewModel;

import net.sharksystem.asap.persons.PersonValues;

public class ContactViewModel extends ViewModel {

    private PersonValues personValues;


    PersonValues getPersonValues() {
        return this.personValues;
    }

    void setPerson(PersonValues values) {
        this.personValues = values;
    }
}
