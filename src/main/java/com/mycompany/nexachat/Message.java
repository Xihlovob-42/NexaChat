
package com.mycompany.nexachat;

import java.util.concurrent.ThreadLocalRandom;

public class Message {

    private String messageID;
    private String recipient;
    private String messageText;
    private String messageHash;

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
}
