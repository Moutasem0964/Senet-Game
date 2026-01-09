package game;

public class Pawn {

    public Player owner;
    public int position;
    public int waitingFor;

    public Pawn(Player owner, int position) {
        this.owner = owner;
        this.position = position;
        this.waitingFor = 0;
    }
}