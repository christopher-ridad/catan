package domain;

import java.util.Map;

public class TradeOffer {
    private final Player offerer;
    private final Player recipient;
    private final Map<ResourceType, Integer> offering;
    private final Map<ResourceType, Integer> requesting;

    public TradeOffer(Player offerer, Player recipient,
                      Map<ResourceType, Integer> offering,
                      Map<ResourceType, Integer> requesting) {

        validateOffererAndRecipient(offerer, recipient);
        validateOfferIsntEmpty(offering);
        validateRequestIsntEmpty(requesting);

        this.offerer = offerer;
        this.recipient = recipient;
        this.offering = offering;
        this.requesting = requesting;
    }

    private void validateOffererAndRecipient(Player offerer, Player recipient) {
        if (offerer.equals(recipient)) {
            throw new IllegalArgumentException("Offerer and recipient must be different players.");
        }
    }

    private void validateOfferIsntEmpty(Map<ResourceType, Integer> offering) {
        if (offering == null || offering.isEmpty()) {
            throw new IllegalArgumentException("Offering must not be null or empty.");
        }
    }

    private void validateRequestIsntEmpty(Map<ResourceType, Integer> requesting) {
        if (requesting == null || requesting.isEmpty()) {
            throw new IllegalArgumentException("Requesting must not be null or empty.");
        }
    }
}
