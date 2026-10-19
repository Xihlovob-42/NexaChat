
package com.mycompany.nexachat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class MessageStorage {

    private static final String FILE_NAME = "stored_messages.json";

    private final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    public void saveMessages(ArrayList<Message> messages) {
        try (FileWriter writer = new FileWriter(FILE_NAME)) {
            gson.toJson(messages, writer);
            System.out.println("Messages saved successfully.");
        } catch (IOException e) {
            System.out.println("Could not save messages: "
                    + e.getMessage());
        }
    }

    public ArrayList<Message> loadMessages() {
        Type messageListType =
                new TypeToken<ArrayList<Message>>() {}.getType();

        try (FileReader reader = new FileReader(FILE_NAME)) {
            ArrayList<Message> messages =
                    gson.fromJson(reader, messageListType);

            return messages != null
                    ? messages : new ArrayList<>();
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }
}
