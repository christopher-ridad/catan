package ui;

import domain.Board;
import domain.HarborType;
import domain.MaritimeTrade;
import domain.Player;
import domain.ResourceType;
import domain.Turn;
import domain.Vertex;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
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
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

/**
 * Modal dialog for maritime (bank) trading.
 *
 * Only shows resources the player can actually afford to trade at their
 * best available rate (computed by MaritimeTrade). The player picks
 * what to give and what to receive, then confirms.
 */
public class MaritimeTradeDialog extends JDialog {

    // -------------------------------------------------------------------------
    // Constants
    // -------------------------------------------------------------------------

    private static final int DIALOG_WIDTH  = 440;
    private static final int DIALOG_HEIGHT = 400;
    private static final int LABEL_FONT    = 13;
    private static final int TITLE_FONT    = 15;
    private static final int BUTTON_WIDTH  = 140;
    private static final int BUTTON_HEIGHT = 38;
    private static final int GAP_SMALL     = 8;
    private static final int GAP_MEDIUM    = 14;

    private static final Color COLOR_GIVE    = new Color( 60,  90, 160);
    private static final Color COLOR_RECEIVE = new Color( 30, 130,  30);
    private static final Color COLOR_ERROR   = new Color(180,  30,  30);
    private static final Color COLOR_SUCCESS = new Color( 30, 130,  30);

    // -------------------------------------------------------------------------
    // State
    // -------------------------------------------------------------------------

    private final Turn turn;
    private final Player player;
    private final Board board;

    private final List<ResourceType> affordableResources = new ArrayList<>();
    private final List<JRadioButton> giveButtons    = new ArrayList<>();
    private final List<JRadioButton> receiveButtons = new ArrayList<>();

    private JLabel statusLabel;
    private JLabel rateLabel;

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    public MaritimeTradeDialog(JFrame parent, Turn turn, Player player, Board board) {
        super(parent, Messages.get("trade_maritime_title"), true);
        this.turn   = turn;
        this.player = player;
        this.board  = board;

        computeAffordableResources();

        setPreferredSize(new Dimension(DIALOG_WIDTH, DIALOG_HEIGHT));
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        initComponents();
        pack();
        setLocationRelativeTo(parent);
    }

    // -------------------------------------------------------------------------
    // Initialization
    // -------------------------------------------------------------------------

    private void computeAffordableResources() {
        for (ResourceType type : ResourceType.values()) {
            int bestRate = computeBestRate(type);
            if (player.getResourceCount(type) >= bestRate) {
                affordableResources.add(type);
            }
        }
    }

    private void initComponents() {
        JPanel root = new JPanel(new BorderLayout(GAP_MEDIUM, GAP_MEDIUM));
        root.setBorder(BorderFactory.createEmptyBorder(GAP_MEDIUM, GAP_MEDIUM, GAP_MEDIUM, GAP_MEDIUM));

        if (affordableResources.isEmpty()) {
            root.add(buildNoTradeLabel(), BorderLayout.CENTER);
        } else {
            root.add(buildTradeSection(), BorderLayout.CENTER);
        }

        root.add(buildBottomSection(), BorderLayout.SOUTH);
        setContentPane(root);
    }

    private JLabel buildNoTradeLabel() {
        JLabel label = new JLabel(Messages.get("trade_no_players"));
        label.setFont(new Font("SansSerif", Font.ITALIC, LABEL_FONT));
        label.setForeground(COLOR_ERROR);
        return label;
    }

    private JPanel buildTradeSection() {
        JPanel panel = new JPanel(new GridLayout(1, 2, GAP_MEDIUM, 0));
        panel.add(buildGivePanel());
        panel.add(buildReceivePanel());
        return panel;
    }

    private JPanel buildGivePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(GAP_SMALL, GAP_SMALL, GAP_SMALL, GAP_SMALL));

        panel.add(buildSectionLabel(Messages.get("trade_maritime_give"), COLOR_GIVE));
        panel.add(Box.createVerticalStrut(GAP_SMALL));

        ButtonGroup group = new ButtonGroup();
        for (ResourceType type : affordableResources) {
            int rate = computeBestRate(type);
            String label = UIStrings.resourceName(type) + " (" + rate + ":1)"
                    + " [" + player.getResourceCount(type) + "]";
            JRadioButton btn = new JRadioButton(label);
            btn.setFont(new Font("SansSerif", Font.PLAIN, LABEL_FONT));
            btn.addActionListener(e -> updateRateLabel(type));
            group.add(btn);
            giveButtons.add(btn);
            panel.add(btn);
        }

        return panel;
    }

    private JPanel buildReceivePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(GAP_SMALL, GAP_SMALL, GAP_SMALL, GAP_SMALL));

        panel.add(buildSectionLabel(Messages.get("trade_maritime_receive"), COLOR_RECEIVE));
        panel.add(Box.createVerticalStrut(GAP_SMALL));

        ButtonGroup group = new ButtonGroup();
        for (ResourceType type : ResourceType.values()) {
            JRadioButton btn = new JRadioButton(UIStrings.resourceName(type));
            btn.setFont(new Font("SansSerif", Font.PLAIN, LABEL_FONT));
            group.add(btn);
            receiveButtons.add(btn);
            panel.add(btn);
        }

        return panel;
    }

    private JLabel buildSectionLabel(String text, Color color) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.BOLD, TITLE_FONT));
        label.setForeground(color);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private JPanel buildBottomSection() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        rateLabel = new JLabel(" ");
        rateLabel.setFont(new Font("SansSerif", Font.BOLD, LABEL_FONT));
        rateLabel.setForeground(COLOR_GIVE);
        rateLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        statusLabel = new JLabel(" ");
        statusLabel.setFont(new Font("SansSerif", Font.ITALIC, LABEL_FONT));
        statusLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(rateLabel);
        panel.add(Box.createVerticalStrut(GAP_SMALL));
        panel.add(statusLabel);
        panel.add(Box.createVerticalStrut(GAP_SMALL));
        panel.add(buildButtonRow());

        return panel;
    }

    private JPanel buildButtonRow() {
        JPanel row = new JPanel();
        row.setLayout(new BoxLayout(row, BoxLayout.X_AXIS));

        JButton confirmBtn = buildButton(Messages.get("trade_maritime_confirm"), COLOR_GIVE);
        JButton cancelBtn  = buildButton(Messages.get("general_cancel"), Color.DARK_GRAY);

        confirmBtn.addActionListener(e -> onConfirm());
        cancelBtn.addActionListener(e -> dispose());

        row.add(confirmBtn);
        row.add(Box.createHorizontalStrut(GAP_SMALL));
        row.add(cancelBtn);
        return row;
    }

    private JButton buildButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("SansSerif", Font.BOLD, LABEL_FONT));
        btn.setForeground(color);
        btn.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createLineBorder(color, 2),
                javax.swing.BorderFactory.createEmptyBorder(6, 16, 6, 16)));
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setMaximumSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        btn.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        return btn;
    }

    // -------------------------------------------------------------------------
    // Trade Flow
    // -------------------------------------------------------------------------

    private void onConfirm() {
        ResourceType giving    = getSelectedResource(giveButtons, affordableResources);
        ResourceType receiving = getSelectedReceiveResource();

        if (giving == null) {
            showStatus(Messages.get("trade_maritime_give") + "?", COLOR_ERROR);
            return;
        }
        if (receiving == null) {
            showStatus(Messages.get("trade_maritime_receive") + "?", COLOR_ERROR);
            return;
        }
        if (giving == receiving) {
            showStatus(Messages.get("error_no_resources"), COLOR_ERROR);
            return;
        }

        try {
            int rate = computeBestRate(giving);
            MaritimeTrade trade = new MaritimeTrade(player, giving, rate, receiving, board);
            turn.submitMaritimeTrade(trade);
            showStatus(Messages.get("trade_maritime_success"), COLOR_SUCCESS);
        } catch (IllegalArgumentException | IllegalStateException e) {
            showStatus(Messages.get("error_maritime_bank_empty"), COLOR_ERROR);
        }
    }

    private void updateRateLabel(ResourceType giving) {
        int rate = computeBestRate(giving);
        rateLabel.setText(Messages.get("trade_maritime_rate", rate));
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    /**
     * Computes the player's best trade rate for a given resource
     * by checking their harbor vertices.
     *
     * NOTE: This mirrors the private MaritimeTrade.computeBestRate() logic.
     * If harbor trade rules change in the domain, this method must be updated
     * to match. The duplication exists because MaritimeTrade does not expose
     * its rate calculation publicly.
     */
    private int computeBestRate(ResourceType giving) {
        int best = 4;
        for (Vertex vertex : board.getVertices()) {
            if (vertex.getOwner().filter(o -> o == player).isEmpty()) {
                continue;
            }
            if (vertex.getHarborType().isEmpty()) {
                continue;
            }
            HarborType harbor = vertex.getHarborType().get();
            if (harbor == HarborType.GENERIC) {
                best = Math.min(best, 3);
            } else if (harborMatchesResource(harbor, giving)) {
                best = Math.min(best, 2);
            }
        }
        return best;
    }

    private boolean harborMatchesResource(HarborType harbor, ResourceType resource) {
        switch (resource) {
            case BRICK:  return harbor == HarborType.BRICK;
            case LUMBER: return harbor == HarborType.LUMBER;
            case ORE:    return harbor == HarborType.ORE;
            case GRAIN:  return harbor == HarborType.GRAIN;
            case WOOL:   return harbor == HarborType.WOOL;
            default:     return false;
        }
    }

    private ResourceType getSelectedResource(List<JRadioButton> buttons,
                                             List<ResourceType> types) {
        for (int i = 0; i < buttons.size(); i++) {
            if (buttons.get(i).isSelected()) {
                return types.get(i);
            }
        }
        return null;
    }

    private ResourceType getSelectedReceiveResource() {
        ResourceType[] all = ResourceType.values();
        for (int i = 0; i < receiveButtons.size(); i++) {
            if (receiveButtons.get(i).isSelected()) {
                return all[i];
            }
        }
        return null;
    }

    private void showStatus(String message, Color color) {
        statusLabel.setText(message);
        statusLabel.setForeground(color);
    }
}