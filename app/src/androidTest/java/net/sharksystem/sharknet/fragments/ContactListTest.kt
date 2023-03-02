package net.sharksystem.sharknet.fragments

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import net.sharksystem.R
import net.sharksystem.ui.contacts.ContactsFragment
import org.junit.Test

class ContactListTest {

    @Test
    fun allComponentsDisplayed() {
        val scenario = launchFragmentInContainer<ContactsFragment>()
        Espresso.onView(ViewMatchers.withId(R.layout.fragment_contacts)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.fragment_contacts_add_contact_button)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.fragment_contacts_recycler_view)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}