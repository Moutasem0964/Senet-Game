package game;

import type.PlayerType;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== SENET ===\n");

        while (true) {
            System.out.println("1. Human vs Computer");
            System.out.println("2. Computer vs Human");
            System.out.println("3. Computer vs Computer");
            System.out.println("4. Exit");
            System.out.print("Choice: ");

            int choice = scanner.nextInt();

            PlayerType t1, t2;

            switch (choice) {
                case 1:
                    t1 = PlayerType.HUMAN;
                    t2 = PlayerType.COMPUTER;
                    break;
                case 2:
                    t1 = PlayerType.COMPUTER;
                    t2 = PlayerType.HUMAN;
                    break;
                case 3:
                    t1 = PlayerType.COMPUTER;
                    t2 = PlayerType.COMPUTER;
                    break;
                case 4:
                    System.out.println("Bye!");
                    return;
                default:
                    System.out.println("Invalid choice!\n");
                    continue;
            }

            System.out.print("Depth (1-10): ");
            int depth = scanner.nextInt();
            while (depth > 10 || depth < 1) {
                System.out.println("Invalid input");
                System.out.println("Depth (1-10): ");
                depth = scanner.nextInt();
            }

            System.out.print("Debug? (1=yes, 0=no): ");
            int d = scanner.nextInt();
            while (d != 0 && d != 1) {
                System.out.println("Invalid input");
                System.out.print("Debug? (1=yes, 0=no): ");
                d=scanner.nextInt();
            }
            boolean debug = (d == 1);


            GameController game = new GameController(t1, t2, depth, debug);
            game.start();

            System.out.println("\nPress Enter to continue...");
            scanner.nextLine();
            scanner.nextLine();
        }
    }
}