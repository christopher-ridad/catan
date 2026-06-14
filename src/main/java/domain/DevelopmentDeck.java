package domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DevelopmentDeck {
    private final List<DevelopmentCard> cards;

    public DevelopmentDeck() {
        cards = new ArrayList<>();
        addKnightCards();
        addProgressCards();
        addVictoryPointCards();
        Collections.shuffle(cards);
    }

    private void addKnightCards() {
        for (int i = 0; i < 14; i++) {
            cards.add(new DevelopmentCard(DevelopmentCardType.KNIGHT));
        }
    }

    private void addProgressCards() {
        for (int i = 0; i < 2; i++) {
            cards.add(new DevelopmentCard(DevelopmentCardType.ROAD_BUILDING));
            cards.add(new DevelopmentCard(DevelopmentCardType.YEAR_OF_PLENTY));
            cards.add(new DevelopmentCard(DevelopmentCardType.MONOPOLY));
        }
    }

    private void addVictoryPointCards() {
        for (int i = 0; i < 5; i++) {
            cards.add(new DevelopmentCard(DevelopmentCardType.VICTORY_POINT));
        }
    }

    public int getRemainingCount() {
        return cards.size();
    }

    public DevelopmentCard draw(int currentTurn) {
        if (cards.isEmpty()) {
            throw new IllegalStateException("Development deck is empty");
        }
        DevelopmentCard card = cards.remove(0);
        card.setTurnPurchased(currentTurn);
        return card;
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }
}