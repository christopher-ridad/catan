package ui;

import domain.DevelopmentCard;
import domain.DevelopmentCardType;
import domain.Player;
import domain.ResourceType;
import domain.Turn;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

/**
 * Modal dialog for playing a development card.
 *
 * Step 1: Player selects which card to play from their playable hand.
 * Step 2: For Year of Plenty and Monopoly, collects resource choices inline.
 *         For Knight and Road Building, dismisses and lets TurnPhasePanel
 *         handle the follow-up board interaction.
 *
 * After the dialog closes, call getResult() to find out what happened.
 */
public class PlayCardDialog extends JDialog {

    // -------------------------------------------------------------------------
    // Constants
    // -------------------------------------------------------------------------

    private static final int DIALOG_WIDTH  = 400;
    private static final int DIALOG_HEIGHT = 360;
    private static final int LABEL_FONT    = 13;
    private static final int TITLE_FONT    = 15;
    private static final int BUTTON_WIDTH  = 150;
    private static final int BUTTON_HEIGHT = 38;
    private static final int GAP_SMALL     = 8;
    private static final int GAP_MEDIUM    = 16;

    private static final Color COLOR_TITLE   = new Color( 60,  90, 160);
    private static final Color COLOR_ERROR   = new Color(180,  30,  30);
    private static final Color COLOR_SUCCESS = new Color( 30, 130,  30);

    // -------------------------------------------------------------------------
    // Result type
    // -------------------------------------------------------------------------

    public enum ResultType { NONE, KNIGHT, ROAD_BUILDING, YEAR_OF_PLENTY, MONOPOLY }

    // -------------------------------------------------------------------------
    // State
    // -------------------------------------------------------------------------

    private final Turn turn;
    private final Player player;
    private final List<DevelopmentCard> playableCards;
    private final List<JRadioButton> cardButtons = new ArrayList<>();

    private ResultType resultType = ResultType.NONE;
    private DevelopmentCard playedCard = null;

    private JLabel statusLabel;

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    public PlayCardDialog(JFrame parent, Turn turn, Player player, int currentTurnNumber) {
        super(parent, Messages.get("play_card_title"), true);
        this.turn   = turn;
        this.player = player;
        this.playableCards = collectPlayableCards(player, currentTurnNumber);

        setPreferredSize(new Dimension(DIALOG_WIDTH, DIALOG_HEIGHT));
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        initComponents();
        pack();
        setLocationRelativeTo(parent);
    }

    // -------------------------------------------------------------------------
    // Public API
    // -------------------------------------------------------------------------

    public ResultType getResultType() {
        return resultType;
    }

    public DevelopmentCard getPlayedCard() {
        return playedCard;
    }

    // -------------------------------------------------------------------------
    // Initialization
    // -------------------------------------------------------------------------

    private void initComponents() {
        JPanel root = new JPanel(new BorderLayout(GAP_MEDIUM, GAP_MEDIUM));
        root.setBorder(BorderFactory.createEmptyBorder(GAP_MEDIUM, GAP_MEDIUM, GAP_MEDIUM, GAP_MEDIUM));
        root.add(buildCardSelectionPanel(), BorderLayout.CENTER);
        root.add(buildBottomPanel(), BorderLayout.SOUTH);
        setContentPane(root);
    }

    private JPanel buildCardSelectionPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(buildSelectionTitle());
        panel.add(Box.createVerticalStrut(GAP_SMALL));
        if (playableCards.isEmpty()) {
            panel.add(buildNoCardsLabel());
        } else {
            addCardRadioButtons(panel);
        }
        return panel;
    }

    private JLabel buildSelectionTitle() {
        JLabel title = new JLabel(Messages.get("play_card_select"));
        title.setFont(new Font("SansSerif", Font.BOLD, TITLE_FONT));
        title.setForeground(COLOR_TITLE);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        return title;
    }

    private JLabel buildNoCardsLabel() {
        JLabel none = new JLabel(Messages.get("play_card_no_playable"));
        none.setFont(new Font("SansSerif", Font.ITALIC, LABEL_FONT));
        none.setForeground(COLOR_ERROR);
        return none;
    }

    private void addCardRadioButtons(JPanel panel) {
        ButtonGroup group = new ButtonGroup();
        for (DevelopmentCard card : playableCards) {
            JRadioButton btn = new JRadioButton(UIStrings.cardName(card.getType()));
            btn.setFont(new Font("SansSerif", Font.PLAIN, LABEL_FONT));
            group.add(btn);
            cardButtons.add(btn);
            panel.add(btn);
            panel.add(Box.createVerticalStrut(GAP_SMALL));
        }
        if (!cardButtons.isEmpty()) {
            cardButtons.get(0).setSelected(true);
        }
    }

    private JPanel buildBottomPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        statusLabel = new JLabel(" ");
        statusLabel.setFont(new Font("SansSerif", Font.ITALIC, LABEL_FONT));
        statusLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(statusLabel);
        panel.add(Box.createVerticalStrut(GAP_SMALL));
        panel.add(buildButtonRow());
        return panel;
    }

    private JPanel buildButtonRow() {
        JPanel row = new JPanel();
        row.setLayout(new BoxLayout(row, BoxLayout.X_AXIS));
        row.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton playBtn   = buildButton(Messages.get("play_card_confirm"), COLOR_TITLE);
        JButton cancelBtn = buildButton(Messages.get("general_cancel"), Color.DARK_GRAY);

        playBtn.addActionListener(e -> onPlay());
        cancelBtn.addActionListener(e -> dispose());
        playBtn.setEnabled(!playableCards.isEmpty());

        row.add(playBtn);
        row.add(Box.createHorizontalStrut(GAP_SMALL));
        row.add(cancelBtn);
        return row;
    }

    // -------------------------------------------------------------------------
    // Play Logic
    // -------------------------------------------------------------------------

    private void onPlay() {
        DevelopmentCard card = getSelectedCard();
        if (card == null) {
            return;
        }

        switch (card.getType()) {
            case KNIGHT:
                playKnight(card);
                break;
            case ROAD_BUILDING:
                playRoadBuilding(card);
                break;
            case YEAR_OF_PLENTY:
                playYearOfPlenty(card);
                break;
            case MONOPOLY:
                playMonopoly(card);
                break;
            default:
                break;
        }
    }

    private void playKnight(DevelopmentCard card) {
        try {
            turn.playKnightCard(player, card);
            playedCard  = card;
            resultType  = ResultType.KNIGHT;
            dispose();
        } catch (IllegalStateException e) {
            showStatus(e.getMessage(), COLOR_ERROR);
        }
    }

    private void playRoadBuilding(DevelopmentCard card) {
        // Validation only — do NOT call turn.playRoadBuildingCard() here.
        // The domain call happens in TurnPhasePanel once both edge IDs are known.
        // Calling with sentinel -1,-1 marks the card played before throwing,
        // making the real follow-up call fail with "already been played".
        if (card.isPlayed()) {
            showStatus(Messages.get("error_card_already_played"), COLOR_ERROR);
            return;
        }
        playedCard = card;
        resultType = ResultType.ROAD_BUILDING;
        dispose();
    }

    private void playYearOfPlenty(DevelopmentCard card) {
        ResourceType[] choices = showTwoResourcePicker(
                Messages.get("play_card_yop_title"));
        if (choices == null) {
            return;
        }
        try {
            turn.playYearOfPlenty(player, card, choices[0], choices[1]);
            playedCard = card;
            resultType = ResultType.YEAR_OF_PLENTY;
            showStatus(Messages.get("play_card_success"), COLOR_SUCCESS);
        } catch (IllegalStateException e) {
            showStatus(Messages.get("error_unexpected"), COLOR_ERROR);
        }
    }

    private void playMonopoly(DevelopmentCard card) {
        ResourceType choice = showOneResourcePicker(
                Messages.get("play_card_monopoly_title"));
        if (choice == null) {
            return;
        }
        try {
            turn.playMonopoly(player, card, choice);
            playedCard = card;
            resultType = ResultType.MONOPOLY;
            showStatus(Messages.get("play_card_success"), COLOR_SUCCESS);
        } catch (IllegalStateException e) {
            showStatus(Messages.get("error_unexpected"), COLOR_ERROR);
        }
    }

    // -------------------------------------------------------------------------
    // Resource Pickers
    // -------------------------------------------------------------------------

    private ResourceType[] showTwoResourcePicker(String title) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JComboBox<String> combo1 = buildResourceCombo();
        JComboBox<String> combo2 = buildResourceCombo();

        panel.add(buildPickerLabel(Messages.get("play_card_resource1")));
        panel.add(combo1);
        panel.add(Box.createVerticalStrut(GAP_SMALL));
        panel.add(buildPickerLabel(Messages.get("play_card_resource2")));
        panel.add(combo2);

        String[] options = {Messages.get("general_confirm"), Messages.get("general_cancel")};
        int result = javax.swing.JOptionPane.showOptionDialog(
                this, panel, title,
                javax.swing.JOptionPane.OK_CANCEL_OPTION,
                javax.swing.JOptionPane.PLAIN_MESSAGE,
                null, options, options[0]);

        if (result != 0) {
            return null;
        }
        return new ResourceType[]{
                resourceAtIndex(combo1.getSelectedIndex()),
                resourceAtIndex(combo2.getSelectedIndex())
        };
    }

    private ResourceType showOneResourcePicker(String title) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JComboBox<String> combo = buildResourceCombo();
        panel.add(buildPickerLabel(Messages.get("play_card_resource")));
        panel.add(combo);

        String[] options = {Messages.get("general_confirm"), Messages.get("general_cancel")};
        int result = javax.swing.JOptionPane.showOptionDialog(
                this, panel, title,
                javax.swing.JOptionPane.OK_CANCEL_OPTION,
                javax.swing.JOptionPane.PLAIN_MESSAGE,
                null, options, options[0]);

        if (result != 0) {
            return null;
        }
        return resourceAtIndex(combo.getSelectedIndex());
    }

    private JComboBox<String> buildResourceCombo() {
        String[] names = new String[ResourceType.values().length];
        ResourceType[] types = ResourceType.values();
        for (int i = 0; i < types.length; i++) {
            names[i] = UIStrings.resourceName(types[i]);
        }
        return new JComboBox<>(names);
    }

    private JLabel buildPickerLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.PLAIN, LABEL_FONT));
        return label;
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    private List<DevelopmentCard> collectPlayableCards(Player player, int currentTurnNumber) {
        List<DevelopmentCard> playable = new ArrayList<>();
        for (DevelopmentCard card : player.getDevelopmentCards()) {
            if (card.isPlayableOnTurn(currentTurnNumber)) {
                playable.add(card);
            }
        }
        return playable;
    }

    private DevelopmentCard getSelectedCard() {
        for (int i = 0; i < cardButtons.size(); i++) {
            if (cardButtons.get(i).isSelected()) {
                return playableCards.get(i);
            }
        }
        return null;
    }

    private void showStatus(String message, Color color) {
        statusLabel.setText(message);
        statusLabel.setForeground(color);
    }

    private JButton buildButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("SansSerif", Font.BOLD, LABEL_FONT));
        btn.setForeground(color);
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(color, 2),
                BorderFactory.createEmptyBorder(6, 16, 6, 16)));
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setMaximumSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        btn.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        return btn;
    }

    private static ResourceType resourceAtIndex(int index) {
        return ResourceType.values()[index];
    }

    // cardName() and resourceName() replaced by UIStrings
}