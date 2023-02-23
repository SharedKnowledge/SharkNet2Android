package net.sharksystem.sharknet;

import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.app.Activity;
import android.content.SharedPreferences;

import androidx.fragment.app.FragmentFactory;
import androidx.test.espresso.Espresso;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.filters.LargeTest;

import net.sharksystem.R;
import net.sharksystem.SharkException;
import net.sharksystem.messenger.android.SNChannelsListActivity;
import net.sharksystem.sharknet.android.InitActivity;
import net.sharksystem.sharknet.android.SharkNetApp;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.io.IOException;

@LargeTest
public class ChannelListViewTest {

    @Rule
    public ActivityScenarioRule<SNChannelsListActivity> activityRule =
            new ActivityScenarioRule<>(SNChannelsListActivity.class);


    /**
     * This test ensures, that all components are displayed
     */
    @Test
    public void channelOverviewShowsNecessaryComponents() throws SharkException, IOException {
        InitActivity activity = Mockito.mock(InitActivity.class);
        SharedPreferences sp = Mockito.mock(SharedPreferences.class);
        Mockito.when(sp.contains(ArgumentMatchers.anyString())).thenReturn(true);
        Mockito.when(activity.getSharedPreferences(ArgumentMatchers.anyString(), ArgumentMatchers.anyInt())).thenReturn(sp);
        SharkNetApp.initializeSharkNetApp(activity,"test");
        Espresso.onView(withId(R.id.sharknet_drawer_layout));
        Espresso.onView(withId(R.id.snAddChannelButton));
        Espresso.onView(withId(R.id.snRemoveAllChannelButton));
    }
}
