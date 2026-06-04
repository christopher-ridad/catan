package domain;

import java.util.Collections;
import java.util.Map;

public class TradeOffer {
    public enum TradeStatus {
        PENDING, ACCEPTED, REJECTED
    }

    private final Player offerer;
    private final Player recipient;
    private final Map<ResourceType, Integer> offering;
    private final Map<ResourceType, Integer> requesting;
    private TradeStatus status;

    public TradeOffer(Player offerer, Player recipient,
                      Map<ResourceType, Integer> offering,
                      Map<ResourceType, Integer> requesting) {

        validateOffererAndRecipient(offerer, recipient);
        validateOffer(offering);
        validateRequest(requesting);

        this.offerer = offerer;
        this.recipient = recipient;
        this.offering = offering;
        this.requesting = requesting;
        this.status = TradeStatus.PENDING;
    }

    public Player getOfferer() {
        return offerer;
    }

    public Player getRecipient() {
        return recipient;
    }

    public Map<ResourceType, Integer> getOffering() {
        return Collections.unmodifiableMap(offering);
    }

    public Map<ResourceType, Integer> getRequesting() {
        return Collections.unmodifiableMap(requesting);
    }

    public TradeStatus getStatus() {
        return status;
    }

    public boolean isPending() {
        return status == TradeStatus.PENDING;
    }

    public void accept() {
        validatePendingTrade();
        status = TradeStatus.ACCEPTED;
    }

    private void validateOffererAndRecipient(Player offerer, Player recipient) {
        if (offerer.equals(recipient)) {
            throw new IllegalArgumentException("Offerer and recipient must be different players.");
        }
    }

    private void validateOffer(Map<ResourceType, Integer> offering) {
        if (offering == null || offering.isEmpty()) {
            throw new IllegalArgumentException("Offering must not be null or empty.");
        }
        for (int value : offering.values()) {
            if (value <= 0) {
                throw new IllegalArgumentException("All values in offering must be greater than 0.");
            }
        }
    }

    private void validateRequest(Map<ResourceType, Integer> requesting) {
        if (requesting == null || requesting.isEmpty()) {
            throw new IllegalArgumentException("Requesting must not be null or empty.");
        }
        for (int value : requesting.values()) {
            if (value <= 0) {
                throw new IllegalArgumentException("All values in requesting must be greater than 0.");
            }
        }
    }

    private void validatePendingTrade() {
        if (status != TradeStatus.PENDING) {
            throw new IllegalStateException("Cannot accept a trade offer that is not pending.");
        }
    }
}
