package net.sharksystem.sharknet.ui.channel;

import androidx.test.ext.junit.rules.ActivityScenarioRule;

import net.sharksystem.SharkException;
import net.sharksystem.messenger.android.SNChannelsListActivity;

import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;

public class ChannelTest {

    @Rule
    public ActivityScenarioRule<SNChannelsListActivity> activityRule =
            new ActivityScenarioRule<>(SNChannelsListActivity.class);


    /**
     * This test ensures, that all components are displayed
     */
    @Test
    public void channelOverviewShowsNecessaryComponents() throws SharkException, IOException {

    }
}
