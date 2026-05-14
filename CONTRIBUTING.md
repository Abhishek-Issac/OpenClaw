# Contributing to OpenClaw: Zenith

Thank you for considering a contribution! This document covers the basics.

## Development environment

- **JDK 17** (Temurin recommended)
- **Android SDK** with API level 34 installed
- Android Studio Koala (or newer) recommended for Compose previews

```bash
git clone https://github.com/Abhishek-Issac/OpenClaw.git
cd OpenClaw
./gradlew assembleDebug
```

## Code style

- All Kotlin code is formatted by **ktlint**. CI fails on violations.
- Static analysis is enforced by **detekt** with the config in
  [`config/detekt/detekt.yml`](config/detekt/detekt.yml).

```bash
./gradlew ktlintFormat   # auto-fix style
./gradlew ktlintCheck    # verify
./gradlew detekt
```

## Branching & PRs

- Branch from `main` using `feature/<short-name>` or `devin/<ts>-<short-name>`.
- Each PR maps to one phase or sub-phase of [`docs/ROADMAP.md`](docs/ROADMAP.md).
- Keep PRs small and reviewable. Prefer multiple sub-phase PRs over one
  monolithic change.
- CI must be green (ktlint + detekt + assembleDebug) before review.

## Commit messages

Conventional but lightweight:

```
feat(notes): add voice-to-text entry point
fix(orchestrator): cancel inflight stream on model swap
docs(roadmap): update Phase 5 acceptance criteria
```

## Reporting bugs

Open an issue with:
- Device, Android version, DeX state (yes/no)
- Steps to reproduce
- Expected vs actual

## Security

Do **not** open public issues for security problems — email the maintainer
directly.
