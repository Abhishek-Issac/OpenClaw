# OpenClaw: Zenith

> Hyper-integrated OnePlus-style AI productivity suite, powered by NVIDIA NIM
> with Google Gemini as the default multimodal reasoning core. Shizuku-elevated
> system bridge, Samsung DeX-optimized, Jetpack Compose UI.

This repository hosts **OpenClaw: Zenith**, an Android application that fuses:

- A futuristic, floating **OpenClaw** glass-morphism overlay
- A central **Notes / Tasks / Reminders** productivity hub
- A **MindSpace** wellness engine that triggers Zen Mode and Digital Detox
  based on usage patterns
- A **Notification Engine** that uses Shizuku to read, summarize and
  appropriately edit incoming notifications
- A provider-agnostic AI orchestrator that hot-swaps between local and cloud
  models via **NVIDIA NIM**, with **Google Gemini** as the default

## Project status

Phase 0 — Foundation. Gradle + Kotlin 2.0 + Jetpack Compose + Hilt scaffold
with ktlint, detekt, and GitHub Actions CI. See
[`docs/ROADMAP.md`](docs/ROADMAP.md) for the full architecture and phased plan.

## Build

Requires JDK 17 and the Android SDK with API 34.

```bash
./gradlew ktlintCheck      # Kotlin style
./gradlew detekt           # Static analysis
./gradlew assembleDebug    # Build the debug APK
```

CI runs all three on every push and pull request to `main`.

## Tech stack

| Layer | Choice |
|---|---|
| Language | Kotlin 2.0 |
| UI | Jetpack Compose + Material 3 |
| DI | Hilt |
| Persistence | Room + SQLCipher (Phase 2) |
| System bridge | Shizuku (Phase 5) |
| LLM orchestrator | NVIDIA NIM + Gemini (Phase 3) |

## Roadmap

The complete phased roadmap, module graph, database schema and NIM
handshake protocol live in [`docs/ROADMAP.md`](docs/ROADMAP.md).

## Contributing

See [`CONTRIBUTING.md`](CONTRIBUTING.md).

## License

[Apache License 2.0](LICENSE).
