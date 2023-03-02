package net.sharksystem.sharknet.ui;

import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.espresso.Espresso;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import net.sharksystem.R;
import net.sharksystem.ui.MainActivity;

import org.junit.Rule;
import org.junit.Test;

public class MainActivityTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void layoutCorrectlyDisplayed() {
        //Espresso.onView(withId(R.layout.activity_main)).check(matches(isDisplayed()));
        Espresso.onView(withId(R.layout.app_bar_main)).check(matches(isDisplayed()));
        Espresso.onView(withId(R.layout.content_main)).check(matches(isDisplayed()));
        Espresso.onView(withId(R.layout.nav_header_main)).check(matches(isDisplayed()));
    }
}
