# OpenClaw: Zenith — Architecture, Roadmap, Schema & NIM Handshake

> Hyper-integrated OnePlus-style AI productivity suite, powered by NVIDIA NIM
> with Google Gemini as the default multimodal reasoning core. Shizuku-elevated
> system bridge, Samsung DeX-optimized, Jetpack Compose UI.

This is the planning document for the `Abhishek-Issac/OpenClaw` repo.
It is intentionally exhaustive — implementation is broken into ~7 phases so each
phase can ship as an independently reviewable PR.

---

## 1. Vision & Non-Negotiable Pillars

| Pillar | What it means in practice |
|---|---|
| **Fast & Smooth** | 60–120 Hz, never block UI thread, all I/O on `Dispatchers.IO`, all model calls on `Dispatchers.Default` with cancellation. |
| **OnePlus visual language** | Slate Gray `#2A2D31`, Vivid Red `#EB0029`, Soft White `#F5F5F7`. Card-based, large radii (20–28 dp), generous spacing. |
| **Gemini-iridescent motion** | Physics-based springs (Compose `spring(stiffness=Medium, damping=0.85)`), shimmer/iridescent gradient on AI surfaces. |
| **No-root power** | All system-level features via Shizuku binder, never `su`. |
| **Privacy by default** | Personal notes/tasks/reminders are encrypted at rest with SQLCipher; biometric unlock required for decryption key. |
| **Model agnosticism** | UI and storage know nothing about which LLM is serving a request — orchestration layer abstracts NIM / Gemini / local. |

---

## 2. Tech Stack (locked-in choices)

| Layer | Choice | Reason |
|---|---|---|
| Language | Kotlin 2.0, Java 17 toolchain | Required for Compose stable + K2 |
| UI | Jetpack Compose + Material 3 | Reactive, declarative |
| Async | Coroutines + Flow + `StateFlow` | Cancellable streaming |
| DI | Hilt | First-party, plays well with WorkManager |
| Persistence | Room 2.6 + SQLCipher 4 | Encrypted-at-rest relational store |
| Key store | AndroidKeyStore + `BiometricPrompt` + `EncryptedSharedPreferences` for tokens | Hardware-backed |
| Networking | OkHttp 4 + Retrofit 2 + kotlinx-serialization | NIM REST + Gemini REST |
| Streaming | OkHttp + SSE adapter (LaunchDarkly's `okhttp-eventsource`) | NIM and Gemini both stream over SSE |
| System bridge | `rikka.shizuku:api` + `rikka.shizuku:provider` | Elevated, no-root |
| Notifications | `NotificationListenerService` (read) + Shizuku binder (snooze/cancel system-wide) | "Appropriately edit" |
| Background | WorkManager + Foreground Service for overlay | Model fetch & wellness monitor |
| Overlay | `SYSTEM_ALERT_WINDOW` + Compose-in-Window via `ComposeView` | Floating OpenClaw |
| Build | Gradle 8.7+ with Kotlin DSL, version catalog | Reproducible |
| CI | GitHub Actions: ktlint + detekt + unit tests + assembleDebug | PR gate |
| Min/Target SDK | `minSdk = 28`, `targetSdk = 34` | Covers DeX (Android 9+) and modern Compose |

---

## 3. Top-level Module Graph

```
:app                      ← thin shell, only Application + MainActivity
:core:designsystem        ← OnePlusTheme, tokens, motion specs
:core:ui                  ← reusable Compose components (Card, Pill, Shimmer, Overlay)
:core:common              ← coroutine scopes, Result wrappers, logging
:core:database            ← Room + SQLCipher, encryption key provider
:core:datastore           ← Proto DataStore for preferences (active model, theme)
:core:security            ← BiometricGate, KeyStoreManager
:feature:mindspace        ← Wellness, focus sessions, usage-driven Zen Mode
:feature:notes            ← Notes CRUD + voice-to-text
:feature:tasks            ← To-Do, sub-tasks, prioritization
:feature:reminders        ← Time + contextual reminders, WorkManager glue
:feature:openclaw         ← Floating overlay, drag-and-drop sink
:feature:settings         ← Model picker, Shizuku status, theme
:ai:orchestrator          ← Provider-agnostic LLM facade
:ai:provider-gemini       ← Gemini REST client (default)
:ai:provider-nim          ← NVIDIA NIM client + model registry
:ai:provider-local        ← Optional: GGUF via llama.cpp JNI (Phase 7)
:system:shizuku           ← Shizuku service binder, permission UX
:system:notifications     ← NotificationListenerService + AI edit pipeline
:system:dex               ← Window-size class adaptation + multi-instance
```

Every `:feature:*` depends only on `:core:*` and `:ai:orchestrator` — never on a
concrete provider. Providers register themselves via Hilt multibinding.

---

## 4. Phased Roadmap

Each phase is sized to land as 1–3 PRs. Acceptance criteria are explicit so we
can verify before moving on.

### Phase 0 — Foundation (1 PR)
- Gradle 8.7 + version catalog (`libs.versions.toml`)
- Kotlin 2.0, Compose BOM, Hilt, Detekt, ktlint
- `:app` shell, `OpenClawApp : Application`, `MainActivity` with empty Scaffold
- GitHub Actions: `./gradlew ktlintCheck detekt assembleDebug`
- README + LICENSE + CONTRIBUTING
- **Done when:** `./gradlew assembleDebug` is green on CI.

### Phase 1 — Design System (1 PR)
- `OnePlusTheme` with light/dark, dynamic color **off** (we want brand identity)
- Color tokens, typography (OnePlus Sans fallback → Inter), motion specs
- Components: `OpClawCard`, `OpClawPill`, `IridescentBorder`, `ShimmerSurface`, `GeminiBlob` (animated)
- Compose previews + Paparazzi screenshot tests
- **Done when:** Component gallery activity renders all primitives.

### Phase 2 — Persistence & Security (1 PR)
- Room + SQLCipher with passphrase derived from a key in AndroidKeyStore
- `BiometricGate` issues the master passphrase only after `BiometricPrompt` success
- DataStore for non-sensitive prefs (active model id, theme)
- DAOs for Notes, Tasks, Reminders (schema in §5)
- **Done when:** Encrypted DB opens only after biometric; instrumentation test proves DB is unreadable without the key.

### Phase 3 — AI Orchestrator + Gemini default (2 PRs)
- `LlmProvider` interface (see §6)
- Gemini provider (REST + SSE streaming, multimodal image input)
- NIM provider (model list, swap, generate, SSE streaming)
- Hilt multibinding registry, `ActiveModelStore` in DataStore
- Settings UI: list available NIM models, swap, persist
- **Done when:** From Settings I can switch active model between Gemini-2.5-pro and any NIM-hosted model and a chat round-trip works on both.

### Phase 4 — Notes, Tasks, Reminders (2 PRs)
- Full Compose UIs (list/detail/edit) for each
- Voice-to-text via `SpeechRecognizer`
- AI auto-categorization & action-item extraction: notes → tasks pipeline
- WorkManager-backed reminder dispatch + `AlarmManager` for exact alarms
- **Done when:** A voice note "remind me to email Sarah tomorrow at 9" produces a Note + Task + Reminder atomically.

### Phase 5 — Shizuku & Notification Engine (2 PRs)
- Shizuku permission request UX with diagnostic page (status, version, fallback if absent)
- `NotificationListenerService` reads incoming notifications
- AI pipeline: summarize / classify importance / propose one-tap replies
- Shizuku-backed system actions (snooze channel, suppress, modify)
- All notification edits logged to `notification_event` table (auditable)
- **Done when:** Live notifications are intercepted, summarized in-app, and non-essential ones are silenced based on the active focus session.

### Phase 6 — OpenClaw Overlay & DeX (2 PRs)
- Foreground service hosting a `ComposeView` in a `SYSTEM_ALERT_WINDOW`
- Glass-morphism (frosted blur via `RenderEffect` on API 31+, fallback shader on 28–30)
- Drag-and-drop sink accepting text/image/file MIME types (`DragEvent`)
- DeX adaptation: `WindowSizeClass`, master/detail panes, multi-instance launch
- **Done when:** OpenClaw floats over any app, accepts dropped content, and the same app, when launched on DeX, renders as a multi-pane desktop app.

### Phase 7 — MindSpace & Extensibility (2 PRs)
- Usage stats via `UsageStatsManager` (with permission UX)
- Cognitive-load heuristic (session length, app switching, time-of-day, notification volume)
- Auto Zen Mode / Digital Detox triggers
- Public API: `content://com.openclaw.zenith/openapi/*` + intent contract `com.openclaw.zenith.RUN_PROMPT`
- Sample integration: "Summarize my last 5 emails → To-Do" via the Gmail share-sheet contract
- **Done when:** A third-party app can fire an intent with a prompt and receive structured tasks added to the user's list.

### Phase 8 — Hardening (1 PR)
- Crash reporting opt-in
- Battery profiling
- Detox tests of golden flows
- F-Droid metadata + Play Store internal track build

---

## 5. Database Schema (Room + SQLCipher)

All tables are encrypted at rest. Timestamps are stored as epoch millis UTC.
Primary keys are UUID v4 strings (avoid PII leakage via sequential IDs).

### 5.1 Tables

```sql
-- ── Notes ───────────────────────────────────────────────────────────────
CREATE TABLE note (
  id              TEXT PRIMARY KEY NOT NULL,            -- uuid v4
  title           TEXT NOT NULL,
  body_md         TEXT NOT NULL DEFAULT '',
  source          TEXT NOT NULL,                        -- VOICE | TEXT | CLIP | IMPORT
  language        TEXT,                                 -- BCP-47, optional
  pinned          INTEGER NOT NULL DEFAULT 0,           -- 0/1
  color_hint      TEXT,                                 -- accent token
  created_at      INTEGER NOT NULL,
  updated_at      INTEGER NOT NULL,
  deleted_at      INTEGER                               -- soft-delete
);
CREATE INDEX idx_note_updated ON note(updated_at DESC);

-- Many-to-many: tags
CREATE TABLE tag (
  id   TEXT PRIMARY KEY NOT NULL,
  name TEXT NOT NULL UNIQUE
);
CREATE TABLE note_tag (
  note_id TEXT NOT NULL REFERENCES note(id) ON DELETE CASCADE,
  tag_id  TEXT NOT NULL REFERENCES tag(id)  ON DELETE CASCADE,
  PRIMARY KEY (note_id, tag_id)
);

-- AI-extracted entities for a note (for fast "find action items" queries)
CREATE TABLE note_entity (
  id        TEXT PRIMARY KEY NOT NULL,
  note_id   TEXT NOT NULL REFERENCES note(id) ON DELETE CASCADE,
  kind      TEXT NOT NULL,                              -- PERSON | DATE | TASK | PLACE
  value     TEXT NOT NULL,
  span_start INTEGER, span_end INTEGER,                 -- offsets into body_md
  confidence REAL NOT NULL DEFAULT 1.0
);
CREATE INDEX idx_note_entity_kind ON note_entity(kind);

-- ── Tasks ───────────────────────────────────────────────────────────────
CREATE TABLE task (
  id              TEXT PRIMARY KEY NOT NULL,
  parent_id       TEXT REFERENCES task(id) ON DELETE CASCADE,   -- sub-tasks
  title           TEXT NOT NULL,
  details_md      TEXT,
  status          TEXT NOT NULL DEFAULT 'TODO',         -- TODO | DOING | DONE | DROPPED
  priority        INTEGER NOT NULL DEFAULT 2,           -- 1=high .. 4=low
  due_at          INTEGER,                              -- nullable
  source_note_id  TEXT REFERENCES note(id) ON DELETE SET NULL,
  ai_extracted    INTEGER NOT NULL DEFAULT 0,
  created_at      INTEGER NOT NULL,
  updated_at      INTEGER NOT NULL,
  completed_at    INTEGER
);
CREATE INDEX idx_task_status_due ON task(status, due_at);
CREATE INDEX idx_task_parent     ON task(parent_id);

-- ── Reminders ───────────────────────────────────────────────────────────
CREATE TABLE reminder (
  id            TEXT PRIMARY KEY NOT NULL,
  task_id       TEXT REFERENCES task(id) ON DELETE CASCADE,
  note_id       TEXT REFERENCES note(id) ON DELETE CASCADE,
  trigger_kind  TEXT NOT NULL,                          -- TIME | LOCATION | CONTEXT
  trigger_at    INTEGER,                                -- epoch ms, nullable for non-TIME
  rrule         TEXT,                                   -- RFC 5545 recurrence, nullable
  geofence_lat  REAL, geofence_lng REAL, geofence_radius_m REAL,
  context_tag   TEXT,                                   -- e.g. "ON_HEADPHONES", "AT_WORK"
  urgency       INTEGER NOT NULL DEFAULT 2,             -- 1..4, AI-derived
  fired_at      INTEGER,
  dismissed_at  INTEGER,
  work_request_id TEXT                                  -- WorkManager UUID
);
CREATE INDEX idx_reminder_trigger ON reminder(trigger_at);

-- ── MindSpace / Wellness ────────────────────────────────────────────────
CREATE TABLE focus_session (
  id          TEXT PRIMARY KEY NOT NULL,
  started_at  INTEGER NOT NULL,
  ended_at    INTEGER,
  mode        TEXT NOT NULL,                            -- ZEN | DETOX | DEEP_WORK | CUSTOM
  trigger     TEXT NOT NULL,                            -- MANUAL | AI_AUTO | SCHEDULE
  cognitive_load REAL,                                  -- 0..1 at start
  app_blocklist_json TEXT NOT NULL DEFAULT '[]',
  notes       TEXT
);

CREATE TABLE usage_signal (
  id         TEXT PRIMARY KEY NOT NULL,
  window_start INTEGER NOT NULL,
  window_end   INTEGER NOT NULL,
  unlocks      INTEGER NOT NULL,
  app_switches INTEGER NOT NULL,
  notif_count  INTEGER NOT NULL,
  foreground_pkg_top TEXT,
  load_score   REAL NOT NULL                            -- 0..1
);
CREATE INDEX idx_usage_signal_window ON usage_signal(window_start);

-- ── Notification Engine (auditable edits) ──────────────────────────────
CREATE TABLE notification_event (
  id            TEXT PRIMARY KEY NOT NULL,
  posted_at     INTEGER NOT NULL,
  pkg           TEXT NOT NULL,
  channel_id    TEXT,
  title         TEXT,
  text          TEXT,
  category      TEXT,                                   -- msg, email, social, system, ...
  importance_in INTEGER NOT NULL,                       -- as posted
  importance_out INTEGER NOT NULL,                      -- after AI rule
  action        TEXT NOT NULL,                          -- PASS | SUMMARIZE | DEFER | SILENCE
  summary       TEXT,                                   -- AI-written, nullable
  reply_suggestions_json TEXT,                          -- ["Ack","On my way","Tomorrow"]
  focus_session_id TEXT REFERENCES focus_session(id) ON DELETE SET NULL,
  model_id      TEXT NOT NULL                           -- which LLM made the call
);
CREATE INDEX idx_notif_event_posted ON notification_event(posted_at DESC);
CREATE INDEX idx_notif_event_pkg    ON notification_event(pkg);

-- ── AI Model registry (mirrored from NIM + Gemini providers) ───────────
CREATE TABLE ai_model (
  id            TEXT PRIMARY KEY NOT NULL,              -- "gemini-2.5-pro", "nim:meta/llama-3.1-70b"
  provider      TEXT NOT NULL,                          -- GEMINI | NIM | LOCAL
  display_name  TEXT NOT NULL,
  modality      TEXT NOT NULL,                          -- TEXT | TEXT_VISION | TEXT_VISION_AUDIO
  context_window INTEGER,
  default_temp  REAL NOT NULL DEFAULT 0.7,
  endpoint_url  TEXT,                                   -- NIM endpoints; null for Gemini
  capabilities_json TEXT NOT NULL DEFAULT '[]',
  last_seen_at  INTEGER NOT NULL,
  is_default    INTEGER NOT NULL DEFAULT 0
);

-- Single-row "active selection" via DataStore proto, not a table. But we
-- keep a history for telemetry/debugging:
CREATE TABLE model_swap_log (
  id           TEXT PRIMARY KEY NOT NULL,
  from_model   TEXT,
  to_model     TEXT NOT NULL,
  reason       TEXT,                                    -- USER | FALLBACK | AUTO
  at           INTEGER NOT NULL
);
```

### 5.2 Migrations & Defaults

- Room v1 ships with these 11 tables. We declare `@Database(version = 1)`.
- On first run we seed `ai_model` with one row:
  `id='gemini-2.5-pro', provider='GEMINI', is_default=1`. This guarantees a
  working default even before NIM connectivity.

### 5.3 Encryption

- Master key: 256-bit, stored in AndroidKeyStore as a non-extractable key
  wrapping a randomly-generated SQLCipher passphrase held in an
  `EncryptedFile`.
- `BiometricPrompt` is required to unwrap the passphrase on cold start. The
  unwrapped passphrase is held in memory inside a `CharArray` and zeroed on
  app background after a configurable timeout (default 5 min).

---

## 6. NVIDIA NIM Model-Swap — API Handshake Protocol

Goal: a clean, provider-agnostic facade so swapping models is a single
DataStore write that fans out to running streams cleanly, and so model
discovery is dynamic (NIM endpoints may come and go).

### 6.1 Provider Interface (Kotlin)

```kotlin
interface LlmProvider {
  val id: String                          // "gemini" | "nim" | "local"
  suspend fun listModels(): List<AiModel> // discovery
  suspend fun health(modelId: String): HealthStatus
  fun complete(req: LlmRequest): Flow<LlmEvent>   // streamed
}

sealed interface LlmEvent {
  data class Token(val text: String) : LlmEvent
  data class ToolCall(val name: String, val argsJson: String) : LlmEvent
  data class Usage(val promptTokens: Int, val completionTokens: Int) : LlmEvent
  data class Done(val finishReason: String) : LlmEvent
  data class Error(val cause: Throwable, val retryable: Boolean) : LlmEvent
}
```

The orchestrator picks a provider by inspecting `activeModelId` prefix:
`gemini-*` → Gemini, `nim:*` → NIM, `local:*` → local.

### 6.2 Discovery Handshake (NIM)

```
Client                                          NIM Gateway
  │                                                  │
  │  1. GET /v1/models                               │
  │     Authorization: Bearer $NIM_API_KEY           │
  │     Accept: application/json                     │
  │  ──────────────────────────────────────────────► │
  │                                                  │
  │  2.   200 OK                                     │
  │     { "data": [ { "id":"meta/llama-3.1-70b",     │
  │                   "object":"model",              │
  │                   "owned_by":"nvidia", ... } ] } │
  │  ◄──────────────────────────────────────────────│
  │                                                  │
  │  3. (per model) GET /v1/models/{id}              │
  │     → capabilities, context_window, modality     │
  │  ──────────────────────────────────────────────► │
  │  4.   200 OK { context_length, supports_vision,  │
  │                supports_tools, ... }             │
  │  ◄──────────────────────────────────────────────│
  │                                                  │
  │  5. UPSERT into ai_model table; mark last_seen   │
  │  6. Soft-delete rows whose last_seen < now-7d    │
```

Discovery runs on app start, on Settings open, and every 6h via WorkManager.

### 6.3 Active-Model Selection

`active_model_id` lives in Proto DataStore so it's observable as a `Flow`.
The orchestrator collects this Flow and tears down inflight streams when it
changes:

```kotlin
class Orchestrator(...) {
  val active: Flow<String> = dataStore.data.map { it.activeModelId }

  fun chat(prompt: Prompt): Flow<LlmEvent> = active
    .flatMapLatest { id -> resolveProvider(id).complete(prompt.toRequest(id)) }
    .catch { e -> emit(LlmEvent.Error(e, retryable = isTransient(e))) }
}
```

`flatMapLatest` is the key: when the user swaps mid-stream, the previous
provider's coroutine is cancelled cleanly.

### 6.4 Generation Handshake (streamed, OpenAI-compatible)

NIM's inference endpoints are OpenAI-API-compatible, so we can reuse the
same wire format for both NIM and (with minor adaptation) Gemini's
`generateContent` endpoint.

```
POST /v1/chat/completions
Host: <nim_endpoint>
Authorization: Bearer $NIM_API_KEY
Content-Type: application/json
Accept: text/event-stream
X-OpenClaw-Client: zenith/0.1.0 (android-34)
X-OpenClaw-Request-Id: <uuidv4>

{
  "model": "meta/llama-3.1-70b-instruct",
  "messages": [
    {"role":"system","content":"You are OpenClaw, ..."},
    {"role":"user","content":[
        {"type":"text","text":"Summarize this note."},
        {"type":"image_url","image_url":{"url":"data:image/png;base64,..."}}
    ]}
  ],
  "stream": true,
  "temperature": 0.7,
  "max_tokens": 1024,
  "tools": [ /* function-call schema */ ],
  "metadata": { "openclaw_module":"notes", "user_locale":"en-IN" }
}
```

Response (SSE):

```
event: message
data: {"id":"...","choices":[{"delta":{"content":"Sum"},"index":0}]}

event: message
data: {"id":"...","choices":[{"delta":{"content":"mary..."},"index":0}]}

event: message
data: {"id":"...","choices":[{"delta":{"tool_calls":[{...}]}}]}

event: done
data: [DONE]
```

The NIM provider parses each `data:` JSON line and emits `LlmEvent.Token`,
`LlmEvent.ToolCall`, `LlmEvent.Usage`, then `LlmEvent.Done`.

### 6.5 Fallback Policy

```
primary (active_model)
   │ on 5xx, 429, or stream timeout (>15s no token)
   ▼
fallback chain in DataStore: [gemini-2.5-pro, nim:meta/llama-3.1-8b, local:phi-3-mini]
   │
   ▼
LlmEvent.Error(retryable=false) → surfaced to UI as a banner
```

Each fallback is logged in `model_swap_log` with `reason='FALLBACK'`.

### 6.6 Auth & Secret Storage

- `NIM_API_KEY`, `GEMINI_API_KEY` are stored in `EncryptedSharedPreferences`
  backed by AndroidKeyStore.
- Keys are never written to logs or crash reports.
- Settings UI shows masked key (`••••••••AB12`) and a "rotate" affordance.

### 6.7 Cost & Rate-limit Awareness

A lightweight `RateBudget` per `(provider, modelId)` tracks tokens-per-minute
and requests-per-minute from response headers (`x-ratelimit-remaining-*`).
When budget < 10% the orchestrator preemptively switches to the next chain
member.

---

## 7. Shizuku Integration Plan (Module `:system:shizuku`)

1. App declares `<uses-permission android:name="moe.shizuku.manager.permission.API_V23"/>`.
2. On first launch, MindSpace settings page detects Shizuku state via
   `Shizuku.pingBinder()` and shows one of:
   - **Active** (binder ok, permission granted) → green pill
   - **Available, needs permission** → `Shizuku.requestPermission(REQ)`
   - **Not running** → deep-link to Shizuku Manager with instructions for ADB / Wireless ADB pairing.
3. Elevated actions are exposed via a typed facade:
   ```kotlin
   class ShizukuBridge {
     suspend fun setNotificationChannelImportance(pkg: String, channel: String, level: Int)
     suspend fun grantRuntimePermission(pkg: String, perm: String)
     suspend fun forceStop(pkg: String)
     suspend fun setAppStandbyBucket(pkg: String, bucket: Int)
   }
   ```
4. Every call is wrapped in `runCatching` and audited to a `shizuku_audit`
   log file (rotated, encrypted) so the user can review what OpenClaw did on
   their behalf.

---

## 8. Notification Engine Pipeline

```
NotificationListenerService.onNotificationPosted(sbn)
        │
        ▼
NotificationCapture → row in notification_event (importance_in)
        │
        ▼
Classifier (small on-device heuristic) → category, base importance
        │
        ▼
If active focus session OR user rules suggest AI handling:
   Orchestrator.chat(promptTemplate(sbn))
        │
        ├─► summary
        ├─► reply_suggestions
        └─► action ∈ {PASS, SUMMARIZE, DEFER, SILENCE}
        │
        ▼
Apply action:
   PASS       → no-op
   SUMMARIZE  → cancelAndRepost via Shizuku with edited extras
   DEFER      → snooze 30m via setNotificationsShown + WorkManager re-post
   SILENCE    → set channel importance LOW via Shizuku
        │
        ▼
Update notification_event (importance_out, action, summary, model_id)
```

Every edit is reversible from a "Notification History" screen.

---

## 9. DeX / Desktop Mode

- Detect `Configuration.uiMode & Configuration.UI_MODE_TYPE_MASK == DESK` and `WindowSizeClass`.
- Layouts switch from single-pane to **List/Detail/Tool** three-pane on
  `widthSizeClass >= Expanded`.
- Activities declare `android:resizeableActivity="true"` and
  `android:supportsPictureInPicture="true"`.
- The OpenClaw overlay is suppressed in DeX in favor of a docked side panel
  that can be undocked into its own window via `Activity.startActivity` with
  `FLAG_ACTIVITY_LAUNCH_ADJACENT | FLAG_ACTIVITY_MULTIPLE_TASK`.

---

## 10. Public Extensibility API

A read/write content provider plus an intent contract:

| Surface | Contract | Auth |
|---|---|---|
| `content://com.openclaw.zenith/tasks` | CRUD tasks | per-app permission `com.openclaw.zenith.READ_TASKS` / `WRITE_TASKS` |
| `content://com.openclaw.zenith/notes`  | CRUD notes | as above |
| Intent `com.openclaw.zenith.RUN_PROMPT` | extras: `prompt:String`, `context:Bundle`; result: `tasksAdded:Int`, `noteId:String?` | user must approve on first use |

A sample receiver app demonstrates the Gmail "Summarize my last 5 emails → To-Do" flow.

---

## 11. Testing Strategy

| Layer | Tooling |
|---|---|
| Pure Kotlin (orchestrator, classifiers) | JUnit 5, Turbine for Flow |
| Compose UI | Compose UI Test + Paparazzi for snapshot |
| Room migrations | `MigrationTestHelper` |
| Shizuku | Fake binder + integration tests on a connected device CI runner |
| End-to-end | Maestro flows for: voice note → task → reminder → notification |

---

## 12. Risk Register

| Risk | Impact | Mitigation |
|---|---|---|
| Shizuku binder unavailable on user's device | Notification engine degraded | Graceful fallback to `NotificationListenerService`-only behaviour; clear UX explaining the limit |
| NIM endpoint variance (different vendors host NIM differently) | Discovery breaks | Strictly follow OpenAI-compat spec; tolerate missing fields; capability probe |
| OEM background-kill (OnePlus, Xiaomi) for overlay service | Overlay disappears | Foreground service + "Battery: Unrestricted" onboarding step |
| Encrypted DB key loss on biometric reset | User data unreadable | On enrollment change, prompt user to re-authenticate with backup PIN; document recovery |
| Gemini quota exhaustion on free tier | Default model down | Fallback chain (§6.5) |

---

## 13. Suggested Branch / PR Cadence

```
main ──┬── devin/<ts>-phase-0-foundation        ← PR #1
       ├── devin/<ts>-phase-1-design-system     ← PR #2
       ├── devin/<ts>-phase-2-persistence       ← PR #3
       ├── devin/<ts>-phase-3-ai-orchestrator   ← PR #4
       ├── devin/<ts>-phase-3b-providers        ← PR #5
       ├── devin/<ts>-phase-4-notes-tasks       ← PR #6
       ├── devin/<ts>-phase-4b-reminders        ← PR #7
       ├── devin/<ts>-phase-5-shizuku           ← PR #8
       ├── devin/<ts>-phase-5b-notif-engine     ← PR #9
       ├── devin/<ts>-phase-6-overlay-dex       ← PR #10
       └── devin/<ts>-phase-7-mindspace-api     ← PR #11
```

Each PR is independently reviewable and CI-gated.

---

## 14. Immediate Next Step

I propose I open **Phase 0** as a draft PR: empty Gradle/Compose/Hilt
scaffold + CI + this roadmap committed under `/docs/`. That gives you
something concrete to react to without me committing 10k lines of code
against an unconfirmed plan.

Confirm and I'll start.
