# Contributing

Thanks for helping improve ReproDeck.

## Guidelines

- Keep device data collection explicit and privacy-conscious.
- Never add hardware identifiers or personal data without a strong, documented reason.
- Keep the comparison engine pure Kotlin where practical.
- Add unit tests for comparison and report-formatting behavior.
- Keep UI state in ViewModels and collect it lifecycle-aware in Compose.
- Avoid network access by default. Any future sync feature must be opt-in and documented.

## Pull requests

Include:
- What changed and why
- Screenshots for UI changes
- Test results
- Any new permissions and why they are necessary
