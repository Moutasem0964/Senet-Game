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

            int choice = Integer.parseInt(scanner.nextLine());

            if (choice == 4) {
                System.out.println("Bye!");
                break;
            }

            System.out.print("Depth (1-10): ");
            int depth = Integer.parseInt(scanner.nextLine());

            System.out.print("Debug? (1=yes, 0=no): ");
            int d = Integer.parseInt(scanner.nextLine());
            boolean debug = (d == 1);

            PlayerType t1, t2;

            if (choice == 1) {
                t1 = PlayerType.HUMAN;
                t2 = PlayerType.COMPUTER;
            } else if (choice == 2) {
                t1 = PlayerType.COMPUTER;
                t2 = PlayerType.HUMAN;
            } else {
                t1 = PlayerType.COMPUTER;
                t2 = PlayerType.COMPUTER;
            }

            GameController game = new GameController(t1, t2, depth, debug);
            game.start();

            System.out.println("\nPress Enter...");
            scanner.nextLine();
        }
    }
}