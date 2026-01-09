package game;

public class Move {

    public Pawn pawn;
    public int from;
    public int to;
    public boolean isExit;
    public boolean isSwap;

    public Move(Pawn pawn, int from, int to) {
        this.pawn = pawn;
        this.from = from;
        this.to = to;
        this.isExit = false;
        this.isSwap = false;
    }

    public String toString() {
        if (isExit) {
            return "Square " + (from + 1) + " -> EXIT";
        }
        String extra = isSwap ? " (swap)" : "";
        return "Square " + (from + 1) + " -> " + (to + 1) + extra;
    }
}