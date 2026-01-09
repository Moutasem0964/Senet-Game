package game;

import type.SquareType;

public class Square {

    public int index;
    public SquareType type;
    public Pawn occupant;

    public Square(int index) {
        this.index = index;
        this.occupant = null;

        if (index == 14) this.type = SquareType.REBIRTH;
        else if (index == 25) this.type = SquareType.HAPPINESS;
        else if (index == 26) this.type = SquareType.WATER;
        else if (index == 27) this.type = SquareType.THREE_TRUTHS;
        else if (index == 28) this.type = SquareType.RE_ATOUM;
        else if (index == 29) this.type = SquareType.HORUS;
        else this.type = SquareType.NORMAL;
    }
}