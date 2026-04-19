// ui/analysis/AnalysisScreen.kt (updated)
package com.example.sentimentvision.ui.analysis

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AnalysisScreen(
    viewModel: AnalysisViewModel = viewModel()
) {
    // You can make this a different screen or redirect to JournalScreen
    JournalScreen(viewModel = viewModel)
}