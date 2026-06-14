package ui;

import domain.Board;
import domain.Game;
import domain.Hex;
import domain.Player;
import domain.PlayerColor;
import domain.TerrainType;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.BorderFactory;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Guides players through setup one at a time:
 *   Step 1 — choose number of players (2, 3, or 4)
 *   Step N — each player enters their name and picks a color
 *
 * Once all players are configured, constructs the Game and Board
 * and transitions MainWindow to the setup phase.
 */
public class PlayerSetupPanel extends JPanel {

    // -------------------------------------------------------------------------
    // Constants
    // -------------------------------------------------------------------------

    private static final int TITLE_FONT_SIZE  = 28;
    private static final int LABEL_FONT_SIZE  = 16;
    private static final int BUTTON_FONT_SIZE = 14;
    private static final int INPUT_FONT_SIZE  = 14;

    private static final int BUTTON_WIDTH       = 120;
    private static final int BUTTON_HEIGHT      = 50;
    private static final int COUNT_BUTTON_WIDTH = 80;
    private static final int ACTION_BUTTON_WIDTH = 200;

    private static final int GAP_SMALL  = 12;
    private static final int GAP_MEDIUM = 24;
    private static final int GAP_LARGE  = 40;

    private static final int NAME_FIELD_COLUMNS = 20;

    private static final Color COLOR_RED    = new Color(200,  60,  60);
    private static final Color COLOR_BLUE   = new Color( 60, 100, 200);
    private static final Color COLOR_WHITE  = new Color(220, 220, 220);
    private static final Color COLOR_ORANGE = new Color(230, 130,  30);
    private static final Color COLOR_ERROR  = new Color(180,  30,  30);

    private static final Color SELECTED_BORDER_COLOR = Color.BLACK;
    private static final Color UNSELECTED_BORDER_COLOR = new Color(150, 150, 150);
    private static final int   SELECTED_BORDER_WIDTH = 4;
    private static final int   UNSELECTED_BORDER_WIDTH = 1;
    private static final float UNSELECTED_ALPHA = 0.45f;

    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private final Main mainWindow;

    // -------------------------------------------------------------------------
    // State
    // -------------------------------------------------------------------------

    private int totalPlayers = 0;
    private int currentPlayerIndex = 0;
    private final List<Player> configuredPlayers = new ArrayList<>();
    private final Set<PlayerColor> takenColors = new HashSet<>();
    private PlayerColor selectedColor = null;

    // Keeps references to color buttons so selection state can be updated
    private final Map<PlayerColor, JButton> colorButtons = new HashMap<>();

    // -------------------------------------------------------------------------
    // UI Components
    // -------------------------------------------------------------------------

    private JLabel errorLabel;

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    public PlayerSetupPanel(Main mainWindow) {
        this.mainWindow = mainWindow;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        showPlayerCountStep();
    }

    // -------------------------------------------------------------------------
    // Step 1 — Player Count
    // -------------------------------------------------------------------------

    private void showPlayerCountStep() {
        removeAll();

        add(Box.createVerticalGlue());
        add(buildCenteredLabel(Messages.get("setup_phase_title"), TITLE_FONT_SIZE, Font.BOLD));
        add(Box.createVerticalStrut(GAP_SMALL));
        add(buildCenteredLabel(Messages.get("player_setup_how_many"), LABEL_FONT_SIZE, Font.PLAIN));
        add(Box.createVerticalStrut(GAP_MEDIUM));
        add(buildPlayerCountButtons());
        add(Box.createVerticalGlue());

        refresh();
    }

    private JPanel buildPlayerCountButtons() {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.CENTER, GAP_SMALL, 0));
        row.setOpaque(false);

        for (int count = 2; count <= 4; count++) {
            final int n = count;
            JButton btn = new JButton(String.valueOf(n));
            btn.setFont(new Font("SansSerif", Font.BOLD, BUTTON_FONT_SIZE));
            btn.setPreferredSize(new Dimension(COUNT_BUTTON_WIDTH, BUTTON_HEIGHT));
            btn.addActionListener(e -> onPlayerCountSelected(n));
            row.add(btn);
        }

        return row;
    }

    private void onPlayerCountSelected(int count) {
        totalPlayers = count;
        currentPlayerIndex = 0;
        showPlayerEntryStep();
    }

    // -------------------------------------------------------------------------
    // Step 2..N — Individual Player Entry
    // -------------------------------------------------------------------------

    private void showPlayerEntryStep() {
        removeAll();
        colorButtons.clear();
        selectedColor = null;

        JTextField nameField = buildNameField();

        add(Box.createVerticalGlue());
        add(buildPlayerTitleLabel());
        add(Box.createVerticalStrut(GAP_MEDIUM));
        addNameSection(nameField);
        addColorSection();
        addErrorAndNextSection(nameField);
        add(Box.createVerticalGlue());

        refresh();
    }

    private JLabel buildPlayerTitleLabel() {
        int humanNumber = currentPlayerIndex + 1;
        return buildCenteredLabel(
                Messages.get("player_setup_player_title", humanNumber, totalPlayers),
                TITLE_FONT_SIZE, Font.BOLD);
    }

    private void addNameSection(JTextField nameField) {
        add(buildCenteredLabel(Messages.get("player_setup_name_label"), LABEL_FONT_SIZE, Font.PLAIN));
        add(Box.createVerticalStrut(GAP_SMALL));
        add(nameField);
        add(Box.createVerticalStrut(GAP_MEDIUM));
    }

    private void addColorSection() {
        add(buildCenteredLabel(Messages.get("player_setup_color_label"), LABEL_FONT_SIZE, Font.PLAIN));
        add(Box.createVerticalStrut(GAP_SMALL));
        add(buildColorButtons());
        add(Box.createVerticalStrut(GAP_SMALL));
        add(buildSelectedColorLabel());
        add(Box.createVerticalStrut(GAP_MEDIUM));
    }

    private void addErrorAndNextSection(JTextField nameField) {
        errorLabel = buildCenteredLabel("", LABEL_FONT_SIZE, Font.PLAIN);
        errorLabel.setForeground(COLOR_ERROR);
        add(errorLabel);
        add(Box.createVerticalStrut(GAP_SMALL));
        add(buildNextButton(nameField));
    }

    private JTextField buildNameField() {
        JTextField field = new JTextField(NAME_FIELD_COLUMNS);
        field.setFont(new Font("SansSerif", Font.PLAIN, INPUT_FONT_SIZE));
        field.setMaximumSize(new Dimension(300, 36));
        field.setAlignmentX(Component.CENTER_ALIGNMENT);
        return field;
    }

    private JPanel buildColorButtons() {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.CENTER, GAP_SMALL, 0));
        row.setOpaque(false);

        for (PlayerColor color : PlayerColor.values()) {
            JButton btn = buildColorButton(color);
            colorButtons.put(color, btn);
            row.add(btn);
        }

        return row;
    }

    /**
     * A persistent label that updates to show which color is currently selected.
     * Stored as an instance field so onColorSelected() can update its text.
     */
    private JLabel selectedColorIndicator;

    private JLabel buildSelectedColorLabel() {
        selectedColorIndicator = buildCenteredLabel(
                Messages.get("player_setup_error_color"), LABEL_FONT_SIZE, Font.ITALIC);
        selectedColorIndicator.setForeground(Color.GRAY);
        return selectedColorIndicator;
    }

    private JButton buildColorButton(PlayerColor color) {
        String label = Messages.get(colorMessageKey(color));
        JButton btn = new JButton(label);
        btn.setFont(new Font("SansSerif", Font.BOLD, BUTTON_FONT_SIZE));
        btn.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        btn.setBackground(swingColor(color));
        btn.setForeground(colorForeground(color));
        btn.setOpaque(true);
        btn.setBorder(BorderFactory.createLineBorder(UNSELECTED_BORDER_COLOR, UNSELECTED_BORDER_WIDTH));

        if (takenColors.contains(color)) {
            btn.setEnabled(false);
            btn.setBackground(swingColor(color).darker());
        } else {
            btn.addActionListener(e -> onColorSelected(color));
        }

        return btn;
    }

    private void onColorSelected(PlayerColor color) {
        selectedColor = color;

        // Reset all buttons to unselected appearance
        for (Map.Entry<PlayerColor, JButton> entry : colorButtons.entrySet()) {
            JButton btn = entry.getValue();
            if (!takenColors.contains(entry.getKey())) {
                btn.setBackground(swingColor(entry.getKey()));
                btn.setBorder(BorderFactory.createLineBorder(UNSELECTED_BORDER_COLOR, UNSELECTED_BORDER_WIDTH));
                btn.setText(Messages.get(colorMessageKey(entry.getKey())));
            }
        }

        // Highlight the selected button
        JButton selected = colorButtons.get(color);
        selected.setBorder(BorderFactory.createLineBorder(SELECTED_BORDER_COLOR, SELECTED_BORDER_WIDTH));
        selected.setBackground(swingColor(color).darker());
        selected.setText("✓ " + Messages.get(colorMessageKey(color)));

        // Update the indicator label
        selectedColorIndicator.setText(Messages.get(colorMessageKey(color)) + " selected");
        selectedColorIndicator.setForeground(swingColor(color).darker());

        refresh();
    }

    private JButton buildNextButton(JTextField nameField) {
        boolean isLastPlayer = (currentPlayerIndex == totalPlayers - 1);
        String label = isLastPlayer
                ? Messages.get("player_setup_start")
                : Messages.get("player_setup_next");

        JButton btn = new JButton(label);
        btn.setFont(new Font("SansSerif", Font.PLAIN, BUTTON_FONT_SIZE));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(ACTION_BUTTON_WIDTH, BUTTON_HEIGHT));
        btn.addActionListener(e -> onNextClicked(nameField));
        return btn;
    }

    // -------------------------------------------------------------------------
    // Event Handlers
    // -------------------------------------------------------------------------

    private void onNextClicked(JTextField nameField) {
        String name = nameField.getText().trim();

        if (!isValidName(name)) {
            errorLabel.setText(Messages.get("player_setup_error_name"));
            return;
        }
        if (selectedColor == null) {
            errorLabel.setText(Messages.get("player_setup_error_color"));
            return;
        }

        configuredPlayers.add(new Player(name, selectedColor));
        takenColors.add(selectedColor);
        currentPlayerIndex++;

        if (currentPlayerIndex < totalPlayers) {
            showPlayerEntryStep();
        } else {
            launchGame();
        }
    }

    private void launchGame() {
        Board board = buildFixedBoard();
        Game game = new Game(configuredPlayers, board);
        mainWindow.showSetupPhase(game);
    }

    // -------------------------------------------------------------------------
    // Fixed Board
    // -------------------------------------------------------------------------

    private Board buildFixedBoard() {
        List<Hex> hexes = new ArrayList<>();

        hexes.add(new Hex(TerrainType.MOUNTAINS, 10));
        hexes.add(new Hex(TerrainType.PASTURE,    2));
        hexes.add(new Hex(TerrainType.FOREST,     9));
        hexes.add(new Hex(TerrainType.FIELDS,    12));
        hexes.add(new Hex(TerrainType.HILLS,      6));
        hexes.add(new Hex(TerrainType.PASTURE,    4));
        hexes.add(new Hex(TerrainType.HILLS,     10));
        hexes.add(new Hex(TerrainType.FIELDS,     9));
        hexes.add(new Hex(TerrainType.FOREST,    11));
        hexes.add(new Hex(TerrainType.DESERT,     0));
        hexes.add(new Hex(TerrainType.FOREST,     3));
        hexes.add(new Hex(TerrainType.MOUNTAINS,  8));
        hexes.add(new Hex(TerrainType.FOREST,     8));
        hexes.add(new Hex(TerrainType.MOUNTAINS,  3));
        hexes.add(new Hex(TerrainType.FIELDS,     4));
        hexes.add(new Hex(TerrainType.PASTURE,    5));
        hexes.add(new Hex(TerrainType.HILLS,      5));
        hexes.add(new Hex(TerrainType.FIELDS,     6));
        hexes.add(new Hex(TerrainType.PASTURE,   11));

        return new Board(hexes);
    }

    // -------------------------------------------------------------------------
    // Validation
    // -------------------------------------------------------------------------

    private boolean isValidName(String name) {
        return !name.isEmpty() && name.length() <= 50;
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    private JLabel buildCenteredLabel(String text, int fontSize, int style) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", style, fontSize));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }

    private void refresh() {
        revalidate();
        repaint();
    }

    private static String colorMessageKey(PlayerColor color) {
        switch (color) {
            case RED:    return "color_red";
            case BLUE:   return "color_blue";
            case WHITE:  return "color_white";
            case ORANGE: return "color_orange";
            default:     return "color_red";
        }
    }

    private static Color swingColor(PlayerColor color) {
        switch (color) {
            case RED:    return COLOR_RED;
            case BLUE:   return COLOR_BLUE;
            case WHITE:  return COLOR_WHITE;
            case ORANGE: return COLOR_ORANGE;
            default:     return Color.GRAY;
        }
    }

    private static Color colorForeground(PlayerColor color) {
        return color == PlayerColor.WHITE ? Color.DARK_GRAY : Color.WHITE;
    }
}