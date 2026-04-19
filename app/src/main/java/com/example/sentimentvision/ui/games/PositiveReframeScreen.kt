package com.example.sentimentvision.ui.games

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PositiveReframeScreen(
    onBack: () -> Unit = {}
) {
    var negativeThought by remember { mutableStateOf("") }
    var positiveReframe by remember { mutableStateOf("") }
    var showExamples by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Positive Reframe",
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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        "Write a negative thought:",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = negativeThought,
                        onValueChange = { negativeThought = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 80.dp),
                        placeholder = { Text("I'm feeling worried about...") },
                        singleLine = false
                    )
                }
            }

            Button(
                onClick = {
                    positiveReframe = generatePositiveReframe(negativeThought)
                },
                enabled = negativeThought.isNotBlank(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.AutoAwesome, contentDescription = "Reframe")
                Spacer(modifier = Modifier.width(8.dp))
                Text("Transform Thought")
            }

            if (positiveReframe.isNotBlank()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            "New Perspective:",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(positiveReframe)

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                negativeThought = ""
                                positiveReframe = ""
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Clear & Start New")
                        }
                    }
                }
            }

            OutlinedButton(
                onClick = { showExamples = !showExamples },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (showExamples) "Hide Examples" else "Show Examples")
            }

            if (showExamples) {
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            "Thought Transformation Examples:",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("• 'I failed the test' → 'I discovered what I need to study more'")
                        Text("• 'This is too hard' → 'This challenge will make me stronger'")
                        Text("• 'I'm not good enough' → 'I'm growing and improving every day'")
                        Text("• 'I'm so stressed' → 'I'm learning to manage pressure better'")
                        Text("• 'Nothing goes right' → 'Some things are working, I'll focus on those'")
                    }
                }
            }
        }
    }
}

fun generatePositiveReframe(negativeThought: String): String {
    val lowerThought = negativeThought.lowercase()

    return when {
        lowerThought.contains("fail") || lowerThought.contains("mistake") ->
            "Every 'failure' is actually feedback. What can this teach you about how to approach things differently next time?"

        lowerThought.contains("hard") || lowerThought.contains("difficult") || lowerThought.contains("struggle") ->
            "Challenges are opportunities in disguise. Each difficulty you face is building your resilience and problem-solving skills."

        lowerThought.contains("tired") || lowerThought.contains("exhausted") || lowerThought.contains("burn") ->
            "Your body and mind are asking for rest. Honoring this need is an act of self-care that will make you more effective later."

        lowerThought.contains("worry") || lowerThought.contains("anxious") || lowerThought.contains("stress") ->
            "Anxiety often comes from caring deeply. Focus on what you can control right now - your breathing, your next small action."

        lowerThought.contains("alone") || lowerThought.contains("lonely") || lowerThought.contains("isolated") ->
            "This feeling is temporary. Connection starts with being present with yourself. What small step could you take toward connection?"

        lowerThought.contains("angry") || lowerThought.contains("mad") || lowerThought.contains("frustrated") ->
            "Anger often signals a boundary has been crossed. What value is important to you here? How can you honor it constructively?"

        lowerThought.contains("scared") || lowerThought.contains("afraid") || lowerThought.contains("fear") ->
            "Fear means you're stepping outside your comfort zone - that's where growth happens. What's the smallest safe step forward?"

        else -> "This thought doesn't define you. Look for the opportunity hidden in this situation - what small positive action can you take right now?"
    }
}