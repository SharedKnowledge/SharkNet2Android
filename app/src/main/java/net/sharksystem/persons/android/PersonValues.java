package net.sharksystem.persons.android;

class PersonValues {
    private static final int DEFAULT_CERTIFICATE_EXCHANGE_FAILURE = 5;
    private final int id;
    private final CharSequence name;
    private final int certificateExchangeFailure;
    private final int identityAssurance;

    PersonValues(int id, CharSequence name,
                 int identityAssurance, int certificateExchangeFailure) {

        this.id = id;
        this.name = name;
        this.identityAssurance = identityAssurance;
        this.certificateExchangeFailure = certificateExchangeFailure;
    }

    public PersonValues(int userID, CharSequence ownerName) {
        this(userID, ownerName,
                PersonsApp.getPersonsApp().getIdentityAssurance(userID),
                DEFAULT_CERTIFICATE_EXCHANGE_FAILURE);
    }

    int getUserID() { return this.id;}
    CharSequence getName() { return this.name;}
    int getIdentityAssurance() { return this.identityAssurance;}
    int getCertificateExchangeFailure() { return this.certificateExchangeFailure;}
}
