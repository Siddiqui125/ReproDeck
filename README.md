# ReproDeck

**Debug with context.**

ReproDeck is an offline-first Android diagnostic companion for developers and QA engineers. It captures a device environment snapshot, stores it locally, compares two snapshots, and exports a Markdown diagnostic report.

## Why ReproDeck?

"It works on my phone" is difficult to debug when the two test environments are not actually the same. ReproDeck makes those differences visible without asking testers to manually collect device information.

## Current MVP (0.1.0)

- Capture device model, manufacturer, Android release, API level and security patch.
- Capture screen resolution, density, font scale, locale and timezone.
- Record the ReproDeck build version for report traceability.
- Capture network transport and Android's validated-internet state.
- Capture battery percentage, charging state and battery-saver mode.
- Persist snapshots locally with Room.
- Compare two snapshots and display only changed fields.
- Export Markdown or JSON diagnostic reports through Android's share sheet.
- Privacy-first defaults: no account, no cloud sync, and no sensitive hardware identifiers.

## Screens

- **Home:** Capture a new device snapshot and see the latest capture.
- **Sessions:** Review saved captures, select two for comparison, share a report, or delete a snapshot.
- **Privacy:** Review what is collected and what is deliberately excluded.

## Tech stack

- Kotlin
- Jetpack Compose + Material 3
- ViewModel + StateFlow + Coroutines
- Repository pattern
- Room
- Android ConnectivityManager, BatteryManager, PowerManager and Build APIs
- JUnit

## Requirements

- Android Studio with support for Android Gradle Plugin 8.13.x
- JDK 17
- Android SDK Platform 36
- Gradle 8.13

Open the project root in Android Studio and allow Gradle sync to complete. This starter was source-reviewed and its pure Kotlin diff/report logic was smoke-tested in the authoring environment; a full Android Gradle build still needs to be run in Android Studio or CI.

## Build and test

```bash
./gradlew test
./gradlew assembleDebug
```

The Gradle wrapper scripts are intentionally not bundled in this starter archive. Android Studio can configure the Gradle distribution during import; for command-line builds, generate the wrapper from a local Gradle 8.13 installation with:

```bash
gradle wrapper --gradle-version 8.13
```

Then run the commands above.

## Permissions

The app requests only `android.permission.ACCESS_NETWORK_STATE`. Android requires this to inspect the active network transport and validated connectivity. ReproDeck does not request location, phone-state, contacts, camera, microphone, storage, or accessibility permissions.

## Data model

A snapshot contains:
- Device and OS metadata
- ReproDeck build version
- Display configuration
- App locale and device timezone
- Active network transport and Android's validated-internet state
- Battery percentage and charging state
- Power saver state

No IMEI, serial number, Android ID, advertising ID, Wi-Fi SSID, or GPS coordinates are collected.

## Roadmap

See [ROADMAP.md](ROADMAP.md).

## License

MIT. See [LICENSE](LICENSE).
