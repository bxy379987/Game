package comp1110.ass2;

import java.util.Random;

public class Dice {
    Random rand;
    int[] diceScore;
    Dice () {
        rand = new Random();
        diceScore = new int[]{1, 2, 2, 3, 3, 4};
    }

    public int rollDice() {
        return  diceScore[rand.nextInt(6)];
    }

}
