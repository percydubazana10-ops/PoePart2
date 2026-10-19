/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package poepart2.pkg1;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;

public class Message {

    public static final String SENT = "Message successfully sent.";
    public static final String DELETE_PROMPT = "Press 0 to delete the message.";
    public static final String STORED = "Message successfully stored.";
    private static final String JSON_FILE = "messages.json";

    private final ArrayList<String> messageIds = new ArrayList<>();
    private final ArrayList<String> messageHashes = new ArrayList<>();
    private final ArrayList<String> recipients = new ArrayList<>();
    private final ArrayList<String> texts = new ArrayList<>();
    private final ArrayList<String> storedJson = new ArrayList<>();

    private final HashSet<String> usedIds = new HashSet<>();
    private final Random random = new Random();
    private final Scanner input;

    private int totalSent = 0;
    private int messageLimit = 0;
    private int entered = 0;

    public Message() {
        this(new Scanner(System.in));
    }

    public Message(Scanner input) {
        this.input = input;
    }

    public void setMessageLimit(int limit) {
        this.messageLimit = limit;
    }

    private int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = input.nextLine().trim();
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number.");
            }
        }
    }

    public String generateMessageID() {
        String id;
        do {
            long number = 1000000000L + random.nextLong(9000000000L);
            id = String.valueOf(number);
        } while (usedIds.contains(id));
        usedIds.add(id);
        return id;
    }

    public boolean checkMessageID(String messageID) {
        return messageID != null && !messageID.isEmpty() && messageID.length() <= 10;
    }

    public String checkRecipientCell(String cell) {
        if (cell != null && cell.matches("^\\+[0-9]{10,13}$")) {
            return "Cell phone number successfully captured.";
        }
        return "Cell phone number is incorrectly formatted or does not contain an international code. "
                + "Please correct the number and try again.";
    }

    public String checkMessageLength(String text) {
        if (text.length() > 250) {
            return "Message exceeds 250 characters by " + (text.length() - 250)
                    + ", please reduce the size.";
        }
        return "Message ready to send.";
    }

    public String createMessageHash(String messageID, int messageNumber, String messageText) {
        String prefix = messageID.length() >= 2 ? messageID.substring(0, 2) : messageID;
        String[] words = messageText.trim().split("\\s+");
        String firstWord = words[0].replaceAll("[^A-Za-z0-9]", "");
        String lastWord = words[words.length - 1].replaceAll("[^A-Za-z0-9]", "");
        return (prefix + ":" + messageNumber + ":" + firstWord + lastWord).toUpperCase();
    }

    public String sentMessage(int choice) {
        switch (choice) {
            case 1:
                return SENT;
            case 2:
                return DELETE_PROMPT;
            case 3:
                return STORED;
            default:
                return "Invalid option.";
        }
    }

    public String sentMessage() {
        while (true) {
            System.out.println("\nChoose what you want to do with this message:");
            System.out.println("1) Send Message");
            System.out.println("2) Disregard Message");
            System.out.println("3) Store Message to send later");
            int choice = readInt("Enter your choice: ");

            if (choice < 1 || choice > 3) {
                System.out.println("Invalid option, please try again.");
                continue;
            }
            String result = sentMessage(choice);
            System.out.println(result);

            if (choice == 2) {
                int del;
                do {
                    del = readInt("");
                } while (del != 0);
            }
            return result;
        }
    }

    public void addSentMessage(String id, String hash, String recipient, String text) {
        messageIds.add(id);
        messageHashes.add(hash);
        recipients.add(recipient);
        texts.add(text);
        totalSent++;
    }

    public String printMessages() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < texts.size(); i++) {
            sb.append("\n----------------------------------------");
            sb.append("\nMessage ID:   ").append(messageIds.get(i));
            sb.append("\nMessage Hash: ").append(messageHashes.get(i));
            sb.append("\nRecipient:    ").append(recipients.get(i));
            sb.append("\nMessage:      ").append(texts.get(i));
        }
        return sb.toString();
    }

    public int returnTotalMessages() {
        return totalSent;
    }

    public boolean storeMessage(String id, String hash, String recipient, String text) {
        String json = "  {\n"
                + "    \"messageID\": \"" + esc(id) + "\",\n"
                + "    \"messageHash\": \"" + esc(hash) + "\",\n"
                + "    \"recipient\": \"" + esc(recipient) + "\",\n"
                + "    \"message\": \"" + esc(text) + "\"\n"
                + "  }";
        storedJson.add(json);

        try (FileWriter writer = new FileWriter(JSON_FILE)) {
            writer.write("[\n" + String.join(",\n", storedJson) + "\n]\n");
            return true;
        } catch (IOException e) {
            System.out.println("Could not write " + JSON_FILE + ": " + e.getMessage());
            return false;
        }
    }

    private String esc(String s) {
        return s.replace("\\", "\\\\").replace("\"", "\\\"")
                .replace("\n", "\\n").replace("\r", "\\r");
    }

    private String readRecipient() {
        while (true) {
            System.out.print("Enter recipient cell number (with international code, e.g. +27718693002): ");
            String cell = input.nextLine().trim();
            String result = checkRecipientCell(cell);
            System.out.println(result);
            if (result.equals("Cell phone number successfully captured.")) {
                return cell;
            }
        }
    }

    private String readMessageText() {
        while (true) {
            System.out.print("Enter your message: ");
            String text = input.nextLine();
            if (text.trim().isEmpty()) {
                System.out.println("Message cannot be empty.");
                continue;
            }
            if (text.length() > 250) {
                System.out.println("Please enter a message of less than 250 characters.");
                System.out.println(checkMessageLength(text));
                continue;
            }
            System.out.println(checkMessageLength(text));
            return text;
        }
    }

    public void sendMessages() {
        if (entered >= messageLimit) {
            System.out.println("You have already entered all " + messageLimit + " messages.");
            return;
        }

        while (entered < messageLimit) {
            System.out.println("\nMESSAGE " + (entered + 1) + " of " + messageLimit);

            String recipient = readRecipient();
            String text = readMessageText();
            String id = generateMessageID();
            String hash = createMessageHash(id, entered, text);

            String result = sentMessage();
            entered++;

            if (result.equals(SENT)) {
                addSentMessage(id, hash, recipient, text);
                System.out.println("\nMessage ID:   " + id);
                System.out.println("Message Hash: " + hash);
                System.out.println("Recipient:    " + recipient);
                System.out.println("Message:      " + text);
            } else if (result.equals(STORED)) {
                storeMessage(id, hash, recipient, text);
            } else {
                System.out.println("Message deleted.");
            }
        }

        System.out.println("\n===== Messages sent =====" + printMessages());
        System.out.println("\nTotal messages sent: " + returnTotalMessages());
    }
}