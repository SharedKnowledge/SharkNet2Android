package net.sharksystem.sharknet.fragments

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import net.sharksystem.R
import net.sharksystem.ui.channels.massage.ChannelViewFragment
import org.junit.jupiter.api.Test

class ChannelViewTest {

    @org.junit.jupiter.api.Test
    fun allComponentsDisplayed() {
        val scenario = launchFragmentInContainer<ChannelViewFragment>()
        Espresso.onView(ViewMatchers.withId(R.layout.fragment_channel_view)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.fragment_channel_view_recycler_view)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.fragment_channel_view_add_message_button)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}