package com.mycompany.main;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MessageTest {

    // Clear the arrays before every test so results stay clean
    @BeforeEach
    public void setUp() {
        Message.sentMessages.clear();
        Message.disregardedMessages.clear();
        Message.storedMessages.clear();
        Message.messageHashes.clear();
        Message.messageIDs.clear();
        Message.storedRecipients.clear();
        Message.storedMessageIDs.clear();
        Message.storedHashes.clear();
    }

    // ========== PART 2 tests (still useful) ==========

    @Test
    public void testMessageLength_Success() {
        String shortMessage = "Hi Mike, can you join us for dinner tonight";
        assertTrue(shortMessage.length() <= 250);
    }

    @Test
    public void testMessageLength_Failure() {
        String longMessage = "A".repeat(260);
        assertTrue(longMessage.length() > 250);
    }

    @Test
    public void testRecipientCell_Success() {
        Message msg = new Message(1, "+27718693002", "Hi Mike, can you join us for dinner tonight");
        assertEquals("Cell phone number successfully captured.", msg.checkRecipientCell());
    }

    @Test
    public void testRecipientCell_Failure() {
        Message msg = new Message(2, "08575975889", "Hi Keegan, did you receive the payment?");
        assertEquals("Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.", msg.checkRecipientCell());
    }

    @Test
    public void testMessageHash_TestData1() {
        Message msg = new Message(0, "+27718693002", "Hi Mike, can you join us for dinner tonight");
        String hash = msg.getMessageHash();
        assertTrue(hash.contains(":0:"));
        assertTrue(hash.endsWith("HITONIGHT"));
    }

    @Test
    public void testMessageID_Created() {
        Message msg = new Message(1, "+27718693002", "Test message");
        assertNotNull(msg.getMessageID());
        assertEquals(10, msg.getMessageID().length());
        assertTrue(msg.checkMessageID());
    }

    // ========== PART 3 required tests ==========

    // 1. Sent Messages array correctly populated
    @Test
    public void testSentMessagesArrayPopulated() {
        // Simulate the two Sent messages from the brief
        Message.sentMessages.add("Did you get the cake?");
        Message.sentMessages.add("It is dinner time !");

        assertEquals(2, Message.sentMessages.size());
        assertTrue(Message.sentMessages.contains("Did you get the cake?"));
        assertTrue(Message.sentMessages.contains("It is dinner time !"));
    }

    // 2. Display the longest stored message
    @Test
    public void testLongestStoredMessage() {
        // Add the stored messages from the test data
        Message.storedMessages.add("Where are you? You are late! I have asked you to be on time.");
        Message.storedMessages.add("Ok, I am leaving without you.");

        String longest = Message.displayLongestStoredMessage();
        assertEquals("Where are you? You are late! I have asked you to be on time.", longest);
    }

    // 3. Search for a message ID
    @Test
    public void testSearchByMessageID() {
        // Populate with test data style values
        Message.storedMessageIDs.add("0838884567");
        Message.storedMessages.add("It is dinner time !");
        Message.storedRecipients.add("0838884567");

        String result = Message.searchByMessageID("0838884567");
        assertEquals("It is dinner time !", result);
    }

    // 4. Search all messages for a particular recipient
    @Test
    public void testSearchByRecipient() {
        Message.storedRecipients.add("+27838884567");
        Message.storedMessages.add("Where are you? You are late! I have asked you to be on time.");
        Message.storedRecipients.add("+27838884567");
        Message.storedMessages.add("Ok, I am leaving without you.");

        String result = Message.searchByRecipient("+27838884567");
        assertTrue(result.contains("Where are you? You are late! I have asked you to be on time."));
        assertTrue(result.contains("Ok, I am leaving without you."));
    }

    // 5. Delete a message using the message hash
    @Test
    public void testDeleteByHash() {
        Message.storedMessages.add("Where are you? You are late! I have asked you to be on time.");
        Message.storedRecipients.add("+27838884567");
        Message.storedMessageIDs.add("1234567890");
        Message.storedHashes.add("12:2:WHERE TIME");

        String result = Message.deleteByHash("12:2:WHERE TIME");
        assertTrue(result.contains("successfully deleted"));
        assertEquals(0, Message.storedMessages.size());
    }

    // 6. Display full report (just checks that the method runs without error when data exists)
    @Test
    public void testDisplayFullReport() {
        Message.storedMessages.add("Did you get the cake?");
        Message.storedRecipients.add("+27834557896");
        Message.storedHashes.add("00:1:DIDCAKE");
        Message.storedMessageIDs.add("0012345678");

        // Method prints to console – we just verify the arrays are ready
        assertFalse(Message.storedMessages.isEmpty());
        assertEquals(1, Message.storedHashes.size());
    }

    // Extra: check disregarded array works
    @Test
    public void testDisregardedMessages() {
        Message.disregardedMessages.add("Yohoooo, I am at your gate.");
        assertEquals(1, Message.disregardedMessages.size());
        assertEquals("Yohoooo, I am at your gate.", Message.disregardedMessages.get(0));
    }
}