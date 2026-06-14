package ui;

import domain.Player;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

/**
 * Displayed when a player reaches 10 victory points.
 * Shows the winner's name and a Play Again / Quit option.
 *
 * Play Again restarts from PlayerSetupPanel.
 * Quit exits the application.
 */
public class WinPanel extends JPanel {

    // -------------------------------------------------------------------------
    // Constants
    // -------------------------------------------------------------------------

    private static final int TITLE_FONT_SIZE   = 48;
    private static final int WINNER_FONT_SIZE  = 32;
    private static final int BUTTON_FONT_SIZE  = 16;
    private static final int BUTTON_WIDTH      = 200;
    private static final int BUTTON_HEIGHT     = 48;
    private static final int GAP_SMALL         = 12;
    private static final int GAP_LARGE         = 40;

    private static final Color COLOR_TITLE  = new Color( 60,  90, 160);
    private static final Color COLOR_WINNER = new Color(180,  30,  30);

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    public WinPanel(Main mainWindow, Player winner) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        add(Box.createVerticalGlue());
        add(buildTitleLabel());
        add(Box.createVerticalStrut(GAP_SMALL));
        add(buildWinnerLabel(winner));
        add(Box.createVerticalStrut(GAP_LARGE));
        add(buildPlayAgainButton(mainWindow));
        add(Box.createVerticalStrut(GAP_SMALL));
        add(buildQuitButton());
        add(Box.createVerticalGlue());
    }

    // -------------------------------------------------------------------------
    // Component Builders
    // -------------------------------------------------------------------------

    private JLabel buildTitleLabel() {
        JLabel label = new JLabel(Messages.get("win_title"));
        label.setFont(new Font("SansSerif", Font.BOLD, TITLE_FONT_SIZE));
        label.setForeground(COLOR_TITLE);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }

    private JLabel buildWinnerLabel(Player winner) {
        JLabel label = new JLabel(Messages.get("win_announcement", winner.getName()));
        label.setFont(new Font("SansSerif", Font.BOLD, WINNER_FONT_SIZE));
        label.setForeground(COLOR_WINNER);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }

    private JButton buildPlayAgainButton(Main mainWindow) {
        JButton btn = new JButton(Messages.get("win_play_again"));
        btn.setFont(new Font("SansSerif", Font.PLAIN, BUTTON_FONT_SIZE));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        btn.addActionListener(e -> mainWindow.showPlayerSetup());
        return btn;
    }

    private JButton buildQuitButton() {
        JButton btn = new JButton(Messages.get("win_quit"));
        btn.setFont(new Font("SansSerif", Font.PLAIN, BUTTON_FONT_SIZE));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        btn.addActionListener(e -> System.exit(0));
        return btn;
    }
}