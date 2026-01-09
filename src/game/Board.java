package game;

import type.SquareType;
import java.util.ArrayList;
import java.util.List;

public class Board {

    public static final int SIZE = 30;
    public static final int PAWNS = 7;

    public Square[] squares;
    public Player player1;
    public Player player2;

    public Board(Player p1, Player p2) {
        this.player1 = p1;
        this.player2 = p2;
        this.squares = new Square[SIZE];

        for (int i = 0; i < SIZE; i++) {
            squares[i] = new Square(i);
        }

        for (int i = 0; i < PAWNS; i++) {
            Pawn pawn1 = new Pawn(p1, i * 2);
            Pawn pawn2 = new Pawn(p2, i * 2 + 1);
            p1.pawns.add(pawn1);
            p2.pawns.add(pawn2);
            squares[i * 2].occupant = pawn1;
            squares[i * 2 + 1].occupant = pawn2;
        }
    }

    public List<Move> getValidMoves(Player player, int roll) {
        List<Move> moves = new ArrayList<>();

        for (Pawn pawn : player.pawns) {
            if (pawn.position == -1) continue;

            int from = pawn.position;

            // waiting pawn
            if (pawn.waitingFor != 0) {
                if (pawn.waitingFor == -1 || pawn.waitingFor == roll) {
                    Move m = new Move(pawn, from, -1);
                    m.isExit = true;
                    moves.add(m);
                }
                continue;
            }

            int to = from + roll;

            // off board - check exit
            if (to >= SIZE) {
                if (to == SIZE && from >= 25) {
                    Move m = new Move(pawn, from, -1);
                    m.isExit = true;
                    moves.add(m);
                }
                continue;
            }

            // cant jump over happiness
            if (from < 25 && to > 25) continue;

            // cant land on own pawn
            if (squares[to].occupant != null &&
                    squares[to].occupant.owner.id == player.id) continue;

            Move m = new Move(pawn, from, to);

            // swap
            if (squares[to].occupant != null &&
                    squares[to].occupant.owner.id != player.id) {
                m.isSwap = true;
            }

            moves.add(m);
        }

        return moves;
    }

    public void doMove(Move move) {
        // exit
        if (move.isExit) {
            squares[move.from].occupant = null;
            move.pawn.position = -1;
            move.pawn.waitingFor = 0;
            move.pawn.owner.exited++;
            return;
        }

        int from = move.from;
        int to = move.to;
        Pawn pawn = move.pawn;

        // swap
        if (move.isSwap) {
            Pawn other = squares[to].occupant;
            other.position = from;
            squares[from].occupant = other;
        } else {
            squares[from].occupant = null;
        }

        pawn.position = to;
        squares[to].occupant = pawn;

        // special squares
        SquareType type = squares[to].type;

        if (type == SquareType.WATER) {
            sendToRebirth(pawn);
        }
        else if (type == SquareType.THREE_TRUTHS) {
            pawn.waitingFor = 3;
        }
        else if (type == SquareType.RE_ATOUM) {
            pawn.waitingFor = 2;
        }
        else if (type == SquareType.HORUS) {
            pawn.waitingFor = -1;
        }
    }

    public void sendToRebirth(Pawn pawn) {
        if (pawn.position >= 0 && pawn.position < SIZE) {
            squares[pawn.position].occupant = null;
        }

        if (squares[14].occupant == null) {
            pawn.position = 14;
            squares[14].occupant = pawn;
        } else {
            for (int i = 13; i >= 0; i--) {
                if (squares[i].occupant == null) {
                    pawn.position = i;
                    squares[i].occupant = pawn;
                    break;
                }
            }
        }
        pawn.waitingFor = 0;
    }

    public void punishWaiting(Player player, int roll, Move played) {
        for (Pawn pawn : player.pawns) {
            if (pawn.position == -1) continue;
            if (pawn.waitingFor == 0) continue;
            if (played != null && played.pawn == pawn) continue;

            int pos = pawn.position;
            boolean punish = false;

            if (pos == 29) punish = true;
            else if (pos == 27 && roll != 3) punish = true;
            else if (pos == 28 && roll != 2) punish = true;

            if (punish) sendToRebirth(pawn);
        }
    }

    public Board copy() {
        Player p1 = new Player(player1.id, player1.type);
        Player p2 = new Player(player2.id, player2.type);
        p1.exited = player1.exited;
        p2.exited = player2.exited;

        Board b = new Board();
        b.player1 = p1;
        b.player2 = p2;
        b.squares = new Square[SIZE];

        for (int i = 0; i < SIZE; i++) {
            b.squares[i] = new Square(i);
        }

        for (Pawn pawn : player1.pawns) {
            Pawn p = new Pawn(p1, pawn.position);
            p.waitingFor = pawn.waitingFor;
            p1.pawns.add(p);
            if (p.position >= 0) {
                b.squares[p.position].occupant = p;
            }
        }

        for (Pawn pawn : player2.pawns) {
            Pawn p = new Pawn(p2, pawn.position);
            p.waitingFor = pawn.waitingFor;
            p2.pawns.add(p);
            if (p.position >= 0) {
                b.squares[p.position].occupant = p;
            }
        }

        return b;
    }

    private Board() {}

    public void print() {
        System.out.println();

        for (int i = 0; i <= 9; i++) System.out.print(cell(i) + " ");
        System.out.println();

        for (int i = 19; i >= 10; i--) System.out.print(cell(i) + " ");
        System.out.println();

        for (int i = 20; i <= 29; i++) System.out.print(cell(i) + " ");
        System.out.println();

        System.out.println("\nP1(O): " + player1.exited + "/7");
        System.out.println("P2(X): " + player2.exited + "/7");
    }

    private String cell(int i) {
        if (squares[i].occupant != null) {
            return squares[i].occupant.owner.id == 1 ? "O" : "X";
        }
        SquareType t = squares[i].type;
        if (t == SquareType.REBIRTH) return "R";
        if (t == SquareType.HAPPINESS) return "H";
        if (t == SquareType.WATER) return "W";
        if (t == SquareType.THREE_TRUTHS) return "3";
        if (t == SquareType.RE_ATOUM) return "2";
        if (t == SquareType.HORUS) return "*";
        return ".";
    }
}