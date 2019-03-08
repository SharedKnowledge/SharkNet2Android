package net.sharksystem.identity.android;

import identity.Person;

public class IdentityStorageAndroid implements SharkIdentityStorage {
    private static SharkIdentityStorage instance;

    private CharSequence ownerName = "dummy name";
    private CharSequence ownerID = "42";

    public static SharkIdentityStorage getIdentityStorage() {
        if(IdentityStorageAndroid.instance == null) {
            IdentityStorageAndroid.instance = new IdentityStorageAndroid();
        }

        return IdentityStorageAndroid.instance;
    }

    @Override
    public CharSequence getNameByID(CharSequence userID) {
        if(this.ownerID.toString().equalsIgnoreCase(userID.toString())) {
            return this.ownerName;
        }
        // else:
        // dummy
        return userID;
    }

    @Override
    public Person getPersonByID(CharSequence userID) {
        if(this.ownerID.toString().equalsIgnoreCase(userID.toString())) {
            return new PersonIdentity(this.ownerName, this.ownerID);
        }

        // dummy
        return new PersonIdentity("dummy name", "43");
    }

    @Override
    public void setOwnerName(CharSequence userName) {
        this.ownerName = userName;
    }

    @Override
    public void setOwnerID(CharSequence ownerID) {
        this.ownerID = ownerID;
    }

    @Override
    public CharSequence getOwnerID() {
        return this.ownerID;
    }

    @Override
    public CharSequence getOwnerName() {
        return this.ownerName;
    }
}
