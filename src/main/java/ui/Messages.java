package ui;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Centralized i18n accessor for all UI strings.
 *
 * All UI code retrieves strings exclusively through this class.
 * To add a new locale, add a new messages_XX.properties file
 * in the i18n resource directory — no code changes required.
 *
 * Usage:
 *   Messages.setLocale(Locale.forLanguageTag("es"));
 *   String text = Messages.get("setup_place_settlement_prompt");
 *   String formatted = Messages.get("turn_rolled", 3, 4, 7);
 */
public final class Messages {

    // -------------------------------------------------------------------------
    // Constants
    // -------------------------------------------------------------------------

    private static final String BUNDLE_BASE_NAME = "domain.messages";

    // -------------------------------------------------------------------------
    // State
    // -------------------------------------------------------------------------

    private static ResourceBundle bundle = ResourceBundle.getBundle(
            BUNDLE_BASE_NAME, Locale.ENGLISH);

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    private Messages() {
        // Utility class — not instantiable
    }

    // -------------------------------------------------------------------------
    // Public API
    // -------------------------------------------------------------------------

    /**
     * Sets the active locale and reloads the resource bundle.
     * Call this once at startup when the user selects a language.
     *
     * @param locale the locale chosen by the user
     */
    public static void setLocale(Locale locale) {
        bundle = ResourceBundle.getBundle(BUNDLE_BASE_NAME, locale);
    }

    /**
     * Returns the current locale in use.
     *
     * @return the active Locale
     */
    public static Locale getLocale() {
        return bundle.getLocale();
    }

    /**
     * Returns the localized string for the given key.
     *
     * @param key the property key
     * @return the localized string, or the key itself in brackets if missing
     */
    public static String get(String key) {
        if (bundle.containsKey(key)) {
            return bundle.getString(key);
        }
        return "[" + key + "]";
    }

    /**
     * Returns the localized string for the given key, with {0}, {1}, ...
     * placeholders substituted using MessageFormat.
     *
     * @param key  the property key
     * @param args values to substitute into the pattern
     * @return the formatted localized string
     */
    public static String get(String key, Object... args) {
        return MessageFormat.format(get(key), args);
    }
}
