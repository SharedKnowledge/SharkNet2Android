package net.sharksystem.sharknet.fragments

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import net.sharksystem.R
import net.sharksystem.ui.network.NetworkFragment
import org.junit.Test

class NetworkTest {

    @Test
    fun allComponentsDisplayed() {
        val scenario = launchFragmentInContainer<NetworkFragment>()
        Espresso.onView(ViewMatchers.withId(R.layout.fragment_network)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.fragment_network_bluetooth_configure_hubs_button)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.fragment_network_bluetooth_configure_hubs_text)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.fragment_network_bluetooth_connect_hubs_button)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.fragment_network_bluetooth_connect_hubs_text)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.fragment_network_bluetooth_on_off_button)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.fragment_network_bluetooth_on_off_text)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.fragment_network_bluetooth_scan_button)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.fragment_network_bluetooth_scan_text)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}