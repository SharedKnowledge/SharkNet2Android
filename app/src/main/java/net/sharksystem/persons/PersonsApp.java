package net.sharksystem.persons;

import android.util.Log;

import net.sharksystem.SharkException;
import net.sharksystem.persons.android.OwnerStorageAndroid;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class PersonsApp {
    private KeyPair rsaKeyPair = null;

    public PersonsApp() {
        // TODO: remove when ready
        this.fillPersonsWithTestData();

        if(this.rsaKeyPair == null) {
            try {
                this.generateKeyPair();
            } catch (Exception e) {
                Log.e(net.sharksystem.asap.util.Log.startLog(this).toString(),
                        "cannot create key pair - fatal");
            }
        }
    }

    public void generateKeyPair() throws SharkException {
        KeyPairGenerator keyGen = null;
        try {
            keyGen = KeyPairGenerator.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            throw new SharkException(e.getLocalizedMessage());
        }

        SecureRandom secRandom = new SecureRandom();
        try {
            keyGen.initialize(2048, secRandom);
            this.rsaKeyPair = keyGen.generateKeyPair();
        }
        catch(RuntimeException re) {
            throw new SharkException(re.getLocalizedMessage());
        }
    }

    public PublicKey getOwnerPublicKey() {
        return this.rsaKeyPair.getPublic();
    }

    //////////////////////////////////////////////////////////////////////////////////////////////
    //                           other persons management - in memory                           //
    //////////////////////////////////////////////////////////////////////////////////////////////

    private List<PersonValues> personsList = new ArrayList<>();
    private HashMap<Integer, SharkCertificate> certificates = new HashMap<>();

    private void fillPersonsWithTestData() {
        this.personsList = new ArrayList<>();
        this.personsList.add(new PersonValues(0, "Person 1", 4, 1));
        this.personsList.add(new PersonValues(1, "Person 2", 4, 2));

        this.setIdentityAssurances();
    }

    PersonValues getPersonValues(int userID) throws SharkException {
        for(PersonValues personValues : this.personsList) {
            if(personValues.getUserID() == userID) {
                return personValues;
            }
        }

        throw new SharkException("person not found with userID: " + userID);
    }

    public PersonValues getPersonValuesByPosition(int position) throws SharkException {
        try {
            PersonValues personValues = this.personsList.get(position);
            return personValues;
        }
        catch(IndexOutOfBoundsException e) {
            throw new SharkException("position too high: " + position);
        }
    }

    public int getNumberOfPersons() {
        return this.personsList.size();
    }

    public int getIdentityAssurance(int userID) {
        // TODO
        return 4;
    }

    public int getOwnerUserID() {
        // TODO
        return new Random(System.currentTimeMillis()).nextInt();
    }


    public void addPerson(PersonValues newPersonValues, PublicKey publicKey)
            throws SharkException {

        int userID = newPersonValues.getUserID();

        // already in there
        for(PersonValues personValues : this.personsList) {
            if(personValues.getUserID() == userID) {
                throw new SharkException("person with userID already exists: " + userID);
            }
        }

        // even owner
        if(userID == this.getOwnerUserID()) {
            throw new SharkException("cannot add person with your userID");
        }

        this.personsList.add(newPersonValues);

        // sign public key - create certificate and store it.
        this.createCertificate(newPersonValues.getUserID(), newPersonValues.getName(), publicKey);
    }

    private void createCertificate(int userID, CharSequence name,
                                               PublicKey publicKey) {

        SharkCertificate cert = new SharkCertificateImpl(
                this.getOwnerUserID(),
                this.getOwnerName(),
                this.getPrivateKey(),
                userID,
                name,
                publicKey);

        // store it
        this.certificates.put(userID, cert);

        // re-calculate identity assurance level
        this.setIdentityAssurances();
    }

    private void setIdentityAssurances() {
        for(PersonValues person : this.personsList) {
            // is there a certificate?
            SharkCertificate certificate = this.certificates.get(person.getUserID());
            if(certificate == null) {
                // we don't know anything about this person
                person.setIdentityAssurance(OtherPerson.LOWEST_IDENTITY_ASSURANCE_LEVEL);
            } else {
                // we have got a certificate
                if(certificate.getSignerID() == this.getOwnerUserID()) {
                    // you signed it - you met this person - you are beyond any doubt
                    person.setIdentityAssurance(OtherPerson.HIGHEST_IDENTITY_ASSURANCE_LEVEL);
                } else {
                    // there is a certificate chain
                    Set<Integer> idChain = new HashSet<>();
                    idChain.add(person.getUserID()); // end of chain - already visited
                    person.setIdentityAssurance(this.calculateIdentityAssurance(
                            idChain, certificate.getSignerID(), -1));
                }
            }
        }
    }

    /**
     * Follow chain backward. If it reaches owner.. there will be an assurance level better than
     * worst. If it does not end at owner or even goes in circles - it worst level.
     * @param idChain already visited ids
     * @param currentPersonID current id
     * @param currentAssuranceProbability current assurance so far
     * @return
     *
     * what we lool for:
     * YOU - Person A - Person B - ...- current Person - ... - Person in question
     *
     * We go backward in chain to - hopefully reach you
     */
    private int calculateIdentityAssurance(Set<Integer> idChain, int currentPersonID,
                                           float currentAssuranceProbability) {

        // finished?
        if(currentPersonID == this.getOwnerUserID())
            return (int) (currentAssuranceProbability * 10); // yes - rescale to 0..10

        // not finished

        // are we in a circle?
        if(idChain.contains(currentPersonID))
            return OtherPerson.LOWEST_IDENTITY_ASSURANCE_LEVEL; // yes - escape

        // remember this step
        idChain.add(currentPersonID);

        try {
            // get information about this person
            PersonValues currentPersonValues = this.getPersonValues(currentPersonID);

            // calculate failure rate in percent
            float failureProbability = currentPersonValues.getCertificateExchangeFailure() / 10;

            // OK. We have information about this person. Calculate assuranceLevel
            if(currentAssuranceProbability < 0) {
                // haven't yet calculated any assurance prob. Set initial value
                currentAssuranceProbability = 1 - failureProbability;
            } else {
                currentAssuranceProbability *= 1 - failureProbability;
            }

            // is there a next step? Yes, if there is a certificate
            SharkCertificate certificate = this.getCertificate(currentPersonValues.getUserID());

            // we have got one - next step
            return this.calculateIdentityAssurance(idChain,
                    certificate.getSignerID(), currentAssuranceProbability);

        } catch (SharkException e) {
            // broken chain - there is no certificated way to you.
            return OtherPerson.LOWEST_IDENTITY_ASSURANCE_LEVEL;
        }
    }

    private SharkCertificate getCertificate(int userID) throws SharkException {
        SharkCertificate certificate = this.certificates.get(userID);
        if(certificate == null) throw new SharkException("no certificate for id: " + userID);

        return certificate;
    }

    private PrivateKey getPrivateKey() {
        return null;
    }

    private CharSequence getOwnerName() {
        return "DummyOwnerName";
    }
}
