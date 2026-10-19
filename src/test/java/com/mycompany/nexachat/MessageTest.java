
package com.mycompany.nexachat;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MessageTest {

    @Test
    public void testMessageIDIsTenDigits() {
        Message message = new Message(
                "1234567890",
                "+2712345678",
                "Hello there",
                ""
        );

        assertTrue(message.checkMessageID());
    }

    @Test
    public void testMessageIDIsInvalid() {
        Message message = new Message(
                "12345",
                "+2712345678",
                "Hello there",
                ""
        );

        assertFalse(message.checkMessageID());
    }

    @Test
    public void testMessageLengthIsValid() {
        Message message = new Message(
                "1234567890",
                "+2712345678",
                "Hello there",
                ""
        );

        assertTrue(message.checkMessageLength());
    }

    @Test
    public void testMessageLengthOver250Characters() {
        String longMessage = "A".repeat(251);

        Message message = new Message(
                "1234567890",
                "+2712345678",
                longMessage,
                ""
        );

        assertFalse(message.checkMessageLength());
    }

    @Test
    public void testRecipientNumberIsValid() {
        Message message = new Message(
                "1234567890",
                "+2712345678",
                "Hello there",
                ""
        );

        assertTrue(message.checkRecipientCell());
    }

    @Test
    public void testRecipientNumberIsInvalid() {
        Message message = new Message(
                "1234567890",
                "08966553",
                "Hello there",
                ""
        );

        assertFalse(message.checkRecipientCell());
    }

    @Test
    public void testMessageHashIsCreated() {
        Message message = new Message(
                "1234567890",
                "+2712345678",
                "Hello Thanks",
                ""
        );

        String hash = message.createMessageHash(0);

        assertEquals("12:0:HELLOTHANKS", hash);
    }

    @Test
    public void testValidMessageCanBeSent() {
        Message message = new Message(
                Message.generateMessageID(),
                "+2712345678",
                "Hello there",
                ""
        );

        message.createMessageHash(0);

        assertEquals(
                "Message sent successfully.",
                message.SentMessage()
        );
    }

    @Test
    public void testTotalMessagesIncreasesAfterSending() {
        int before = Message.returnTotalMessages();

        Message message = new Message(
                Message.generateMessageID(),
                "+2712345678",
                "Testing message count",
                ""
        );

        message.createMessageHash(before);
        message.SentMessage();

        assertEquals(before + 1, Message.returnTotalMessages());
    }

    @Test
    public void testInvalidMessageCannotBeSent() {
        int before = Message.returnTotalMessages();

        Message message = new Message(
                "12345",
                "+2712345678",
                "Hello there",
                ""
        );

        assertEquals(
                "Message could not be sent.",
                message.SentMessage()
        );

        assertEquals(before, Message.returnTotalMessages());
    }
}
