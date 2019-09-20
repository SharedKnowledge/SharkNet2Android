package net.sharksystem.identity.android;

public interface SharkIdentityStorage {
    CharSequence getOwnerID();
    CharSequence getOwnerName();

    void setOwnerName(CharSequence name);
    boolean isOwnerSet();
}
