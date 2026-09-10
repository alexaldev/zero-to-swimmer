# Replace Navigation 3 with Jetpack Navigation Compose

This plan aims to simplify the navigation architecture by replacing the recently implemented **Navigation 3** with the standard **Jetpack Navigation Compose**. This will reduce custom infrastructure code while maintaining a fully Compose-based, Fragment-free UI.

## User Review Required

> [!NOTE]
> We will continue to use **type-safe navigation** with KotlinX Serialization, as it's the modern standard for Jetpack Navigation and makes refactoring easier.

> [!IMPORTANT]
> The custom `NavigationState` and `Navigator` classes created for Navigation 3 will be deleted.

## Proposed Changes

### Build Configuration

#### [MODIFY] [libs.versions.toml](file:///home/pesimatik/StudioProjects/zero-to-swimmer/gradle/libs.versions.toml)
- Add `androidx-navigation-compose` dependency.
- Remove Navigation 3 versions and libraries.

#### [MODIFY] [app/build.gradle.kts](file:///home/pesimatik/StudioProjects/zero-to-swimmer/app/build.gradle.kts)
- Replace Navigation 3 dependencies with `androidx.navigation:navigation-compose`.

#### [MODIFY] [app/presentation/build.gradle.kts](file:///home/pesimatik/StudioProjects/zero-to-swimmer/app/presentation/build.gradle.kts)
- Replace Navigation 3 dependencies with `androidx.navigation:navigation-compose`.

---

### Navigation Infrastructure

#### [MODIFY] [TrainingNavKey.kt](file:///home/pesimatik/StudioProjects/zero-to-swimmer/app/presentation/src/main/java/com/alexallafi/app/presentation/navigation/TrainingNavKey.kt)
- Remove `NavKey` interface implementation.
- Keep the `Serializable` data objects for routes.

#### [DELETE] [NavigationState.kt](file:///home/pesimatik/StudioProjects/zero-to-swimmer/app/presentation/src/main/java/com/alexallafi/app/presentation/navigation/NavigationState.kt)
- Remove custom Nav3 state holder.

#### [DELETE] [Navigator.kt](file:///home/pesimatik/StudioProjects/zero-to-swimmer/app/presentation/src/main/java/com/alexallafi/app/presentation/navigation/Navigator.kt)
- Remove custom Nav3 navigator.

---

### UI Refactoring

#### [MODIFY] [HomeActivity.kt](file:///home/pesimatik/StudioProjects/zero-to-swimmer/app/src/main/java/com/alexallafi/zerotoswimmer/HomeActivity.kt)
- Replace `NavDisplay` with standard `NavHost`.
- Use `rememberNavController()` for navigation management.
- Update `NavigationBarItem` logic to use `navController.currentBackStackEntryAsState()`.
- Implement standard navigation logic for switching tabs (e.g., using `launchSingleTop` and `restoreState`).

## Verification Plan

### Automated Tests
- Build the project to ensure standard Navigation Compose is correctly integrated.

### Manual Verification
- Deploy to emulator.
- Verify tab switching works correctly.
- Verify back stack behavior (standard NavCompose behavior: switching tabs preserves state if configured).
