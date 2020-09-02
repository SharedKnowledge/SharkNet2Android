package net.sharksystem.persons.android;

import android.content.Context;

import net.sharksystem.asap.ASAPException;
import net.sharksystem.asap.android.apps.ASAPActivity;
import net.sharksystem.sharknet.android.Owner;

public class PersonAppActivity extends ASAPActivity {
    public PersonAppActivity() {
        super(PersonsStorageAndroidComponent.getPersonsStorage().getASAPApplication());
    }

    Context getContext() throws ASAPException {
        return PersonsStorageAndroidComponent.getPersonsStorage().getContext();
    }

    Owner getOwnerStorage() {
        return PersonsStorageAndroidComponent.getPersonsStorage().getOwner();
    }
}
