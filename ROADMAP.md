# Roadmap

## Milestone 1: Local diagnostic MVP
- [x] Compose dashboard and navigation
- [x] Capture device and environment snapshot
- [x] Persist snapshots locally with Room
- [x] Compare tracked fields
- [x] Share Markdown report
- [x] Add privacy explanation
- [x] Add unit tests for repository mapping, comparison and report formatting

## Milestone 2: Better test sessions
- [ ] Session names and tester notes
- [ ] Add reproduction steps with timestamps
- [x] Share JSON report text through Android Sharesheet
- [ ] Export Markdown and JSON as files with appropriate MIME types
- [ ] Snapshot detail screen with grouped sections
- [ ] Import a report from another device

## Milestone 3: QA-focused diagnostics
- [ ] User-selected test app package and version selector (with Android package visibility constraints)
- [ ] Permission-state summary for a user-selected app
- [ ] Screen orientation and refresh-rate context
- [ ] Network callback timeline during a test session
- [ ] Optional logcat attachment workflow through an explicit user-selected file

## Milestone 4: Portfolio polish
- [ ] Compose UI tests
- [x] CI workflow for unit tests and debug build
- [ ] App icon and screenshots
- [ ] Accessibility review and tablet layout
- [ ] Release build and signed APK instructions
