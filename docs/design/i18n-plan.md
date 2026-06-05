# Internationalization (i18n) Plan

## Supported Languages

| Language | Locale Code | Status      |
|----------|-------------|-------------|
| English  | (default)   | Planned     |
| Spanish  | `es`        | Planned     |

English is the default locale. Spanish is the first additional language to be implemented.

## Technical Approach

### Java ResourceBundle + Properties Files

All user-facing strings will be externalized from Java source into `.properties` files and loaded at runtime via `java.util.ResourceBundle`. This follows the standard Java i18n pattern and means **adding a new locale requires only a new `.properties` file — no source code changes**.

### File Structure

```
src/main/resources/
  messages.properties        # English (default)
  messages_es.properties     # Spanish
```

Each file uses the `baseName_language.properties` naming convention that Java's `ResourceBundle` uses for automatic locale resolution.

### Example

**`src/main/resources/messages.properties`** (English default)
```properties
bank.error.insufficient=Insufficient resources in bank
bank.error.type.null=Resource type cannot be null
player.error.name.null=Player name cannot be null
```

**`src/main/resources/messages_es.properties`** (Spanish)
```properties
bank.error.insufficient=Recursos insuficientes en el banco
bank.error.type.null=El tipo de recurso no puede ser nulo
player.error.name.null=El nombre del jugador no puede ser nulo
```

**Java usage with `ResourceBundle`:**
```java
ResourceBundle messages = ResourceBundle.getBundle("messages");
throw new IllegalArgumentException(messages.getString("bank.error.insufficient"));
```

For strings with dynamic values, use `MessageFormat` instead of string concatenation to handle differing word orders across languages:
```java
String msg = MessageFormat.format(messages.getString("setup.error.vertex.occupied"), vertexId, ownerName);
```

### Key Principles

- **Externalized strings** — no hardcoded user-facing text in `.java` files; all strings live in `.properties` files
- **ResourceBundle** — Java's standard i18n mechanism; automatically picks the right file based on the active `Locale`
- **MessageFormat** — used for parameterized strings (e.g., "Vertex {0} is occupied by {1}") instead of `+` concatenation, which breaks for languages with different word orders
- **Open to extension** — adding a new language (e.g., `messages_fr.properties` for French, `messages_de.properties` for German) requires no changes to existing Java code

## Scope of Strings to Externalize

The following categories of user-facing strings will be moved to `.properties` files:

- Validation error messages in domain classes (`Bank`, `Player`, `Game`, `BuildManager`, `SetupPhase`, `Board`, `Vertex`, `Edge`, `Hex`, `Turn`)
- Any UI output strings added in `ui/` as the interface is developed

Internal strings not shown to users (e.g., internal assertions, log messages) do not need to be externalized.

## Implementation Progress

- [ ] Create `src/main/resources/messages.properties` with all English strings
- [ ] Create `src/main/resources/messages_es.properties` with Spanish translations
- [ ] Update `Bank` to use `ResourceBundle`
- [ ] Update `Player` to use `ResourceBundle`
- [ ] Update `Game` to use `ResourceBundle`
- [ ] Update `BuildManager` to use `ResourceBundle`
- [ ] Update `SetupPhase` to use `ResourceBundle`
- [ ] Update `Board`, `Vertex`, `Edge`, `Hex`, `Turn` to use `ResourceBundle`
- [ ] Update any UI strings in `ui/` as that layer is developed
