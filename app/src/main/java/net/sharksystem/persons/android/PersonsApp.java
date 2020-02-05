package net.sharksystem.persons.android;

import android.app.Activity;
import android.util.Log;

public class PersonsApp {
    private static PersonsApp instance = null;

    public static PersonsApp getPersonsApp(Activity activity) {
        if(PersonsApp.instance == null) {
            PersonsApp.instance = new PersonsApp(activity);
        }

        return PersonsApp.instance;
    }

    private PersonsApp(Activity activity) {
        Log.d(this.getLogStart(), "create singleton");
    }

    protected String getLogStart() {
        return this.getClass().getSimpleName();
    }
}
