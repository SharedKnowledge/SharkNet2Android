package net.sharksystem.persons;

import net.sharksystem.persons.android.PersonsAppAndroid;

public class PersonValues {
    private static final int DEFAULT_CERTIFICATE_EXCHANGE_FAILURE = 5;
    private final int id;
    private CharSequence name;
    private final int certificateExchangeFailure;
    private int identityAssurance;

    public PersonValues(int id, CharSequence name,
                 int identityAssurance, int certificateExchangeFailure) {

        this.id = id;
        this.name = name;
        this.identityAssurance = identityAssurance;
        this.certificateExchangeFailure = certificateExchangeFailure;
    }

    public PersonValues(int userID, CharSequence ownerName) {
        this(userID, ownerName,
                PersonsAppAndroid.getPersonsApp().getIdentityAssurance(userID),
                DEFAULT_CERTIFICATE_EXCHANGE_FAILURE);
    }

    public int getUserID() { return this.id;}
    public CharSequence getName() { return this.name;}
    public int getIdentityAssurance() { return this.identityAssurance;}
    public int getCertificateExchangeFailure() { return this.certificateExchangeFailure;}

    public void setIdentityAssurance(int identityAssuranceLevel) {
        this.identityAssurance = identityAssuranceLevel;
    }
}
