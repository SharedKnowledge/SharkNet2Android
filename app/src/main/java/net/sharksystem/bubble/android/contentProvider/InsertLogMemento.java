package net.sharksystem.bubble.android.contentProvider;

import android.content.ContentValues;
import android.net.Uri;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class InsertLogMemento {
    private final DataInputStream is;
    private boolean more = false;

    private Uri nextUri = null;
    private ContentValues nextContentValues = null;

    InsertLogMemento() {
        this.is = null;
    }

    InsertLogMemento(DataInputStream is) {
        this.is = is;
        this.more = true;
    }

    void writeEntry(DataOutputStream os, Uri uri, ContentValues values) throws IOException {
        String uriString = uri.toString();

        os.writeUTF(uriString);

        // TODO: values!!
        // values.
    }


    boolean hasMore() {
        if(this.nextContentValues == null) {
            // read ahead

            try {
                String uriString = this.is.readUTF();
                this.nextUri = Uri.parse(uriString);

                // TODO  values
            }
            catch(Exception e) {
                // TODO
            }
        }

        return this.nextContentValues != null;
    }

    ContentValues getContentValues() {
        ContentValues temp = this.nextContentValues;
        this.nextContentValues = null;
        return temp;
    }

    Uri getUri() {
        Uri temp = this.nextUri;
        this.nextUri = null;
        return temp;
    }



}
