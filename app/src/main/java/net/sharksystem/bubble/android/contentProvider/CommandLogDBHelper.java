package net.sharksystem.bubble.android.contentProvider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CommandLogDBHelper extends SQLiteOpenHelper {
        public static final int DATABASE_VERSION = 1;
        public static final String DATABASE_NAME = "SyncCP_CommandLogDB.db";

        public CommandLogDBHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CommandLogDBContract.SQL_CREATE_COMMAND_LOG_ENTRY_TABLE);
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // TODO
        }

        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // TODO
        }
}
