package domain;

import java.util.Objects;
import java.util.Random;

public class DiceRoll {

    private final Random random;

    public DiceRoll(Random random) {
        Objects.requireNonNull(random, "Random cannot be null");
        this.random = random;
    }

    public int roll() {
        int die1 = random.nextInt(6) + 1;
        int die2 = random.nextInt(6) + 1;
        return die1 + die2;
    }
}
