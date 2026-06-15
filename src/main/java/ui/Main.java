package ui;

import domain.Bank;
import domain.Game;
import domain.Player;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.CardLayout;
import java.awt.Dimension;

/**
 * The top-level application window.
 *
 * Owns a CardLayout that switches between all game phases:
 *   1. Language selection
 *   2. Player setup (name + color, one player at a time)
 *   3. Setup phase (settlement + road placement)
 *   4. Turn phase (dice, build, trade)
 *   5. Win screen
 *
 * Panel references are tracked directly to avoid CardLayout component leaks.
 * When a phase transitions, the old panel is explicitly removed before the
 * new one is added under the same card name.
 */
public class Main extends JFrame {

    // -------------------------------------------------------------------------
    // Constants
    // -------------------------------------------------------------------------

    private static final int WINDOW_WIDTH  = 1280;
    private static final int WINDOW_HEIGHT = 800;

    private static final String CARD_LANGUAGE     = "LANGUAGE";
    private static final String CARD_PLAYER_SETUP = "PLAYER_SETUP";
    private static final String CARD_SETUP        = "SETUP";
    private static final String CARD_TURN         = "TURN";
    private static final String CARD_WIN          = "WIN";

    // -------------------------------------------------------------------------
    // UI Components
    // -------------------------------------------------------------------------

    private final CardLayout cardLayout;
    private final JPanel cardContainer;

    // Tracked panel references — avoids getCardByName() which never matched
    private JPanel playerSetupPanel;
    private JPanel setupPanel;
    private JPanel turnPanel;
    private JPanel winPanel;

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    public Main() {
        cardLayout = new CardLayout();
        cardContainer = new JPanel(cardLayout);
        initWindow();
        initCards();
        showLanguageSelection();
    }

    // -------------------------------------------------------------------------
    // Initialization
    // -------------------------------------------------------------------------

    private void initWindow() {
        setTitle(Messages.get("lang_select_title"));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        setResizable(false);
        setContentPane(cardContainer);
        pack();
        setLocationRelativeTo(null);
    }

    private void initCards() {
        cardContainer.add(new LanguageSelectionPanel(this), CARD_LANGUAGE);

        playerSetupPanel = new PlayerSetupPanel(this);
        cardContainer.add(playerSetupPanel, CARD_PLAYER_SETUP);

        setupPanel = buildStubPanel();
        cardContainer.add(setupPanel, CARD_SETUP);

        turnPanel = buildStubPanel();
        cardContainer.add(turnPanel, CARD_TURN);

        winPanel = buildStubPanel();
        cardContainer.add(winPanel, CARD_WIN);
    }

    // -------------------------------------------------------------------------
    // Phase Navigation
    // -------------------------------------------------------------------------

    public void showLanguageSelection() {
        setTitle(Messages.get("lang_select_title"));
        cardLayout.show(cardContainer, CARD_LANGUAGE);
    }

    public void showPlayerSetup() {
        setTitle(Messages.get("setup_phase_title"));
        cardContainer.remove(playerSetupPanel);
        playerSetupPanel = new PlayerSetupPanel(this);
        cardContainer.add(playerSetupPanel, CARD_PLAYER_SETUP);
        cardLayout.show(cardContainer, CARD_PLAYER_SETUP);
    }

    public void showSetupPhase(Game game, Bank bank) {
        setTitle(Messages.get("setup_phase_title"));
        cardContainer.remove(setupPanel);
        setupPanel = new SetupPhasePanel(this, game, bank);
        cardContainer.add(setupPanel, CARD_SETUP);
        cardLayout.show(cardContainer, CARD_SETUP);
    }

    public void showTurnPhase(Game game, Bank bank) {
        setTitle(Messages.get("turn_phase_title"));
        cardContainer.remove(turnPanel);
        turnPanel = new TurnPhasePanel(this, game, bank);
        cardContainer.add(turnPanel, CARD_TURN);
        cardLayout.show(cardContainer, CARD_TURN);
    }

    public void showWinScreen(Player winner) {
        setTitle(Messages.get("win_title"));
        cardContainer.remove(winPanel);
        winPanel = new WinPanel(this, winner);
        cardContainer.add(winPanel, CARD_WIN);
        cardLayout.show(cardContainer, CARD_WIN);
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    private JPanel buildStubPanel() {
        return new JPanel();
    }

    // -------------------------------------------------------------------------
    // Entry Point
    // -------------------------------------------------------------------------

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Main window = new Main();
            window.setVisible(true);
        });
    }
}