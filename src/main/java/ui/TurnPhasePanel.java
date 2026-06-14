package ui;

import domain.Bank;
import domain.DiceRoll;
import domain.Game;
import domain.Player;
import domain.ResourceType;
import domain.Turn;
import domain.TurnManager;
import domain.TurnPhase;
import domain.VictoryPointCalculator;
import domain.SpecialCardTracker;
import domain.DevelopmentCardType;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.util.List;
import java.util.Random;

/**
 * Drives the main game loop turn by turn.
 *
 * Owns the TurnManager and coordinates between BoardPanel click events
 * and domain calls. The side panel shows the current player, their
 * resource hand, VP count, phase, and contextual action buttons.
 *
 * Robber flow: when a 7 is rolled, hex clicks move the robber.
 * If candidates exist after moving, a steal dialog is shown.
 */
public class TurnPhasePanel extends JPanel {

    // -------------------------------------------------------------------------
    // Constants
    // -------------------------------------------------------------------------

    private static final int SIDE_PANEL_WIDTH  = 420;
    private static final int PLAYER_FONT_SIZE  = 22;
    private static final int LABEL_FONT_SIZE   = 15;
    private static final int BUTTON_FONT_SIZE  = 15;
    private static final int BUTTON_HEIGHT     = 44;
    private static final int BUTTON_MAX_WIDTH  = 380;
    private static final int GAP_SMALL         = 6;
    private static final int GAP_MEDIUM        = 14;

    private static final Color COLOR_SIDE_BG  = new Color(245, 245, 240);
    private static final Color COLOR_ERROR    = new Color(180,  30,  30);
    private static final Color COLOR_SUCCESS  = new Color( 30, 130,  30);
    private static final Color COLOR_PHASE    = new Color( 60,  90, 160);

    // -------------------------------------------------------------------------
    // Domain
    // -------------------------------------------------------------------------

    private final Game game;
    private final TurnManager turnManager;
    private final VictoryPointCalculator vpCalc;
    private final SpecialCardTracker specialCardTracker;
    private Turn currentTurn;

    // -------------------------------------------------------------------------
    // UI
    // -------------------------------------------------------------------------

    private final Main mainWindow;
    private final BoardPanel boardPanel;
    private final PlayerHandView handView;

    private JLabel playerNameLabel;
    private JLabel vpLabel;
    private JLabel phaseLabel;
    private JTextArea statusArea;

    private JButton rollButton;
    private JButton advanceButton;
    private JButton tradeButton;
    private JButton maritimeTradeButton;
    private JButton buildSettlementButton;
    private JButton buildCityButton;
    private JButton buildRoadButton;
    private JButton buyDevCardButton;
    private JButton endTurnButton;

    private final DevCardHandView devCardHandView = new DevCardHandView();
    private final SpecialCardsView specialCardsView = new SpecialCardsView();

    // -------------------------------------------------------------------------
    // State
    // -------------------------------------------------------------------------

    private enum BoardClickMode { NONE, PLACE_SETTLEMENT, PLACE_CITY, PLACE_ROAD, MOVE_ROBBER }
    private BoardClickMode clickMode = BoardClickMode.NONE;

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    public TurnPhasePanel(Main mainWindow, Game game, Bank bank) {
        this.mainWindow = mainWindow;
        this.game = game;
        this.turnManager = new TurnManager(game, bank, new DiceRoll(new Random()));
        this.vpCalc = new VictoryPointCalculator();
        this.specialCardTracker = new SpecialCardTracker();
        this.handView = new PlayerHandView();
        this.boardPanel = new BoardPanel(
                game.getBoard(),
                this::onVertexClicked,
                this::onEdgeClicked,
                this::onHexClicked);

        setLayout(new BorderLayout());
        add(boardPanel, BorderLayout.CENTER);
        add(buildSidePanel(), BorderLayout.EAST);

        startNextTurn();
    }

    // -------------------------------------------------------------------------
    // Side Panel
    // -------------------------------------------------------------------------

    private JPanel buildSidePanel() {
        JPanel side = new JPanel();
        side.setLayout(new BoxLayout(side, BoxLayout.Y_AXIS));
        side.setBackground(COLOR_SIDE_BG);
        side.setBorder(BorderFactory.createEmptyBorder(GAP_MEDIUM, GAP_MEDIUM, GAP_MEDIUM, GAP_MEDIUM));
        side.setPreferredSize(new Dimension(SIDE_PANEL_WIDTH, 0));
        initSideLabels();
        addSideComponents(side);
        return side;
    }

    private void initSideLabels() {
        playerNameLabel = buildLabel("", PLAYER_FONT_SIZE, Font.BOLD);
        vpLabel         = buildLabel("", LABEL_FONT_SIZE, Font.PLAIN);
        phaseLabel      = buildLabel("", LABEL_FONT_SIZE, Font.BOLD);
        phaseLabel.setForeground(COLOR_PHASE);
        statusArea      = buildStatusArea();
    }

    private void addSideComponents(JPanel side) {
        side.add(Box.createVerticalStrut(GAP_SMALL));
        side.add(playerNameLabel);
        side.add(vpLabel);
        side.add(Box.createVerticalStrut(GAP_MEDIUM));
        side.add(handView);
        side.add(Box.createVerticalStrut(GAP_SMALL));
        side.add(devCardHandView);
        side.add(Box.createVerticalStrut(GAP_SMALL));
        side.add(specialCardsView);
        side.add(Box.createVerticalStrut(GAP_MEDIUM));
        side.add(phaseLabel);
        side.add(Box.createVerticalStrut(GAP_SMALL));
        side.add(statusArea);
        side.add(Box.createVerticalStrut(GAP_MEDIUM));
        side.add(buildActionButtons());
        side.add(Box.createVerticalGlue());
    }

    private JPanel buildActionButtons() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        initButtons();
        wireButtonListeners();
        addButtonsToPanel(panel);
        return panel;
    }

    private void initButtons() {
        rollButton            = buildButton(Messages.get("turn_roll_dice"));
        advanceButton         = buildButton(Messages.get("turn_advance_to_build"));
        tradeButton           = buildButton(Messages.get("trade_title"));
        maritimeTradeButton   = buildButton(Messages.get("trade_maritime_title"));
        buildSettlementButton = buildButton(Messages.get("turn_build_settlement"));
        buildCityButton       = buildButton(Messages.get("turn_build_city"));
        buildRoadButton       = buildButton(Messages.get("turn_build_road"));
        buyDevCardButton      = buildButton(Messages.get("dev_card_buy"));
        endTurnButton         = buildButton(Messages.get("turn_end_turn"));
    }

    private void wireButtonListeners() {
        rollButton.addActionListener(e -> onRollDice());
        advanceButton.addActionListener(e -> onAdvanceToBuild());
        tradeButton.addActionListener(e -> onTrade());
        maritimeTradeButton.addActionListener(e -> onMaritimeTrade());
        buildSettlementButton.addActionListener(e -> onBuildSettlement());
        buildCityButton.addActionListener(e -> onBuildCity());
        buildRoadButton.addActionListener(e -> onBuildRoad());
        buyDevCardButton.addActionListener(e -> onBuyDevCard());
        endTurnButton.addActionListener(e -> onEndTurn());
    }

    private void addButtonsToPanel(JPanel panel) {
        panel.add(rollButton);
        panel.add(Box.createVerticalStrut(GAP_SMALL));
        panel.add(advanceButton);
        panel.add(Box.createVerticalStrut(GAP_SMALL));
        panel.add(tradeButton);
        panel.add(Box.createVerticalStrut(GAP_SMALL));
        panel.add(maritimeTradeButton);
        panel.add(Box.createVerticalStrut(GAP_SMALL));
        panel.add(buildSettlementButton);
        panel.add(Box.createVerticalStrut(GAP_SMALL));
        panel.add(buildCityButton);
        panel.add(Box.createVerticalStrut(GAP_SMALL));
        panel.add(buildRoadButton);
        panel.add(Box.createVerticalStrut(GAP_SMALL));
        panel.add(buyDevCardButton);
        panel.add(Box.createVerticalStrut(GAP_SMALL));
        panel.add(endTurnButton);
    }

    // -------------------------------------------------------------------------
    // Turn Management
    // -------------------------------------------------------------------------

    private void startNextTurn() {
        currentTurn = turnManager.startNextTurn();
        clickMode = BoardClickMode.NONE;
        DeviceHandoffDialog.show(mainWindow, turnManager.getCurrentPlayer().getName());
        refreshUI();
    }

    // -------------------------------------------------------------------------
    // Action Handlers
    // -------------------------------------------------------------------------

    private void onRollDice() {
        try {
            int roll = currentTurn.rollDice();
            if (currentTurn.isSevenRolled()) {
                showStatus(Messages.get("turn_seven_rolled"), COLOR_ERROR);
                clickMode = BoardClickMode.MOVE_ROBBER;
                showStatus(Messages.get("turn_move_robber"), COLOR_ERROR);
            } else {
                showStatus(Messages.get("turn_rolled",
                        "", "", roll), COLOR_SUCCESS);
            }
            refreshUI();
        } catch (IllegalStateException e) {
            showStatus(e.getMessage(), COLOR_ERROR);
        }
    }

    private void onAdvanceToBuild() {
        try {
            currentTurn.advanceToBuild();
            clickMode = BoardClickMode.NONE;
            refreshUI();
        } catch (IllegalStateException e) {
            showStatus(e.getMessage(), COLOR_ERROR);
        }
    }

    private void onBuildSettlement() {
        clickMode = BoardClickMode.PLACE_SETTLEMENT;
        showStatus(Messages.get("setup_place_settlement_prompt"), COLOR_PHASE);
    }

    private void onBuildCity() {
        clickMode = BoardClickMode.PLACE_CITY;
        showStatus(Messages.get("build_city"), COLOR_PHASE);
    }

    private void onBuildRoad() {
        clickMode = BoardClickMode.PLACE_ROAD;
        showStatus(Messages.get("setup_place_road_prompt"), COLOR_PHASE);
    }

    private void onTrade() {
        Player activePlayer = turnManager.getCurrentPlayer();
        List<Player> others = new java.util.ArrayList<>(game.getPlayers());
        others.remove(activePlayer);
        if (others.isEmpty()) {
            showStatus(Messages.get("trade_no_players"), COLOR_ERROR);
            return;
        }
        TradeDialog dialog = new TradeDialog(mainWindow, currentTurn, activePlayer, others);
        dialog.setVisible(true);
        refreshUI();
    }

    private void onMaritimeTrade() {
        Player activePlayer = turnManager.getCurrentPlayer();
        MaritimeTradeDialog dialog = new MaritimeTradeDialog(
                mainWindow, currentTurn, activePlayer, game.getBoard());
        dialog.setVisible(true);
        refreshUI();
    }

    private void onBuyDevCard() {
        try {
            currentTurn.buyDevelopmentCard();
            showStatus(Messages.get("dev_card_buy"), COLOR_SUCCESS);
            refreshUI();
        } catch (IllegalStateException e) {
            showStatus(e.getMessage(), COLOR_ERROR);
        }
    }

    private void onEndTurn() {
        try {
            turnManager.endCurrentTurn();
            if (turnManager.isGameOver()) {
                Player winner = turnManager.getWinner().get();
                mainWindow.showWinScreen(winner);
                return;
            }
            startNextTurn();
        } catch (IllegalStateException e) {
            showStatus(e.getMessage(), COLOR_ERROR);
        }
    }

    // -------------------------------------------------------------------------
    // Board Click Callbacks
    // -------------------------------------------------------------------------

    private void onVertexClicked(int vertexId) {
        if (clickMode == BoardClickMode.PLACE_SETTLEMENT) {
            tryBuildSettlement(vertexId);
        } else if (clickMode == BoardClickMode.PLACE_CITY) {
            tryBuildCity(vertexId);
        }
    }

    private void onEdgeClicked(int edgeId) {
        if (clickMode == BoardClickMode.PLACE_ROAD) {
            tryBuildRoad(edgeId);
        }
    }

    private void onHexClicked(int hexId) {
        if (clickMode != BoardClickMode.MOVE_ROBBER) {
            return;
        }
        try {
            currentTurn.moveRobber(hexId);
            showStatus(Messages.get("turn_robber_moved"), COLOR_SUCCESS);
            clickMode = BoardClickMode.NONE;
            boardPanel.repaint();
            handleStealIfNeeded();
            refreshUI();
        } catch (IllegalArgumentException e) {
            showStatus(e.getMessage(), COLOR_ERROR);
        }
    }

    // -------------------------------------------------------------------------
    // Build Helpers
    // -------------------------------------------------------------------------

    private void tryBuildSettlement(int vertexId) {
        try {
            currentTurn.buildSettlement(vertexId);
            clickMode = BoardClickMode.NONE;
            showStatus(Messages.get("build_settlement_placed"), COLOR_SUCCESS);
            boardPanel.repaint();
            refreshUI();
        } catch (IllegalStateException e) {
            showStatus(Messages.get("build_invalid_location"), COLOR_ERROR);
        }
    }

    private void tryBuildCity(int vertexId) {
        try {
            currentTurn.buildCity(vertexId);
            clickMode = BoardClickMode.NONE;
            showStatus(Messages.get("build_city_placed"), COLOR_SUCCESS);
            boardPanel.repaint();
            refreshUI();
        } catch (IllegalStateException e) {
            showStatus(Messages.get("build_invalid_location"), COLOR_ERROR);
        }
    }

    private void tryBuildRoad(int edgeId) {
        try {
            currentTurn.buildRoad(edgeId);
            clickMode = BoardClickMode.NONE;
            showStatus(Messages.get("build_road_placed"), COLOR_SUCCESS);
            boardPanel.repaint();
            refreshUI();
        } catch (IllegalStateException e) {
            showStatus(Messages.get("build_invalid_location"), COLOR_ERROR);
        }
    }

    // -------------------------------------------------------------------------
    // Robber / Steal
    // -------------------------------------------------------------------------

    private void handleStealIfNeeded() {
        List<Player> candidates = currentTurn.getRobbingCandidates();
        if (candidates.isEmpty()) {
            return;
        }
        Player target = showStealDialog(candidates);
        if (target != null) {
            try {
                currentTurn.steal(target);
                refreshUI();
            } catch (IllegalArgumentException e) {
                showStatus(e.getMessage(), COLOR_ERROR);
            }
        }
    }

    private Player showStealDialog(List<Player> candidates) {
        String[] names = candidates.stream()
                .map(Player::getName)
                .toArray(String[]::new);

        JList<String> list = new JList<>(names);
        list.setSelectedIndex(0);

        int result = JOptionPane.showConfirmDialog(
                mainWindow,
                new JScrollPane(list),
                Messages.get("turn_steal_prompt"),
                JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            return candidates.get(list.getSelectedIndex());
        }
        return null;
    }

    // -------------------------------------------------------------------------
    // UI Refresh
    // -------------------------------------------------------------------------

    private void refreshUI() {
        Player player = turnManager.getCurrentPlayer();
        playerNameLabel.setText(player.getName());

        int vp = vpCalc.getTotalVP(player, game.getBoard(), specialCardTracker);
        vpLabel.setText(Messages.get("turn_vp_count", vp));

        handView.refresh(player);
        devCardHandView.refresh(player);
        specialCardsView.refresh(player, specialCardTracker);
        updatePhaseLabel();
        updateButtonStates();
        boardPanel.repaint();
    }

    private void updatePhaseLabel() {
        TurnPhase phase = currentTurn.getPhase();
        String phaseKey;
        switch (phase) {
            case PRODUCTION:
                phaseKey = "turn_phase_production";
                break;
            case TRADE:
                phaseKey = "turn_phase_trade";
                break;
            case BUILD:
                phaseKey = "turn_phase_build";
                break;
            default:
                phaseKey = "turn_phase_production";
                break;
        }
        phaseLabel.setText(Messages.get("turn_phase_label", Messages.get(phaseKey)));
    }

    private void updateButtonStates() {
        TurnPhase phase = currentTurn.getPhase();
        Player player = turnManager.getCurrentPlayer();

        rollButton.setEnabled(phase == TurnPhase.PRODUCTION);
        advanceButton.setEnabled(phase == TurnPhase.TRADE);
        tradeButton.setEnabled(phase == TurnPhase.TRADE);
        maritimeTradeButton.setEnabled(phase == TurnPhase.TRADE);
        buildSettlementButton.setEnabled(phase == TurnPhase.BUILD && canAffordSettlement(player));
        buildCityButton.setEnabled(phase == TurnPhase.BUILD && canAffordCity(player));
        buildRoadButton.setEnabled(phase == TurnPhase.BUILD && canAffordRoad(player));
        buyDevCardButton.setEnabled(phase == TurnPhase.BUILD
                && canAffordDevCard(player)
                && currentTurn.getRemainingDeckSize() > 0);
        endTurnButton.setEnabled(phase == TurnPhase.BUILD);
    }

    private boolean canAffordRoad(Player player) {
        return player.getResourceCount(ResourceType.BRICK) >= 1
                && player.getResourceCount(ResourceType.LUMBER) >= 1;
    }

    private boolean canAffordSettlement(Player player) {
        return player.getResourceCount(ResourceType.BRICK)  >= 1
                && player.getResourceCount(ResourceType.LUMBER) >= 1
                && player.getResourceCount(ResourceType.WOOL)   >= 1
                && player.getResourceCount(ResourceType.GRAIN)  >= 1;
    }

    private boolean canAffordCity(Player player) {
        return player.getResourceCount(ResourceType.ORE)   >= 3
                && player.getResourceCount(ResourceType.GRAIN) >= 2;
    }

    private boolean canAffordDevCard(Player player) {
        return player.getResourceCount(ResourceType.ORE)   >= 1
                && player.getResourceCount(ResourceType.GRAIN) >= 1
                && player.getResourceCount(ResourceType.WOOL)  >= 1;
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    private void showStatus(String message, Color color) {
        statusArea.setText(message);
        statusArea.setForeground(color);
    }

    private JTextArea buildStatusArea() {
        JTextArea area = new JTextArea(3, 18);
        area.setFont(new Font("SansSerif", Font.ITALIC, LABEL_FONT_SIZE));
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setEditable(false);
        area.setFocusable(false);
        area.setOpaque(false);
        area.setAlignmentX(Component.LEFT_ALIGNMENT);
        area.setMaximumSize(new Dimension(BUTTON_MAX_WIDTH, 70));
        return area;
    }

    private JLabel buildLabel(String text, int fontSize, int style) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", style, fontSize));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private JButton buildButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("SansSerif", Font.PLAIN, BUTTON_FONT_SIZE));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setMaximumSize(new Dimension(BUTTON_MAX_WIDTH, BUTTON_HEIGHT));
        return btn;
    }
}