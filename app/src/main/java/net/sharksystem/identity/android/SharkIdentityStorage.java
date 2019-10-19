package net.sharksystem.identity.android;

import identity.IdentityStorage;

public interface SharkIdentityStorage extends IdentityStorage {
    CharSequence getOwnerID();
    CharSequence getOwnerName();
    void setOwnerName(CharSequence name);
    void setOwnerID(CharSequence ownerID);

//    /**
//     * Create and set a new uuid based on user name
//     */
//    void setNewOwnerUUID(String userNameString);
}
