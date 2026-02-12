package com.example.camvidgram.core.utils

fun formatTime(millis: Long): String {
    val formatter = java.text.SimpleDateFormat("dd MMM, hh:mm a", java.util.Locale.getDefault())
    return formatter.format(java.util.Date(millis))
}
