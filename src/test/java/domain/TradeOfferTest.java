package domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class TradeOfferTest {

    private Player offerer;
    private Player recipient;
    private Map<ResourceType, Integer> validOffering;
    private Map<ResourceType, Integer> validRequesting;

    @BeforeEach
    void setUp() {
        offerer = new Player("Alice", PlayerColor.RED);
        recipient = new Player("Bob", PlayerColor.BLUE);
        validOffering = Map.of(ResourceType.BRICK, 1);
        validRequesting = Map.of(ResourceType.LUMBER, 1);
    }

    @Test
    void tradeOfferConstructor_offererEqualsRecipient_throwsIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () ->
                new TradeOffer(offerer, offerer, validOffering, validRequesting));
    }

    @Test
    void tradeOfferConstructor_offeringNull_throwsIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () ->
                new TradeOffer(offerer, recipient, null, validRequesting));
    }

    @Test
        // TO-03
    void tradeOfferConstructor_requestingNull_throwsIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () ->
                new TradeOffer(offerer, recipient, validOffering, null));
    }

    @Test
    void tradeOfferConstructor_offeringEmpty_throwsIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () ->
                new TradeOffer(offerer, recipient, Map.of(), validRequesting));
    }

    @Test
    void tradeOfferConstructor_requestingEmpty_throwsIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () ->
                new TradeOffer(offerer, recipient, validOffering, Map.of()));
    }

    @Test
    void tradeOfferConstructor_offeringValueZero_throwsIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () ->
                new TradeOffer(offerer, recipient, Map.of(ResourceType.BRICK, 0), validRequesting));
    }

    @Test
    void tradeOfferConstructor_offeringValueNegative_throwsIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () ->
                new TradeOffer(offerer, recipient, Map.of(ResourceType.BRICK, -1), validRequesting));
    }
}
