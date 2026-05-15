# Jan-Aushadhi Finder 💊
### MindMatrix VTU Internship Program — Android App (Project 71)
---

# 📸 Project Screenshots

## Splash Screen
<img src="splash_screen.jpeg" width="300">

## State Selection Screen
<img src="state_selection.jpeg" width="300">

## Medicine Search Screen
<img src="medicine_search.jpeg" width="300">

## Nearby Stores Screen
<img src="nearby_stores.jpeg" width="300">

## Map Navigation Screen
<img src="map_navigation.jpeg" width="300">

## Health Reminders Screen
<img src="health_reminders.jpeg" width="300">

## Add Reminder Screen
<img src="add_reminder.jpeg" width="300">


---

## 📁 Project Structure (MVVM)

```
app/src/main/java/com/mindmatrix/janaushadhifinder/
│
├── data/
│   ├── local/
│   │   ├── entity/
│   │   │   ├── MedicineEntity.kt       ← Room DB table (SRD §8.1)
│   │   │   └── ReminderEntity.kt       ← Reminder table
│   │   ├── dao/
│   │   │   ├── MedicineDao.kt          ← DB queries (never called from UI!)
│   │   │   └── ReminderDao.kt
│   │   ├── AppDatabase.kt              ← Room DB singleton
│   │   └── SeedData.kt                 ← 100+ pre-loaded medicines
│   ├── model/
│   │   └── Store.kt                    ← Simulated store data (UC-JAF-03)
│   └── repository/
│       ├── MedicineRepository.kt       ← Data operations layer
│       └── ReminderRepository.kt
│
├── ui/
│   ├── navigation/
│   │   └── Screen.kt                   ← Nav routes
│   ├── viewmodel/
│   │   ├── MedicineViewModel.kt        ← Business logic (FR-JAF-01, 02, 05)
│   │   └── ReminderViewModel.kt        ← Reminder business logic
│   ├── screens/
│   │   ├── SplashScreen.kt             ← Auto-transition 2s (SRD §9.2)
│   │   ├── HomeScreen.kt               ← UC-JAF-01: Search Medicine
│   │   ├── DetailScreen.kt             ← UC-JAF-02: Price Comparison
│   │   ├── StoreListScreen.kt          ← UC-JAF-03: Store Locator
│   │   ├── ReminderScreen.kt           ← UC-JAF-04: Medicine Reminders
│   │   └── BottomNavBar.kt             ← Shared nav bar
│   └── theme/
│       └── Theme.kt                    ← SRD §9.1 colour palette
│
└── MainActivity.kt                     ← NavGraph + ViewModel wiring
```

---

## 🚀 How to Set Up in Android Studio

### Step 1 — Open Project
1. Open Android Studio (Hedgehog or newer)
2. File → Open → select the `JanAushadhiFinder` folder
3. Let Gradle sync finish (takes 2–3 minutes first time)

### Step 2 — Add KSP Plugin
In your **project-level** `build.gradle.kts`, add:
```kotlin
plugins {
    id("com.google.devtools.ksp") version "2.0.0-1.0.21" apply false
}
```

### Step 3 — Run
1. Connect a device (Android 9+) OR create an emulator
2. Click ▶️ Run
3. App will auto-populate the Room DB with medicines on first launch

---

## 🗺️ How Each FR Is Implemented

| FR ID | Requirement | File |
|-------|------------|------|
| FR-JAF-01 | Medicine search (fuzzy) | `MedicineDao.kt` → LIKE query, `HomeScreen.kt` |
| FR-JAF-02 | Price comparison | `DetailScreen.kt` — animated bars |
| FR-JAF-03 | Room DB storage | `AppDatabase.kt`, `MedicineEntity.kt` |
| FR-JAF-04 | Medicine reminders | `ReminderScreen.kt`, `ReminderViewModel.kt` |
| FR-JAF-05 | Green savings badge | `HomeScreen.kt` MedicineCard, `DetailScreen.kt` |
| FR-JAF-06 | Simple UI | All screens — Material 3 |
| FR-JAF-07 | Smooth navigation | `MainActivity.kt` NavHost |
| FR-JAF-08 | Load within 2 seconds | StateFlow + debounce in ViewModel |

---

## ✅ SRD Success Criteria Checklist

- [x] Search is "Fuzzy" — handles spelling variations via SQL LIKE
- [x] Price comparison visually clear — ₹Brand vs ₹Generic with bars
- [x] UI is clean, clinical, and professional — Material 3 + SRD colors
- [x] MVVM — No DAO calls in UI layer (SRD §3.2 critical constraint)
- [x] Data persists after app restart — Room DB
- [x] Runs on Android 9.0 (API 28) and above

---

## 🎨 Color Reference (SRD §9.1)

| Color | Hex | Usage |
|-------|-----|-------|
| Primary Blue | `#1976D2` | AppBar, buttons, navigation |
| Accent Green | `#43A047` | Savings badge, generic price |
| Alert Red | `#E53935` | Branded price, errors |
| Background | `#F5F5F5` | Screen background |
| White | `#FFFFFF` | Cards |

---

## 📝 NFR Test Checklist

| NFR | Test | Target |
|-----|------|--------|
| NFR-PERF-01 | Android Studio Profiler — home screen load | < 2000ms |
| NFR-PERF-02 | Search result update after input | < 1000ms |
| NFR-RELY-01 | Force close → reopen, verify data | 100% retained |
| NFR-PORT-01 | Test on API 28 emulator | Zero crashes |
| NFR-SCAL-01 | Code review — no DB calls in UI | Pass |

---

*Jan-Aushadhi Finder · SRD-JAF-001 v1.0 · Abhishek HG · MindMatrix*
