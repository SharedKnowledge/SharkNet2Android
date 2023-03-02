package net.sharksystem.sharknet.ui.channel

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import net.sharksystem.ui.channels.ChannelListFragment
import org.junit.Test
import org.junit.runner.RunWith
import net.sharksystem.R

@RunWith(AndroidJUnit4::class)
class ChannelListTest {

    @Test
    fun test() {
        val scenario = launchFragmentInContainer<ChannelListFragment>()
        Espresso.onView(withId(R.layout.fragment_channel_list)).check(matches(isDisplayed()))
    }
}