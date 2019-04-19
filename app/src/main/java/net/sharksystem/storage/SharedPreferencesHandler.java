package net.sharksystem.storage;

import android.content.Context;
import android.content.SharedPreferences;

import net.sharksystem.android.util.Constants;

public class SharedPreferencesHandler {


    private static final String TAG = SharedPreferencesHandler.class.getSimpleName();
    private Context context;


    public SharedPreferencesHandler(Context context) {
        this.context = context;
    }

    public void writeValue(String key, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.FILENAME, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getValue(String key) {
        if(context.getSharedPreferences(Constants.FILENAME, Context.MODE_PRIVATE) != null ) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.FILENAME, Context.MODE_PRIVATE);
            return sharedPreferences.getString(key, null);
        }else {
            return null;
        }

    }

    public void removeValue(String key, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.FILENAME, Context.MODE_PRIVATE);
        if (sharedPreferences != null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove(key);
            editor.apply();
        }
    }
}
