package domain;

import java.util.Objects;
import java.util.Random;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

public class DiceRoll {

    private final Random random;

    @SuppressFBWarnings(value = "EI_EXPOSE_REP2",
            justification = "Random instance is intentionally shared so callers can pass a seeded Random for deterministic testing.")
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
