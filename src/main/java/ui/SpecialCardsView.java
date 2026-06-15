package ui;

import domain.Player;
import domain.SpecialCardTracker;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

/**
 * Displays Longest Road and Largest Army status for the current player.
 *
 * Each card shows its current length/size in brackets.
 * Highlighted in gold if the player holds it, greyed out if not.
 *
 * Call refresh(player, tracker) after any action that could change
 * special card ownership (end of turn).
 */
public class SpecialCardsView extends JPanel {

    // -------------------------------------------------------------------------
    // Constants
    // -------------------------------------------------------------------------

    private static final int TITLE_FONT_SIZE = 13;
    private static final int CARD_FONT_SIZE  = 12;
    private static final int GAP_SMALL       = 4;

    private static final Color COLOR_TITLE    = new Color( 60,  90, 160);
    private static final Color COLOR_HELD     = new Color(180, 140,   0);
    private static final Color COLOR_NOT_HELD = new Color(160, 160, 160);

    private static final String ICON_ROAD  = "🏆";
    private static final String ICON_ARMY  = "⚔";

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    public SpecialCardsView() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false);
    }

    // -------------------------------------------------------------------------
    // Public API
    // -------------------------------------------------------------------------

    /**
     * Rebuilds the display for the given player and tracker state.
     *
     * @param player  the current player
     * @param tracker the special card tracker
     */
    public void refresh(Player player, SpecialCardTracker tracker) {
        removeAll();

        add(buildTitleLabel());
        add(Box.createVerticalStrut(GAP_SMALL));
        add(buildRoadLabel(player, tracker));
        add(Box.createVerticalStrut(GAP_SMALL));
        add(buildArmyLabel(player, tracker));

        revalidate();
        repaint();
    }

    // -------------------------------------------------------------------------
    // Component Builders
    // -------------------------------------------------------------------------

    private JLabel buildTitleLabel() {
        JLabel label = new JLabel(Messages.get("special_cards_title"));
        label.setFont(new Font("SansSerif", Font.BOLD, TITLE_FONT_SIZE));
        label.setForeground(COLOR_TITLE);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private JLabel buildRoadLabel(Player player, SpecialCardTracker tracker) {
        boolean held   = tracker.holdsLongestRoad(player);
        int length     = tracker.getLongestRoadLength();
        String text    = ICON_ROAD + " " + Messages.get("special_longest_road", length);
        return buildCardLabel(text, held ? COLOR_HELD : COLOR_NOT_HELD, held);
    }

    private JLabel buildArmyLabel(Player player, SpecialCardTracker tracker) {
        boolean held   = tracker.holdsLargestArmy(player);
        int size       = tracker.getLargestArmySize();
        String text    = ICON_ARMY + " " + Messages.get("special_largest_army", size);
        return buildCardLabel(text, held ? COLOR_HELD : COLOR_NOT_HELD, held);
    }

    private JLabel buildCardLabel(String text, Color color, boolean bold) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif",
                bold ? Font.BOLD : Font.PLAIN, CARD_FONT_SIZE));
        label.setForeground(color);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }
}