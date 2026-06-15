package ui;

import domain.DevelopmentCardType;
import domain.ResourceType;

/**
 * Centralized localized string helpers for domain enum display.
 *
 * Eliminates duplicated resourceName/cardName switch statements that
 * previously appeared in PlayerHandView, TradeDialog, MaritimeTradeDialog,
 * PlayCardDialog, and DevCardHandView.
 *
 * All methods delegate to Messages.get() so strings respond to locale changes.
 */
public final class UIStrings {

    private UIStrings() {
        // Utility class — not instantiable
    }

    /**
     * Returns the localized display name for a resource type.
     *
     * @param type the resource type
     * @return localized name
     */
    public static String resourceName(ResourceType type) {
        switch (type) {
            case BRICK:  return Messages.get("resource_brick");
            case LUMBER: return Messages.get("resource_wood");
            case ORE:    return Messages.get("resource_ore");
            case GRAIN:  return Messages.get("resource_wheat");
            case WOOL:   return Messages.get("resource_sheep");
            default:     return "[" + type + "]";
        }
    }

    /**
     * Returns the localized display name for a development card type.
     *
     * @param type the development card type
     * @return localized name
     */
    public static String cardName(DevelopmentCardType type) {
        switch (type) {
            case KNIGHT:         return Messages.get("dev_card_knight");
            case ROAD_BUILDING:  return Messages.get("dev_card_road_building");
            case YEAR_OF_PLENTY: return Messages.get("dev_card_year_of_plenty");
            case MONOPOLY:       return Messages.get("dev_card_monopoly");
            case VICTORY_POINT:  return Messages.get("dev_card_victory_point");
            default:             return "[" + type + "]";
        }
    }
}