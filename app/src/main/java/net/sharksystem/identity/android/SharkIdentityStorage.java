package net.sharksystem.identity.android;

public interface SharkIdentityStorage {
    CharSequence getOwnerID();
    CharSequence getOwnerName();
    void setOwnerName(CharSequence name);
    void setOwnerID(CharSequence ownerID);

    /**
     * Create and set a new uuid based on user name
     */
    void setNewOwnerUUID(String userNameString);
}
