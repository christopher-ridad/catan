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

    @Test
    void UpdateLongestRoad_WithNegativeRoadLength_ThrowsIllegalArgumentException() {
        SpecialCardTracker tracker = new SpecialCardTracker();
        Player player = new Player("Alice", PlayerColor.RED);
        assertThrows(IllegalArgumentException.class, () -> tracker.updateLongestRoad(player, -1));
    }

    @Test
    void UpdateLongestRoad_WithZeroRoadLength_RemainsUnclaimed() {
        SpecialCardTracker tracker = new SpecialCardTracker();
        Player player = new Player("Alice", PlayerColor.RED);
        assertDoesNotThrow(() -> tracker.updateLongestRoad(player, 0));
        assertTrue(tracker.getLongestRoadHolder().isEmpty());
        assertEquals(0, tracker.getLongestRoadLength());
    }

    @Test
    void UpdateLongestRoad_WithRoadLengthBelowClaimThreshold_RemainsUnclaimed() {
        SpecialCardTracker tracker = new SpecialCardTracker();
        Player player = new Player("Alice", PlayerColor.RED);
        tracker.updateLongestRoad(player, 4);
        assertTrue(tracker.getLongestRoadHolder().isEmpty());
        assertEquals(0, tracker.getLongestRoadLength());
    }

    @Test
    void UpdateLongestRoad_WithRoadLengthAtClaimThreshold_ClaimsCard() {
        SpecialCardTracker tracker = new SpecialCardTracker();
        Player player = new Player("Alice", PlayerColor.RED);
        tracker.updateLongestRoad(player, 5);
        assertEquals(player, tracker.getLongestRoadHolder().orElseThrow());
        assertEquals(5, tracker.getLongestRoadLength());
    }

    @Test
    void UpdateLongestRoad_WhenCandidateTiesCurrentHolder_DoesNotSteal() {
        SpecialCardTracker tracker = new SpecialCardTracker();
        Player holder = new Player("Alice", PlayerColor.RED);
        Player challenger = new Player("Bob", PlayerColor.BLUE);
        tracker.updateLongestRoad(holder, 6);
        tracker.updateLongestRoad(challenger, 6);
        assertEquals(holder, tracker.getLongestRoadHolder().orElseThrow());
        assertEquals(6, tracker.getLongestRoadLength());
    }

    @Test
    void UpdateLongestRoad_WhenCandidateExceedsCurrentHolderByOne_StealsCard() {
        SpecialCardTracker tracker = new SpecialCardTracker();
        Player holder = new Player("Alice", PlayerColor.RED);
        Player challenger = new Player("Bob", PlayerColor.BLUE);
        tracker.updateLongestRoad(holder, 5);
        tracker.updateLongestRoad(challenger, 6);
        assertEquals(challenger, tracker.getLongestRoadHolder().orElseThrow());
        assertEquals(6, tracker.getLongestRoadLength());
    }

    @Test
    void UpdateLongestRoad_WhenSameHolderIncreasesRoadLength_UpdatesLength() {
        SpecialCardTracker tracker = new SpecialCardTracker();
        Player holder = new Player("Alice", PlayerColor.RED);
        tracker.updateLongestRoad(holder, 5);
        tracker.updateLongestRoad(holder, 7);
        assertEquals(holder, tracker.getLongestRoadHolder().orElseThrow());
        assertEquals(7, tracker.getLongestRoadLength());
    }

    @Test
    void HoldsLongestRoad_BeforeClaim_ReturnsFalse() {
        SpecialCardTracker tracker = new SpecialCardTracker();
        Player player = new Player("Alice", PlayerColor.RED);
        assertFalse(tracker.holdsLongestRoad(player));
    }

    @Test
    void HoldsLongestRoad_AfterClaim_ReturnsTrueForHolderAndFalseForOthers() {
        SpecialCardTracker tracker = new SpecialCardTracker();
        Player holder = new Player("Alice", PlayerColor.RED);
        Player other = new Player("Bob", PlayerColor.BLUE);
        tracker.updateLongestRoad(holder, 5);
        assertTrue(tracker.holdsLongestRoad(holder));
        assertFalse(tracker.holdsLongestRoad(other));
    }
}
