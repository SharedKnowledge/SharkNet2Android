package net.sharksystem.sharknet.fragments

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import net.sharksystem.R
import net.sharksystem.ui.network.hub.HubListFragment
import org.junit.jupiter.api.Test

class HubListTest {

    @org.junit.jupiter.api.Test
    fun allComponentsDisplayed() {
        val scenario = launchFragmentInContainer<HubListFragment>()
        Espresso.onView(ViewMatchers.withId(R.layout.fragment_hub_list)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.fragment_hub_list_add_hub_button)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.fragment_hub_list_recycler_view)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}