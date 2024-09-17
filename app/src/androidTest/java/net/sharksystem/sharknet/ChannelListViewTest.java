package net.sharksystem.sharknet;

import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.filters.LargeTest;

import net.sharksystem.R;
import net.sharksystem.ui.MainActivity;

import org.junit.Rule;
import org.junit.jupiter.api.Test;

@LargeTest
public class ChannelListViewTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);


    /**
     * This test ensures, that all components are displayed
     */
    @Test
    public void channelOverviewShowsNecessaryComponents() {
        Espresso.onView(withId(R.layout.fragment_channel_list));
        Espresso.onView(withId(R.id.fragment_channel_list_recycler_view));
        Espresso.onView(withId(R.id.fragment_channel_list_add_channel_button)).perform(ViewActions.longClick());
        Espresso.onView(withId(R.menu.menu_channel_list_delete));
    }
}
