package ui;

import domain.Player;
import domain.ResourceType;
import domain.TradeOffer;
import domain.Turn;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Modal dialog for player-to-player trading.
 *
 * Flow:
 *   1. Active player selects offer and request amounts, picks recipient
 *   2. DeviceHandoffDialog shown for recipient
 *   3. Recipient sees offer and accepts or rejects
 *   4. DeviceHandoffDialog shown to return device to active player
 *   5. Result shown to active player
 */
public class TradeDialog extends JDialog {

    // -------------------------------------------------------------------------
    // Constants
    // -------------------------------------------------------------------------

    private static final int DIALOG_WIDTH   = 500;
    private static final int DIALOG_HEIGHT  = 460;
    private static final int LABEL_FONT     = 13;
    private static final int TITLE_FONT     = 16;
    private static final int BUTTON_WIDTH   = 140;
    private static final int BUTTON_HEIGHT  = 38;
    private static final int SPINNER_WIDTH  = 60;
    private static final int SPINNER_HEIGHT = 30;
    private static final int GAP_SMALL      = 8;
    private static final int GAP_MEDIUM     = 16;
    private static final int MAX_RESOURCE   = 19;

    private static final Color COLOR_OFFER   = new Color( 60,  90, 160);
    private static final Color COLOR_REQUEST = new Color(160,  60,  60);
    private static final Color COLOR_ERROR   = new Color(180,  30,  30);
    private static final Color COLOR_SUCCESS = new Color( 30, 130,  30);

    // -------------------------------------------------------------------------
    // State
    // -------------------------------------------------------------------------

    private final JFrame parent;
    private final Turn turn;
    private final Player activePlayer;
    private final List<Player> otherPlayers;

    private final Map<ResourceType, JSpinner> offerSpinners  = new HashMap<>();
    private final Map<ResourceType, JSpinner> requestSpinners = new HashMap<>();
    private JComboBox<String> recipientCombo;
    private JLabel statusLabel;

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    public TradeDialog(JFrame parent, Turn turn, Player activePlayer, List<Player> otherPlayers) {
        super(parent, Messages.get("trade_title"), true);
        this.parent       = parent;
        this.turn         = turn;
        this.activePlayer = activePlayer;
        this.otherPlayers = otherPlayers;

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

    private void initComponents() {
        JPanel root = new JPanel(new BorderLayout(GAP_MEDIUM, GAP_MEDIUM));
        root.setBorder(BorderFactory.createEmptyBorder(GAP_MEDIUM, GAP_MEDIUM, GAP_MEDIUM, GAP_MEDIUM));

        root.add(buildResourceSection(), BorderLayout.CENTER);
        root.add(buildBottomSection(),   BorderLayout.SOUTH);

        setContentPane(root);
    }

    private JPanel buildResourceSection() {
        JPanel panel = new JPanel(new GridLayout(1, 2, GAP_MEDIUM, 0));
        panel.add(buildSpinnerGroup(Messages.get("trade_offer_label"),   COLOR_OFFER,   offerSpinners,   true));
        panel.add(buildSpinnerGroup(Messages.get("trade_request_label"), COLOR_REQUEST, requestSpinners, false));
        return panel;
    }

    private JPanel buildSpinnerGroup(String title, Color titleColor,
                                     Map<ResourceType, JSpinner> spinners, boolean limitToHand) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(GAP_SMALL, GAP_SMALL, GAP_SMALL, GAP_SMALL));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, TITLE_FONT));
        titleLabel.setForeground(titleColor);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(GAP_SMALL));

        for (ResourceType type : ResourceType.values()) {
            int max = limitToHand ? activePlayer.getResourceCount(type) : MAX_RESOURCE;
            JSpinner spinner = new JSpinner(new SpinnerNumberModel(0, 0, max, 1));
            spinner.setMaximumSize(new Dimension(SPINNER_WIDTH, SPINNER_HEIGHT));
            spinners.put(type, spinner);
            panel.add(buildSpinnerRow(type, spinner));
            panel.add(Box.createVerticalStrut(GAP_SMALL));
        }

        return panel;
    }

    private JPanel buildSpinnerRow(ResourceType type, JSpinner spinner) {
        JPanel row = new JPanel(new BorderLayout(GAP_SMALL, 0));
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel label = new JLabel(resourceName(type));
        label.setFont(new Font("SansSerif", Font.PLAIN, LABEL_FONT));
        row.add(label,   BorderLayout.CENTER);
        row.add(spinner, BorderLayout.EAST);
        return row;
    }

    private JPanel buildBottomSection() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(buildRecipientRow());
        panel.add(Box.createVerticalStrut(GAP_SMALL));

        statusLabel = new JLabel(" ");
        statusLabel.setFont(new Font("SansSerif", Font.ITALIC, LABEL_FONT));
        statusLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(statusLabel);
        panel.add(Box.createVerticalStrut(GAP_SMALL));
        panel.add(buildButtonRow());

        return panel;
    }

    private JPanel buildRecipientRow() {
        JPanel row = new JPanel(new BorderLayout(GAP_SMALL, 0));
        JLabel label = new JLabel(Messages.get("trade_select_player"));
        label.setFont(new Font("SansSerif", Font.PLAIN, LABEL_FONT));

        String[] names = otherPlayers.stream().map(Player::getName).toArray(String[]::new);
        recipientCombo = new JComboBox<>(names);

        row.add(label,         BorderLayout.WEST);
        row.add(recipientCombo, BorderLayout.CENTER);
        return row;
    }

    private JPanel buildButtonRow() {
        JPanel row = new JPanel();
        row.setLayout(new BoxLayout(row, BoxLayout.X_AXIS));

        JButton proposeBtn = buildButton(Messages.get("general_confirm"), COLOR_OFFER);
        JButton cancelBtn  = buildButton(Messages.get("general_cancel"),  Color.DARK_GRAY);

        proposeBtn.addActionListener(e -> onPropose());
        cancelBtn.addActionListener(e -> dispose());

        row.add(proposeBtn);
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

    private void onPropose() {
        Map<ResourceType, Integer> offering   = collectSpinnerValues(offerSpinners);
        Map<ResourceType, Integer> requesting = collectSpinnerValues(requestSpinners);

        if (isEmpty(offering)) {
            showStatus(Messages.get("error_no_resources"), COLOR_ERROR);
            return;
        }
        if (isEmpty(requesting)) {
            showStatus(Messages.get("error_no_resources"), COLOR_ERROR);
            return;
        }

        Player recipient = otherPlayers.get(recipientCombo.getSelectedIndex());

        try {
            TradeOffer offer = turn.proposeTrade(recipient, offering, requesting);
            showStatus(Messages.get("trade_offer_sent", recipient.getName()), COLOR_SUCCESS);
            runRecipientFlow(offer, recipient);
        } catch (IllegalArgumentException | IllegalStateException e) {
            showStatus(e.getMessage(), COLOR_ERROR);
        }
    }

    private void runRecipientFlow(TradeOffer offer, Player recipient) {
        DeviceHandoffDialog.show(parent, recipient.getName());

        int result = showOfferToRecipient(offer, recipient);

        if (result == 0) {
            try {
                turn.acceptTrade(offer);
                DeviceHandoffDialog.show(parent, activePlayer.getName());
                showStatus(Messages.get("trade_offer_accepted"), COLOR_SUCCESS);
            } catch (IllegalStateException e) {
                DeviceHandoffDialog.show(parent, activePlayer.getName());
                showStatus(e.getMessage(), COLOR_ERROR);
            }
        } else {
            turn.rejectTrade(offer);
            DeviceHandoffDialog.show(parent, activePlayer.getName());
            showStatus(Messages.get("trade_offer_rejected", recipient.getName()), COLOR_ERROR);
        }
    }

    private int showOfferToRecipient(TradeOffer offer, Player recipient) {
        JPanel offerPanel = buildOfferSummaryPanel(offer, recipient);
        String[] options = {
                Messages.get("general_yes"),
                Messages.get("general_no")
        };
        return javax.swing.JOptionPane.showOptionDialog(
                parent,
                offerPanel,
                Messages.get("trade_offer_received", offer.getOfferer().getName()),
                javax.swing.JOptionPane.YES_NO_OPTION,
                javax.swing.JOptionPane.QUESTION_MESSAGE,
                null, options, options[0]);
    }

    private JPanel buildOfferSummaryPanel(TradeOffer offer, Player recipient) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(buildSummaryLabel(
                Messages.get("trade_offer_label") + ":", COLOR_OFFER));
        addResourceSummary(panel, offer.getOffering());

        panel.add(Box.createVerticalStrut(GAP_SMALL));
        panel.add(buildSummaryLabel(
                Messages.get("trade_request_label") + ":", COLOR_REQUEST));
        addResourceSummary(panel, offer.getRequesting());

        panel.add(Box.createVerticalStrut(GAP_SMALL));
        panel.add(buildSummaryLabel(
                recipient.getName() + " — " + buildHandSummary(recipient), Color.DARK_GRAY));

        return panel;
    }

    private JLabel buildSummaryLabel(String text, Color color) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.BOLD, LABEL_FONT));
        label.setForeground(color);
        return label;
    }

    private void addResourceSummary(JPanel panel, Map<ResourceType, Integer> resources) {
        for (Map.Entry<ResourceType, Integer> entry : resources.entrySet()) {
            if (entry.getValue() > 0) {
                JLabel line = new JLabel("  " + resourceName(entry.getKey())
                        + ": " + entry.getValue());
                line.setFont(new Font("SansSerif", Font.PLAIN, LABEL_FONT));
                panel.add(line);
            }
        }
    }

    private String buildHandSummary(Player player) {
        StringBuilder sb = new StringBuilder();
        for (ResourceType type : ResourceType.values()) {
            int count = player.getResourceCount(type);
            if (count > 0) {
                if (sb.length() > 0) {
                    sb.append(", ");
                }
                sb.append(resourceName(type)).append(": ").append(count);
            }
        }
        return sb.length() > 0 ? sb.toString() : "-";
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    private Map<ResourceType, Integer> collectSpinnerValues(Map<ResourceType, JSpinner> spinners) {
        Map<ResourceType, Integer> result = new HashMap<>();
        for (ResourceType type : ResourceType.values()) {
            int val = (int) spinners.get(type).getValue();
            if (val > 0) {
                result.put(type, val);
            }
        }
        return result;
    }

    private boolean isEmpty(Map<ResourceType, Integer> map) {
        return map.isEmpty();
    }

    private void showStatus(String message, Color color) {
        statusLabel.setText(message);
        statusLabel.setForeground(color);
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