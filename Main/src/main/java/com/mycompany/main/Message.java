package com.mycompany.main;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

// Creates the Message class
public class Message {

    // Stores the unique Message ID (10 digits)
    private String messageID;

    // Stores the message number (1, 2, 3...)
    private int messageNumber;

    // Stores the recipient cell number
    private String recipient;

    // Stores the actual message text
    private String messageText;

    // Stores the Message Hash
    private String messageHash;

    // Stores whether the message was Sent, Stored or Disregarded
    private String status;

    // Keeps a list of all messages created while the program runs
    private static ArrayList<Message> allMessages = new ArrayList<>();

    // Counts how many messages were actually sent
    private static int totalMessagesSent = 0;

    // ========== PART 3 ARRAYS 
    public static ArrayList<String> sentMessages = new ArrayList<>();
    public static ArrayList<String> disregardedMessages = new ArrayList<>();
    public static ArrayList<String> storedMessages = new ArrayList<>();
    public static ArrayList<String> messageHashes = new ArrayList<>();
    public static ArrayList<String> messageIDs = new ArrayList<>();

    // Extra parallel arrays for stored messages (needed for search and delete)
    public static ArrayList<String> storedRecipients = new ArrayList<>();
    public static ArrayList<String> storedMessageIDs = new ArrayList<>();
    public static ArrayList<String> storedHashes = new ArrayList<>();

    // Creates the Message constructor
    public Message(int messageNumber, String recipient, String messageText) {
        this.messageNumber = messageNumber;
        this.recipient = recipient;
        this.messageText = messageText;
        this.messageID = generateMessageID();
        this.messageHash = createMessageHash();
        this.status = "";
    }

    // Boolean: checkMessageID()
    public boolean checkMessageID() {
        if (messageID != null && messageID.length() <= 10) {
            return true;
        } else {
            return false;
        }
    }

    // String: checkRecipientCell()
    public String checkRecipientCell() {
        if (recipient != null
                && recipient.startsWith("+")
                && recipient.length() <= 13
                && recipient.substring(1).matches("[0-9]+")) {
            return "Cell phone number successfully captured.";
        } else {
            return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
        }
    }

    // String: createMessageHash()
    public String createMessageHash() {
        String firstTwo = messageID.substring(0, 2);

        String[] words = messageText.trim().split("\\s+");

        String firstWord = "";
        String lastWord = "";

        if (words.length > 0) {
            firstWord = words[0];
        }
        if (words.length > 1) {
            lastWord = words[words.length - 1];
        } else {
            lastWord = firstWord;
        }

        firstWord = firstWord.replaceAll("[^a-zA-Z]", "");
        lastWord = lastWord.replaceAll("[^a-zA-Z]", "");

        messageHash = (firstTwo + ":" + messageNumber + ":" + firstWord + lastWord).toUpperCase();
        return messageHash;
    }

    // String: SentMessage()
    public String SentMessage() {
        System.out.println("\nChoose an option:");
        System.out.println("1) Send Message");
        System.out.println("2) Disregard Message");
        System.out.println("3) Store Message to send later");
        System.out.print("Enter choice (1-3): ");

        String choice = Main.input.nextLine().trim();

        if (choice.equals("1")) {
            status = "Sent";
            totalMessagesSent = totalMessagesSent + 1;
            allMessages.add(this);

            // Populate Part 3 arrays
            sentMessages.add(messageText);
            messageHashes.add(messageHash);
            messageIDs.add(messageID);

            return "Message successfully sent.";
        } else if (choice.equals("2")) {
            status = "Disregarded";
            disregardedMessages.add(messageText);
            return "Press 0 to delete the message.";
        } else if (choice.equals("3")) {
            status = "Stored";
            allMessages.add(this);

            // Populate Part 3 arrays
            storedMessages.add(messageText);
            storedRecipients.add(recipient);
            storedMessageIDs.add(messageID);
            storedHashes.add(messageHash);
            messageHashes.add(messageHash);
            messageIDs.add(messageID);

            storeMessage();
            return "Message successfully stored.";
        } else {
            status = "Disregarded";
            disregardedMessages.add(messageText);
            return "Invalid option – message disregarded.";
        }
    }

    // String: printMessages()
    public static String printMessages() {
        if (allMessages.isEmpty()) {
            return "No messages have been sent yet.";
        }

        String result = "";
        for (int i = 0; i < allMessages.size(); i++) {
            Message m = allMessages.get(i);
            result = result + "Message ID: " + m.messageID + "\n";
            result = result + "Message Hash: " + m.messageHash + "\n";
            result = result + "Recipient: " + m.recipient + "\n";
            result = result + "Message: " + m.messageText + "\n";
            result = result + "Status: " + m.status + "\n";
            result = result + "------------------------\n";
        }
        return result;
    }

    // Int: returnTotalMessages()
    public static int returnTotalMessages() {
        return totalMessagesSent;
    }

    @SuppressWarnings("unchecked")
    public void storeMessage() {
        try {
            JSONArray jsonArray = new JSONArray();

            for (int i = 0; i < storedMessages.size(); i++) {
                JSONObject obj = new JSONObject();
                obj.put("messageID", storedMessageIDs.get(i));
                obj.put("recipient", storedRecipients.get(i));
                obj.put("message", storedMessages.get(i));
                obj.put("messageHash", storedHashes.get(i));
                obj.put("status", "Stored");
                jsonArray.add(obj);
            }

            FileWriter file = new FileWriter("messages.json");
            file.write(jsonArray.toJSONString());
            file.close();
            System.out.println("Messages successfully stored in messages.json");
        } catch (IOException e) {
            System.out.println("Error writing to JSON file: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public static void loadStoredMessagesFromJSON() {
        try {
            JSONParser parser = new JSONParser();
            FileReader reader = new FileReader("messages.json");
            Object obj = parser.parse(reader);
            JSONArray jsonArray = (JSONArray) obj;

            storedMessages.clear();
            storedRecipients.clear();
            storedMessageIDs.clear();
            storedHashes.clear();

            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObj = (JSONObject) jsonArray.get(i);
                storedMessageIDs.add((String) jsonObj.get("messageID"));
                storedRecipients.add((String) jsonObj.get("recipient"));
                storedMessages.add((String) jsonObj.get("message"));
                storedHashes.add((String) jsonObj.get("messageHash"));
            }

            reader.close();
            System.out.println("Stored messages loaded from JSON file.");
        } catch (Exception e) {
            System.out.println("No stored messages file found or error reading JSON.");
        }
    }

    // ========== PART 3 FEATURE METHODS ==========

    // a. Display recipient and message of all stored messages
    public static void displayStoredSendersAndRecipients() {
        if (storedMessages.isEmpty()) {
            System.out.println("No stored messages found.");
            return;
        }
        System.out.println("\n----- Stored Messages -----");
        for (int i = 0; i < storedMessages.size(); i++) {
            System.out.println("Recipient: " + storedRecipients.get(i));
            System.out.println("Message: " + storedMessages.get(i));
            System.out.println("------------------------");
        }
    }

    // b. Display the longest stored message
    public static String displayLongestStoredMessage() {
        if (storedMessages.isEmpty()) {
            return "No stored messages found.";
        }

        String longest = storedMessages.get(0);
        for (int i = 1; i < storedMessages.size(); i++) {
            if (storedMessages.get(i).length() > longest.length()) {
                longest = storedMessages.get(i);
            }
        }
        return longest;
    }

    // c. Search for a message ID and return the message text
    public static String searchByMessageID(String id) {
        for (int i = 0; i < storedMessageIDs.size(); i++) {
            if (storedMessageIDs.get(i).equals(id)) {
                return storedMessages.get(i);
            }
        }
        return "No message found with that ID.";
    }

    // d. Search for all messages stored for a particular recipient
    public static String searchByRecipient(String recipient) {
        String result = "";
        boolean found = false;
        for (int i = 0; i < storedRecipients.size(); i++) {
            if (storedRecipients.get(i).equals(recipient)) {
                result = result + storedMessages.get(i) + "\n";
                found = true;
            }
        }
        if (!found) {
            return "No messages found for that recipient.";
        }
        return result.trim();
    }

    // e. Delete a message using the message hash
    public static String deleteByHash(String hash) {
        for (int i = 0; i < storedHashes.size(); i++) {
            if (storedHashes.get(i).equals(hash)) {
                String deletedMessage = storedMessages.get(i);
                storedMessages.remove(i);
                storedRecipients.remove(i);
                storedMessageIDs.remove(i);
                storedHashes.remove(i);

                // Update the JSON file after deletion
                try {
                    JSONArray jsonArray = new JSONArray();
                    for (int j = 0; j < storedMessages.size(); j++) {
                        JSONObject obj = new JSONObject();
                        obj.put("messageID", storedMessageIDs.get(j));
                        obj.put("recipient", storedRecipients.get(j));
                        obj.put("message", storedMessages.get(j));
                        obj.put("messageHash", storedHashes.get(j));
                        obj.put("status", "Stored");
                        jsonArray.add(obj);
                    }
                    FileWriter file = new FileWriter("messages.json");
                    file.write(jsonArray.toJSONString());
                    file.close();
                } catch (IOException e) {
                    System.out.println("Error updating JSON file after delete.");
                }

                return "Message: \"" + deletedMessage + "\" successfully deleted.";
            }
        }
        return "No message found with that hash.";
    }

    // f. Display a full report of all stored messages
    public static void displayFullReport() {
        if (storedMessages.isEmpty()) {
            System.out.println("No stored messages to display.");
            return;
        }

        System.out.println("\n========== FULL STORED MESSAGES REPORT ==========");
        for (int i = 0; i < storedMessages.size(); i++) {
            System.out.println("Message Hash: " + storedHashes.get(i));
            System.out.println("Recipient: " + storedRecipients.get(i));
            System.out.println("Message: " + storedMessages.get(i));
            System.out.println("------------------------");
        }
        System.out.println("=================================================");
    }

    // Helper method to generate a random 10-digit Message ID
    private String generateMessageID() {
        Random rnd = new Random();
        long number = 1000000000L + (long) (rnd.nextDouble() * 9000000000L);
        return String.valueOf(number);
    }

    // Getters
    public String getMessageID() {
        return messageID;
    }

    public String getMessageHash() {
        return messageHash;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getMessageText() {
        return messageText;
    }

    public int getMessageNumber() {
        return messageNumber;
    }

    public String getStatus() {
        return status;
    }
}