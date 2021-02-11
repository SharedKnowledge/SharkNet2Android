package net.sharksystem.pki.android;

import net.sharksystem.asap.android.apps.ASAPComponentNotYetInitializedException;
import net.sharksystem.asap.persons.CredentialMessage;

import java.util.HashSet;
import java.util.Set;

public class PersonStatusHelper {
    private static PersonStatusHelper instance = null;
    private Set<CharSequence> selectedItemIDs = null;
    private CredentialMessage credentialMessage;

    public void setLastPersonsSelection(Set<CharSequence> selectedItemIDs) {
        this.selectedItemIDs = selectedItemIDs;
    }

    public Set<CharSequence> getLastPersonsSelection() {
        if(this.selectedItemIDs == null) return new HashSet();
        else return this.selectedItemIDs;
    }

    private Set<CharSequence> preselectedIDs = null;
    public void setPreselectionSet(Set<CharSequence> preselectedIDs) {
        this.preselectedIDs = preselectedIDs;
    }

    public void setReceivedCredential(CredentialMessage credentialMessage) {
        this.credentialMessage = credentialMessage;
    }

    public CredentialMessage getReceivedCredential() {
        return this.credentialMessage;
    }

    public Set<CharSequence> getPreselectionSet() {
        if(this.preselectedIDs == null) return new HashSet();
        else return this.preselectedIDs;
    }

    public static synchronized PersonStatusHelper getPersonsStorage()
            throws ASAPComponentNotYetInitializedException {
        if(PersonStatusHelper.instance == null) {
            PersonStatusHelper.instance = new PersonStatusHelper();
        }

        return PersonStatusHelper.instance;
    }
}
