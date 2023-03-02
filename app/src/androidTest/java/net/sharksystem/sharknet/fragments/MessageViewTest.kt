package net.sharksystem.sharknet.fragments

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import net.sharksystem.R
import org.junit.Test
import net.sharksystem.ui.channels.massage.MessageViewFragment

class MessageViewTest {

    @Test
    fun allComponentsDisplayed() {
        val scenario = launchFragmentInContainer<MessageViewFragment>()
        Espresso.onView(ViewMatchers.withId(R.layout.fragment_message_view)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.fragment_message_view_iA)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.fragment_message_view_message)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.fragment_message_view_hops)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.fragment_message_view_note)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.fragment_message_view_recipients)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.fragment_message_view_sender)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.fragment_message_view_time)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}