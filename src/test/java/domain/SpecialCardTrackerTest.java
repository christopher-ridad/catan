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
    void UpdateLongestRoad_WhenHolderRoadShrinksButStillQualifies_UpdatesLength() {
        SpecialCardTracker tracker = new SpecialCardTracker();
        Player holder = new Player("Alice", PlayerColor.RED);
        tracker.updateLongestRoad(holder, 7);
        tracker.updateLongestRoad(holder, 5);
        assertEquals(holder, tracker.getLongestRoadHolder().orElseThrow());
        assertEquals(5, tracker.getLongestRoadLength());
    }

    @Test
    void UpdateLongestRoad_WhenHolderRoadShrinksBelowClaimThreshold_RelinquishesCard() {
        SpecialCardTracker tracker = new SpecialCardTracker();
        Player holder = new Player("Alice", PlayerColor.RED);
        tracker.updateLongestRoad(holder, 7);
        tracker.updateLongestRoad(holder, 4);
        assertTrue(tracker.getLongestRoadHolder().isEmpty());
        assertEquals(0, tracker.getLongestRoadLength());
        assertFalse(tracker.holdsLongestRoad(holder));
    }

    @Test
    void UpdateLongestRoad_WithNullCandidate_ThrowsNullPointerException() {
        SpecialCardTracker tracker = new SpecialCardTracker();
        assertThrows(NullPointerException.class, () -> tracker.updateLongestRoad(null, 5));
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

    @Test
    void UpdateLargestArmy_WithNullCandidate_ThrowsNullPointerException() {
        SpecialCardTracker tracker = new SpecialCardTracker();
        assertThrows(NullPointerException.class, () -> tracker.updateLargestArmy(null, 3));
    }

    @Test
    void UpdateLargestArmy_WithNegativeKnightCount_ThrowsIllegalArgumentException() {
        SpecialCardTracker tracker = new SpecialCardTracker();
        Player player = new Player("Alice", PlayerColor.RED);
        assertThrows(IllegalArgumentException.class, () -> tracker.updateLargestArmy(player, -1));
    }

    @Test
    void UpdateLargestArmy_WithZeroKnightCount_RemainsUnclaimed() {
        SpecialCardTracker tracker = new SpecialCardTracker();
        Player player = new Player("Alice", PlayerColor.RED);
        assertDoesNotThrow(() -> tracker.updateLargestArmy(player, 0));
        assertTrue(tracker.getLargestArmyHolder().isEmpty());
        assertEquals(0, tracker.getLargestArmySize());
    }

    @Test
    void UpdateLargestArmy_WithKnightCountBelowClaimThreshold_RemainsUnclaimed() {
        SpecialCardTracker tracker = new SpecialCardTracker();
        Player player = new Player("Alice", PlayerColor.RED);
        tracker.updateLargestArmy(player, 2);
        assertTrue(tracker.getLargestArmyHolder().isEmpty());
        assertEquals(0, tracker.getLargestArmySize());
    }

    @Test
    void UpdateLargestArmy_WithKnightCountAtClaimThreshold_ClaimsCard() {
        SpecialCardTracker tracker = new SpecialCardTracker();
        Player player = new Player("Alice", PlayerColor.RED);
        tracker.updateLargestArmy(player, 3);
        assertEquals(player, tracker.getLargestArmyHolder().orElseThrow());
        assertEquals(3, tracker.getLargestArmySize());
    }

    @Test
    void UpdateLargestArmy_WhenCandidateTiesCurrentHolder_DoesNotSteal() {
        SpecialCardTracker tracker = new SpecialCardTracker();
        Player holder = new Player("Alice", PlayerColor.RED);
        Player challenger = new Player("Bob", PlayerColor.BLUE);
        tracker.updateLargestArmy(holder, 4);
        tracker.updateLargestArmy(challenger, 4);
        assertEquals(holder, tracker.getLargestArmyHolder().orElseThrow());
        assertEquals(4, tracker.getLargestArmySize());
    }

    @Test
    void UpdateLargestArmy_WhenCandidateExceedsCurrentHolderByOne_StealsCard() {
        SpecialCardTracker tracker = new SpecialCardTracker();
        Player holder = new Player("Alice", PlayerColor.RED);
        Player challenger = new Player("Bob", PlayerColor.BLUE);
        tracker.updateLargestArmy(holder, 3);
        tracker.updateLargestArmy(challenger, 4);
        assertEquals(challenger, tracker.getLargestArmyHolder().orElseThrow());
        assertEquals(4, tracker.getLargestArmySize());
    }

    @Test
    void UpdateLargestArmy_WhenSameHolderIncreasesKnightCount_UpdatesCount() {
        SpecialCardTracker tracker = new SpecialCardTracker();
        Player holder = new Player("Alice", PlayerColor.RED);
        tracker.updateLargestArmy(holder, 3);
        tracker.updateLargestArmy(holder, 5);
        assertEquals(holder, tracker.getLargestArmyHolder().orElseThrow());
        assertEquals(5, tracker.getLargestArmySize());
    }

    @Test
    void HoldsLargestArmy_BeforeClaim_ReturnsFalse() {
        SpecialCardTracker tracker = new SpecialCardTracker();
        Player player = new Player("Alice", PlayerColor.RED);
        assertFalse(tracker.holdsLargestArmy(player));
    }

    @Test
    void HoldsLargestArmy_AfterClaim_ReturnsTrueForHolderAndFalseForOthers() {
        SpecialCardTracker tracker = new SpecialCardTracker();
        Player holder = new Player("Alice", PlayerColor.RED);
        Player other = new Player("Bob", PlayerColor.BLUE);
        tracker.updateLargestArmy(holder, 3);
        assertTrue(tracker.holdsLargestArmy(holder));
        assertFalse(tracker.holdsLargestArmy(other));
    }
}
