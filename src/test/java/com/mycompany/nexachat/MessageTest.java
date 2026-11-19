
package com.mycompany.nexachat;

import java.util.ArrayList;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MessageTest {

    @Test
    public void testMessageIDIsTenDigits() {
        Message message = new Message("1234567890", "+2712345678", "Hello there", "");
        assertTrue(message.checkMessageID());
    }

    @Test
    public void testMessageIDIsInvalid() {
        Message message = new Message("12345", "+2712345678", "Hello there", "");
        assertFalse(message.checkMessageID());
    }

    @Test
    public void testMessageLengthIsValid() {
        Message message = new Message("1234567890", "+2712345678", "Hello there", "");
        assertTrue(message.checkMessageLength());
    }

    @Test
    public void testMessageLengthOver250Characters() {
        String longMessage = "A".repeat(251);
        Message message = new Message("1234567890", "+2712345678", longMessage, "");
        assertFalse(message.checkMessageLength());
    }

    @Test
    public void testRecipientNumberIsValid() {
        Message message = new Message("1234567890", "+2712345678", "Hello there", "");
        assertTrue(message.checkRecipientCell());
    }

    @Test
    public void testRecipientNumberIsInvalid() {
        Message message = new Message("1234567890", "08966553", "Hello there", "");
        assertFalse(message.checkRecipientCell());
    }

    @Test
    public void testMessageHashIsCreated() {
        Message message = new Message("1234567890", "+2712345678", "Hello Thanks", "");
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
        assertEquals("Message sent successfully.", message.SentMessage());
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

        Message message = new Message("12345", "+2712345678", "Hello there", "");

        assertEquals("Message could not be sent.", message.SentMessage());
        assertEquals(before, Message.returnTotalMessages());
    }

    @Test
    public void testSentMessagesArrayIsPopulated() {
        Message first = new Message(
                "1000000001", "+27834557896", "Did you get the cake?", ""
        );

        Message second = new Message(
                "1000000004", "0838884567", "It is dinner time!", ""
        );

        first.createMessageHash(0);
        second.createMessageHash(1);

        first.SentMessage();
        second.SentMessage();

        ArrayList<Message> messages = Message.getSentMessages();

        assertTrue(messages.stream().anyMatch(
                message -> message.getMessageText().equals("Did you get the cake?")
        ));

        assertTrue(messages.stream().anyMatch(
                message -> message.getMessageText().equals("It is dinner time!")
        ));
    }

    @Test
    public void testLongestStoredMessage() {
        Message message = new Message(
                "2000000002",
                "+27838884567",
                "Where are you? You are late! I have asked you to be on time.",
                ""
        );

        message.createMessageHash(1);
        message.storeMessage();

        assertEquals(
                "Where are you? You are late! I have asked you to be on time.",
                Message.getLongestStoredMessage()
        );
    }

    @Test
    public void testSearchMessageByID() {
        Message message = new Message(
                "1000000004", "0838884567", "It is dinner time!", ""
        );

        message.createMessageHash(3);
        message.storeMessage();

        Message result = Message.searchMessageByID("1000000004");

        assertNotNull(result);
        assertEquals("0838884567", result.getRecipient());
        assertEquals("It is dinner time!", result.getMessageText());
    }

    @Test
    public void testSearchMessagesByRecipient() {
        Message first = new Message(
                "2000000002",
                "+27838884567",
                "Where are you? You are late! I have asked you to be on time.",
                ""
        );

        Message second = new Message(
                "2000000005",
                "+27838884567",
                "Ok, I am leaving without you.",
                ""
        );

        first.createMessageHash(1);
        second.createMessageHash(4);

        first.storeMessage();
        second.storeMessage();

        ArrayList<Message> results =
                Message.searchMessagesByRecipient("+27838884567");

        assertTrue(results.stream().anyMatch(
                message -> message.getMessageText().equals(
                        "Where are you? You are late! I have asked you to be on time."
                )
        ));

        assertTrue(results.stream().anyMatch(
                message -> message.getMessageText().equals(
                        "Ok, I am leaving without you."
                )
        ));
    }

    @Test
    public void testDeleteMessageByHash() {
        String uniqueID = Message.generateMessageID();
        int uniqueNumber = (int) (System.nanoTime() % 1000000);

        Message message = new Message(
                uniqueID,
                "+27838884567",
                "Unique deletion test message",
                ""
        );

        String hash = message.createMessageHash(uniqueNumber);
        message.storeMessage();

        String result = Message.deleteMessageByHash(hash);

        assertTrue(result.contains("successfully deleted"));

        assertFalse(Message.getStoredMessages().stream().anyMatch(
                stored -> stored.getMessageHash().equals(hash)
        ));
    }

    @Test
    public void testStoredMessageReportContainsMessageDetails() {
        Message message = new Message(
                "1000000001",
                "+27834557896",
                "Did you get the cake?",
                ""
        );

        message.createMessageHash(0);
        message.storeMessage();

        String report = Message.displayStoredMessageReport();

        assertNotNull(report);
        assertTrue(report.contains("Did you get the cake?"));
        assertTrue(report.contains("+27834557896"));
        assertTrue(report.contains(message.getMessageHash()));
    }
}
