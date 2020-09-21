package net.sharksystem.sharknet.android;

import net.sharksystem.asap.ASAPException;

public interface PersonsStorage {
    /**
     * @param peerID
     * @return clear name of person with ID
     * @throws ASAPException if not found
     */
    CharSequence getPersonName(CharSequence peerID) throws ASAPException;
}
