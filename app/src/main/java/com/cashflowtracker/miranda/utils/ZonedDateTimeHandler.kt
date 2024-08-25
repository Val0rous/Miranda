package com.cashflowtracker.miranda.utils

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.TimeZone

fun buildZonedDateTime(
    dateString: String,
    timeString: String,
    timeZoneString: String
): ZonedDateTime? {
    return try {
        val dateFormatter = DateTimeFormatter.ofPattern("EEE, MMM d, yyyy", Locale.getDefault())
        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
        val localDate = LocalDate.parse(dateString, dateFormatter)
        val localTime = LocalTime.parse(timeString, timeFormatter)
        val localDateTime = LocalDateTime.of(localDate, localTime)
        val gmtOffsetString = timeZoneString.substringBefore(" -").trim()
        val timeZone = try {
            TimeZone.getTimeZone(gmtOffsetString)
        } catch (e: Exception) {
            println("Invalid time zone: ${e.message}")
            null
        }
        timeZone?.let {
            val instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant()
            ZonedDateTime.ofInstant(instant, it.toZoneId())
        }
    } catch (e: Exception) {
        println("Error building ZonedDateTime: ${e.message}")
        null
    }
}