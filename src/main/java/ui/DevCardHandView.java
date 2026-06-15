package ui;

import domain.DevelopmentCard;
import domain.DevelopmentCardType;
import domain.Player;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static ui.UIStrings.cardName;

/**
 * Displays the current player's development card hand.
 *
 * Cards are grouped by type and shown with an unplayed/played count.
 * Victory Point cards are always shown since they are never "played".
 * Call refresh(player) after any action that changes the hand.
 */
public class DevCardHandView extends JPanel {

    // -------------------------------------------------------------------------
    // Constants
    // -------------------------------------------------------------------------

    private static final int TITLE_FONT_SIZE = 13;
    private static final int CARD_FONT_SIZE  = 12;
    private static final int GAP_SMALL       = 4;
    private static final int GAP_MEDIUM      = 8;

    private static final Color COLOR_TITLE    = new Color( 60,  90, 160);
    private static final Color COLOR_UNPLAYED = new Color( 30,  30,  30);
    private static final Color COLOR_PLAYED   = new Color(160, 160, 160);
    private static final Color COLOR_VP       = new Color(180,  30,  30);

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    public DevCardHandView() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false);
    }

    // -------------------------------------------------------------------------
    // Public API
    // -------------------------------------------------------------------------

    /**
     * Rebuilds the display to reflect the given player's current dev card hand.
     * Call after buying a card or playing a card.
     *
     * @param player the player whose hand to display
     */
    public void refresh(Player player) {
        removeAll();

        JLabel title = new JLabel(Messages.get("dev_card_hand_title"));
        title.setFont(new Font("SansSerif", Font.BOLD, TITLE_FONT_SIZE));
        title.setForeground(COLOR_TITLE);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(title);
        add(Box.createVerticalStrut(GAP_SMALL));

        List<DevelopmentCard> cards = player.getDevelopmentCards();

        if (cards.isEmpty()) {
            add(buildCardLabel("—", COLOR_PLAYED, false));
        } else {
            addCardRows(cards);
        }

        revalidate();
        repaint();
    }

    // -------------------------------------------------------------------------
    // Card Row Building
    // -------------------------------------------------------------------------

    private void addCardRows(List<DevelopmentCard> cards) {
        Map<DevelopmentCardType, int[]> counts = countCards(cards);

        for (Map.Entry<DevelopmentCardType, int[]> entry : counts.entrySet()) {
            DevelopmentCardType type = entry.getKey();
            int unplayed = entry.getValue()[0];
            int played   = entry.getValue()[1];

            add(buildCardRow(type, unplayed, played));
            add(Box.createVerticalStrut(GAP_SMALL));
        }
    }

    private JPanel buildCardRow(DevelopmentCardType type, int unplayed, int played) {
        JPanel row = new JPanel();
        row.setLayout(new BoxLayout(row, BoxLayout.X_AXIS));
        row.setOpaque(false);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);

        String name = cardName(type);
        Color color = type == DevelopmentCardType.VICTORY_POINT ? COLOR_VP : COLOR_UNPLAYED;

        if (type == DevelopmentCardType.VICTORY_POINT) {
            row.add(buildCardLabel(name + " ×" + unplayed, color, false));
        } else {
            row.add(buildCardLabel(name + " ×" + unplayed, color, false));
            if (played > 0) {
                row.add(Box.createHorizontalStrut(GAP_MEDIUM));
                row.add(buildCardLabel(
                        Messages.get("dev_card_played") + " ×" + played,
                        COLOR_PLAYED, true));
            }
        }

        return row;
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    /**
     * Counts unplayed [0] and played [1] cards per type.
     * Uses LinkedHashMap to preserve DevelopmentCardType enum order.
     */
    private Map<DevelopmentCardType, int[]> countCards(List<DevelopmentCard> cards) {
        Map<DevelopmentCardType, int[]> counts = new LinkedHashMap<>();
        for (DevelopmentCardType type : DevelopmentCardType.values()) {
            counts.put(type, new int[]{0, 0});
        }
        for (DevelopmentCard card : cards) {
            int[] c = counts.get(card.getType());
            if (card.isPlayed()) {
                c[1]++;
            } else {
                c[0]++;
            }
        }
        counts.entrySet().removeIf(e -> e.getValue()[0] == 0 && e.getValue()[1] == 0);
        return counts;
    }

    private JLabel buildCardLabel(String text, Color color, boolean italic) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif",
                italic ? Font.ITALIC : Font.PLAIN, CARD_FONT_SIZE));
        label.setForeground(color);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }
}