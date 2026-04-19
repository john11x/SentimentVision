package com.example.sentimentvision.ui.therapist

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.lifecycle.lifecycleScope
import com.example.sentimentvision.R
import com.example.sentimentvision.data.repository.RepositoryProvider
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class TherapistDashboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_therapist_dashboard)

        setupNokDialer()
    }

    private fun setupNokDialer() {
        val nokNumbers = mapOf(
            R.id.cardNok1 to "+254712345678",
            R.id.cardNok2 to "+254733456789",
            R.id.cardNok3 to "+254722567890",
            R.id.cardNok4 to "+254700678901"
        )
        nokNumbers.forEach { (cardId, number) ->
            findViewById<CardView>(cardId).setOnClickListener {
                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$number"))
                startActivity(intent)
            }
        }
    }

    private fun openPatientDetails(userId: Int, name: String) {
        lifecycleScope.launch {
            try {
                RepositoryProvider.sentimentRepository.getSentimentsByUser(userId).collectLatest { sentiments ->
                    if (sentiments.isEmpty()) {
                        Toast.makeText(this@TherapistDashboardActivity, "No sentiment data yet for $name", Toast.LENGTH_SHORT).show()
                        return@collectLatest
                    }

                    val latestSentiment = sentiments.last().sentiment
                    val summary = "Analyzed ${sentiments.size} entries. Latest sentiment: $latestSentiment."

                    val intent = Intent(this@TherapistDashboardActivity, TherapistViewPatientActivity::class.java).apply {
                        putExtra("userId", userId)
                        putExtra("name", name)
                        putExtra("sentiment", latestSentiment)
                        putExtra("summary", summary)
                    }
                    startActivity(intent)
                }
            } catch (e: Exception) {
                Toast.makeText(this@TherapistDashboardActivity, "Error loading data: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}