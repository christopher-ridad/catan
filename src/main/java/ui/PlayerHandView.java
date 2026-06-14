package ui;

import domain.Player;
import domain.ResourceType;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

/**
 * Displays the current player's resource hand as a row of colored cards,
 * each showing the resource name and count.
 *
 * Call refresh(player) whenever the hand changes (after dice roll,
 * trade, or build action) to update the display.
 *
 * This component is read-only — it never modifies player state.
 */
public class PlayerHandView extends JPanel {

    // -------------------------------------------------------------------------
    // Constants
    // -------------------------------------------------------------------------

    private static final int CARD_WIDTH       = 68;
    private static final int CARD_HEIGHT      = 110;
    private static final int CARD_GAP         = 8;
    private static final int CARD_ARC         = 10;
    private static final int RESOURCE_FONT_SIZE = 12;
    private static final int COUNT_FONT_SIZE    = 34;
    private static final int LABEL_FONT_SIZE    = 15;
    private static final int GAP_SMALL          = 6;
    private static final int GAP_MEDIUM         = 12;

    private static final Color COLOR_BRICK  = new Color(178,  98,  54);
    private static final Color COLOR_LUMBER = new Color( 34, 110,  34);
    private static final Color COLOR_ORE    = new Color(120, 120, 120);
    private static final Color COLOR_GRAIN  = new Color(210, 180,  40);
    private static final Color COLOR_WOOL   = new Color(100, 200, 100);
    private static final Color COLOR_CARD_TEXT    = Color.WHITE;
    private static final Color COLOR_ZERO_OVERLAY = new Color(0, 0, 0, 60);
    private static final Color COLOR_PANEL_BG     = new Color(245, 245, 240);
    private static final Color COLOR_HEADER       = new Color( 60,  60,  60);

    // -------------------------------------------------------------------------
    // State
    // -------------------------------------------------------------------------

    private final JLabel playerNameLabel;
    private final ResourceCardPanel[] resourceCards;

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    public PlayerHandView() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(COLOR_PANEL_BG);
        setBorder(BorderFactory.createEmptyBorder(GAP_MEDIUM, GAP_MEDIUM, GAP_MEDIUM, GAP_MEDIUM));

        playerNameLabel = buildNameLabel();
        resourceCards   = buildResourceCards();

        add(playerNameLabel);
        add(Box.createVerticalStrut(GAP_SMALL));
        add(buildResourceRow());
    }

    // -------------------------------------------------------------------------
    // Public API
    // -------------------------------------------------------------------------

    /**
     * Updates the display to reflect the given player's current hand.
     * Call this after any game action that changes resources.
     *
     * @param player the player whose hand to display
     */
    public void refresh(Player player) {
        playerNameLabel.setText(player.getName());

        for (ResourceCardPanel card : resourceCards) {
            int count = player.getResourceCount(card.getResourceType());
            card.setCount(count);
        }

        revalidate();
        repaint();
    }

    // -------------------------------------------------------------------------
    // Component Builders
    // -------------------------------------------------------------------------

    private JLabel buildNameLabel() {
        JLabel label = new JLabel(" ");
        label.setFont(new Font("SansSerif", Font.BOLD, LABEL_FONT_SIZE));
        label.setForeground(COLOR_HEADER);
        return label;
    }

    private ResourceCardPanel[] buildResourceCards() {
        ResourceType[] types = ResourceType.values();
        ResourceCardPanel[] cards = new ResourceCardPanel[types.length];
        for (int i = 0; i < types.length; i++) {
            cards[i] = new ResourceCardPanel(types[i]);
        }
        return cards;
    }

    private JPanel buildResourceRow() {
        JPanel row = new JPanel(new GridLayout(1, ResourceType.values().length, CARD_GAP, 0));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(
                (CARD_WIDTH + CARD_GAP) * ResourceType.values().length,
                CARD_HEIGHT));

        for (ResourceCardPanel card : resourceCards) {
            row.add(card);
        }

        return row;
    }

    // -------------------------------------------------------------------------
    // ResourceCardPanel (inner)
    // -------------------------------------------------------------------------

    /**
     * A single resource card showing the resource color, name, and count.
     * Dims slightly when the count is zero.
     */
    private static class ResourceCardPanel extends JPanel {

        private final ResourceType resourceType;
        private final JLabel countLabel;
        private int count = 0;

        ResourceCardPanel(ResourceType resourceType) {
            this.resourceType = resourceType;
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setBackground(resourceColor(resourceType));
            setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(resourceColor(resourceType).darker(), 1),
                    BorderFactory.createEmptyBorder(6, 4, 6, 4)));
            setPreferredSize(new Dimension(CARD_WIDTH, CARD_HEIGHT));
            setMaximumSize(new Dimension(CARD_WIDTH, CARD_HEIGHT));

            JLabel nameLabel = buildNameLabel(resourceType);
            countLabel       = buildCountLabel();

            add(Box.createVerticalGlue());
            add(nameLabel);
            add(Box.createVerticalStrut(4));
            add(countLabel);
            add(Box.createVerticalGlue());
        }

        ResourceType getResourceType() {
            return resourceType;
        }

        void setCount(int count) {
            this.count = count;
            countLabel.setText(String.valueOf(count));
            // Dim the card when the player has none of this resource
            setBackground(count == 0
                    ? resourceColor(resourceType).darker()
                    : resourceColor(resourceType));
            repaint();
        }

        private JLabel buildNameLabel(ResourceType type) {
            JLabel label = new JLabel(resourceName(type));
            label.setFont(new Font("SansSerif", Font.BOLD, RESOURCE_FONT_SIZE));
            label.setForeground(COLOR_CARD_TEXT);
            label.setAlignmentX(CENTER_ALIGNMENT);
            return label;
        }

        private JLabel buildCountLabel() {
            JLabel label = new JLabel("0");
            label.setFont(new Font("SansSerif", Font.BOLD, COUNT_FONT_SIZE));
            label.setForeground(COLOR_CARD_TEXT);
            label.setAlignmentX(CENTER_ALIGNMENT);
            return label;
        }

        // ---------------------------------------------------------------------
        // Helpers
        // ---------------------------------------------------------------------

        private static Color resourceColor(ResourceType type) {
            switch (type) {
                case BRICK:  return COLOR_BRICK;
                case LUMBER: return COLOR_LUMBER;
                case ORE:    return COLOR_ORE;
                case GRAIN:  return COLOR_GRAIN;
                case WOOL:   return COLOR_WOOL;
                default:     return Color.GRAY;
            }
        }

        private static String resourceName(ResourceType type) {
            switch (type) {
                case BRICK:  return Messages.get("resource_brick");
                case LUMBER: return Messages.get("resource_wood");
                case ORE:    return Messages.get("resource_ore");
                case GRAIN:  return Messages.get("resource_wheat");
                case WOOL:   return Messages.get("resource_sheep");
                default:     return "?";
            }
        }
    }
}