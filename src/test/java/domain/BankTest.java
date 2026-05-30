package domain;

import org.junit.jupiter.api.Test;

import java.util.EnumMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class BankTest {

    private Bank bankWithBrick(int amount) {
        Map<ResourceType, Integer> resources = new EnumMap<>(ResourceType.class);
        resources.put(ResourceType.BRICK, amount);
        resources.put(ResourceType.LUMBER, 0);
        resources.put(ResourceType.ORE, 0);
        resources.put(ResourceType.GRAIN, 0);
        resources.put(ResourceType.WOOL, 0);
        return new Bank(resources);
    }

    @Test
    void Constructor_DefaultBank_Has19OfEachResource() {
        Bank bank = new Bank();
        for (ResourceType type : ResourceType.values()) {
            assertEquals(19, bank.getResourceCount(type));
        }
    }

    @Test
    void GetResourceCount_WithNullType_ThrowsIllegalArgumentException() {
        Bank bank = new Bank();
        assertThrows(IllegalArgumentException.class, () -> bank.getResourceCount(null));
    }

    @Test
    void GetResourceCount_WithValidType_ReturnsCurrentCount() {
        Bank bank = bankWithBrick(5);
        assertEquals(5, bank.getResourceCount(ResourceType.BRICK));
    }

    @Test
    void Deduct_WithNullType_ThrowsIllegalArgumentException() {
        Bank bank = new Bank();
        assertThrows(IllegalArgumentException.class, () -> bank.deduct(null, 1));
    }

    @Test
    void Deduct_WithNegativeAmount_ThrowsIllegalArgumentException() {
        Bank bank = new Bank();
        assertThrows(IllegalArgumentException.class, () -> bank.deduct(ResourceType.BRICK, -1));
    }

    @Test
    void Deduct_WithZeroAmount_NoChange() {
        Bank bank = bankWithBrick(5);
        bank.deduct(ResourceType.BRICK, 0);
        assertEquals(5, bank.getResourceCount(ResourceType.BRICK));
    }

    @Test
    void Deduct_ExactlyAvailableAmount_LeavesZero() {
        Bank bank = bankWithBrick(5);
        bank.deduct(ResourceType.BRICK, 5);
        assertEquals(0, bank.getResourceCount(ResourceType.BRICK));
    }

    @Test
    void Deduct_OneMoreThanAvailable_ThrowsIllegalArgumentException() {
        Bank bank = bankWithBrick(5);
        assertThrows(IllegalArgumentException.class, () -> bank.deduct(ResourceType.BRICK, 6));
    }
}
