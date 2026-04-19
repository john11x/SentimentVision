package com.example.sentimentvision.ui.games

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BreathingExerciseScreen(
    onBack: () -> Unit = {}
) {
    var timeLeft by remember { mutableStateOf(60) }
    var isRunning by remember { mutableStateOf(false) }
    var currentPhase by remember { mutableStateOf(BreathingPhase.INHALE) }
    var phaseTimeLeft by remember { mutableStateOf(4) }

    LaunchedEffect(isRunning) {
        while (isRunning && timeLeft > 0) {
            delay(1000)
            timeLeft--
            phaseTimeLeft--

            if (phaseTimeLeft <= 0) {
                currentPhase = when (currentPhase) {
                    BreathingPhase.INHALE -> {
                        phaseTimeLeft = 4
                        BreathingPhase.HOLD
                    }
                    BreathingPhase.HOLD -> {
                        phaseTimeLeft = 6
                        BreathingPhase.EXHALE
                    }
                    BreathingPhase.EXHALE -> {
                        phaseTimeLeft = 2
                        BreathingPhase.REST
                    }
                    BreathingPhase.REST -> {
                        phaseTimeLeft = 4
                        BreathingPhase.INHALE
                    }
                }
            }
        }
        if (timeLeft == 0) {
            isRunning = false
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Breathing Exercise",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = String.format("%02d:%02d", timeLeft / 60, timeLeft % 60),
                style = MaterialTheme.typography.displayLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = currentPhase.instruction,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "$phaseTimeLeft",
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(modifier = Modifier.height(32.dp))

            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        "4-4-6-2 Breathing Pattern:",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("• Breathe IN for 4 seconds")
                    Text("• Hold for 4 seconds")
                    Text("• Breathe OUT for 6 seconds")
                    Text("• Rest for 2 seconds")
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = { isRunning = !isRunning },
                    enabled = timeLeft > 0
                ) {
                    Text(if (isRunning) "Pause" else "Start")
                }

                OutlinedButton(onClick = {
                    isRunning = false
                    timeLeft = 60
                    currentPhase = BreathingPhase.INHALE
                    phaseTimeLeft = 4
                }) {
                    Text("Reset")
                }
            }
        }
    }
}

enum class BreathingPhase(val instruction: String) {
    INHALE("Breathe In..."),
    HOLD("Hold..."),
    EXHALE("Breathe Out..."),
    REST("Rest...")
}