package net.sharksystem.sharknet;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isNotEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.filters.LargeTest;

import net.sharksystem.R;
import net.sharksystem.sharknet.android.InitActivity;
import net.sharksystem.sharknet.utils.RecyclerViewItemCountAssertion;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

/**
 * Test specific scenarios for sending messages.
 * All tests assume, that previously a peer was created at
 * first app start.
 * <p>
 * The naming of scenarios follows this structure:
 * <blockquote>
 * scenario{number}_scenarioName_variation
 * </blockquote>
 * The scenario name describes what happens in
 * the scenario.
 * <p>
 * The variation signals a already tested scenario
 * with some minor differences in execution.
 */
@LargeTest
public class TestAddMessageScenarios {

    private static final String CHANNEL_NAME = "TestChannel";
    private static final String TEST_MESSAGE = "I am Test";
    private static final String PEER_NAME = "from: unknown";
    private static final String TO_ANYBODY = "anybody";
    private static final String TEST_RECIPIENT = "Iris";
    private static final String SIGNED_TEXT = "signed";
    private static final String UNSIGNED_TEXT = "unsigned";
    private static final String ENCRYPTED_TEXT = "encrypted";
    private static final String UNENCRYPTED_TEXT = "unencrypted";

    // TODO: right now it is not possible to start from SNChannelViewActivity
    @Rule
    public ActivityScenarioRule<InitActivity> activityRule =
            new ActivityScenarioRule<>(InitActivity.class);

    @Before
    public void setUp() {
        // clear all channels
        Espresso.onView(withId(R.id.snRemoveAllChannelButton))
                .perform(click());

        // go to addChannel
        Espresso.onView(withId(R.id.snAddChannelButton))
                .perform(click());

        // add channel
        Espresso.onView(withId(R.id.snChannelAddName))
                .perform(typeText(CHANNEL_NAME));
        Espresso.onView(withId(R.id.snChannelAddDoAdd))
                .perform(click());
    }

    /**
     * Validate the contents of a sent message match up.
     * Make sure the corresponding channel view
     * is in the hierarchy before calling this method.
     * @param receiver of the message.
     * @param encrypted true if message is encrypted, false otherwise.
     * @param signed true if message is signed, false otherwise.
     */
    private void validateSentMessage(String receiver, boolean encrypted, boolean signed) {
        String encryptedText = encrypted ? ENCRYPTED_TEXT : UNENCRYPTED_TEXT;
        String signedText = signed ? SIGNED_TEXT : UNSIGNED_TEXT;

        // validate sender name
        Espresso.onView(withId(R.id.sn_message_sender))
                .check(matches(withText(containsString(PEER_NAME))));

        // validate message content
        Espresso.onView(withId(R.id.sn_message_content))
                .check(matches(withText(TEST_MESSAGE)));

        // validate receiver name or anybody
        Espresso.onView(withId(R.id.sn_message_receivers))
                .check(matches(withText(containsString(receiver))));

        // validate if message is encrypted
        Espresso.onView(withId(R.id.sn_message_encrypted))
                .check(matches(withText(containsString(encryptedText))));

        // validate if message is signed
        Espresso.onView(withId(R.id.sn_message_verified))
                .check(matches(withText(containsString(signedText))));
    }

    @Test
    public void scenarioX_sendMessageToAnybody() {
        // go to channel
        Espresso.onView(withId(R.id.sn_channel_list_recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        // go to addMessage
        Espresso.onView(withId(R.id.snChannelViewMenuAddMessage))
                .perform(click());

        // add message
        Espresso.onView(withId(R.id.snMessage))
                .perform(typeText(TEST_MESSAGE));
        Espresso.onView(withId(R.id.removeAllRecipients))
                .perform(click());
        // validate, that encryption is not possible
        Espresso.onView(withId(R.id.snEncrypted))
                .check(matches(not(isDisplayed())));
        // validate channel name
        Espresso.onView(withId(R.id.snChannelName))
                .check(matches(withText(CHANNEL_NAME)));
        // validate recipient name
        Espresso.onView(withId(R.id.snMessageRecipients))
                .check(matches(withText(TO_ANYBODY)));
        Espresso.onView(withId(R.id.addButton))
                .perform(click());

        // go to sent message
        Espresso.onView(withId(R.id.sn_channel_view_recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        validateSentMessage(TO_ANYBODY, false, false);
    }

    @Test
    public void scenarioX_sendMessageToAnybody_signed() {
        // go to channel
        Espresso.onView(withId(R.id.sn_channel_list_recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        // go to addMessage
        Espresso.onView(withId(R.id.snChannelViewMenuAddMessage))
                .perform(click());

        // add message
        Espresso.onView(withId(R.id.snSigned))
                .perform(click());
        Espresso.onView(withId(R.id.snMessage))
                .perform(typeText(TEST_MESSAGE));
        Espresso.onView(withId(R.id.removeAllRecipients))
                .perform(click());
        Espresso.onView(withId(R.id.addButton))
                .perform(click());

        // go to sent message
        Espresso.onView(withId(R.id.sn_channel_view_recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        validateSentMessage(TO_ANYBODY, false, true);
    }

    @Test
    public void scenarioX_sendMessageToAnybody_abortContactSelection() {
        // go to channel
        Espresso.onView(withId(R.id.sn_channel_list_recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        // go to addMessage
        Espresso.onView(withId(R.id.snChannelViewMenuAddMessage))
                .perform(click());

        // add message
        Espresso.onView(withId(R.id.snMessage))
                .perform(typeText(TEST_MESSAGE));
        Espresso.onView(withId(R.id.selectRecipients))
                .perform(click());
        Espresso.onView(withId(R.id.person_list_recycler_view))
                .perform(RecyclerViewActions.actionOnItem(hasDescendant(withText(TEST_RECIPIENT)), click())
                        .atPosition(0));
        // abort selection: send to anybody
        Espresso.onView(withId(R.id.abortButton))
                .perform(click());
        // validate recipient name
        Espresso.onView(withId(R.id.snMessageRecipients))
                .check(matches(withText(TO_ANYBODY)));
        Espresso.onView(withId(R.id.addButton))
                .perform(click());

        // go to sent message
        Espresso.onView(withId(R.id.sn_channel_view_recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        validateSentMessage(TO_ANYBODY, false, false);
    }

    @Test
    public void scenarioX_sendMessageToContact() {
        // go to channel
        Espresso.onView(withId(R.id.sn_channel_list_recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        // go to addMessage
        Espresso.onView(withId(R.id.snChannelViewMenuAddMessage))
                .perform(click());

        // add message with recipient
        Espresso.onView(withId(R.id.snMessage))
                .perform(typeText(TEST_MESSAGE));
        Espresso.onView(withId(R.id.selectRecipients))
                .perform(click());
        Espresso.onView(withId(R.id.person_list_recycler_view))
                .perform(RecyclerViewActions.actionOnItem(hasDescendant(withText(TEST_RECIPIENT)), click())
                        .atPosition(0));
        Espresso.onView(withId(R.id.personListSelectionDoneButton))
                .perform(click());
        // validate recipient name
        Espresso.onView(withId(R.id.snMessageRecipients))
                .check(matches(withText(TEST_RECIPIENT)));
        Espresso.onView(withId(R.id.addButton))
                .perform(click());

        // go to sent message
        Espresso.onView(withId(R.id.sn_channel_view_recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        validateSentMessage(TEST_RECIPIENT, false, false);
    }

    // TODO: right now no encrypted exchange with contacts possible
    @Test
    public void scenarioX_sendMessageToContact_encrypted() {
        // go to channel
        Espresso.onView(withId(R.id.sn_channel_list_recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        // go to addMessage
        Espresso.onView(withId(R.id.snChannelViewMenuAddMessage))
                .perform(click());

        // add message with recipient
        Espresso.onView(withId(R.id.snMessage))
                .perform(typeText(TEST_MESSAGE));
        Espresso.onView(withId(R.id.selectRecipients))
                .perform(click());
        Espresso.onView(withId(R.id.person_list_recycler_view))
                .perform(RecyclerViewActions.actionOnItem(hasDescendant(withText(TEST_RECIPIENT)), click())
                        .atPosition(0));
        Espresso.onView(withId(R.id.personListSelectionDoneButton))
                .perform(click());
        // encrypt message
        Espresso.onView(withId(R.id.snEncrypted))
                .perform(click());
        Espresso.onView(withId(R.id.addButton))
                .perform(click());

        // go to sent message
        Espresso.onView(withId(R.id.sn_channel_view_recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        validateSentMessage(TEST_RECIPIENT, true, false);
    }

    @Test
    public void scenarioX_sendMessageToContact_signed() {
        // go to channel
        Espresso.onView(withId(R.id.sn_channel_list_recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        // go to addMessage
        Espresso.onView(withId(R.id.snChannelViewMenuAddMessage))
                .perform(click());

        // add message with recipient
        Espresso.onView(withId(R.id.snMessage))
                .perform(typeText(TEST_MESSAGE));
        Espresso.onView(withId(R.id.selectRecipients))
                .perform(click());
        Espresso.onView(withId(R.id.person_list_recycler_view))
                .perform(RecyclerViewActions.actionOnItem(hasDescendant(withText(TEST_RECIPIENT)), click())
                        .atPosition(0));
        Espresso.onView(withId(R.id.personListSelectionDoneButton))
                .perform(click());
        // sign message
        Espresso.onView(withId(R.id.snSigned))
                .perform(click());
        Espresso.onView(withId(R.id.addButton))
                .perform(click());

        // go to sent message
        Espresso.onView(withId(R.id.sn_channel_view_recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        validateSentMessage(TEST_RECIPIENT, false, true);

    }

    @Test
    public void scenarioX_sendMessageToContact_signedAndEncrypted() {
        // go to channel
        Espresso.onView(withId(R.id.sn_channel_list_recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        // go to addMessage
        Espresso.onView(withId(R.id.snChannelViewMenuAddMessage))
                .perform(click());

        // add message with recipient
        Espresso.onView(withId(R.id.snMessage))
                .perform(typeText(TEST_MESSAGE));
        Espresso.onView(withId(R.id.selectRecipients))
                .perform(click());
        Espresso.onView(withId(R.id.person_list_recycler_view))
                .perform(RecyclerViewActions.actionOnItem(hasDescendant(withText(TEST_RECIPIENT)), click())
                        .atPosition(0));
        Espresso.onView(withId(R.id.personListSelectionDoneButton))
                .perform(click());
        // encrypt and sign message
        Espresso.onView(withId(R.id.snEncrypted))
                .perform(click());
        Espresso.onView(withId(R.id.snSigned))
                .perform(click());
        Espresso.onView(withId(R.id.addButton))
                .perform(click());

        // go to sent message
        Espresso.onView(withId(R.id.sn_channel_view_recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        validateSentMessage(TEST_RECIPIENT, true, true);
    }

    @Test
    public void scenarioX_abortSendingMessage() {
        // go to channel
        Espresso.onView(withId(R.id.sn_channel_list_recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        // go to addMessage
        Espresso.onView(withId(R.id.snChannelViewMenuAddMessage))
                .perform(click());

        // add message
        Espresso.onView(withId(R.id.snMessage))
                .perform(typeText(TEST_MESSAGE));
        Espresso.onView(withId(R.id.abortButton))
                .perform(click());

        // check that no message was sent
        Espresso.onView(withId(R.id.sn_channel_view_recycler_view))
                .check(new RecyclerViewItemCountAssertion(0));
    }

    @Test
    public void scenarioX_sendEmptyMessageIsProhibited_sendButtonDisabled() {
        // go to channel
        Espresso.onView(withId(R.id.sn_channel_list_recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        // go to addMessage
        Espresso.onView(withId(R.id.snChannelViewMenuAddMessage))
                .perform(click());
        // send button should be disabled
        Espresso.onView(withId(R.id.addButton))
                .check(matches(isNotEnabled()));
    }

    @Test
    public void scenarioX_sendMultipleMessagesShowsAllMessagesInChannel() {
        // go to channel
        Espresso.onView(withId(R.id.sn_channel_list_recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        // go to addMessage
        Espresso.onView(withId(R.id.snChannelViewMenuAddMessage))
                .perform(click());

        // add message
        Espresso.onView(withId(R.id.snMessage))
                .perform(typeText(TEST_MESSAGE));
        Espresso.onView(withId(R.id.addButton))
                .perform(click());

        // go to addMessage
        Espresso.onView(withId(R.id.snChannelViewMenuAddMessage))
                .perform(click());

        // add message
        Espresso.onView(withId(R.id.snMessage))
                .perform(typeText(TEST_MESSAGE));
        Espresso.onView(withId(R.id.addButton))
                .perform(click());

        // go to addMessage
        Espresso.onView(withId(R.id.snChannelViewMenuAddMessage))
                .perform(click());

        // add message
        Espresso.onView(withId(R.id.snMessage))
                .perform(typeText(TEST_MESSAGE));
        Espresso.onView(withId(R.id.addButton))
                .perform(click());

        // check that 3 messages were sent
        Espresso.onView(withId(R.id.sn_channel_view_recycler_view))
                .check(new RecyclerViewItemCountAssertion(3));
    }
}
