git package domain;

import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class DiceRollTest {

    @Test
    void constructor_withNullRandom_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new DiceRoll(null));
    }

    @Test
    void constructor_withValidRandom_doesNotThrow() {
        assertDoesNotThrow(() -> new DiceRoll(new Random()));
    }

    @Test
    void roll_returnsValueAtLeast2() {
        DiceRoll dice = new DiceRoll(new Random());
        for (int i = 0; i < 1000; i++) {
            assertTrue(dice.roll() >= 2);
        }
    }

    @Test
    void roll_returnsValueAtMost12() {
        DiceRoll dice = new DiceRoll(new Random());
        for (int i = 0; i < 1000; i++) {
            assertTrue(dice.roll() <= 12);
        }
    }

    @Test
    void roll_canReturnMinimumSum() {
        Random mockRandom = EasyMock.createMock(Random.class);
        EasyMock.expect(mockRandom.nextInt(6)).andReturn(0).andReturn(0);
        EasyMock.replay(mockRandom);

        DiceRoll dice = new DiceRoll(mockRandom);
        assertEquals(2, dice.roll());

        EasyMock.verify(mockRandom);
    }

    @Test
    void roll_canReturnMaximumSum() {
        Random mockRandom = EasyMock.createMock(Random.class);
        EasyMock.expect(mockRandom.nextInt(6)).andReturn(5).andReturn(5);
        EasyMock.replay(mockRandom);

        DiceRoll dice = new DiceRoll(mockRandom);
        assertEquals(12, dice.roll());

        EasyMock.verify(mockRandom);
    }

    @Test
    void roll_canReturn7() {
        Random mockRandom = EasyMock.createMock(Random.class);
        EasyMock.expect(mockRandom.nextInt(6)).andReturn(2).andReturn(4);
        EasyMock.replay(mockRandom);

        DiceRoll dice = new DiceRoll(mockRandom);
        assertEquals(7, dice.roll());

        EasyMock.verify(mockRandom);
    }

    @Test
    void roll_canReturnNon7Value() {
        Random mockRandom = EasyMock.createMock(Random.class);
        EasyMock.expect(mockRandom.nextInt(6)).andReturn(1).andReturn(2);
        EasyMock.replay(mockRandom);

        DiceRoll dice = new DiceRoll(mockRandom);
        assertEquals(5, dice.roll());

        EasyMock.verify(mockRandom);
    }
}
