package net.sharksystem.makan.android.viewadapter;

import net.sharksystem.makan.android.MakanApp;

public class MakanInformation {
    public CharSequence getURI() {
        return MakanApp.getMakanApp(null).getExampleMakanURI();
    }

    public CharSequence getName() {
        return MakanApp.getMakanApp(null).getExampleMakanName();
    }
}
