# ZeroToSwimmer 🏊‍♂️

ZeroToSwimmer is an Android application designed to help users transition from beginners to confident swimmers by following a structured 6-week program.

## Purpose
The app provides a clear, day-by-day swimming curriculum. It removes the guesswork from training by presenting specific sets (meters, repetitions, and rest intervals) and tracking the user's progress through a completed history.

## Architectural Decisions

The project follows **Clean Architecture** principles and is modularized by layer to ensure separation of concerns, testability, and maintainability.

### Project Structure
- **`:app`**: The Android application module. It handles the dependency injection root and the host Activity (`HomeActivity`).
- **`:app:presentation`**: The UI layer. Built using **MVVM (Model-View-ViewModel)**. 
    - Uses **ViewBinding** for type-safe layout interaction.
    - Leverages **Kotlin Flow** and **StateFlow** for reactive UI updates.
    - Navigation is fragment-based, hosted within a single activity with a Bottom Navigation Bar.
- **`:app:domain`**: The core business logic layer. It is a **pure Kotlin module** (non-Android).
    - Contains **Entities** (e.g., `SwimSession`, `SwimmingWeek`).
    - Defines **Repository Interfaces** that specify how data should be accessed without knowing about the implementation details.
- **`:app:data`**: The infrastructure layer.
    - Implements the repositories defined in the domain.
    - Uses **SharedPreferences** and **File storage** with **Kotlin Serialization** for local data persistence.

### Key Libraries
- **Koin**: Used for Dependency Injection across all modules.
- **Kotlin Coroutines & Flow**: For asynchronous operations and reactive data streams.
- **Material Components**: For a modern UI following Material 3 guidelines.
- **JUnit 5 & MockK**: For a robust unit testing suite.

## Features

### 📅 Structured Curriculum
- Access a 6-week training plan with 3 sessions per week.
- View detailed breakdowns of each session, including sets, distances, and rest periods.

### 📈 Progress Tracking
- **Session Toggle**: Mark sessions as completed to track your journey.
- **Visual Feedback**: Progress is tracked at a week level with distance summaries.
- **History**: A dedicated history tab that groups your completed sessions by month for a clear overview of past achievements.

### 🎯 Next Session Focus
- A "Next Up" dashboard that intelligently surfaces the next available session in your program.
- Support for marking "Favorite Sessions" for quick access.

### ⚙️ Settings & Customization
- Configure pool size (25m/50m) to automatically adjust session descriptions.
- **Data Management**: Option to reset all progress and start the program fresh.

---

## CI/CD
The project includes a **GitHub Actions** workflow (`android_build_test.yml`) that automatically:
1. Validates the build using `assembleDebug`.
2. Runs unit tests for the data layer to ensure storage integrity.
3. (Planned) Uploads build artifacts and test reports for inspection.

## Release Roadmap & Timetable

We are currently at **v1.0.0 (Stable)**. The following features are planned for upcoming releases, following a "main-for-release" branching strategy.

| Version | Feature | Description | Status |
| :--- | :--- | :--- | :--- |
| **v1.0.0** | **Initial Release** | Stable 6-week program, History, and Settings. | ✅ Released |
| **v1.1.0** | **Session Notes** | Ability to add personal notes/reflections to items in History. | 📅 Planned |
| **v1.2.0** | **Dark Theme** | Full Material 3 Dark Mode support for evening swimmers. | 📅 Planned |
| **v2.0.0** | **Strava Integration** | Export completed sessions directly to Strava. | 🚀 Backlog |

## Pipeline & Branching Strategy

To maintain stability, the project uses a **GitFlow-inspired** model:

- **`development`**: The main integration branch. All feature branches merge here. The CI runs `assembleDebug` and all Unit Tests on every push.
- **`main`**: The "Production" branch. Merges to `main` only happen for tagged releases.
- **Release Pipeline Suggestions**:
    - **Automated Versioning**: Consider using GitHub Actions to automatically increment `projectVersionCode` in `libs.versions.toml` when merging to `main`.
    - **Release Artifacts**: Enhance the pipeline to run `./gradlew assembleRelease` and `bundleRelease` upon merging to `main`, automatically creating a **GitHub Release** with the attached APK/AAB.
    - **Linting**: Add `./gradlew lint` to the pipeline to ensure code quality before release.
