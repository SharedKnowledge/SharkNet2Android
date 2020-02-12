package net.sharksystem.persons.android;

import android.util.Log;

import net.sharksystem.SharkException;
import net.sharksystem.asap.ASAPException;
import net.sharksystem.sharknet.android.SharkNetActivity;
import net.sharksystem.sharknet.android.SharkNetApp;

import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PersonsApp {
    public static final CharSequence APP_NAME = "SN2Persons";
    public static final CharSequence CREDENTIAL_URI = "sn2://credential";
    public static final CharSequence CERTIFICATE_URI = "sn2://certificate";

    private static PersonsApp instance = null;
    private KeyPair rsaKeyPair = null;

    public static PersonsApp getPersonsApp() {
        if(PersonsApp.instance == null) {
            PersonsApp.instance = new PersonsApp();

            // TODO: remove when ready
            PersonsApp.instance.fillPersonsWithTestData();
        }

        return PersonsApp.instance;
    }

    private PersonsApp() {
        if(this.rsaKeyPair == null) {
            try {
                this.generateKeyPair();
            } catch (NoSuchAlgorithmException e) {
                Log.e(net.sharksystem.asap.util.Log.startLog(this).toString(),
                        "cannot create key pair - fatal");
            }
        }
    }

    void generateKeyPair() throws NoSuchAlgorithmException {
        SecureRandom secRandom = new SecureRandom();
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048, secRandom);
        rsaKeyPair = keyGen.generateKeyPair();
    }

    private byte[] getPublicKey() {
        return new byte[2048];
    }

    private CharSequence getOwnerName() {
        return SharkNetApp.getSharkNetApp().getASAPOwner();
    }

    public void sendCredentialMessage(SharkNetActivity snActivity, int randomInt, int userID)
            throws IOException, ASAPException {

        CredentialMessage credentialMessage =
                new CredentialMessage(randomInt, userID, this.getOwnerName(), this.getPublicKey());

        snActivity.sendASAPMessage(APP_NAME,
                CREDENTIAL_URI,
                null,
                credentialMessage.getMessageAsBytes());
    }

    //////////////////////////////////////////////////////////////////////////////////////////////
    //                           other persons management - in memory                           //
    //////////////////////////////////////////////////////////////////////////////////////////////

    private List<PersonValues> personsList = new ArrayList<>();

    private void fillPersonsWithTestData() {
        this.personsList = new ArrayList<>();
        this.personsList.add(new PersonValues(0, "Person 1", 4, 1));
        this.personsList.add(new PersonValues(1, "Person 2", 4, 2));
    }

    PersonValues getPersonValues(int userID) throws SharkException {
        for(PersonValues personValues : this.personsList) {
            if(personValues.getUserID() == userID) {
                return personValues;
            }
        }

        throw new SharkException("person not found with userID: " + userID);
    }

    PersonValues getPersonValuesByPosition(int position) throws SharkException {
        try {
            PersonValues personValues = this.personsList.get(position);
            return personValues;
        }
        catch(IndexOutOfBoundsException e) {
            throw new SharkException("position too high: " + position);
        }
    }

    int getNumberOfPersons() {
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

    public void addPerson(PersonValues newPersonValues) throws SharkException {
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
    }
}
