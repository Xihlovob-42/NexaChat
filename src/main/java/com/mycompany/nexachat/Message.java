
package com.mycompany.nexachat;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class Message {

    private String messageID;
    private String recipient;
    private String messageText;
    private String messageHash;

    // Required message collections
    private static final ArrayList<Message> sentMessages = new ArrayList<>();
    private static final ArrayList<Message> disregardedMessages = new ArrayList<>();
    private static final ArrayList<String> messageHashes = new ArrayList<>();
    private static final ArrayList<String> messageIDs = new ArrayList<>();

    public Message(String messageID, String recipient,
                   String messageText, String messageHash) {
        this.messageID = messageID;
        this.recipient = recipient;
        this.messageText = messageText;
        this.messageHash = messageHash;
    }

    public String getMessageID() {
        return messageID;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getMessageText() {
        return messageText;
    }

    public String getMessageHash() {
        return messageHash;
    }

    public boolean checkMessageLength() {
        return messageText != null
                && !messageText.trim().isEmpty()
                && messageText.length() <= 250;
    }

    public boolean checkRecipientCell() {
        return recipient != null
                && recipient.matches("^\\+\\d{1,3}\\d{1,7}$");
    }

    public static String generateMessageID() {
        long id = ThreadLocalRandom.current().nextLong(
                1_000_000_000L, 10_000_000_000L);
        return String.valueOf(id);
    }

    public boolean checkMessageID() {
        return messageID != null
                && messageID.matches("\\d{10}");
    }

    public String createMessageHash(int messageNumber) {
        if (messageID == null || messageID.length() < 2
                || messageText == null
                || messageText.trim().isEmpty()) {
            return "";
        }

        String[] words = messageText.trim().split("\\s+");

        String firstWord = words[0].toUpperCase();
        String lastWord = words[words.length - 1].toUpperCase();

        messageHash = messageID.substring(0, 2)
                + ":" + messageNumber
                + ":" + firstWord + lastWord;

        return messageHash;
    }

    // Records a successfully sent message
    public String SentMessage() {
        if (!checkMessageID()
                || !checkRecipientCell()
                || !checkMessageLength()) {
            return "Message could not be sent.";
        }

        sentMessages.add(this);
        messageIDs.add(messageID);

        if (messageHash != null && !messageHash.isEmpty()) {
            messageHashes.add(messageHash);
        }

        return "Message sent successfully.";
    }

    // Displays sent messages
    public static void printMessages() {
        if (sentMessages.isEmpty()) {
            System.out.println("No sent messages.");
            return;
        }

        for (Message message : sentMessages) {
            System.out.println("Message ID: " + message.getMessageID());
            System.out.println("Recipient: " + message.getRecipient());
            System.out.println("Message: " + message.getMessageText());
            System.out.println("Message Hash: " + message.getMessageHash());
            System.out.println("--------------------");
        }
    }

    // Returns the total number of sent messages
    public static int returnTotalMessages() {
        return sentMessages.size();
    }

    // Supports the alternate spelling if it appears in the brief
    public static int returnTotalMessagess() {
        return returnTotalMessages();
    }

    // Records a disregarded message
    public void disregardMessage() {
        disregardedMessages.add(this);
    }

    public static ArrayList<Message> getSentMessages() {
        return new ArrayList<>(sentMessages);
    }

    public static ArrayList<Message> getDisregardedMessages() {
        return new ArrayList<>(disregardedMessages);
    }

    public static ArrayList<String> getMessageHashes() {
        return new ArrayList<>(messageHashes);
    }

    public static ArrayList<String> getMessageIDs() {
        return new ArrayList<>(messageIDs);
    }
}
