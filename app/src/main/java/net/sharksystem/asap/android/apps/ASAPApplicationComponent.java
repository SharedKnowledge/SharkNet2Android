package net.sharksystem.asap.android.apps;

import android.content.Context;
import net.sharksystem.asap.ASAPException;

import java.util.Collection;

public interface ASAPApplicationComponent {
    Context getContext() throws ASAPException;
    ASAPApplication getASAPApplication();
}
