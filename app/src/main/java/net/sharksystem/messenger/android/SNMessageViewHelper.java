package net.sharksystem.messenger.android;

import android.widget.Toast;

import net.sharksystem.app.messenger.SharkMessage;
import net.sharksystem.asap.ASAPException;
import net.sharksystem.asap.ASAPHop;
import net.sharksystem.asap.ASAPSecurityException;
import net.sharksystem.asap.utils.DateTimeHelper;
import net.sharksystem.sharknet.android.SharkNetApp;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

class SNMessageViewHelper {
    static CharSequence getReceiversCharSequence(SharkMessage sharkMessage) {
        CharSequence receiversCharSequence;
        Set<CharSequence> recipients = sharkMessage.getRecipients();
        if (recipients == null || recipients.isEmpty()) {
            receiversCharSequence = "anybody";
        } else {
            StringBuilder sb = new StringBuilder();

            boolean firstRound = true;
            for (CharSequence recipientID : recipients) {
                if (firstRound) {
                    firstRound = false;
                    sb.append("to: ");
                } else {
                    sb.append("|");
                }

                CharSequence recipientName = null;
                try {
                    recipientName = SharkNetApp.getSharkNetApp().getSharkPKI()
                            .getPersonValuesByID(recipientID).getName();
                    sb.append(recipientName);
                } catch (ASAPException e) {
                    // no name found
                    sb.append(recipientID);
                }
            }

            receiversCharSequence = sb.toString();
        }

        return receiversCharSequence;
    }

    static CharSequence getEncryptedCharSequence(SharkMessage sharkMessage) {
        CharSequence encryptedCharSequence = "not E2E encrypted";
        if (sharkMessage.encrypted()) {
            encryptedCharSequence = "is E2E encrypted";
        }

        return encryptedCharSequence;
    }

    static CharSequence getSenderCharSequence(SharkMessage sharkMessage) {

        CharSequence senderName;

        try {
            CharSequence senderID = sharkMessage.getSender();
            senderName = SharkNetApp.getSharkNetApp().getSharkPKI()
                    .getPersonValuesByID(senderID).getName();
        } catch (ASAPException e) {
            // no name found
            senderName = "unknown";
        }

        return "from: " + senderName;
    }

    public static CharSequence getContentCharSequence(SharkMessage sharkMessage) {
        CharSequence contentCharSequence;

        if (sharkMessage.couldBeDecrypted()) {
            byte[] snContent = new byte[0];
            try {
                snContent = sharkMessage.getContent();
            } catch (ASAPSecurityException e) {
                contentCharSequence = "no content in message";
            }
            contentCharSequence = new String(snContent);
        } else {
            contentCharSequence = "cannot decrypt message";
        }

        return contentCharSequence;
    }

    public static CharSequence getVerifiedCharSequence(SharkMessage sharkMessage) {
        CharSequence verified2View = "not verified";
        if (sharkMessage.couldBeDecrypted()) {
            try {
                if (sharkMessage.verified()) {
                    verified2View = "verified sender";
                } else {
                    verified2View = "cannot verify sender";
                }
            } catch (ASAPSecurityException e) {
                verified2View = "failure while verifying";
            }
        } else {
            verified2View = "cannot decrypt message";
        }

        return verified2View;
    }

    public static CharSequence getCreationTimeCharSequence(SharkMessage sharkMessage) {
        CharSequence creationTimeCharSequence = "time: unknown";

        if(sharkMessage.couldBeDecrypted()) {
            long creationTime = 0;
            try {
                creationTime = sharkMessage.getCreationTime();
                creationTimeCharSequence = DateTimeHelper.long2DateString(creationTime);
            } catch (ASAPException | IOException e) {
                creationTimeCharSequence = "failure while reading timestamp";
            }
        } else {
            creationTimeCharSequence = "cannot decrypt message";
        }
        return creationTimeCharSequence;
    }

    public static CharSequence getIdentityAssuranceCharSequence(SharkMessage sharkMessage) {
        CharSequence iA2CharSequence = "iA: unknown";

        try {
            if(sharkMessage.couldBeDecrypted()) {
                if(sharkMessage.verified()) {
                    int identityAssurance =
                            SharkNetApp.getSharkNetApp().getSharkPKI().
                                    getIdentityAssurance(sharkMessage.getSender());
                    iA2CharSequence = "iA is " + identityAssurance;
                }
            } else {
                iA2CharSequence = "cannot decrypt message";
            }
        } catch (ASAPSecurityException e) {
            iA2CharSequence = "failure while reading identity assurance";
        }

        return iA2CharSequence;
    }

    public static CharSequence getASAPHopsCharSequence(SharkMessage sharkMessage) {
        List<ASAPHop> asapHopsList = sharkMessage.getASAPHopsList();
        if(asapHopsList != null && asapHopsList.size() > 0)
            return asapHopsList.toString();

        // else
        return "no hops yet - you created this message";
    }
}
