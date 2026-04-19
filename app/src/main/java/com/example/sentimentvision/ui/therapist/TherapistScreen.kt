package com.example.sentimentvision.ui.therapist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sentimentvision.data.model.SentimentResult
import com.example.sentimentvision.data.model.UserEntity
import com.example.sentimentvision.data.repository.RepositoryProvider
import com.example.sentimentvision.ui.components.HistoryCard
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun TherapistScreen() {
    val repo = RepositoryProvider.sentimentRepository
    val authRepo = RepositoryProvider.authRepository
    val scope = rememberCoroutineScope()

    var users by remember { mutableStateOf<List<UserEntity>>(emptyList()) }
    var selectedUser by remember { mutableStateOf<UserEntity?>(null) }
    var userEntries by remember { mutableStateOf<List<SentimentResult>>(emptyList()) }
    var scheduledCount by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        // Load users
        scope.launch {
            authRepo.listUsers().collectLatest { userList ->
                users = userList
                if (users.isNotEmpty() && selectedUser == null) {
                    selectedUser = users.first()
                    refreshUserEntries(users.first(), repo, scope) { userEntries = it }
                }
            }
        }
        // Load appointments count
        scope.launch {
            scheduledCount = repo.getAppointments().size
        }
    }

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Brush.verticalGradient(listOf(Color(0xFFE3F2FD), Color(0xFFBBDEFB))))
                .padding(16.dp)
        ) {
            Text("Therapist Dashboard", fontSize = 28.sp, color = Color(0xFF0D47A1))
            Spacer(Modifier.height(4.dp))
            Text("Scheduled Appointments: $scheduledCount", fontSize = 16.sp, color = Color(0xFF1976D2))
            Spacer(Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxSize(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {

                // Left panel: User list
                Column(modifier = Modifier.weight(0.35f)) {
                    Text("Users", fontSize = 20.sp, color = Color(0xFF0D47A1))
                    Spacer(Modifier.height(8.dp))
                    LazyColumn {
                        items(users) { user ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .padding(12.dp)
                                        .fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column {
                                        Text(user.username, fontSize = 16.sp, color = Color.Black)
                                        user.displayName?.let {
                                            Text(it, fontSize = 14.sp, color = Color.DarkGray)
                                        }
                                    }
                                    Button(onClick = {
                                        selectedUser = user
                                        refreshUserEntries(user, repo, scope) { userEntries = it }
                                    }) {
                                        Text("View")
                                    }
                                }
                            }
                        }
                    }
                }

                // Right panel: Selected user details & entries
                Column(modifier = Modifier.weight(0.65f)) {
                    Text("Selected User", fontSize = 20.sp, color = Color(0xFF0D47A1))
                    Spacer(Modifier.height(8.dp))

                    if (selectedUser == null) {
                        Text("No user selected", color = Color.Gray)
                    } else {
                        val su = selectedUser!!
                        Text("Username: ${su.username}", fontSize = 16.sp)
                        su.displayName?.let { Text("Name: $it") }

                        Spacer(Modifier.height(8.dp))

                        // Demo next of kin entries
                        val nextOfKin = listOf(
                            Pair("Margaret Wanjiku", "+254 712 345 678"),
                            Pair("James Otieno", "+254 733 456 789"),
                            Pair("Grace Achieng", "+254 722 567 890"),
                            Pair("David Kamau", "+254 700 678 901")
                        )
                        Text("Next of Kin", fontSize = 16.sp, color = Color(0xFF0D47A1))
                        Spacer(Modifier.height(4.dp))
                        nextOfKin.forEach { (name, phone) ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 2.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(name, fontSize = 14.sp, color = Color.Black)
                                Text(phone, fontSize = 14.sp, color = Color(0xFF1976D2))
                            }
                        }

                        Spacer(Modifier.height(8.dp))

                        Button(onClick = {
                            scope.launch {
                                val scheduled = System.currentTimeMillis() + 48 * 60 * 60 * 1000L
                                repo.scheduleAppointment(su.username, scheduled, "Follow-up by therapist")
                                scheduledCount = repo.getAppointments().size
                            }
                        }) {
                            Text("Schedule Appointment (48h)")
                        }

                        Spacer(Modifier.height(12.dp))
                        Text("Recent Entries", fontSize = 18.sp, color = Color(0xFF1976D2))
                        Spacer(Modifier.height(8.dp))

                        LazyColumn {
                            items(userEntries) { entry ->
                                HistoryCard(result = entry)
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun refreshUserEntries(
    user: UserEntity,
    repo: com.example.sentimentvision.data.repository.SentimentRepository,
    scope: kotlinx.coroutines.CoroutineScope,
    onResult: (List<SentimentResult>) -> Unit
) {
    scope.launch {
        onResult(repo.getHistory())
    }
}