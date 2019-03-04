package net.sharksystem.sharknet.android;

import identity.IdentityStorage;
import identity.Person;

public class DummyIdentityStorage implements IdentityStorage {
    @Override
    public CharSequence getNameByID(CharSequence userID) {
        return userID;
    }

    @Override
    public Person getPersonByID(CharSequence userID) {
        // dummy
        return new Person() {
            @Override
            public CharSequence getName() {
                return "dummy name";
            }

            @Override
            public CharSequence getID() {
                return "42";
            }
        };
    }
}
