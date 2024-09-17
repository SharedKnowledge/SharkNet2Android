package net.sharksystem.sharknet.ui;

import androidx.test.ext.junit.rules.ActivityScenarioRule;

import net.sharksystem.ui.MainActivity;

import org.junit.Rule;
import org.junit.jupiter.api.Test;

public class MainActivityTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void layoutCorrectlyDisplayed() {

    }
}
