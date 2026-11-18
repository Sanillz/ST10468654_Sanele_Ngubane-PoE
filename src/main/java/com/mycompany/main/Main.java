package com.mycompany.main;

// Imports Scanner so we can get user input
import java.util.Scanner;

// Creates the Main class
public class Main {

    // Creates Scanner for user input
    static Scanner input = new Scanner(System.in);

    // Stores the user's entered details
    static String username;
    static String password;
    static String CellPhone;

    // Stores the successfully registered details
    static String registeredUsername;
    static String registeredPassword;
    static String registeredCellPhone;

    // Creates the username checking method
    public static boolean checkUserName(String username) {
        // Username must have 5 characters or less and contain an underscore
        if (username.length() <= 5 && username.contains("_")) {
            return true;
        } else {
            return false;
        }
    }

    // Create the Password checking method
    public static boolean checkPasswordComplexity(String password) {
        if (password.length() >= 8
                && password.matches(".*[A-Z].*")
                && password.matches(".*[a-z].*")
                && password.matches(".*[0-9].*")
                && password.matches(".*[^a-zA-Z0-9].*")) {
            return true;
        } else {
            return false;
        }
    }

    // Create the cellphone checking method
    public static boolean checkCellPhoneNumber(String cellphone) {
        if (cellphone.matches("^\\+27[0-9]{9}$")) {
            return true;
        } else {
            return false;
        }
    }

    // Create the registration method
    public static void registerUser() {

        // ===== Username loop =====
        boolean usernameCorrect = false;
        while (!usernameCorrect) {
            System.out.print("Enter your username: ");
            username = input.nextLine();

            usernameCorrect = checkUserName(username);

            if (usernameCorrect) {
                System.out.println("Username successfully captured.");
            } else {
                System.out.println("Username is not correctly formatted, please ensure that your username contains an underscore and is no more than five characters in length.");
            }
        }

        // ===== Password loop =====
        boolean passwordCorrect = false;
        while (!passwordCorrect) {
            System.out.print("Enter your password: ");
            password = input.nextLine();

            passwordCorrect = checkPasswordComplexity(password);

            if (passwordCorrect) {
                System.out.println("Password successfully captured.");
            } else {
                System.out.println("Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.");
            }
        }

        // ===== Cellphone loop =====
        boolean phoneCorrect = false;
        while (!phoneCorrect) {
            System.out.print("Enter your cellPhone number (+27): ");
            CellPhone = input.nextLine();

            phoneCorrect = checkCellPhoneNumber(CellPhone);

            if (phoneCorrect) {
                System.out.println("Cell phone number successfully captured.");
            } else {
                System.out.println("Cell phone number incorrectly formatted or does not contain international code.");
            }
        }

        // Assign the values after the three loops
        registeredUsername = username;
        registeredPassword = password;
        registeredCellPhone = CellPhone;

        System.out.println("User registered successfully.");
    }

    // Creates the login method
    public static boolean loginUser(String username, String password) {
        if (username.equals(registeredUsername)
                && password.equals(registeredPassword)) {
            return true;
        } else {
            return false;
        }
    }

    // Creates the login status method
    public static String returnLoginStatus(boolean loginSuccessful) {
        if (loginSuccessful) {
            return "Welcome " + registeredUsername + ", it is great to see you again.";
        } else {
            return "Username or password incorrect, please try again.";
        }
    }

    // ========== MAIN MENU (now with option 4) ==========
    public static void showMainMenu() {
        System.out.println("\nWelcome to QuickChat.");

        boolean running = true;

        while (running) {
            System.out.println("\nPlease choose an option:");
            System.out.println("1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Quit");
            System.out.println("4) Stored Messages");
            System.out.print("Enter choice: ");

            String choice = input.nextLine().trim();

            if (choice.equals("1")) {
                sendMessages();
            } else if (choice.equals("2")) {
                System.out.println("Coming Soon.");
            } else if (choice.equals("3")) {
                System.out.println("Goodbye!");
                running = false;
            } else if (choice.equals("4")) {
                storedMessagesMenu();
            } else {
                System.out.println("Invalid option. Please choose 1, 2, 3 or 4.");
            }
        }
    }

    // ========== PART 3 – Stored Messages sub-menu ==========
    public static void storedMessagesMenu() {
        // Load any previously stored messages from the JSON file
        Message.loadStoredMessagesFromJSON();

        boolean back = false;

        while (!back) {
            System.out.println("\n----- Stored Messages Menu -----");
            System.out.println("a) Display recipient and message of all stored messages");
            System.out.println("b) Display the longest stored message");
            System.out.println("c) Search for a message by Message ID");
            System.out.println("d) Search for messages by recipient");
            System.out.println("e) Delete a message using the message hash");
            System.out.println("f) Display full report of all stored messages");
            System.out.println("g) Back to main menu");
            System.out.print("Enter choice: ");

            String choice = input.nextLine().trim().toLowerCase();

            if (choice.equals("a")) {
                Message.displayStoredSendersAndRecipients();
            } else if (choice.equals("b")) {
                System.out.println("\nLongest stored message:");
                System.out.println(Message.displayLongestStoredMessage());
            } else if (choice.equals("c")) {
                System.out.print("Enter Message ID to search: ");
                String id = input.nextLine().trim();
                System.out.println(Message.searchByMessageID(id));
            } else if (choice.equals("d")) {
                System.out.print("Enter recipient number: ");
                String recipient = input.nextLine().trim();
                System.out.println(Message.searchByRecipient(recipient));
            } else if (choice.equals("e")) {
                System.out.print("Enter Message Hash to delete: ");
                String hash = input.nextLine().trim();
                System.out.println(Message.deleteByHash(hash));
            } else if (choice.equals("f")) {
                Message.displayFullReport();
            } else if (choice.equals("g")) {
                back = true;
            } else {
                System.out.println("Invalid option. Please choose a–g.");
            }
        }
    }

    // Handles the sending of messages
    public static void sendMessages() {
        System.out.print("How many messages do you wish to enter? ");
        int numMessages = 0;

        try {
            numMessages = Integer.parseInt(input.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid number. Returning to menu.");
            return;
        }

        // For loop to allow the user to enter the set number of messages
        for (int i = 1; i <= numMessages; i++) {
            System.out.println("\n--- Message " + i + " of " + numMessages + " ---");

            // Ask for recipient
            System.out.print("Enter recipient cell number (+...): ");
            String recipient = input.nextLine().trim();

            // Ask for message text and check length
            String messageText = "";
            boolean messageOk = false;

            while (!messageOk) {
                System.out.print("Enter your message (max 250 characters): ");
                messageText = input.nextLine();

                if (messageText.length() > 250) {
                    int excess = messageText.length() - 250;
                    System.out.println("Message exceeds 250 characters by " + excess + "; please reduce the size.");
                } else {
                    System.out.println("Message ready to send.");
                    messageOk = true;
                }
            }

            // Create the Message object
            Message msg = new Message(i, recipient, messageText);

            // Check the recipient cell number
            String cellResult = msg.checkRecipientCell();
            System.out.println(cellResult);

            // If the cell number is wrong, skip this message
            if (!cellResult.equals("Cell phone number successfully captured.")) {
                System.out.println("Skipping this message because of invalid recipient.");
                continue;
            }

            // Check Message ID
            if (msg.checkMessageID()) {
                System.out.println("Message ID generated: " + msg.getMessageID());
            } else {
                System.out.println("Message ID is invalid.");
                continue;
            }

            // Show the Message Hash
            System.out.println("Message Hash: " + msg.getMessageHash());

            // Let the user choose Send / Disregard / Store
            String result = msg.SentMessage();
            System.out.println(result);

            // If the message was sent, display the full details
            if (result.equals("Message successfully sent.")) {
                System.out.println("\n----- Message Details -----");
                System.out.println("Message ID: " + msg.getMessageID());
                System.out.println("Message Hash: " + msg.getMessageHash());
                System.out.println("Recipient: " + msg.getRecipient());
                System.out.println("Message: " + msg.getMessageText());
            }
        }

        // After all messages have been entered
        System.out.println("\nTotal number of messages sent: " + Message.returnTotalMessages());
        System.out.println("\nAll messages so far:\n" + Message.printMessages());
    }

    // Main method where the program starts
    public static void main(String[] args) {

        // First register the user
        registerUser();

        // Now ask for login details
        System.out.print("Enter username to login: ");
        String loginUsername = input.nextLine();

        System.out.print("Enter password to login: ");
        String loginPassword = input.nextLine();

        // Use loginUser(loginUsername, loginPassword)
        boolean loginSuccessful = loginUser(loginUsername, loginPassword);

        // Call login status
        System.out.println(returnLoginStatus(loginSuccessful));

        // Only show the menu if the user logged in successfully
        if (loginSuccessful) {
            showMainMenu();
        }
    }
}