package net.sharksystem.sharknet.fragments

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import net.sharksystem.R
import net.sharksystem.ui.network.hub.HubViewFragment
import org.junit.jupiter.api.Test

class HubViewTest {

    @org.junit.jupiter.api.Test
    fun allComponentsDisplayed() {
        val scenario = launchFragmentInContainer<HubViewFragment>()
        Espresso.onView(ViewMatchers.withId(R.layout.fragment_hub_view)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.fragment_hub_view_abort_button)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.fragment_hub_view_default_values_button)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        //Espresso.onView(ViewMatchers.withId(R.id.fragment_hub_view_delete_button)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.fragment_hub_view_host_name_input)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.fragment_hub_view_host_name_text)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.fragment_hub_view_multi_channel_button)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.fragment_hub_view_multi_channel_text)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.fragment_hub_view_port_input)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.fragment_hub_view_port_text)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.fragment_hub_view_save_button)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}