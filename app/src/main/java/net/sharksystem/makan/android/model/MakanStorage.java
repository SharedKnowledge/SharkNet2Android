package net.sharksystem.makan.android.model;

import net.sharksystem.makan.MakanException;
import net.sharksystem.makan.android.viewadapter.MakanInformation;

public class MakanStorage {

    public int size() throws MakanException {
        // dummy
        return 10;
    }

    public MakanInformation getMakanInfo(int position) throws MakanException  {
        // dummy
        return new MakanInformation();
    }
}
