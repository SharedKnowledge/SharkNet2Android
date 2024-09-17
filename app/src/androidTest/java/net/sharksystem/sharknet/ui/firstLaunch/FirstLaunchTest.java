package net.sharksystem.sharknet.ui.firstLaunch;

import androidx.test.ext.junit.rules.ActivityScenarioRule;

import net.sharksystem.SharkException;
import net.sharksystem.ui.MainActivity;

import org.junit.Rule;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class FirstLaunchTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);


    /**
     * This test ensures, that all components are displayed
     */
    @Test
    public void firstStartShowsNecessaryComponents() throws SharkException, IOException {
        //Espresso.onView()
    }
}
