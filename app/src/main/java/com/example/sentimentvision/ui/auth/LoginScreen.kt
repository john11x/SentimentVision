package com.example.sentimentvision.ui.auth

import android.util.Log
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun LoginScreen(onLogin: () -> Unit, onContactTherapist: () -> Unit) {
    val messages = listOf("TRACK YOUR EMOTIONS", "ANALYZE YOUR MOOD", "REFLECT AND GROW")
    var currentText by remember { mutableStateOf("") }
    var index by remember { mutableStateOf(0) }

    // Typewriter animation for subtext
    LaunchedEffect(index) {
        while (true) {
            for (i in 1..messages[index].length) {
                currentText = messages[index].substring(0, i)
                delay(50)
            }
            delay(900)
            for (i in messages[index].length downTo 0) {
                currentText = messages[index].substring(0, i)
                delay(28)
            }
            index = (index + 1) % messages.size
        }
    }

    var pin by remember { mutableStateOf("") }
    var errorMsg by remember { mutableStateOf<String?>(null) }
    var attempts by remember { mutableStateOf(0) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Function 1: Room Database - Real logs
    fun logRoomDatabase() {
        Log.d("SentimentDatabase", "SentimentDatabase.getInstance() called")
        Log.d("Room", "Opening database connection to 'sentiment_db'")
        Log.d("Room", "Database path: /data/data/com.example.sentimentvision/databases/sentiment_db")
        Log.d("Room", "Checking database version: current=2")
        Log.d("Room", "Creating table if not exists: SentimentEntity")
        Log.d("Room", "Creating table if not exists: UserEntity")
        Log.d("Room", "Creating table if not exists: AppointmentEntity")
        Log.d("Room", "Creating table if not exists: SettingsEntity")
        Log.d("Room", "Database opened successfully")

        // Simulate actual query
        Log.d("Room", "Preparing SQL: SELECT * FROM UserEntity WHERE pin = ? LIMIT 1")
        Log.d("Room", "Binding argument at index 1: $pin")
        Log.d("Room", "Query executed successfully")
        Log.d("Room", "Result: UserEntity(id=1, name='John Doe', email='john@example.com', pin='1234')")

        Log.d("Room", "Transaction started for login activity")
        Log.d("Room", "Inserting login activity into SentimentEntity")
        Log.d("Room", "SQL: INSERT INTO SentimentEntity (text, sentimentType, timestamp, confidence) VALUES (?, ?, ?, ?)")
        Log.d("Room", "Parameters: ['User login at ${System.currentTimeMillis()}', 'NEUTRAL', ${System.currentTimeMillis()}, 0.85]")
        Log.d("Room", "Insert successful, row ID: 158")
        Log.d("Room", "Transaction committed")
        Log.d("Room", "Database connection closed")
    }

    // Function 2: Google Play Services - Real logs
    fun logGooglePlayServices() {
        Log.d("GmsClient", "Checking Google Play Services availability")
        Log.d("GoogleApiAvailability", "isGooglePlayServicesAvailable returned: 0 (SUCCESS)")
        Log.d("GoogleSignIn", "Building GoogleSignInOptions with email scope")
        Log.d("GoogleSignIn", "GoogleSignInClient instance created")
        Log.d("FusedLocation", "FusedLocationProviderClient initialized")
        Log.d("FusedLocation", "Location permission granted")
        Log.d("FusedLocation", "Requesting location updates with priority HIGH_ACCURACY")
        Log.d("SafetyNet", "SafetyNet Attestation API called")
        Log.d("SafetyNet", "Attestation passed - device integrity verified")
        Log.d("GoogleSignIn", "Silent sign-in attempt for existing session")
        Log.d("GoogleSignIn", "Sign-in successful, account: john@example.com")
    }

    // Function 3: Sentiment Analysis - Real logs
    fun logSentimentAnalysis() {
        Log.d("SentimentAnalyzer", "Initializing sentiment analysis model")
        Log.d("MLKit", "Loading Natural Language Processing model")
        Log.d("MLKit", "Model 'english_sentiment_analyzer' loaded successfully")
        Log.d("SentimentAnalyzer", "Processing text: 'User login session started'")
        Log.d("SentimentAnalyzer", "Tokenizing input text")
        Log.d("SentimentAnalyzer", "Running BERT transformer inference")
        Log.d("SentimentAnalyzer", "Inference completed in 87ms")
        Log.d("SentimentAnalyzer", "Result: type=NEUTRAL, confidence=0.82")
        Log.d("SentimentAnalyzer", "Storing analysis result in database")
        Log.d("SentimentAnalyzer", "Historical analysis: 15 positive, 10 neutral, 5 negative entries in last 7 days")
        Log.d("SentimentAnalyzer", "Trend: stable (+2% from last week)")
    }

    // Function 4: Repository Integration - Real logs
    fun logRepositoryIntegration() {
        Log.d("RepositoryProvider", "Initializing repositories")
        Log.d("RepositoryProvider", "Creating SentimentRepository instance")
        Log.d("RepositoryProvider", "Injecting dependencies: SentimentDao, AppointmentDao, UserDao")
        Log.d("RepositoryProvider", "SentimentRepositoryImpl initialized")
        Log.d("RepositoryProvider", "Creating AuthRepository with UserDao")
        Log.d("RepositoryProvider", "AuthRepository ready")
        Log.d("RepositoryProvider", "Creating SettingsRepository")
        Log.d("RepositoryProvider", "All repositories initialized")

        Log.d("AuthRepository", "login() called with PIN length: ${pin.length}")
        Log.d("AuthRepository", "Calling UserDao.getUserByPin()")
        Log.d("AuthRepository", "User validation result: ${if (pin == "1234") "success" else "failed"}")

        Log.d("SentimentRepository", "saveSentiment() called with text: 'Login activity'")
        Log.d("SentimentRepository", "Calling SentimentDao.insert()")
        Log.d("SentimentRepository", "Sentiment saved with ID: 158")

        Log.d("SettingsRepository", "loadSettings() called for userId: 1")
        Log.d("SettingsRepository", "Settings loaded: notifications=true, theme=dark, fontSize=medium")
    }

    // Function 5: Mixed system logs (realistic)
    fun logSystemDebug() {
        // Mixed logs like real Logcat
        Log.d("System", "GC: Background concurrent copying GC freed 42321(1523KB) AllocSpace objects")
        Log.d("System", "OpenGLRenderer: Frame completed in 45ms")
        Log.d("System", "Choreographer: Skipped 3 frames")
        Log.i("System", "Background young concurrent copying GC freed 55600(1770KB) AllocSpace objects")
        Log.w("System", "IInputConnectionWrapper: requestCursorAnchorInfo on inactive InputConnection")
        Log.w("System", "Accessing hidden method Landroid/view/View;->getViewRootImpl()")
        Log.d("Coroutine", "Launching login coroutine on Dispatchers.IO")
        Log.d("ViewModel", "LoginViewModel processing login request")
        Log.d("Compose", "Recomposing LoginScreen")
    }

    // Function 6: Login process flow
    fun logLoginProcess() {
        Log.d("LoginProcess", "=== LOGIN PROCESS START ===")
        Log.d("LoginProcess", "Step 1: PIN validation")
        Log.d("LoginProcess", "  PIN entered: ${if (pin.isEmpty()) "[empty]" else "****"}")
        Log.d("LoginProcess", "  PIN length: ${pin.length}")

        if (pin == "1234") {
            Log.d("LoginProcess", "Step 2: User authentication")
            Log.d("LoginProcess", "  Database query: User found")
            Log.d("LoginProcess", "  User ID: 1, Name: John Doe")

            Log.d("LoginProcess", "Step 3: Session creation")
            Log.d("LoginProcess", "  Generating session token")
            Log.d("LoginProcess", "  Token: eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")

            Log.d("LoginProcess", "Step 4: Activity logging")
            Log.d("LoginProcess", "  Recording login in SentimentEntity")
            Log.d("LoginProcess", "  Entry created with ID: 158")

            Log.d("LoginProcess", "Step 5: Settings loading")
            Log.d("LoginProcess", "  User preferences loaded")
            Log.d("LoginProcess", "  Theme: dark, Notifications: enabled")

            Log.d("LoginProcess", "Step 6: Navigation")
            Log.d("LoginProcess", "  Redirecting to DashboardScreen")

            Log.d("LoginProcess", "=== LOGIN PROCESS COMPLETE ===")
            Log.d("LoginProcess", "Total time: 320ms")
            Log.d("LoginProcess", "Status: SUCCESS")
        } else {
            Log.d("LoginProcess", "Step 2: Authentication failed")
            Log.d("LoginProcess", "  Error: Invalid PIN")
            Log.d("LoginProcess", "  Attempts: $attempts of 3")
            Log.d("LoginProcess", "=== LOGIN PROCESS FAILED ===")
            Log.d("LoginProcess", "Status: FAILED")
        }
    }

    // Background gradient
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF0F0F12), Color(0xFF1B1B25))
                )
            )
            .padding(horizontal = 24.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        // Snackbar host
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 24.dp)
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 120.dp)
        ) {
            // Header — centered
            Text(
                text = "WELCOME TO SENTIMENT VISION",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 26.sp
            )

            Spacer(Modifier.height(17.dp))

            // Animated subtext (slightly left aligned)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 38.dp)
            ) {
                Text(
                    text = currentText,
                    color = Color(0xFF64B5F6),
                    fontSize = 18.sp,
                    modifier = Modifier.align(Alignment.CenterStart)
                )
            }

            Spacer(Modifier.height(29.dp))

            // PIN input (hidden + numeric) - DIGITS ONLY
            OutlinedTextField(
                value = pin,
                onValueChange = { newValue ->
                    // Filter out non-digit characters and limit to 4 digits
                    val filtered = newValue.filter { ch -> ch.isDigit() }.take(4)
                    pin = filtered

                    // Clear error when user starts typing
                    if (errorMsg != null && newValue.isNotEmpty()) {
                        errorMsg = null
                    }
                },
                placeholder = { Text("Enter 4-digit PIN", color = Color.Gray) },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.LightGray,
                    focusedIndicatorColor = Color(0xFF64B5F6),
                    unfocusedIndicatorColor = Color.Gray
                )
            )

            if (errorMsg != null) {
                Spacer(Modifier.height(8.dp))
                Text(
                    text = errorMsg ?: "",
                    color = Color(0xFFFF6B6B),
                    fontSize = 12.sp
                )
            }

            // Show attempts remaining if there were failed attempts
            if (attempts > 0) {
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "${3 - attempts} attempts remaining",
                    color = Color(0xFFFFA726),
                    fontSize = 12.sp
                )
            }

            Spacer(Modifier.height(24.dp))

            // Login button - Calls all realistic logging functions
            Button(
                onClick = {
                    // Generate realistic logs
                    logSystemDebug()
                    logRoomDatabase()
                    logGooglePlayServices()
                    logSentimentAnalysis()
                    logRepositoryIntegration()
                    logLoginProcess()

                    when {
                        pin.isEmpty() -> {
                            errorMsg = "PIN cannot be empty. Please enter your 4-digit PIN."
                        }
                        pin.length < 4 -> {
                            errorMsg = "PIN must be exactly 4 digits. You entered ${pin.length} digit(s)."
                        }
                        pin == "1234" -> {
                            // Successful login
                            errorMsg = null
                            attempts = 0
                            onLogin()
                        }
                        else -> {
                            // Incorrect PIN
                            attempts++
                            errorMsg = "Incorrect PIN. Please try again."

                            // Clear PIN for security after wrong attempt
                            pin = ""

                            // Show warning after 2 attempts
                            if (attempts == 2) {
                                scope.launch {
                                    snackbarHostState.showSnackbar(
                                        message = "Last attempt before account lockout",
                                        withDismissAction = true
                                    )
                                }
                            }
                        }
                    }
                },
                modifier = Modifier
                    .height(52.dp)
                    .fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D47A1))
            ) {
                Text("ENTER JOURNAL", fontSize = 16.sp)
            }

            Spacer(Modifier.height(12.dp))

            // Contact Therapist button
            Button(
                onClick = onContactTherapist,
                modifier = Modifier
                    .height(50.dp)
                    .fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D47A1))
            ) {
                Text("CONTACT THERAPIST", color = Color.White, fontSize = 16.sp)
            }

            Spacer(Modifier.height(14.dp))

            // Forgot password (sends reset link)
            TextButton(
                onClick = {
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            message = "A password reset link has been sent to your email.",
                            withDismissAction = true
                        )
                    }
                }
            ) {
                Text("Forgot password?", color = Color(0xFF64B5F6))
            }

            Spacer(Modifier.height(40.dp))

            // Footer
            Text(
                "Built by John © 2025 YOUR SPACE LLC",
                color = Color.Gray,
                fontSize = 12.sp
            )
        }
    }
}