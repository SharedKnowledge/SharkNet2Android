package net.sharksystem.sharknet.fragments

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import net.sharksystem.R
import net.sharksystem.ui.channels.AddChannelFragment
import org.junit.jupiter.api.Test

class AddContactTest {

    @Test
    fun allComponentsDisplayed() {
        val scenario = launchFragmentInContainer<AddChannelFragment>()
        Espresso.onView(ViewMatchers.withId(R.layout.fragment_add_contact)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.fragment_add_contact_abort_button)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.fragment_add_contact_continue_button)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.fragment_add_contact_explanation_text)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.fragment_add_contact_network_button)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.fragment_add_contact_radar_button)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}