package net.sharksystem.sharknet.fragments

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import net.sharksystem.R
import net.sharksystem.ui.contacts.ContactViewFragment
import org.junit.jupiter.api.Test

class ContactViewTest {

    @Test
    fun allComponentsDisplayed() {
        val scenario = launchFragmentInContainer<ContactViewFragment>()
        Espresso.onView(ViewMatchers.withId(R.layout.fragment_contact_view)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.fragment_contact_view_userID_description_button)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.fragment_contact_view_identity_assurance_description_button)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.fragment_contact_view_name_description_button)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.fragment_contact_view_signing_failure_description_button)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}