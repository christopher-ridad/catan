package ui;

import domain.Bank;
import domain.Game;
import domain.Player;
import domain.SetupPhase;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

/**
 * Drives the setup phase: each player places a settlement then a road,
 * twice (snake draft order), with a device handoff between turns.
 *
 * Flow per turn:
 *   1. Show prompt: "Place your settlement"
 *   2. Player clicks a vertex on BoardPanel → placeSettlement()
 *   3. Show prompt: "Place your road"
 *   4. Player clicks an edge on BoardPanel → placeRoad()
 *   5. DeviceHandoffDialog shown for next player
 *   6. Repeat until isComplete() → transition to TurnPhasePanel
 *
 * This panel owns the SetupPhase domain object and coordinates
 * between BoardPanel click events and domain calls.
 */
public class SetupPhasePanel extends JPanel {

    // -------------------------------------------------------------------------
    // Constants
    // -------------------------------------------------------------------------

    private static final int SIDE_PANEL_WIDTH  = 420;
    private static final int PROMPT_FONT_SIZE  = 17;
    private static final int PLAYER_FONT_SIZE  = 22;
    private static final int STATUS_FONT_SIZE  = 15;
    private static final int BUTTON_FONT_SIZE  = 15;
    private static final int BUTTON_HEIGHT     = 44;
    private static final int GAP_SMALL         = 8;
    private static final int GAP_MEDIUM        = 16;

    private static final Color COLOR_ERROR     = new Color(180, 30, 30);
    private static final Color COLOR_SUCCESS   = new Color(30, 130, 30);
    private static final Color COLOR_SIDE_BG   = new Color(245, 245, 240);

    // -------------------------------------------------------------------------
    // Domain
    // -------------------------------------------------------------------------

    private final SetupPhase setupPhase;
    private final Game game;

    // -------------------------------------------------------------------------
    // UI
    // -------------------------------------------------------------------------

    private final Main mainWindow;
    private final BoardPanel boardPanel;

    private JLabel currentPlayerLabel;
    private JLabel promptLabel;
    private JLabel statusLabel;

    // -------------------------------------------------------------------------
    // State
    // -------------------------------------------------------------------------

    private enum PlacementStep { SETTLEMENT, ROAD }
    private PlacementStep currentStep = PlacementStep.SETTLEMENT;

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    public SetupPhasePanel(Main mainWindow, Game game) {
        this.mainWindow = mainWindow;
        this.game = game;
        this.setupPhase = new SetupPhase(game, new Bank());
        this.boardPanel = new BoardPanel(game.getBoard(), this::onVertexClicked, this::onEdgeClicked, null);

        setLayout(new BorderLayout());
        add(boardPanel, BorderLayout.CENTER);
        add(buildSidePanel(), BorderLayout.EAST);

        refreshPrompt();
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

        currentPlayerLabel = buildLabel("", PLAYER_FONT_SIZE, Font.BOLD);
        promptLabel        = buildLabel("", PROMPT_FONT_SIZE, Font.PLAIN);
        statusLabel        = buildLabel("", STATUS_FONT_SIZE, Font.ITALIC);

        side.add(Box.createVerticalStrut(GAP_MEDIUM));
        side.add(buildLabel(Messages.get("setup_phase_title"), PLAYER_FONT_SIZE, Font.BOLD));
        side.add(Box.createVerticalStrut(GAP_MEDIUM));
        side.add(currentPlayerLabel);
        side.add(Box.createVerticalStrut(GAP_MEDIUM));
        side.add(promptLabel);
        side.add(Box.createVerticalStrut(GAP_SMALL));
        side.add(statusLabel);
        side.add(Box.createVerticalGlue());

        return side;
    }

    // -------------------------------------------------------------------------
    // Board Click Callbacks
    // -------------------------------------------------------------------------

    private void onVertexClicked(int vertexId) {
        if (currentStep != PlacementStep.SETTLEMENT) {
            showStatus(Messages.get("setup_invalid_vertex"), COLOR_ERROR);
            return;
        }

        Player player = setupPhase.getCurrentPlayer();
        try {
            setupPhase.placeSettlement(player, vertexId);
            currentStep = PlacementStep.ROAD;
            showStatus(Messages.get("build_settlement_placed"), COLOR_SUCCESS);
            refreshPrompt();
        } catch (IllegalStateException e) {
            showStatus(Messages.get("setup_invalid_vertex"), COLOR_ERROR);
        }
    }

    private void onEdgeClicked(int edgeId) {
        if (currentStep != PlacementStep.ROAD) {
            showStatus(Messages.get("setup_invalid_edge"), COLOR_ERROR);
            return;
        }

        Player player = setupPhase.getCurrentPlayer();
        try {
            setupPhase.placeRoad(player, edgeId);
            boardPanel.repaint();
            currentStep = PlacementStep.SETTLEMENT;

            if (setupPhase.isComplete()) {
                mainWindow.showTurnPhase(game);
            } else {
                showStatus(Messages.get("build_road_placed"), COLOR_SUCCESS);
                DeviceHandoffDialog.show(mainWindow, setupPhase.getCurrentPlayer().getName());
                refreshPrompt();
            }
        } catch (IllegalStateException e) {
            showStatus(Messages.get("setup_invalid_edge"), COLOR_ERROR);
        }
    }

    // -------------------------------------------------------------------------
    // UI Updates
    // -------------------------------------------------------------------------

    private void refreshPrompt() {
        if (setupPhase.isComplete()) {
            return;
        }

        Player player = setupPhase.getCurrentPlayer();
        currentPlayerLabel.setText(player.getName());

        if (currentStep == PlacementStep.SETTLEMENT) {
            String key = setupPhase.getCurrentRound() == 1
                    ? "setup_place_settlement_prompt"
                    : "setup_place_settlement_prompt_round2";
            promptLabel.setText(Messages.get(key));
        } else {
            promptLabel.setText(Messages.get("setup_place_road_prompt"));
        }

        statusLabel.setText("");
        boardPanel.repaint();
    }

    private void showStatus(String message, Color color) {
        statusLabel.setText(message);
        statusLabel.setForeground(color);
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    private JLabel buildLabel(String text, int fontSize, int style) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", style, fontSize));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }
}