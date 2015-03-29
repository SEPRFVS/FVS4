package gameLogic;

import java.util.Random;

public class RandomSingleton {
    private static Random random;

    public static Random getRandom() {
        return random;
    }

    public static void setFromSeed(long seed) {
        System.out.println("Using seed: " + seed);
        random = new Random(seed);
    }
}
