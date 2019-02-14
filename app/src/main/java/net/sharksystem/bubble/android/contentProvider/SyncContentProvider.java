package net.sharksystem.bubble.android.contentProvider;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * This class is a wrapper around a content provider offering
 * a very slim synchronization feature. This implementation
 * does not detect or even solve data conflicts as a result
 * of conflicting updates or removals.
 *
 * This class assumes a content provider that can be accessed
 * by different distributed parties which are rarely connected.
 *
 * It also assumes that those different parties will not
 * change data which are inserted into the database by others.
 *
 * This implementation can be used e.g. for sensors which
 * collect data and store it. It can be used for a distributed
 * chat application which keeps conversations in a content
 * provider.
 *
 * This implementaion would <b>not</b> work for distributed
 * contact lists in which more than one user can change data.
 *
 */
public class SyncContentProvider extends ContentProvider {
    private static Context context;
    private static String cpAuthority = null;
    private static final String CONTENT_STRING = "content://";
    private static final int CONTENT_STRING_LENGTH = CONTENT_STRING.length();

    private static final String INSERT_LOGFILE_NAME = "insertLogs";
    private static final String DELETE_LOGFILE_NAME = "deleteLogs";
    private static final String UPDATE_LOGFILE_NAME = "updateLogs";

    private LogWriter insertWriter = null;
    private LogWriter updateWriter = null;
    private LogWriter deleteWriter = null;

    private InsertLogMemento insertLogMemento = new InsertLogMemento();

    private static File logDir;

    public static void init(String cpAuthority, Context context) {
        SyncContentProvider.context = context;

        if(cpAuthority.startsWith(CONTENT_STRING)) {
            SyncContentProvider.cpAuthority = cpAuthority;
        } else {
            SyncContentProvider.cpAuthority = CONTENT_STRING + cpAuthority;
        }

        SyncContentProvider.logDir = context.getFilesDir();

        // cut misplaced slashes if any. We only need the authority
        int index = SyncContentProvider.cpAuthority.indexOf('/', CONTENT_STRING_LENGTH);
        if(index > 0) {
            SyncContentProvider.cpAuthority = SyncContentProvider.cpAuthority.substring(0, index);
        }
    }

    private void setupInsertWriter() throws FileNotFoundException {
        if(this.insertWriter == null) {
            this.insertWriter = new LogWriter(SyncContentProvider.logDir, SyncContentProvider.INSERT_LOGFILE_NAME);
        }
    }

    private void setupDeleteWriter() throws FileNotFoundException {
        if(this.deleteWriter == null) {
            this.deleteWriter = new LogWriter(SyncContentProvider.logDir, SyncContentProvider.DELETE_LOGFILE_NAME);
        }
    }

    private void setupUpdateWriter() throws FileNotFoundException {
        if(this.updateWriter == null) {
            this.updateWriter = new LogWriter(SyncContentProvider.logDir, SyncContentProvider.UPDATE_LOGFILE_NAME);
        }
    }

    private Uri convertUri(Uri toConvert) {
        String toConvertString = toConvert.toString();

        String uriString = null;
        // cut authority
        int index = toConvertString.indexOf('/', CONTENT_STRING_LENGTH);
        if(index > 0) {
            uriString = SyncContentProvider.cpAuthority + toConvertString.substring(index);
        } else {
            uriString = SyncContentProvider.cpAuthority;
        }

        return Uri.parse(uriString);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // add sync stuff here

        return SyncContentProvider.context.getContentResolver().update(
                this.convertUri(uri),
                values, selection, selectionArgs);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // add sync stuff here

        return SyncContentProvider.context.getContentResolver().delete(
                this.convertUri(uri),
                selection, selectionArgs);
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //                                      INSERT                                    //
    ////////////////////////////////////////////////////////////////////////////////////

    public void syncInsert(DataInputStream is) {
        InsertLogMemento readMemento = new InsertLogMemento(is);

        try {
            while (readMemento.hasMore()) {
                Uri uri = readMemento.getUri();
                ContentValues values = readMemento.getContentValues();

                // play back
                SyncContentProvider.context.getContentResolver().insert(
                        this.convertUri(uri), values);
            }
        }
        catch(Exception e) {
            // TODO
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // add sync stuff here
        try {
            this.setupInsertWriter();
            this.insertLogMemento.writeEntry(this.insertWriter.getLogStream(), uri, values);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return SyncContentProvider.context.getContentResolver().insert(
                this.convertUri(uri), values);
    }

    ///////////////////////////////////////////////////////////////
    //                           pure delegate                   //
    ///////////////////////////////////////////////////////////////

    @Override
    public boolean onCreate() {
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        return SyncContentProvider.context.getContentResolver().query(
                this.convertUri(uri), projection,
                selection, selectionArgs, sortOrder);
    }

    @Override
    public String getType(Uri uri) {
        return SyncContentProvider.context.getContentResolver().getType(
                this.convertUri(uri));
    }
}
