package net.sharksystem.persons;

import java.util.Calendar;

public interface SharkCertificate {
    /**
     * @return person which public key is matched with its name
     */
    int getOwnerID();

    CharSequence getOwnerName();

    /**
     * @return person who signed this certificate
     */
    int getSignerID();

    CharSequence getSignerName();

    Calendar getValidSince();

    Calendar getValidUntil();
}
