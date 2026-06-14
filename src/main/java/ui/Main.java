package ui;

import domain.Game;

import javax.swing.JFrame;
import javax.swing.JLabel;
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
 *   3. Setup phase (board: settlement + road placement)
 *   4. Turn phase
 *   5. Win screen
 *
 * Phase panels are responsible for their own logic.
 * MainWindow only controls which panel is visible.
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
        cardContainer.add(new PlayerSetupPanel(this),       CARD_PLAYER_SETUP);
        cardContainer.add(buildStubPanel("setup_phase_title"), CARD_SETUP);
        cardContainer.add(buildStubPanel("turn_phase_title"),  CARD_TURN);
        cardContainer.add(buildStubPanel("win_title"),         CARD_WIN);
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
        cardLayout.show(cardContainer, CARD_PLAYER_SETUP);
    }

    public void showSetupPhase(Game game) {
        setTitle(Messages.get("setup_phase_title"));
        cardContainer.add(new SetupPhasePanel(this, game), CARD_SETUP);
        cardLayout.show(cardContainer, CARD_SETUP);
    }

    public void showTurnPhase(Game game) {
        setTitle(Messages.get("turn_phase_title"));
        cardLayout.show(cardContainer, CARD_TURN);
    }

    public void showWinScreen(Game game) {
        setTitle(Messages.get("win_title"));
        cardLayout.show(cardContainer, CARD_WIN);
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    private JPanel buildStubPanel(String titleKey) {
        JPanel stub = new JPanel();
        stub.add(new JLabel(Messages.get(titleKey) + " — coming soon"));
        return stub;
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