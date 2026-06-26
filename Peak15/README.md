# Peak15 — 15-Day Performance Optimization Android App

A production-ready Android application built with Kotlin, Jetpack Compose, and Material 3 to guide users through an evidence-based 15-day physical and mental performance program.

---

## Architecture

```
peak15/
├── app/src/main/java/com/peak15/
│   ├── data/
│   │   ├── local/
│   │   │   ├── dao/           ← Room DAOs (9 interfaces)
│   │   │   ├── database/      ← Peak15Database.kt
│   │   │   ├── entities/      ← Room @Entity classes (9 tables)
│   │   │   └── ProgramDataSource.kt  ← Hardcoded 15-day content
│   │   └── repository/        ← Repository interfaces + implementations
│   ├── di/                    ← Hilt modules (DatabaseModule, RepositoryModule)
│   ├── domain/
│   │   └── model/             ← Pure Kotlin data models
│   ├── notification/          ← WorkManager workers + NotificationScheduler
│   └── presentation/
│       ├── components/        ← Shared Compose components
│       ├── navigation/        ← NavHost + Routes + BottomNav
│       ├── screens/
│       │   ├── analytics/     ← AnalyticsScreen
│       │   ├── cardio/        ← CardioScreen (live timer)
│       │   ├── confidence/    ← ConfidenceScreen
│       │   ├── dashboard/     ← DashboardScreen (home)
│       │   ├── fitness/       ← FitnessScreen + WorkoutDetailScreen
│       │   ├── nutrition/     ← NutritionScreen
│       │   ├── pelvicfloor/   ← PelvicFloorScreen (animated timer)
│       │   ├── recovery/      ← RecoveryScreen
│       │   ├── roadmap/       ← RoadmapScreen + DayDetailScreen
│       │   └── settings/      ← SettingsScreen
│       ├── theme/             ← Material 3 theme, typography, spacing
│       └── viewmodel/         ← MVVM ViewModels (one per feature)
├── MainActivity.kt
└── Peak15Application.kt
```

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Kotlin 2.0 |
| UI | Jetpack Compose + Material 3 |
| Architecture | MVVM + Clean Architecture |
| DI | Hilt |
| Database | Room 2.6 |
| Async | Kotlin Coroutines + Flow |
| Navigation | Compose Navigation |
| Background | WorkManager |
| Preferences | DataStore |
| Charts | Custom Compose (no external chart lib required) |
| Splash | Core SplashScreen API |

---

## Database Schema

### Tables

| Table | Purpose |
|---|---|
| `day_progress` | Per-day task completion tracking (1 row per day 1–15) |
| `pelvic_sessions` | Individual pelvic floor sessions with exercise type |
| `cardio_sessions` | Cardio sessions with duration and type |
| `water_log` | Multi-entry per day water intake (ml) |
| `sleep_log` | One entry per day sleep duration + quality |
| `daily_metrics` | Daily mood/energy/erection quality scores |
| `user_settings` | App config, current day, notification prefs |
| `confidence_log` | Confidence challenge completions |
| `supplement_log` | Supplement taken log |

---

## Screens

### 1. Dashboard (Home)
- Hero completion ring with animated arc progress
- Water intake tracker with quick-add buttons (200/250/500/750ml)
- Daily task checklist (workout, pelvic floor, cardio, supplements)
- Daily check-in for mood/energy/erection quality (1–10 sliders)
- Streak counter badge
- Supplement and avoid lists for the current day

### 2. 15-Day Roadmap
- Phase-grouped cards (Foundation/Build/Peak)
- Locked/unlocked/current/completed states
- Progress bar per card
- Tap to open full day detail

### 3. Day Detail
- Quick navigation tiles to each section
- Morning routine
- Pelvic floor session breakdown
- Nutrition plan with macro targets
- Mental performance protocol
- Why it works (evidence section)
- Things to avoid

### 4. Pelvic Floor Trainer
- Animated circular timer with pulse effect
- Contract/Release/Rest phases with distinct colors
- Quick Flick, Kegel, Reverse Kegel, Elevator, Breathing exercises
- Exercise queue with progress tracking
- Session history log
- Evidence-based educational content

### 5. Cardio Tracker
- 5 activity types (Walk, Run, Cycle, HIIT, Swim)
- Live timer with pause/resume/stop
- Auto-logs to Room on stop
- Heart rate zones guide
- Evidence section (NO/vasodilation science)

### 6. Nutrition Screen
- Daily macro targets (kcal, protein, carbs, fat)
- Per-meal breakdown with timing and protein content
- Water target with visual context
- Key functional foods with explanations
- Nutrition science expandable section

### 7. Confidence Training
- 6 daily challenges across 6 categories
- Tap to complete
- Breathing exercises (Box, 4-7-8, Physiological Sigh, Wim Hof)
- Posture training guide
- Eye contact and social practice protocols
- 10-step visualization protocol

### 8. Recovery Center
- Hip flexor and pelvic release routine
- Thoracic and neck mobility exercises
- Neck strengthening protocol
- Foam rolling guide
- Sleep optimisation science

### 9. Analytics
- Summary KPI grid (streak, workouts, avg sleep, avg mood)
- Custom day-by-day completion bar chart (no external library needed)
- Metrics trend mini-charts (mood/energy/EQ)
- Hydration trend bar chart
- Habit consistency progress bars
- Evidence-based insight cards

### 10. Settings
- Current day display
- Porn-free streak tracker
- Notifications toggle (morning reminder, water, supplements)
- Medical warning signs
- Program reset

---

## Build Instructions

### Prerequisites
- Android Studio Hedgehog (2023.1.1) or later
- JDK 17
- Android SDK 35

### Steps

```bash
# 1. Clone or unzip the project
cd Peak15

# 2. Open in Android Studio
# File → Open → select Peak15/ folder

# 3. Sync Gradle
# Android Studio will auto-sync. If not: File → Sync Project with Gradle Files

# 4. Run on device or emulator
# Select a device (API 26+) and press Run ▶
```

### Build variants
```bash
# Debug APK
./gradlew assembleDebug

# Release APK (requires keystore setup)
./gradlew assembleRelease

# Install debug to connected device
./gradlew installDebug
```

### Minimum requirements
- minSdk: 26 (Android 8.0 Oreo)
- targetSdk: 35 (Android 15)
- RAM: 2GB+ recommended

---

## Key Design Decisions

### Offline-First
All 15-day program content is compiled into `ProgramDataSource.kt` as Kotlin data objects — zero network dependency. Room handles all persistence locally.

### State Management
Each screen has a dedicated ViewModel exposing a single `UiState` data class via `StateFlow`. UI collects with `collectAsStateWithLifecycle()` for lifecycle-safe observation.

### Pelvic Floor Timer Logic
The timer runs as a coroutine in the ViewModel, not a Service, keeping it lifecycle-aware. Phases transition automatically: CONTRACT → RELEASE → (REST if sets remain) → next exercise.

### Phase-Aware Colors
All UI components are phase-aware: Foundation (blue `#4F8EF7`), Build (green `#32C88A`), Peak (gold `#FF9500`). The `phaseColor(day: Int)` function provides consistent theming everywhere.

### Custom Charts
Charts use only Compose Canvas/Box primitives — no Vico or MPAndroidChart required. This keeps APK size down and avoids dependency conflicts.

---

## Notifications

Four channels:
- `peak15_morning` — Daily 7am program reminder
- `peak15_water` — Every 2 hours hydration reminder  
- `peak15_supplements` — 7pm supplement reminder
- `peak15_pelvic` — Training reminders

WorkManager handles scheduling with `PeriodicWorkRequest`. Boot receiver re-schedules after device restart.

---

## Evidence Base

All program content is based on peer-reviewed research including:

- **Zone 2 Cardio & Erection Quality**: Endothelial function and nitric oxide synthesis improvement (Sá et al., 2023; Esposito et al., 2004)
- **Resistance Training & Testosterone**: Compound lifts (deadlift, squat, hip thrust) acute and chronic testosterone elevation (Vingren et al., 2010)
- **Pelvic Floor Training**: Physiotherapy protocols for erectile dysfunction (Dorey et al., 2004; Lavoisier et al., 2014)
- **L-Citrulline**: RCT evidence for mild erectile dysfunction (Cormio et al., 2011)
- **Ashwagandha KSM-66**: Cortisol reduction and testosterone support (Wankhede et al., 2015)
- **Sleep & Testosterone**: REM sleep dependency of testosterone synthesis (Leproult & Van Cauter, 2011)
- **Stop-Start Technique**: Ejaculatory control (Masters & Johnson, 1970)

---

## Medical Disclaimer

This application is for educational and general wellness purposes only. It is not a substitute for medical advice, diagnosis, or treatment. Users experiencing pelvic pain, erectile dysfunction, chest pain during exercise, blood in urine or semen, or any worsening symptoms should consult a qualified medical professional immediately.

---

## License

MIT License. Use freely for personal and commercial purposes with attribution.
