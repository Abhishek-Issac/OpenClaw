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

### One-line install + build (fresh Ubuntu)

From any Ubuntu/Debian shell with `git` available:

```bash
git clone https://github.com/Abhishek-Issac/OpenClaw.git && cd OpenClaw && bash setup.sh
```

[`setup.sh`](setup.sh) is idempotent. It will:

1. `apt install` OpenJDK 17 + `unzip`/`curl`/`git`/`file` if missing
2. Download Android command-line tools into `~/android-sdk`
3. Accept all SDK licences and install `platform-tools`, `platforms;android-34`, `build-tools;34.0.0`
4. Write a local `local.properties` pointing Gradle at the SDK
5. Run `./gradlew assembleDebug` with the bundled Gradle 8.7 wrapper
6. Copy the resulting APK into **`./dist/`** as both a timestamped file and `OpenClawZenith-debug.apk`

Install the APK on a device:

```bash
adb install -r dist/OpenClawZenith-debug.apk
```

### Manual build (existing setup)

If you already have JDK 17 and the Android SDK with API 34:

```bash
./gradlew ktlintCheck      # Kotlin style
./gradlew detekt           # Static analysis
./gradlew assembleDebug    # Build the debug APK
```

A CI workflow template is shipped at
[`docs/ci/ci.yml.template`](docs/ci/ci.yml.template). A maintainer should
copy it to `.github/workflows/ci.yml` to enable GitHub Actions on every
push and pull request to `main`. (Devin's GitHub App does not hold the
`workflow` scope required to write under `.github/workflows/`, so the
file is shipped as a template instead.)

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
