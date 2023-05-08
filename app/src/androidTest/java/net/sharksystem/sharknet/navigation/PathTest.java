package net.sharksystem.sharknet.navigation;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;


import androidx.test.espresso.Espresso;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import net.sharksystem.R;
import net.sharksystem.ui.MainActivity;

import org.junit.Rule;
import org.junit.Test;

public class PathTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void fromChannelListToChannelView() {
        Espresso.onView(withId(R.id.fragment_channel_list)).check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.fragment_channel_list_add_channel_button)).perform(click());
        Espresso.onView(withId(R.id.fragment_add_channel)).check(matches(isDisplayed()));
    }

    @Test
    public void fromChannelListToAddChannel() {
        Espresso.onView(withId(R.layout.fragment_channel_list)).check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.fragment_channel_list_add_channel_button)).perform(click());
        Espresso.onView(withId(R.id.fragment_add_channel_add_button)).perform(click());
        Espresso.onView(withId(R.id.fragment_channel_list_recycler_view)).perform(click());
        Espresso.onView(withId(R.layout.fragment_channel_view)).check(matches(isDisplayed()));
    }
}
