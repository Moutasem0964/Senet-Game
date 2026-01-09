package search;

import game.Move;

public class SearchResult {

    public Move bestMove;
    public double score;
    public int nodes;

    public SearchResult(Move move, double score, int nodes) {
        this.bestMove = move;
        this.score = score;
        this.nodes = nodes;
    }
}