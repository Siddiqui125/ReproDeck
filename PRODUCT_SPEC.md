# Product brief

## Problem

Android bug reports often omit the environment details needed to reproduce the issue. A developer may receive a screenshot and a short description, while the failing device differs from the working device in OS version, ReproDeck build version, display configuration, network state, app locale, battery state, or power-saving mode.

## Product

ReproDeck is a privacy-first companion app that makes Android test environments comparable.

A tester captures a snapshot at the time of a test. Another tester captures a snapshot from a working or failing device. ReproDeck compares the known environment fields and produces a readable report that can be attached to a bug ticket.

## Target users

- Android developers
- Mobile QA engineers
- Interoperability and carrier certification testers
- Support engineers investigating device-specific failures

## Primary user journey

1. Open ReproDeck on a test device.
2. Capture a baseline snapshot before reproducing a bug.
3. Add a short test note and reproduction steps (Milestone 2).
4. Capture a second snapshot on a different device or after changing the environment.
5. Compare the two snapshots.
6. Share a report with the team.

## Success criteria for the MVP

- A tester can capture a snapshot without granting a runtime permission.
- A saved snapshot survives app restarts.
- A tester can compare any two saved snapshots.
- Differences are presented field by field.
- A tester can share a readable diagnostic report.
- The app remains usable without an internet connection.

## Explicit non-goals for the MVP

- Reading another app's private data.
- Recording taps or screen content across other apps.
- Capturing calls, SMS, contacts, GPS location, IMEI, serial numbers, or Wi-Fi SSIDs.
- Uploading reports automatically.
- Replacing Android Studio, logcat, or a full device management suite.

## Interview talking points

- Android system APIs and permission minimization.
- Layered architecture and repository abstraction.
- Room persistence and reactive flows.
- Compose state management and ViewModel lifecycle.
- Pure Kotlin diff logic with unit tests.
- Share-sheet integration and structured report formatting.
- CI verification with GitHub Actions.
