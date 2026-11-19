
package com.mycompany.nexachat;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class Message {

    private String messageID;
    private String recipient;
    private String messageText;
    private String messageHash;
    private String sender;

    // Part 3 message collections
    private static final ArrayList<Message> sentMessages = new ArrayList<>();
    private static final ArrayList<Message> disregardedMessages = new ArrayList<>();
    private static final ArrayList<Message> storedMessages = new ArrayList<>();

    private static final ArrayList<String> messageHashes = new ArrayList<>();
    private static final ArrayList<String> messageIDs = new ArrayList<>();

    // Default constructor for JSON loading
    public Message() {
    }

    // Existing constructor
    public Message(String messageID, String recipient,
                   String messageText, String messageHash) {

        this.messageID = messageID;
        this.recipient = recipient;
        this.messageText = messageText;
        this.messageHash = messageHash;
        this.sender = "Developer";
    }

    // Constructor with sender
    public Message(String messageID, String recipient,
                   String messageText, String messageHash,
                   String sender) {

        this(messageID, recipient, messageText, messageHash);
        this.sender = sender;
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

    public String getSender() {
        return sender;
    }

    // Validate message length
    public boolean checkMessageLength() {

        return messageText != null
                && !messageText.trim().isEmpty()
                && messageText.length() <= 250;
    }

    // Validate international and South African numbers
    public boolean checkRecipientCell() {

        return recipient != null
                && recipient.matches("^(\\+\\d{8,15}|0\\d{9})$");
    }

    // Generate a ten-digit message ID
    public static String generateMessageID() {

        long id = ThreadLocalRandom.current().nextLong(
                1_000_000_000L, 10_000_000_000L);

        return String.valueOf(id);
    }

    public boolean checkMessageID() {

        return messageID != null
                && messageID.matches("\\d{10}");
    }

    // Create message hash
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

    // Add message ID and hash to their collections
    private void addMessageDetails() {

        if (messageID != null && !messageIDs.contains(messageID)) {
            messageIDs.add(messageID);
        }

        if (messageHash != null && !messageHash.isEmpty()
                && !messageHashes.contains(messageHash)) {

            messageHashes.add(messageHash);
        }
    }

    // Send message
    public String SentMessage() {

        if (!checkMessageID()
                || !checkRecipientCell()
                || !checkMessageLength()) {

            return "Message could not be sent.";
        }

        if (!sentMessages.contains(this)) {
            sentMessages.add(this);
        }

        addMessageDetails();

        return "Message sent successfully.";
    }

    // Print all sent messages
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

    public static int returnTotalMessages() {
        return sentMessages.size();
    }

    // Disregard message
    public void disregardMessage() {

        if (!disregardedMessages.contains(this)) {
            disregardedMessages.add(this);
        }

        addMessageDetails();
    }

    // Store message
    public void storeMessage() {

        if (!storedMessages.contains(this)) {
            storedMessages.add(this);
        }

        addMessageDetails();
    }

    // Load stored messages into the collection
    public static void setStoredMessages(ArrayList<Message> messages) {

        storedMessages.clear();

        if (messages != null) {
            for (Message message : messages) {
                if (message != null) {
                    message.storeMessage();
                }
            }
        }
    }

    // Display sender and recipient of stored messages
    public static String displayStoredSenderRecipient() {

        StringBuilder report = new StringBuilder();

        for (Message message : storedMessages) {

            report.append("Sender: ")
                    .append(message.getSender())
                    .append("\nRecipient: ")
                    .append(message.getRecipient())
                    .append("\n\n");
        }

        return report.length() == 0
                ? "No stored messages."
                : report.toString();
    }

    // Find the longest stored message
    public static String getLongestStoredMessage() {

        if (storedMessages.isEmpty()) {
            return "";
        }

        Message longest = storedMessages.get(0);

        for (Message message : storedMessages) {

            if (message.getMessageText().length()
                    > longest.getMessageText().length()) {

                longest = message;
            }
        }

        return longest.getMessageText();
    }

    // Search for a message using its ID
    public static Message searchMessageByID(String id) {

        for (Message message : storedMessages) {

            if (message.getMessageID().equals(id)) {
                return message;
            }
        }

        return null;
    }

    // Search messages using recipient number
    public static ArrayList<Message> searchMessagesByRecipient(
            String recipient) {

        ArrayList<Message> results = new ArrayList<>();

        for (Message message : storedMessages) {

            if (message.getRecipient().equals(recipient)) {
                results.add(message);
            }
        }

        return results;
    }

    // Delete a stored message using its hash
    public static String deleteMessageByHash(String hash) {

        for (Message message : storedMessages) {

            if (message.getMessageHash().equals(hash)) {

                storedMessages.remove(message);

                return "Message: \"" + message.getMessageText()
                        + "\" successfully deleted.";
            }
        }

        return "Message hash not found.";
    }

    // Display complete stored-message report
    public static String displayStoredMessageReport() {

        StringBuilder report = new StringBuilder();

        if (storedMessages.isEmpty()) {
            return "No stored messages.";
        }

        for (Message message : storedMessages) {

            report.append("Message ID: ")
                    .append(message.getMessageID())
                    .append("\nMessage Hash: ")
                    .append(message.getMessageHash())
                    .append("\nSender: ")
                    .append(message.getSender())
                    .append("\nRecipient: ")
                    .append(message.getRecipient())
                    .append("\nMessage: ")
                    .append(message.getMessageText())
                    .append("\n--------------------\n");
        }

        return report.toString();
    }

    // Get collection copies
    public static ArrayList<Message> getSentMessages() {
        return new ArrayList<>(sentMessages);
    }

    public static ArrayList<Message> getDisregardedMessages() {
        return new ArrayList<>(disregardedMessages);
    }

    public static ArrayList<Message> getStoredMessages() {
        return new ArrayList<>(storedMessages);
    }

    public static ArrayList<String> getMessageHashes() {
        return new ArrayList<>(messageHashes);
    }

    public static ArrayList<String> getMessageIDs() {
        return new ArrayList<>(messageIDs);
    }
}
