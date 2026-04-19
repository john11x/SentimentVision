package com.example.sentimentvision.ui.therapist

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.random.Random

data class Therapist(val name: String, val specialty: String, val phone: String)

@Composable
fun ContactTherapistScreen() {
    val therapists = remember { generateTherapists() }
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF1B1B25), Color(0xFF0F0F12))
                )
            )
            .padding(16.dp)
    ) {
        Column {
            Text(
                text = "Find a Therapist",
                color = Color(0xFFBB86FC),
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Reach out to licensed professionals whenever you need support.",
                color = Color.LightGray,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(horizontal = 20.dp)
            )

            Spacer(Modifier.height(24.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(therapists) { therapist ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF262636)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = therapist.name,
                                    color = Color.White,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = therapist.specialty,
                                    color = Color(0xFFBB86FC),
                                    fontSize = 14.sp
                                )
                                Text(
                                    text = "📞 ${therapist.phone}",
                                    color = Color.LightGray,
                                    fontSize = 14.sp
                                )
                            }

                            IconButton(onClick = {
                                val intent = Intent(Intent.ACTION_DIAL).apply {
                                    data = Uri.parse("tel:${therapist.phone}")
                                }
                                context.startActivity(intent)
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Call,
                                    contentDescription = "Call",
                                    tint = Color(0xFF03DAC6),
                                    modifier = Modifier.size(30.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

fun generateTherapists(): List<Therapist> {
    val names = listOf(
        "Dr. Sarah Mwangi", "Dr. Kelvin Odhiambo", "Dr. Jane Achieng", "Dr. Brian Kiptoo", "Dr. Alice Njeri", "Dr. Mark Wambua", "Dr. Nancy Cherono", "Dr. Peter Mutiso", "Dr. Joy Wangui", "Dr. Samuel Ouma"
    )
    val specialties = listOf(
        "Clinical Psychologist", "Trauma Specialist", "Cognitive Therapist",
        "Behavioral Counselor", "Mental Health Expert"
    )
    return List(10) {
        Therapist(
            name = names[it],
            specialty = specialties.random(),
            phone = "+2547${Random.nextInt(10000000, 99999999)}"
        )
    }
}
