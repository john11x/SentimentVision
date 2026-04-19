# SentimentVision

A comprehensive Android application for mental health monitoring and sentiment analysis using TensorFlow Lite and modern Android development practices.

## Overview

SentimentVision is an innovative mental health tracking app that provides users with tools to monitor their emotional well-being through sentiment analysis, journaling, and therapeutic features. The app uses machine learning to analyze text input and provide insights into emotional patterns.

## Features

### Core Functionality
- **Sentiment Analysis**: Real-time text analysis using TensorFlow Lite models
- **Emotion Tracking**: Comprehensive emotion monitoring with visual charts
- **Journal System**: Personal journaling with mood tracking
- **Analytics Dashboard**: Detailed insights into emotional patterns over time

### User Experience
- **Authentication**: Secure user login and registration system
- **Therapist Portal**: Dedicated interface for mental health professionals
- **Reminder System**: Background notifications for check-ins and journaling
- **Mini-Games**: Breathing exercises and positive reframing activities

### Data Visualization
- **Interactive Charts**: Line charts, pie charts, and emotion wheels
- **Timeline Views**: Historical sentiment tracking
- **Weekly Trends**: Pattern analysis and progress tracking
- **Word Clouds**: Visual representation of common themes

## Technical Architecture

### Technology Stack
- **Language**: Kotlin
- **UI Framework**: Jetpack Compose + XML layouts
- **Architecture**: MVVM (Model-View-ViewModel)
- **Database**: Room with SQLite
- **Machine Learning**: TensorFlow Lite
- **Dependency Injection**: Manual DI implementation
- **Asynchronous Operations**: Coroutines

### Key Components
- **SentimentAnalyzer**: Core ML processing engine
- **Repository Pattern**: Data access abstraction
- **LiveData/StateFlow**: Reactive UI updates
- **Navigation Component**: Single-activity architecture

## Requirements

- **Android SDK**: Minimum API 24 (Android 7.0)
- **Target SDK**: API 34 (Android 14)
- **Java Version**: 17
- **Kotlin**: 1.9.0+

## Installation

1. Clone the repository:
```bash
git clone https://github.com/john11x/SentimentVision.git
```

2. Open the project in Android Studio

3. Sync the project with Gradle files

4. Build and run the application

## Project Structure

```
app/src/main/java/com/example/sentimentvision/
|- data/
|  |- database/          # Room database entities and DAOs
|  |- repository/        # Repository implementations
|- domain/
|  |- SentimentAnalyzer.kt  # Core ML logic
|- ui/
|  |- analysis/          # Sentiment analysis screens
|  |- analytics/         # Analytics and charts
|  |- auth/              # Authentication screens
|  |- components/        # Reusable UI components
|  |- dashboard/         # Main dashboard
|  |- games/             # Therapeutic mini-games
|  |- navigation/        # Navigation setup
|  |- settings/          # Settings and preferences
|  |- therapist/         # Therapist portal
|- notifications/        # Background services
|- utils/               # Utility classes
```

## Key Features Explained

### Sentiment Analysis
- Uses TensorFlow Lite for on-device ML processing
- Analyzes text input for emotional content
- Provides real-time sentiment scores
- Supports multiple emotion categories

### Data Persistence
- Room database for local storage
- User profiles and settings
- Journal entries and sentiment history
- Analytics data aggregation

### User Interface
- Material Design 3 theming
- Jetpack Compose for modern UI
- Responsive layouts for different screen sizes
- Accessibility features integrated

### Background Services
- Boot receiver for automatic startup
- Reminder notifications
- Data synchronization
- Periodic sentiment check-ins

## Dependencies

### Core Android
- AndroidX libraries
- Material Design components
- Navigation Component
- Lifecycle components

### UI & Graphics
- Jetpack Compose
- MPAndroidChart for data visualization
- Material Icons Extended

### Machine Learning
- TensorFlow Lite
- TensorFlow Lite Support Library

### Database & Storage
- Room Runtime
- Room KTX extensions
- Room Compiler (KAPT)

### Testing
- JUnit 4
- AndroidX Test
- MockK for mocking
- Coroutines Test

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests if applicable
5. Submit a pull request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Privacy & Security

- All sentiment analysis is performed on-device
- No personal data is transmitted to external servers
- User data is encrypted and stored locally
- Anonymous analytics for app improvement

## Future Enhancements

- Integration with wearable devices
- Multi-language support
- Advanced ML models for better accuracy
- Cloud synchronization (opt-in)
- Community features and peer support

## Support

For support, feature requests, or bug reports, please open an issue on the GitHub repository.

---

**Built with passion for mental health awareness and technological innovation.**
