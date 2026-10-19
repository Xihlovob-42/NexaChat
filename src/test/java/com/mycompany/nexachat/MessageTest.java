
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
}
