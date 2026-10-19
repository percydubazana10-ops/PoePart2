/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package poepart2.pkg1;

import java.util.Scanner;

public class PartTwo {

    private static final Scanner in = new Scanner(System.in);

    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(in.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number.");
            }
        }
    }

    public static void main(String[] args) {

        Registration reg = new Registration();
        String u, p, c;
        do {
            System.out.print("Register - username: ");
            u = in.nextLine();
            System.out.print("Register - password: ");
            p = in.nextLine();
            System.out.print("Register - cell number: ");
            c = in.nextLine();
            if (!reg.registerStatus(u, p, c)) {
                System.out.println("Registration details are invalid, please try again.\n");
            }
        } while (!reg.registerStatus(u, p, c));
        System.out.println(reg.registerUser(u, p, c));

        boolean loggedIn = false;
        while (!loggedIn) {
            System.out.print("\nLogin - username: ");
            String lu = in.nextLine();
            System.out.print("Login - password: ");
            String lp = in.nextLine();
            loggedIn = lu.equals(reg.userName) && lp.equals(reg.passWord);
            System.out.println(loggedIn ? "Login successful." : "Login failed, please try again.");
        }

        System.out.println("\nWelcome to QuickChat.");

        Message messages = new Message(in);
        int limit;
        do {
            limit = readInt("How many messages do you wish to enter? ");
        } while (limit < 1);
        messages.setMessageLimit(limit);

        int choice = 0;
        while (choice != 3) {
            System.out.println("\n1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Quit");
            choice = readInt("Choose an option: ");

            switch (choice) {
                case 1 -> messages.sendMessages();
                case 2 -> System.out.println("Coming Soon.");
                case 3 -> System.out.println("Goodbye.");
                default -> System.out.println("Invalid option, please try again.");
            }
        }
    }
}