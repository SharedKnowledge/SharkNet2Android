package net.sharksystem.persons;

public interface Person {
    /**
     * @return Name which is meant to be used on user interface
     */
    CharSequence getDisplayName();

    void setDisplayName(CharSequence name);

    /**
     *
     * @return a unique id for that person.
     * Concept of subject identifiers will be introduced in further versions
     */
    CharSequence getUUID();
}
