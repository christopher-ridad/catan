package domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class SetupPhaseTest {
    @Test
    public void constructor_nullGame_throwsIllegalArgument(){
        Game game = null;

        assertThrows(IllegalArgumentException.class, () -> new SetupPhase(game));
    }
}
