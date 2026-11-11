# Architecture

ReproDeck uses a small layered architecture with unidirectional data flow.

```text
Compose UI
   |
   v
ReproDeckViewModel (StateFlow)
   |
   v
SnapshotRepository
   |                    |
   v                    v
Room DAO       DeviceSnapshotDataSource
                         |
                         v
             Android system services
```

## Package responsibilities

- `data/device`: Reads permitted device and environment information.
- `data/local`: Room entities, DAO and database.
- `data/repository`: Coordinates snapshot capture and persistence.
- `domain`: Snapshot comparison and report formatting.
- `ui/screens`: Compose screens.
- `ui/viewmodel`: UI state and user actions.
- `ui/theme`: App color scheme.

## Design decisions

1. **Local first:** snapshots remain in the app's Room database.
2. **Least privilege:** `ACCESS_NETWORK_STATE` is the only manifest permission in the MVP.
3. **No stable hardware identifier:** the app avoids values such as IMEI, serial number and Android ID.
4. **Testable comparison logic:** `SnapshotDiffEngine` is a pure Kotlin object and has unit tests.
5. **Share only on user action:** diagnostic text is sent to Android's share sheet only after the user taps Share.

## Known MVP limits

- Battery and connectivity values are point-in-time observations.
- The network label reports the active transport, not signal quality or carrier identity.
- ReproDeck compares tracked environment fields; it does not yet inspect another app's private state.
- Markdown and JSON reports are shared as text through Android's Sharesheet. A later iteration can add file-based exports with MIME types and attachments.
