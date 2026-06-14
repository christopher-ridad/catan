package ui;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

/**
 * A modal dialog shown between turns in hot-seat multiplayer.
 *
 * Blocks the board from view while the device is physically passed
 * to the next player. Dismissed only by pressing Continue.
 *
 * Usage:
 *   DeviceHandoffDialog.show(mainWindow, "Alice");
 */
public class DeviceHandoffDialog extends JDialog {

    // -------------------------------------------------------------------------
    // Constants
    // -------------------------------------------------------------------------

    private static final int DIALOG_WIDTH  = 420;
    private static final int DIALOG_HEIGHT = 260;

    private static final int TITLE_FONT_SIZE  = 24;
    private static final int PROMPT_FONT_SIZE = 16;
    private static final int INSTRUCTION_FONT_SIZE = 13;
    private static final int BUTTON_FONT_SIZE = 14;

    private static final int BUTTON_WIDTH  = 160;
    private static final int BUTTON_HEIGHT = 44;

    private static final int GAP_SMALL  = 10;
    private static final int GAP_MEDIUM = 20;
    private static final int GAP_LARGE  = 32;

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    private DeviceHandoffDialog(JFrame parent, String nextPlayerName) {
        super(parent, Messages.get("handoff_title"), true);
        initWindow(parent);
        initComponents(nextPlayerName);
    }

    // -------------------------------------------------------------------------
    // Initialization
    // -------------------------------------------------------------------------

    private void initWindow(JFrame parent) {
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setResizable(false);
        setPreferredSize(new Dimension(DIALOG_WIDTH, DIALOG_HEIGHT));
        pack();
        setLocationRelativeTo(parent);
    }

    private void initComponents(String nextPlayerName) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(Box.createVerticalGlue());
        panel.add(buildCenteredLabel(
                Messages.get("handoff_title"), TITLE_FONT_SIZE, Font.BOLD));
        panel.add(Box.createVerticalStrut(GAP_MEDIUM));
        panel.add(buildCenteredLabel(
                Messages.get("handoff_prompt", nextPlayerName), PROMPT_FONT_SIZE, Font.PLAIN));
        panel.add(Box.createVerticalStrut(GAP_SMALL));
        panel.add(buildCenteredLabel(
                Messages.get("handoff_instruction"), INSTRUCTION_FONT_SIZE, Font.ITALIC));
        panel.add(Box.createVerticalStrut(GAP_LARGE));
        panel.add(buildContinueButton());
        panel.add(Box.createVerticalGlue());

        setContentPane(panel);
        pack();
    }

    // -------------------------------------------------------------------------
    // Component Builders
    // -------------------------------------------------------------------------

    private JLabel buildCenteredLabel(String text, int fontSize, int style) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", style, fontSize));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }

    private JButton buildContinueButton() {
        JButton button = new JButton(Messages.get("handoff_continue"));
        button.setFont(new Font("SansSerif", Font.PLAIN, BUTTON_FONT_SIZE));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        button.addActionListener(e -> dispose());
        return button;
    }

    // -------------------------------------------------------------------------
    // Public API
    // -------------------------------------------------------------------------

    /**
     * Shows the handoff dialog and blocks until the user presses Continue.
     *
     * @param parent         the parent JFrame to center the dialog on
     * @param nextPlayerName the name of the player whose turn is starting
     */
    public static void show(JFrame parent, String nextPlayerName) {
        DeviceHandoffDialog dialog = new DeviceHandoffDialog(parent, nextPlayerName);
        dialog.setVisible(true);
    }
}