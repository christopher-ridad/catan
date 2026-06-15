package domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
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

    @Test
    void tradeOfferConstructor_requestingValueZero_throwsIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () ->
                new TradeOffer(offerer, recipient, validOffering, Map.of(ResourceType.LUMBER, 0)));
    }

    @Test
    void tradeOfferConstructor_requestingValueNegative_throwsIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () ->
                new TradeOffer(offerer, recipient, validOffering, Map.of(ResourceType.LUMBER, -1)));
    }

    @Test
    void tradeOfferConstructor_allValuesMinimumValid_objectCreatedWithPendingStatus() {
        TradeOffer offer = new TradeOffer(offerer, recipient, validOffering, validRequesting);
        assertEquals(TradeOffer.TradeStatus.PENDING, offer.getStatus());
    }

    @Test
    void tradeOfferConstructor_offeringContainsNullKey_throwsIllegalArgument() {
        Map<ResourceType, Integer> offeringWithNullKey = new HashMap<>();
        offeringWithNullKey.put(null, 1);

        assertThrows(IllegalArgumentException.class, () ->
                new TradeOffer(offerer, recipient, offeringWithNullKey, validRequesting));
    }

    @Test
    void tradeOfferConstructor_requestingContainsNullKey_throwsIllegalArgument() {
        Map<ResourceType, Integer> requestingWithNullKey = new HashMap<>();
        requestingWithNullKey.put(null, 1);

        assertThrows(IllegalArgumentException.class, () ->
                new TradeOffer(offerer, recipient, validOffering, requestingWithNullKey));
    }

    @Test
    void tradeOfferConstructor_offererNull_throwsIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () ->
                new TradeOffer(null, recipient, validOffering, validRequesting));
    }

    @Test
    void tradeOfferConstructor_recipientNull_throwsIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () ->
                new TradeOffer(offerer, null, validOffering, validRequesting));
    }

    @Test
    void getOffering_validTradeOffer_returnsMatchingOfferingMap() {
        TradeOffer offer = new TradeOffer(offerer, recipient, validOffering, validRequesting);
        assertEquals(validOffering, offer.getOffering());
    }

    @Test
    void getOffering_validTradeOffer_mutationAttemptThrowsUnsupportedOperation() {
        TradeOffer offer = new TradeOffer(offerer, recipient, validOffering, validRequesting);
        assertThrows(UnsupportedOperationException.class, () ->
                offer.getOffering().put(ResourceType.ORE, 1));
    }

    @Test
    void getRequesting_validTradeOffer_returnsMatchingRequestingMap() {
        TradeOffer offer = new TradeOffer(offerer, recipient, validOffering, validRequesting);
        assertEquals(validRequesting, offer.getRequesting());
    }

    @Test
    void getRequesting_validTradeOffer_mutationAttemptThrowsUnsupportedOperation() {
        TradeOffer offer = new TradeOffer(offerer, recipient, validOffering, validRequesting);
        assertThrows(UnsupportedOperationException.class, () ->
                offer.getRequesting().put(ResourceType.ORE, 1));
    }

    @Test
    void getOfferer_validTradeOffer_returnsCorrectOfferer() {
        TradeOffer offer = new TradeOffer(offerer, recipient, validOffering, validRequesting);
        assertEquals(offerer, offer.getOfferer());
    }

    @Test
    void getRecipient_validTradeOffer_returnsCorrectRecipient() {
        TradeOffer offer = new TradeOffer(offerer, recipient, validOffering, validRequesting);
        assertEquals(recipient, offer.getRecipient());
    }

    @Test
    void getStatus_freshlyConstructed_returnsPending() {
        TradeOffer offer = new TradeOffer(offerer, recipient, validOffering, validRequesting);
        assertEquals(TradeOffer.TradeStatus.PENDING, offer.getStatus());
    }

    @Test
    void isPending_freshlyConstructed_returnsTrue() {
        TradeOffer offer = new TradeOffer(offerer, recipient, validOffering, validRequesting);
        assertTrue(offer.isPending());
    }

    @Test
    void isPending_statusAccepted_returnsFalse() {
        TradeOffer offer = new TradeOffer(offerer, recipient, validOffering, validRequesting);
        offer.accept();
        assertFalse(offer.isPending());
    }

    @Test
    void isPending_statusRejected_returnsFalse() {
        TradeOffer offer = new TradeOffer(offerer, recipient, validOffering, validRequesting);
        offer.reject();
        assertFalse(offer.isPending());
    }

    @Test
    void accept_statusPending_statusBecomesAccepted() {
        TradeOffer offer = new TradeOffer(offerer, recipient, validOffering, validRequesting);
        offer.accept();
        assertEquals(TradeOffer.TradeStatus.ACCEPTED, offer.getStatus());
    }

    @Test
    void accept_statusAlreadyAccepted_throwsIllegalState() {
        TradeOffer offer = new TradeOffer(offerer, recipient, validOffering, validRequesting);
        offer.accept();
        assertThrows(IllegalStateException.class, offer::accept);
    }

    @Test
    void accept_statusRejected_throwsIllegalState() {
        TradeOffer offer = new TradeOffer(offerer, recipient, validOffering, validRequesting);
        offer.reject();
        assertThrows(IllegalStateException.class, offer::accept);
    }

    @Test
    void reject_statusPending_statusBecomesRejected() {
        TradeOffer offer = new TradeOffer(offerer, recipient, validOffering, validRequesting);
        offer.reject();
        assertEquals(TradeOffer.TradeStatus.REJECTED, offer.getStatus());
    }

    @Test
    void reject_statusAlreadyRejected_throwsIllegalState() {
        TradeOffer offer = new TradeOffer(offerer, recipient, validOffering, validRequesting);
        offer.reject();
        assertThrows(IllegalStateException.class, offer::reject);
    }

    @Test
    void reject_statusAccepted_throwsIllegalState() {
        TradeOffer offer = new TradeOffer(offerer, recipient, validOffering, validRequesting);
        offer.accept();
        assertThrows(IllegalStateException.class, offer::reject);
    }
}
