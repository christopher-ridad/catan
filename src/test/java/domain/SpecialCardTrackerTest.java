package domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SpecialCardTrackerTest {

    @Test
    void Constructor_InitialState_LongestRoadHolderIsEmpty() {
        SpecialCardTracker tracker = new SpecialCardTracker();
        assertTrue(tracker.getLongestRoadHolder().isEmpty());
    }

    @Test
    void Constructor_InitialState_LongestRoadLengthIsZero() {
        SpecialCardTracker tracker = new SpecialCardTracker();
        assertEquals(0, tracker.getLongestRoadLength());
    }

    @Test
    void Constructor_InitialState_LargestArmyHolderIsEmpty() {
        SpecialCardTracker tracker = new SpecialCardTracker();
        assertTrue(tracker.getLargestArmyHolder().isEmpty());
    }

    @Test
    void Constructor_InitialState_LargestArmySizeIsZero() {
        SpecialCardTracker tracker = new SpecialCardTracker();
        assertEquals(0, tracker.getLargestArmySize());
    }
}
