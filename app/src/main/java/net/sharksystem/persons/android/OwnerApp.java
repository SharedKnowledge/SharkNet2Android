package net.sharksystem.persons.android;

import net.sharksystem.asap.android.apps.ASAPApplication;

import java.util.ArrayList;
import java.util.List;

public class OwnerApp extends ASAPApplication {
    public static final CharSequence OWNER_ASAP_FORMAT = "asap/owner";

    private static OwnerApp instance = null;

    public static OwnerApp getOwnerApp() {
        if(OwnerApp.instance == null) {
            OwnerApp.instance = new OwnerApp();
        }

        return OwnerApp.instance;
    }

    private OwnerApp() {
        super(OWNER_ASAP_FORMAT);
    }

    /**
     * send credentials via BT
     * @return control number
     */
    public int sendCredentials() {
        return 42;
    }
}
