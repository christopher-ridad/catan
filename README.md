# Catan

A full Java implementation of Settlers of Catan, built as a 5-person team project for Northwestern's Software Quality Engineering course, with a strong emphasis on test-driven development, boundary value analysis, and design-before-code practice.

![Gradle Build](https://github.com/nu-cs-sqe/course-project-20252603-team-16-20252603/actions/workflows/main.yml/badge.svg)

---

## What it is

A hot-seat (pass-and-play) desktop implementation of Catan covering the full game loop: board setup, settlements and roads, resource production, dice rolls, development cards, maritime and player trading, special cards (Longest Road, Largest Army), and win condition tracking.

## Highlights

- **615 unit tests** across the domain layer, written test-first following TDD practice
- **100% mutation test threshold** on core domain classes (Board, Edge, Vertex, DiceRoll, Bank, and others) enforced via PIT, going beyond line coverage to check that tests actually catch introduced bugs
- **JaCoCo code coverage** and **SpotBugs static analysis** run as part of the build
- **Boundary value analysis docs** (`docs/bva/`) for every core domain class, documenting edge cases before implementation
- **Design docs written before code** (`docs/design/`) for setup, turn flow, multi-turn play, and win conditions
- **Internationalization support**: all UI strings are centralized through a single `Messages` accessor backed by locale-specific resource bundles, so adding a new language requires no code changes
- **Hot-seat multiplayer**: a device handoff flow between turns so multiple players can share one machine
- **CI on every push**: Gradle build and full test suite run via GitHub Actions
- **Checkstyle enforced** for consistent code style across the team

## Tech Stack

Java 11 · Swing (UI) · JUnit 5 · EasyMock · Gradle 8 · PIT (mutation testing) · JaCoCo · SpotBugs · Checkstyle

## Architecture

- **`domain/`**: pure game logic, board state, players, resources, trading, turn management, and win conditions, fully decoupled from the UI
- **`ui/`**: Swing views and dialogs, all user-facing strings routed through `Messages` for i18n
- Design docs for each major system live in `docs/design/`, and boundary value analysis for each domain class lives in `docs/bva/`

## Running It

There's no Gradle `application` plugin configured, so the game runs via its compiled classpath:

```bash
./gradlew build
java -cp build/classes/java/main ui.Main
```

## Testing

```bash
./gradlew test              # run the full test suite
./gradlew jacocoTestReport  # generate a coverage report
./gradlew pitest            # run mutation testing
```

## Contributors

Isabella Socci · Ariel Garcia · Christopher Ridad · Ananya Bhatia · Harrison Smith
