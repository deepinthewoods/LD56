# Repository Guidelines

## Project Structure & Module Organization
- `core/`: Shared game logic (Java 11). Main package `ninja.trek`.
- `lwjgl3/`: Desktop launcher and packaging (LWJGL3). Uses `application` plugin; main class `ninja.trek.lwjgl3.Lwjgl3Launcher`.
- `android/`: Android launcher and config. Assets pulled from `../assets`.
- `html/`: GWT backend and dev server.
- `assets/`: Runtime assets bundled to all platforms; `assets.txt` is auto‑generated at build time.

## Build, Test, and Development Commands
- Desktop run: `./gradlew :lwjgl3:run` (Windows: `gradlew.bat :lwjgl3:run`).
- Build all: `./gradlew build` — compiles all modules and packages jars.
- Desktop jar: `./gradlew :lwjgl3:jar` — outputs to `lwjgl3/build/libs/`.
- HTML dev: `./gradlew :html:superDev` — serves at `http://localhost:8080/html`.
- HTML dist: `./gradlew :html:dist` — static site in `html/build/dist/`.
- Android run: `./gradlew :android:run` — launches via ADB (requires SDK).
- Tests (if present): `./gradlew test`.

## Coding Style & Naming Conventions
- Indentation: Java 4 spaces; Gradle 2 spaces. See `.editorconfig` (LF, UTF‑8, trim trailing whitespace).
- Java: classes `PascalCase`, methods/fields `camelCase`, constants `UPPER_SNAKE_CASE`.
- Packages remain under `ninja.trek`. Keep platform‑specific code in its module.

## Testing Guidelines
- Framework: JUnit (recommended). Place tests under `core/src/test/java` mirroring package structure.
- Naming: `*Test.java` per class or feature. Aim for fast, platform‑agnostic tests in `core`.
- Run: `./gradlew test`. Consider minimum coverage thresholds when adding CI.

## Commit & Pull Request Guidelines
- Commits: present‑tense, concise, scoped (e.g., "Fix null check in Player"). Prefer small, focused changes.
- Branches: feature branches named `feature/<short-desc>` or `fix/<short-desc>`.
- PRs: include purpose, key changes, testing steps, and platform(s) affected (desktop/android/html). Add screenshots/GIFs for UI.
- Link issues with `Fixes #<id>` when applicable.

## Security & Configuration Tips
- Java 11+ required. For Android, set SDK via `local.properties` (`sdk.dir=...`) or `ANDROID_SDK_ROOT`.
- Do not commit large binaries; place runtime assets in `assets/`. The build generates `assets/assets.txt` automatically.
- Avoid committing `html/war/` and build outputs; existing `.gitignore` covers common cases.

