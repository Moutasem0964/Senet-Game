package game;

import java.util.Random;

public class Stick {

    public static Random rand = new Random();

    public static double[] PROB = {4.0/16, 6.0/16, 4.0/16, 1.0/16, 1.0/16};

    public int result;

    public int roll() {
        int dark = 0;
        for (int i = 0; i < 4; i++) {
            if (rand.nextBoolean()) dark++;
        }

        if (dark == 0) result = 5;
        else result = dark;

        return result;
    }
}