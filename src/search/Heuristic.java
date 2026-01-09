package search;

import game.Board;
import game.Pawn;
import game.Player;

public class Heuristic {

    public double evaluate(Board board, Player computer) {
        Player opponent;
        if (computer.id == 1) opponent = board.player2;
        else opponent = board.player1;

        double score = 0;

        score += computer.exited * 100;
        score -= opponent.exited * 100;

        for (Pawn p : computer.pawns) {
            if (p.position != -1) score += p.position * 2;
        }

        for (Pawn p : opponent.pawns) {
            if (p.position != -1) score -= p.position * 2;
        }

        return score;
    }
}