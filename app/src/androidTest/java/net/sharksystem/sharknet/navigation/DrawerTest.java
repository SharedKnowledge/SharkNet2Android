package net.sharksystem.sharknet.navigation;

import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.DrawerMatchers.isClosed;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.view.Gravity;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.espresso.contrib.NavigationViewActions;

import net.sharksystem.R;

import org.junit.jupiter.api.Test;

public class DrawerTest {


    @org.junit.jupiter.api.Test
    public void fromChannelListToSettings() {
        Espresso.onView(withId(R.id.drawer_layout)).
                check(matches(isClosed(Gravity.LEFT))).
                perform(DrawerActions.open());

        Espresso.onView(withId(R.id.nav_view)).
                perform(NavigationViewActions.navigateTo(R.id.nav_settings));

        Espresso.onView(withId(R.layout.fragment_settings)).check(matches(isDisplayed()));
    }

    @org.junit.jupiter.api.Test
    public void fromChannelListToContactList() {
        Espresso.onView(withId(R.id.drawer_layout)).
                check(matches(isClosed(Gravity.LEFT))).
                perform(DrawerActions.open());

        Espresso.onView(withId(R.id.nav_view)).
                perform(NavigationViewActions.navigateTo(R.id.nav_contacts));

        Espresso.onView(withId(R.layout.fragment_contact_list)).check(matches(isDisplayed()));
    }

    @org.junit.jupiter.api.Test
    public void fromChannelListToRadar() {
        Espresso.onView(withId(R.id.drawer_layout)).
                check(matches(isClosed(Gravity.LEFT))).
                perform(DrawerActions.open());

        Espresso.onView(withId(R.id.nav_view)).
                perform(NavigationViewActions.navigateTo(R.id.nav_radar));

        Espresso.onView(withId(R.layout.fragment_radar)).check(matches(isDisplayed()));
    }

    @org.junit.jupiter.api.Test
    public void fromChannelListToNetwork() {
        Espresso.onView(withId(R.id.drawer_layout)).
                check(matches(isClosed(Gravity.LEFT))).
                perform(DrawerActions.open());

        Espresso.onView(withId(R.id.nav_view)).
                perform(NavigationViewActions.navigateTo(R.id.nav_network));

        Espresso.onView(withId(R.layout.fragment_network)).check(matches(isDisplayed()));
    }

    @org.junit.jupiter.api.Test
    public void drawerTopToBottom() {
        Espresso.onView(withId(R.id.drawer_layout)).
                check(matches(isClosed(Gravity.LEFT))).
                perform(DrawerActions.open());

        Espresso.onView(withId(R.id.nav_view)).
                perform(NavigationViewActions.navigateTo(R.id.nav_settings));

        Espresso.onView(withId(R.layout.fragment_settings)).check(matches(isDisplayed()));

        Espresso.onView(withId(R.id.drawer_layout)).
                check(matches(isClosed(Gravity.LEFT))).
                perform(DrawerActions.open());

        Espresso.onView(withId(R.id.nav_view)).
                perform(NavigationViewActions.navigateTo(R.id.nav_contacts));

        Espresso.onView(withId(R.layout.fragment_contact_list)).check(matches(isDisplayed()));

        Espresso.onView(withId(R.id.drawer_layout)).
                check(matches(isClosed(Gravity.LEFT))).
                perform(DrawerActions.open());

        Espresso.onView(withId(R.id.nav_view)).
                perform(NavigationViewActions.navigateTo(R.id.nav_radar));

        Espresso.onView(withId(R.layout.fragment_radar)).check(matches(isDisplayed()));

        Espresso.onView(withId(R.id.drawer_layout)).
                check(matches(isClosed(Gravity.LEFT))).
                perform(DrawerActions.open());

        Espresso.onView(withId(R.id.nav_view)).
                perform(NavigationViewActions.navigateTo(R.id.nav_network));

        Espresso.onView(withId(R.layout.fragment_network)).check(matches(isDisplayed()));
    }

    @Test
    public void drawerRandomNavigation() {
        Espresso.onView(withId(R.id.drawer_layout)).
                check(matches(isClosed(Gravity.LEFT))).
                perform(DrawerActions.open());

        Espresso.onView(withId(R.id.nav_view)).
                perform(NavigationViewActions.navigateTo(R.id.nav_network));

        Espresso.onView(withId(R.layout.fragment_network)).check(matches(isDisplayed()));

        Espresso.onView(withId(R.id.drawer_layout)).
                check(matches(isClosed(Gravity.LEFT))).
                perform(DrawerActions.open());

        Espresso.onView(withId(R.id.nav_view)).
                perform(NavigationViewActions.navigateTo(R.id.nav_settings));

        Espresso.onView(withId(R.layout.fragment_settings)).check(matches(isDisplayed()));

        Espresso.onView(withId(R.id.drawer_layout)).
                check(matches(isClosed(Gravity.LEFT))).
                perform(DrawerActions.open());

        Espresso.onView(withId(R.id.nav_view)).
                perform(NavigationViewActions.navigateTo(R.id.nav_radar));

        Espresso.onView(withId(R.layout.fragment_radar)).check(matches(isDisplayed()));

        Espresso.onView(withId(R.id.drawer_layout)).
                check(matches(isClosed(Gravity.LEFT))).
                perform(DrawerActions.open());

        Espresso.onView(withId(R.id.nav_view)).
                perform(NavigationViewActions.navigateTo(R.id.nav_network));

        Espresso.onView(withId(R.layout.fragment_network)).check(matches(isDisplayed()));

        Espresso.onView(withId(R.id.drawer_layout)).
                check(matches(isClosed(Gravity.LEFT))).
                perform(DrawerActions.open());

        Espresso.onView(withId(R.id.nav_view)).
                perform(NavigationViewActions.navigateTo(R.id.nav_contacts));

        Espresso.onView(withId(R.layout.fragment_contact_list)).check(matches(isDisplayed()));

        Espresso.onView(withId(R.id.drawer_layout)).
                check(matches(isClosed(Gravity.LEFT))).
                perform(DrawerActions.open());

        Espresso.onView(withId(R.id.nav_view)).
                perform(NavigationViewActions.navigateTo(R.id.nav_radar));
    }
}
