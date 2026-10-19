
package com.mycompany.nexachat;

import java.util.ArrayList;
import java.util.Scanner;

public class NexaChat {

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);
        Register user = new Register();
        MessageStorage storage = new MessageStorage();

        ArrayList<Message> storedMessages = storage.loadMessages();

        System.out.println("Welcome to QuickChat.");
        System.out.println("Please register to continue.");

        System.out.print("Enter first name: ");
        user.setFirstName(input.nextLine());

        System.out.print("Enter last name: ");
        user.setLastName(input.nextLine());

        System.out.print("Enter username: ");
        user.setUsername(input.nextLine());

        System.out.print("Enter password: ");
        user.setPassword(input.nextLine());

        System.out.print("Enter cellphone number (+27 format): ");
        user.setCellPhoneNumber(input.nextLine());

        String registrationStatus = user.registerUser();
        System.out.println(registrationStatus);

        if (!registrationStatus.equals("Registration successful.")) {
            System.out.println("Please restart and register again.");
            input.close();
            return;
        }

        System.out.println("\nPlease log in.");

        System.out.print("Enter username: ");
        String enteredUsername = input.nextLine();

        System.out.print("Enter password: ");
        String enteredPassword = input.nextLine();

        boolean loginSuccessful =
                user.loginUser(enteredUsername, enteredPassword);

        System.out.println(user.returnLoginStatus(loginSuccessful));

        if (!loginSuccessful) {
            input.close();
            return;
        }

        int choice = 0;

        while (choice != 3) {

            System.out.println("\nPlease select an option:");
            System.out.println("1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Quit");
            System.out.println("4) Stored Messages");
            System.out.print("Enter your choice: ");

            if (!input.hasNextInt()) {
                System.out.println("Please enter a number.");
                input.nextLine();
                continue;
            }

            choice = input.nextInt();
            input.nextLine();

            switch (choice) {

                case 1:
                    System.out.print("Enter recipient number: ");
                    String recipient = input.nextLine().trim();

                    System.out.print("Enter your message: ");
                    String messageText = input.nextLine();

                    String messageID = Message.generateMessageID();

                    Message message = new Message(
                            messageID, recipient, messageText, "");

                    if (!message.checkMessageID()) {
                        System.out.println("Invalid message ID.");
                        break;
                    }

                    if (!message.checkRecipientCell()) {
                        System.out.println("Invalid recipient number.");
                        break;
                    }

                    if (!message.checkMessageLength()) {
                        System.out.println(
                                "Message must contain 1 to 250 characters.");
                        break;
                    }

                    int messageNumber = Message.returnTotalMessages();
                    String messageHash =
                            message.createMessageHash(messageNumber);

                    System.out.println("\nMessage ID: "
                            + message.getMessageID());
                    System.out.println("Message Hash: " + messageHash);
                    System.out.println("Recipient: " + message.getRecipient());
                    System.out.println("Message: " + message.getMessageText());

                    System.out.println("\nChoose what to do with the message:");
                    System.out.println("1) Send Message");
                    System.out.println("2) Disregard Message");
                    System.out.println("3) Store Message");
                    System.out.print("Enter your choice: ");

                    if (!input.hasNextInt()) {
                        System.out.println("Invalid choice.");
                        input.nextLine();
                        break;
                    }

                    int messageChoice = input.nextInt();
                    input.nextLine();

                    switch (messageChoice) {

                        case 1:
                            System.out.println(message.SentMessage());
                            break;

                        case 2:
                            message.disregardMessage();
                            System.out.println("Message disregarded.");
                            break;

                        case 3:
                            message.storeMessage();
                            storedMessages.add(message);
                            storage.saveMessages(storedMessages);
                            System.out.println("Message stored successfully.");
                            break;

                        default:
                            System.out.println("Invalid choice.");
                    }

                    System.out.println("Total messages sent: "
                            + Message.returnTotalMessages());
                    break;

                case 2:
                    Message.printMessages();
                    break;

                case 3:
                    System.out.println("Thank you for using QuickChat.");
                    break;

                case 4:
                    if (storedMessages.isEmpty()) {
                        System.out.println("No stored messages.");
                    } else {
                        System.out.println("\nStored Messages:");

                        for (Message stored : storedMessages) {
                            System.out.println("Message ID: "
                                    + stored.getMessageID());
                            System.out.println("Recipient: "
                                    + stored.getRecipient());
                            System.out.println("Message: "
                                    + stored.getMessageText());
                            System.out.println("Message Hash: "
                                    + stored.getMessageHash());
                            System.out.println("--------------------");
                        }
                    }
                    break;

                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }

        input.close();
    }
}
