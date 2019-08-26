package net.sharksystem.identity.android;

import net.sharksystem.identity.Person;

public class PersonIdentity implements Person {

    private final CharSequence name;
    private final CharSequence id;

    PersonIdentity(CharSequence name, CharSequence id) {
        this.name = name;
        this.id = id;
    }

    @Override
    public CharSequence getName() {
        return this.name;
    }

    @Override
    public CharSequence getID() {
        return this.id;
    }
}
