package ui;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.util.Locale;

/**
 * The first screen shown to the user.
 * Presents language options and sets the active locale before
 * transitioning to the setup phase.
 *
 * All strings are loaded after the locale is set, so the
 * panel refreshes itself immediately upon selection.
 */
public class LanguageSelectionPanel extends JPanel {

    // -------------------------------------------------------------------------
    // Constants
    // -------------------------------------------------------------------------

    private static final int TITLE_FONT_SIZE = 36;
    private static final int PROMPT_FONT_SIZE = 18;
    private static final int BUTTON_FONT_SIZE = 16;
    private static final int BUTTON_WIDTH = 200;
    private static final int BUTTON_HEIGHT = 50;
    private static final int GAP_SMALL = 16;
    private static final int GAP_LARGE = 40;

    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private final Main mainWindow;

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    public LanguageSelectionPanel(Main mainWindow) {
        this.mainWindow = mainWindow;
        initLayout();
        initComponents();
    }

    // -------------------------------------------------------------------------
    // Initialization
    // -------------------------------------------------------------------------

    private void initLayout() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    }

    private void initComponents() {
        add(Box.createVerticalGlue());
        add(buildTitleLabel());
        add(Box.createVerticalStrut(GAP_SMALL));
        add(buildPromptLabel());
        add(Box.createVerticalStrut(GAP_LARGE));
        add(buildLanguageButton("lang_select_english", Locale.ENGLISH));
        add(Box.createVerticalStrut(GAP_SMALL));
        add(buildLanguageButton("lang_select_spanish", Locale.forLanguageTag("es")));
        add(Box.createVerticalGlue());
    }

    // -------------------------------------------------------------------------
    // Component Builders
    // -------------------------------------------------------------------------

    private JLabel buildTitleLabel() {
        JLabel title = new JLabel(Messages.get("lang_select_title"));
        title.setFont(new Font("SansSerif", Font.BOLD, TITLE_FONT_SIZE));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        return title;
    }

    private JLabel buildPromptLabel() {
        JLabel prompt = new JLabel(Messages.get("lang_select_prompt"));
        prompt.setFont(new Font("SansSerif", Font.PLAIN, PROMPT_FONT_SIZE));
        prompt.setAlignmentX(Component.CENTER_ALIGNMENT);
        return prompt;
    }

    private JButton buildLanguageButton(String messageKey, Locale locale) {
        JButton button = new JButton(Messages.get(messageKey));
        button.setFont(new Font("SansSerif", Font.PLAIN, BUTTON_FONT_SIZE));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        button.addActionListener(e -> onLanguageSelected(locale));
        return button;
    }

    // -------------------------------------------------------------------------
    // Event Handlers
    // -------------------------------------------------------------------------

    private void onLanguageSelected(Locale locale) {
        Messages.setLocale(locale);
        mainWindow.showPlayerSetup();
    }
}