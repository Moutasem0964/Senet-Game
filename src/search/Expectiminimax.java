package search;

import game.Board;
import game.Move;
import game.Pawn;
import game.Player;
import game.Stick;

import java.util.List;

public class Expectiminimax {

    public int maxDepth;
    public boolean debug;
    public int nodes;
    public Heuristic heuristic;
    public Player computer;

    public Expectiminimax(int depth, boolean debug) {
        this.maxDepth = depth;
        this.debug = debug;
        this.heuristic = new Heuristic();
    }

    public SearchResult search(Board board, Player player, int roll) {
        nodes = 0;
        computer = player;

        List<Move> moves = board.getValidMoves(player, roll);

        if (moves.isEmpty()) {
            return new SearchResult(null, 0, 0);
        }

        Move best = null;
        double bestVal = Double.NEGATIVE_INFINITY;

        for (Move move : moves) {
            Board copy = board.copy();
            applyMove(copy, move);

            double val = chance(copy, maxDepth - 1, false);

            if (debug) {
                System.out.println("Move: " + move + " -> " + val);
            }

            if (val > bestVal) {
                bestVal = val;
                best = move;
            }
        }

        return new SearchResult(best, bestVal, nodes);
    }

    private double chance(Board board, int depth, boolean isMax) {
        nodes++;

        if (depth <= 0 || board.player1.exited == 7 || board.player2.exited == 7) {
            return heuristic.evaluate(board, computer);
        }

        double expected = 0;
        int[] rolls = {1, 2, 3, 4, 5};

        for (int roll : rolls) {
            double prob = Stick.PROB[roll - 1];
            double val;

            if (isMax) val = max(board, depth, roll);
            else val = min(board, depth, roll);

            expected += prob * val;
        }

        return expected;
    }

    private double max(Board board, int depth, int roll) {
        nodes++;

        Player player;
        if (computer.id == 1) player = board.player1;
        else player = board.player2;

        List<Move> moves = board.getValidMoves(player, roll);

        if (moves.isEmpty()) {
            return chance(board, depth - 1, false);
        }

        double maxVal = Double.NEGATIVE_INFINITY;

        for (Move move : moves) {
            Board copy = board.copy();
            applyMove(copy, move);
            double val = chance(copy, depth - 1, false);
            if (val > maxVal) maxVal = val;
        }

        return maxVal;
    }

    private double min(Board board, int depth, int roll) {
        nodes++;

        Player opponent;
        if (computer.id == 1) opponent = board.player2;
        else opponent = board.player1;

        List<Move> moves = board.getValidMoves(opponent, roll);

        if (moves.isEmpty()) {
            return chance(board, depth - 1, true);
        }

        double minVal = Double.POSITIVE_INFINITY;

        for (Move move : moves) {
            Board copy = board.copy();
            applyMove(copy, move);
            double val = chance(copy, depth - 1, true);
            if (val < minVal) minVal = val;
        }

        return minVal;
    }

    private void applyMove(Board board, Move move) {
        Player player;
        if (move.pawn.owner.id == 1) player = board.player1;
        else player = board.player2;

        for (Pawn pawn : player.pawns) {
            if (pawn.position == move.from) {
                Move m = new Move(pawn, move.from, move.to);
                m.isExit = move.isExit;
                m.isSwap = move.isSwap;
                board.doMove(m);
                break;
            }
        }
    }
}