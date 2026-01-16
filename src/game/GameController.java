package game;

import search.Expectiminimax;
import search.SearchResult;
import type.PlayerType;

import java.util.List;
import java.util.Scanner;

public class GameController {

    public Board board;
    public Player player1;
    public Player player2;
    public Player current;
    public Stick stick;
    public Scanner scanner;
    public int depth;
    public boolean debug;

    public GameController(PlayerType t1, PlayerType t2, int depth, boolean debug) {
        this.player1 = new Player(1, t1);
        this.player2 = new Player(2, t2);
        this.board = new Board(player1, player2);
        this.current = player1;
        this.stick = new Stick();
        this.scanner = new Scanner(System.in);
        this.depth = depth;
        this.debug = debug;
    }

    public void start() {
        System.out.println("Game started!");
        System.out.println("P1: " + player1.type);
        System.out.println("P2: " + player2.type);
        System.out.println("Depth: " + depth);

        board.print();

        while (true) {
            playTurn();

            // check win after each turn
            if (player1.exited == Board.PAWNS) {
                System.out.println("\n=== GAME OVER ===");
                System.out.println("Player 1 (O) WINS!");
                break;
            }

            if (player2.exited == Board.PAWNS) {
                System.out.println("\n=== GAME OVER ===");
                System.out.println("Player 2 (X) WINS!");
                break;
            }

            // switch player
            if (current.id == 1) current = player2;
            else current = player1;
        }
    }

    private void playTurn() {
        System.out.println("\n" + current + "'s turn");

        int roll = stick.roll();
        System.out.println("Roll: " + roll);

        List<Move> moves = board.getValidMoves(current, roll);

        if (moves.isEmpty()) {
            System.out.println("No moves");
            board.punishWaiting(current, roll, null);
            board.print();
            return;
        }

        Move chosen;

        if (current.type == PlayerType.HUMAN) {
            System.out.println("Moves:");
            for (int i = 0; i < moves.size(); i++) {
                System.out.println((i + 1) + ". " + moves.get(i));
            }

            System.out.print("Choose: ");
            int choice = scanner.nextInt();
            while (choice < 1 || choice > moves.size()) {
                System.out.println("Enter 1-" + moves.size());
                System.out.print("Choose: ");
                choice = scanner.nextInt();
            }
            chosen = moves.get(choice - 1);
        } else {
            System.out.println("Thinking...");
            Expectiminimax ai = new Expectiminimax(depth, debug);
            SearchResult result = ai.search(board, current, roll);

            if (debug) {
                System.out.println("Nodes: " + result.nodes);
                System.out.println("Score: " + result.score);
            }
            chosen = result.bestMove;
        }

        if (chosen != null) {
            board.doMove(chosen);
            System.out.println("Played: " + chosen);
            board.punishWaiting(current, roll, chosen);
        }

        board.print();
    }
}