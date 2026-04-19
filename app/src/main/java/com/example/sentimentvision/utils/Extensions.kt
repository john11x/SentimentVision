package com.example.sentimentvision.utils

import java.text.SimpleDateFormat
import java.util.*

fun Long.toFormattedDate(): String {
    val sdf = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
    return sdf.format(Date(this))
}

fun Float.toPercentage(): String {
    return "${(this * 100).toInt()}%"
}