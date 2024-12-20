package com.cashflowtracker.miranda.utils

import android.content.Context
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

fun buildZonedDateTime(
    dateString: String,
    timeString: String,
    timeZoneString: String
): ZonedDateTime? {
    return try {
        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
        val localDate = LocalDate.parse(dateString, dateFormatter)
        val localTime = LocalTime.parse(timeString, timeFormatter)
        val localDateTime = LocalDateTime.of(localDate, localTime)

//        val gmtOffsetString = timeZoneString.substringBefore(" -").trim()
        val zoneId = try {
            ZoneId.of(timeZoneString)
        } catch (e: Exception) {
            println("Invalid ZoneId: ${e.message}. Falling back to default time zone.")
            ZoneId.systemDefault()
        }
        ZonedDateTime.of(localDateTime, zoneId)
    } catch (e: Exception) {
        println("Error building ZonedDateTime: ${e.message}")
        null
    }
}

fun formatZonedDateTime(context: Context, storedDateTimeString: String): String {
    val zdt = ZonedDateTime.parse(storedDateTimeString, DateTimeFormatter.ISO_ZONED_DATE_TIME)
    val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val date = formatDate(zdt.format(dateFormatter))
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
    val time = formatTime(context, zdt.format(timeFormatter))
//    val timezone = retrievedZonedDateTime.zone.toString()
//    val timezone = retrievedZonedDateTime.format(DateTimeFormatter.ofPattern("z"))
    val timezone = formatTimezone(zdt)
    return "$date  ·  $time  ·  $timezone"
//    return retrievedZonedDateTime.format(formatter)
}

fun getDate(storedDateTimeString: String): String {
    val zdt = ZonedDateTime.parse(storedDateTimeString, DateTimeFormatter.ISO_ZONED_DATE_TIME)
    val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    return formatDate(zdt.format(dateFormatter))
}