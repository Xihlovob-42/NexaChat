package com.mycompany.nexachat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class MessageStorage {

    private static final String FILE_NAME = "stored_messages.json";

    private final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    public void saveMessages(ArrayList<Message> messages) {
        Message[] messageArray = messages.toArray(new Message[0]);

        try (FileWriter writer = new FileWriter(FILE_NAME)) {
            gson.toJson(messageArray, writer);
            System.out.println("Messages saved successfully.");
        } catch (IOException e) {
            System.out.println("Could not save messages: " + e.getMessage());
        }
    }

    public Message[] loadMessages() {
        try (FileReader reader = new FileReader(FILE_NAME)) {
            Message[] messages = gson.fromJson(reader, Message[].class);

            return messages != null ? messages : new Message[0];

        } catch (IOException e) {
            return new Message[0];
        }
    }

    public ArrayList<Message> loadMessagesAsList() {
        return new ArrayList<>(Arrays.asList(loadMessages()));
    }
}