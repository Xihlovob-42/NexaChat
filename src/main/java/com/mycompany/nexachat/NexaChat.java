
package com.mycompany.nexachat;

import java.util.ArrayList;
import java.util.Scanner;

public class NexaChat {

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);
        Register user = new Register();
        MessageStorage storage = new MessageStorage();

        Message[] loadedMessages = storage.loadMessages();
        ArrayList<Message> storedMessages = new ArrayList<>();

        for (Message message : loadedMessages) {
            storedMessages.add(message);
        }

        Message.setStoredMessages(storedMessages);

        System.out.println("Welcome to QuickChat.");
        System.out.println("Please register to continue.");

        System.out.print("Enter first name: ");
        String firstName = input.nextLine();

        System.out.print("Enter last name: ");
        String lastName = input.nextLine();

        System.out.print("Enter username: ");
        String username = input.nextLine();

        System.out.print("Enter password: ");
        String password = input.nextLine();

        System.out.print("Enter cellphone number: ");
        String cellphone = input.nextLine();

        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setUsername(username);
        user.setPassword(password);
        user.setCellPhoneNumber(cellphone);

        String registrationStatus = user.registerUser();
        System.out.println(registrationStatus);

        if (!registrationStatus.equals("Registration successful.")) {
            input.close();
            return;
        }

        System.out.println("Please log in.");

        System.out.print("Enter username: ");
        String loginUsername = input.nextLine();

        System.out.print("Enter password: ");
        String loginPassword = input.nextLine();

        boolean loginSuccessful = user.loginUser(
                loginUsername, loginPassword
        );

        System.out.println(user.returnLoginStatus(loginSuccessful));

        if (!loginSuccessful) {
            input.close();
            return;
        }

        int choice = 0;

        while (choice != 3) {

            System.out.println("\n1. Send Messages");
            System.out.println("2. Show recently sent messages");
            System.out.println("3. Quit");
            System.out.println("4. Stored Messages");
            System.out.print("Choose an option: ");

            if (!input.hasNextInt()) {
                input.nextLine();
                System.out.println("Invalid option.");
                continue;
            }

            choice = input.nextInt();
            input.nextLine();

            switch (choice) {

                case 1:

                    System.out.print("Enter recipient cellphone number: ");
                    String recipient = input.nextLine();

                    System.out.print("Enter message: ");
                    String messageText = input.nextLine();

                    String messageID = Message.generateMessageID();

                    Message message = new Message(
                            messageID,
                            recipient,
                            messageText,
                            "",
                            firstName + " " + lastName
                    );

                    if (!message.checkMessageID()
                            || !message.checkRecipientCell()
                            || !message.checkMessageLength()) {
                        System.out.println("Invalid message details.");
                        break;
                    }

                    String messageHash = message.createMessageHash(
                            Message.returnTotalMessages()
                    );

                    message = new Message(
                            messageID,
                            recipient,
                            messageText,
                            messageHash,
                            firstName + " " + lastName
                    );

                    System.out.println("Message ID: " + message.getMessageID());
                    System.out.println("Message Hash: " + message.getMessageHash());
                    System.out.println("Recipient: " + message.getRecipient());
                    System.out.println("Message: " + message.getMessageText());

                    System.out.println("1. Send Message");
                    System.out.println("2. Disregard Message");
                    System.out.println("3. Store Message");
                    System.out.print("Choose an option: ");

                    if (!input.hasNextInt()) {
                        input.nextLine();
                        System.out.println("Invalid option.");
                        break;
                    }

                    int action = input.nextInt();
                    input.nextLine();

                    if (action == 1) {
                        System.out.println(message.SentMessage());
                    } else if (action == 2) {
                        message.disregardMessage();
                        System.out.println("Message disregarded.");
                    } else if (action == 3) {
                        message.storeMessage();
                        storedMessages.add(message);
                        storage.saveMessages(storedMessages);
                    } else {
                        System.out.println("Invalid option.");
                    }

                    System.out.println("Total messages sent: "
                            + Message.returnTotalMessages());

                    break;

                case 2:

                    Message.printMessages();
                    break;

                case 3:

                    System.out.println("Goodbye.");
                    break;

                case 4:

                    int storedChoice = 0;

                    while (storedChoice != 7) {

                        System.out.println("\nStored Messages");
                        System.out.println("1. Display sender and recipient");
                        System.out.println("2. Display longest stored message");
                        System.out.println("3. Search by message ID");
                        System.out.println("4. Search by recipient");
                        System.out.println("5. Delete message by hash");
                        System.out.println("6. Display complete report");
                        System.out.println("7. Return to main menu");
                        System.out.print("Choose an option: ");

                        if (!input.hasNextInt()) {
                            input.nextLine();
                            System.out.println("Invalid option.");
                            continue;
                        }

                        storedChoice = input.nextInt();
                        input.nextLine();

                        switch (storedChoice) {

                            case 1:

                                System.out.println(
                                        Message.displayStoredSenderRecipient()
                                );
                                break;

                            case 2:

                                System.out.println(
                                        Message.getLongestStoredMessage()
                                );
                                break;

                            case 3:

                                System.out.print("Enter message ID: ");
                                String searchID = input.nextLine();

                                Message foundMessage =
                                        Message.searchMessageByID(searchID);

                                if (foundMessage != null) {
                                    System.out.println("Recipient: "
                                            + foundMessage.getRecipient());
                                    System.out.println("Message: "
                                            + foundMessage.getMessageText());
                                } else {
                                    System.out.println("Message not found.");
                                }

                                break;

                            case 4:

                                System.out.print("Enter recipient number: ");
                                String searchRecipient = input.nextLine();

                                ArrayList<Message> recipientMessages =
                                        Message.searchMessagesByRecipient(
                                                searchRecipient
                                        );

                                if (recipientMessages.isEmpty()) {
                                    System.out.println("No messages found.");
                                } else {
                                    for (Message storedMessage : recipientMessages) {
                                        System.out.println(
                                                storedMessage.getMessageText()
                                        );
                                    }
                                }

                                break;

                            case 5:

                                System.out.print("Enter message hash: ");
                                String searchHash = input.nextLine();

                                System.out.println(
                                        Message.deleteMessageByHash(searchHash)
                                );

                                storedMessages = Message.getStoredMessages();
                                storage.saveMessages(storedMessages);

                                break;

                            case 6:

                                System.out.println(
                                        Message.displayStoredMessageReport()
                                );
                                break;

                            case 7:

                                System.out.println("Returning to main menu.");
                                break;

                            default:

                                System.out.println("Invalid option.");
                        }
                    }

                    break;

                default:

                    System.out.println("Invalid option.");
            }
        }

        input.close();
    }
}
