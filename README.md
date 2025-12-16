# BAZOOKA Bikes Inventory

A comprehensive Android inventory management system for bike parts and projects, designed for bike enthusiasts, mechanics, and builders.

## Download and Install

**[Download BAZOOKA-Inventory.apk](https://github.com/ssigidd/BazookaBikesInventory/raw/main/BAZOOKA-Inventory.apk)**

### Installation Instructions

1. Download the APK file from the link above
2. On your Android device, go to **Settings > Security** (or **Settings > Apps**)
3. Enable **Install from Unknown Sources** (or **Install Unknown Apps** for your browser)
4. Open the downloaded APK file and tap **Install**
5. Once installed, open the BAZOOKA app and start managing your inventory

**Minimum Android Version:** Android 8.0 (API 26) or higher

## Features

### Bike Parts Management
- **Complete CRUD Operations** - Add, view, edit, and delete bike parts
- **Comprehensive Details** - Track name, category, description, price, quantity, manufacturer, and serial number
- **Stock Management** - Add to stock or remove from stock with quantity controls
- **Low Stock Alerts** - Set minimal stock levels and get visual warnings on low inventory
- **Search and Filter** - Quickly find parts by name, category, or other attributes
- **Categories** - Organize parts into categories (Frame, Wheels, Brakes, Drivetrain, Handlebars, Saddle, Pedals, Other)

### Project Management
- **Project Tracking** - Create and manage bike building projects
- **Part Assignment** - Assign bike parts to projects with specific quantities
- **Archive System** - Archive completed projects to keep your active list clean
- **Budget Tracking** - Set and monitor project budgets
- **Target Completion Dates** - Track project timelines
- **Cost Calculation** - Automatic calculation of total parts cost per project

### Advanced Functionality
- **Junction Table Architecture** - Proper many-to-many relationship between parts and projects
- **Optional Project Assignment** - Remove parts from stock without assigning to a project
- **Partial Quantity Management** - Remove specific quantities of parts from projects
- **Real-Time Updates** - Live low stock count indicator on home screen
- **Disassociation** - Remove parts from projects without affecting stock levels

### User Interface
- **Material Design 3** - Modern, clean interface following Material Design guidelines
- **Dark/Light Theme Support** - Automatic theme switching based on system preferences
- **Responsive Layout** - Optimized for various Android screen sizes
- **Intuitive Navigation** - Easy-to-use navigation with clear action buttons
- **Visual Feedback** - Color-coded alerts and status indicators

## Tech Stack

- **Language:** Kotlin
- **UI Framework:** Jetpack Compose
- **Architecture:** MVVM (Model-View-ViewModel)
- **Database:** Room (SQLite)
- **Dependency Injection:** Hilt
- **Async Operations:** Kotlin Coroutines & Flow
- **Navigation:** Compose Navigation
- **Build System:** Gradle with Kotlin DSL

## Screenshots

### Home Screen
The main dashboard with quick access to Bike Parts, Projects, and Low Stock alerts. Features centered BAZOOKA branding with live low stock count indicator.

### Bike Parts List
Browse all bike parts with search functionality. Each part displays name, category, quantity, and price.

### Part Details
Detailed view of individual parts with options to edit, delete, add stock, or remove stock. Optional project assignment when removing parts.

### Projects List
View active and archived projects. Toggle between active and archived views with the archive icon.

### Project Details
See all parts assigned to a project with quantities and cost calculations. Remove parts from projects with quantity selection.

### Low Stock Alerts
Dedicated screen showing all parts below their minimal stock level, with visual indicators for out-of-stock items.

## Building from Source

### Prerequisites
- Android Studio Hedgehog or later
- JDK 21
- Android SDK with API 36
- Gradle 8.7+

### Build Instructions

1. Clone the repository:
```bash
git clone https://github.com/ssigidd/BazookaBikesInventory.git
cd BazookaBikesInventory
```

2. Open the project in Android Studio

3. Sync Gradle files (Android Studio will prompt automatically)

4. Build the project:
```bash
./gradlew clean assembleDebug
```

5. The APK will be generated at: `app/build/outputs/apk/debug/app-debug.apk`

### Running on Emulator/Device

1. Connect your Android device via USB (with USB debugging enabled) or start an Android emulator
2. Click the **Run** button in Android Studio or use:
```bash
./gradlew installDebug
```

## Project Structure

```
app/src/main/java/com/bazooka/inventory/
├── data/
│   ├── local/
│   │   ├── dao/               # Data Access Objects
│   │   ├── entity/            # Room entities (BikePart, Project, ProjectPart)
│   │   └── BazookaDatabase.kt
│   └── repository/            # Repository layer
├── di/                        # Dependency injection modules
├── ui/
│   ├── navigation/           # Navigation setup
│   ├── screens/              # Composable screens
│   ├── theme/                # Material theme configuration
│   └── viewmodel/            # ViewModels
├── BazookaApplication.kt
└── MainActivity.kt
```

## Database Schema

### BikePart
- **id:** Primary key
- **name:** Part name
- **description:** Detailed description
- **category:** Part category
- **quantity:** Available stock quantity
- **minimalStock:** Minimum stock threshold
- **price:** Part price
- **dateAdded:** Timestamp of creation
- **imageUrl:** Optional image URL
- **manufacturer:** Optional manufacturer name
- **serialNumber:** Optional serial number

### Project
- **id:** Primary key
- **name:** Project name
- **description:** Project description
- **dateCreated:** Creation timestamp
- **targetCompletionDate:** Optional target date
- **budget:** Optional budget amount
- **isArchived:** Archive status

### ProjectPart (Junction Table)
- **id:** Primary key
- **projectId:** Foreign key to Project
- **bikePartId:** Foreign key to BikePart
- **quantity:** Quantity assigned to project
- **dateAdded:** Assignment timestamp

## Contributing

Contributions are welcome! This project was developed with assistance from Claude Code.

## License

This project is open source and available for personal and commercial use.

## Support

For issues, questions, or feature requests, please open an issue on the [GitHub repository](https://github.com/ssigidd/BazookaBikesInventory/issues).

## Credits

Developed with assistance from [Claude Code](https://claude.com/claude-code) by Anthropic.

---

**Built with ❤️ for the bike community**
