package net.sharksystem.asap.sharknet.android;

import android.content.Context;

import net.sharksystem.asap.ASAPException;
import net.sharksystem.asap.android.apps.ASAPActivity;
import net.sharksystem.persons.android.PersonsStorageAndroidComponent;

public class SNChannelsActivity extends ASAPActivity {
    public SNChannelsActivity() {
        super(SNChannelsComponent.getSharkNetChannelComponent().getASAPApplication());
    }

    Context getContext() throws ASAPException {
        return PersonsStorageAndroidComponent.getPersonsStorage().getContext();
    }
}
