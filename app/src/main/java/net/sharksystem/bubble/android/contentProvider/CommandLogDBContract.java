package net.sharksystem.bubble.android.contentProvider;

import android.provider.BaseColumns;

public class CommandLogDBContract {

    private CommandLogDBContract() {}

    /* Inner class that defines the table contents */
    public static class CommandLogEntry implements BaseColumns {
        public static final String TABLE_NAME = "commandLog";
        public static final String COLUMN_NAME_VALUES = "values";
        public static final String COLUMN_NAME_SELECTION = "selection";
        public static final String COLUMN_NAME_SELECTION_ARGS = "selection_args";
    }

    static final String SQL_CREATE_COMMAND_LOG_ENTRY_TABLE =
            "CREATE TABLE " + CommandLogEntry.TABLE_NAME + " (" +
                    CommandLogEntry._ID + " INTEGER PRIMARY KEY," +
                    CommandLogEntry.COLUMN_NAME_VALUES + " TEXT," +
                    CommandLogEntry.COLUMN_NAME_SELECTION + " TEXT," +
                    CommandLogEntry.COLUMN_NAME_SELECTION_ARGS + " TEXT)";

    static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + CommandLogEntry.TABLE_NAME;
}
