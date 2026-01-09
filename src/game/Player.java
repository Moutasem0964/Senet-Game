package game;

import type.PlayerType;
import java.util.ArrayList;
import java.util.List;

public class Player {

    public int id;
    public PlayerType type;
    public List<Pawn> pawns;
    public int exited;

    public Player(int id, PlayerType type) {
        this.id = id;
        this.type = type;
        this.pawns = new ArrayList<>();
        this.exited = 0;
    }

    public String toString() {
        return "Player " + id;
    }
}