package io.github.edgardobarriam.techkandroidchallenge.ui.util

import java.text.SimpleDateFormat
import java.util.*

object TimestampFormatter {

    fun timestampToDateTimeString(timestamp: Long) : String {
        val calendar = Calendar.getInstance()
        val timeZone = TimeZone.getDefault()
        calendar.add(Calendar.MILLISECOND, timeZone.getOffset(calendar.timeInMillis))
        val dateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault())
        val currentTime = java.util.Date(timestamp * 1000)
        return dateFormat.format(currentTime)
    }
}