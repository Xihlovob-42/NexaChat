
package com.mycompany.nexachat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

public class Message {

    private String messageID;
    private String recipient;
    private String messageText;
    private String messageHash;
    private String sender;

    private static Message[] sentMessages = new Message[0];
    private static Message[] disregardedMessages = new Message[0];
    private static Message[] storedMessages = new Message[0];

    private static String[] messageHashes = new String[0];
    private static String[] messageIDs = new String[0];

    public Message() {
        this.sender = "Xihlovo M";
    }

    public Message(String messageID, String recipient,
                   String messageText, String messageHash) {

        this.messageID = messageID;
        this.recipient = recipient;
        this.messageText = messageText;
        this.messageHash = messageHash;
        this.sender = "Xihlovo M";
    }

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
        return sender == null || sender.trim().isEmpty()
                ? "Xihlovo M" : sender;
    }

    public boolean checkMessageLength() {
        return messageText != null
                && !messageText.trim().isEmpty()
                && messageText.length() <= 250;
    }

    public boolean checkRecipientCell() {
        return recipient != null
                && recipient.matches("^(\\+\\d{8,15}|0\\d{9})$");
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

    private void addMessageDetails() {

        if (messageID != null && !contains(messageIDs, messageID)) {
            messageIDs = Arrays.copyOf(messageIDs, messageIDs.length + 1);
            messageIDs[messageIDs.length - 1] = messageID;
        }

        if (messageHash != null && !messageHash.isEmpty()
                && !contains(messageHashes, messageHash)) {

            messageHashes = Arrays.copyOf(
                    messageHashes, messageHashes.length + 1);

            messageHashes[messageHashes.length - 1] = messageHash;
        }
    }

    private static boolean contains(String[] array, String value) {
        for (String item : array) {
            if (item.equals(value)) {
                return true;
            }
        }
        return false;
    }

    private static boolean contains(Message[] array, Message value) {
        for (Message item : array) {
            if (item == value) {
                return true;
            }
        }
        return false;
    }

    private static Message[] addMessage(Message[] array, Message message) {
        if (contains(array, message)) {
            return array;
        }

        Message[] updated = Arrays.copyOf(array, array.length + 1);
        updated[updated.length - 1] = message;

        return updated;
    }

    public String SentMessage() {

        if (!checkMessageID()
                || !checkRecipientCell()
                || !checkMessageLength()) {
            return "Message could not be sent.";
        }

        sentMessages = addMessage(sentMessages, this);
        addMessageDetails();

        return "Message sent successfully.";
    }

    public static void printMessages() {

        if (sentMessages.length == 0) {
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
        return sentMessages.length;
    }

    public void disregardMessage() {
        disregardedMessages = addMessage(disregardedMessages, this);
        addMessageDetails();
    }

    public void storeMessage() {
        storedMessages = addMessage(storedMessages, this);
        addMessageDetails();
    }

    public static void setStoredMessages(ArrayList<Message> messages) {

        storedMessages = new Message[0];

        if (messages != null) {
            for (Message message : messages) {
                if (message != null) {
                    message.storeMessage();
                }
            }
        }
    }

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

    public static String getLongestStoredMessage() {

        if (storedMessages.length == 0) {
            return "";
        }

        Message longest = storedMessages[0];

        for (Message message : storedMessages) {
            if (message.getMessageText().length()
                    > longest.getMessageText().length()) {
                longest = message;
            }
        }

        return longest.getMessageText();
    }

    public static Message searchMessageByID(String id) {

        for (Message message : storedMessages) {
            if (message.getMessageID().equals(id)) {
                return message;
            }
        }

        return null;
    }

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

    public static String deleteMessageByHash(String hash) {

        for (int i = 0; i < storedMessages.length; i++) {

            if (storedMessages[i].getMessageHash().equals(hash)) {

                Message deleted = storedMessages[i];

                Message[] updated = new Message[storedMessages.length - 1];

                System.arraycopy(storedMessages, 0, updated, 0, i);

                System.arraycopy(
                        storedMessages, i + 1,
                        updated, i,
                        storedMessages.length - i - 1
                );

                storedMessages = updated;

                return "Message: \"" + deleted.getMessageText()
                        + "\" successfully deleted.";
            }
        }

        return "Message hash not found.";
    }

    public static String displayStoredMessageReport() {

        StringBuilder report = new StringBuilder();

        if (storedMessages.length == 0) {
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

    public static ArrayList<Message> getSentMessages() {
        return new ArrayList<>(Arrays.asList(sentMessages));
    }

    public static ArrayList<Message> getDisregardedMessages() {
        return new ArrayList<>(Arrays.asList(disregardedMessages));
    }

    public static ArrayList<Message> getStoredMessages() {
        return new ArrayList<>(Arrays.asList(storedMessages));
    }

    public static ArrayList<String> getMessageHashes() {
        return new ArrayList<>(Arrays.asList(messageHashes));
    }

    public static ArrayList<String> getMessageIDs() {
        return new ArrayList<>(Arrays.asList(messageIDs));
    }
}
