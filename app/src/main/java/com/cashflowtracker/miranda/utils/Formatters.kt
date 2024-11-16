package com.cashflowtracker.miranda.utils

import android.annotation.SuppressLint
import android.content.Context
import android.text.format.DateFormat
import com.cashflowtracker.miranda.data.database.Transaction
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Currency
import java.util.Date
import java.util.Locale

private const val MONEY_FORMAT = "###,###,##0.00"
private const val MONEY_FORMAT_NO_CENTS = "###,###,###"

fun Float.toMoneyFormat(
    removeTrailingZeroes: Boolean = false,
): String {
    val format =
        if (removeTrailingZeroes && (this % 1 == 0.0f)) DecimalFormat(MONEY_FORMAT_NO_CENTS)
        else DecimalFormat(MONEY_FORMAT)

    return format.format(this)
}

fun formatAmount(amount: Double, currency: Currencies, transactionType: String = ""): String {
    val numberFormat = NumberFormat.getNumberInstance(Locale.getDefault())
    numberFormat.minimumFractionDigits = if (currency.showDecimals) 2 else 0
    numberFormat.maximumFractionDigits = if (currency.showDecimals) 2 else 0

    val formattedNumber = numberFormat.format(amount)
    val currencySymbol = Currency.getInstance(currency.name).symbol

    val sign = when (transactionType) {
        TransactionType.OUTPUT.type -> if (amount != 0.0) "-" else ""
        TransactionType.INPUT.type -> if (amount != 0.0) "+" else ""
        else -> ""
    }
    return "$sign$formattedNumber $currencySymbol"
}

fun formatTime(context: Context, time: String): String {
    return try {
        // Parse the input time string (HH:mm)
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        val parsedTime = LocalTime.parse(time, formatter)
        val is24HourFormat = DateFormat.is24HourFormat(context)

        val timeFormatter = Date.from(
            parsedTime
                .atDate(LocalDate.now())
                .atZone(java.time.ZoneId.systemDefault())
                .toInstant()
        )

        if (is24HourFormat) {
            SimpleDateFormat("HH:mm", Locale.getDefault()).format(timeFormatter)
        } else {
            SimpleDateFormat("h:mm a", Locale.getDefault()).format(timeFormatter)
        }
    } catch (e: Exception) {
        "Invalid Time"
    }
}

fun formatDateWithWeekday(date: String): String {
    return try {
        val isoFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val parsedDate = isoFormatter.parse(date, LocalDate::from)
        val dateFormatter = Date.from(
            parsedDate.atStartOfDay(java.time.ZoneId.systemDefault()).toInstant()
        )
        val fullDate =
            java.text.DateFormat.getDateInstance(java.text.DateFormat.MEDIUM, Locale.getDefault())
                .format(dateFormatter)
        val dayOfWeek = SimpleDateFormat("EEE", Locale.getDefault())
            .format(dateFormatter)
        "$dayOfWeek, $fullDate"
    } catch (e: Exception) {
        "Invalid Date"
    }
}

fun formatDate(date: String): String {
    return try {
        val isoFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val parsedDate = isoFormatter.parse(date, LocalDate::from)
        val dateFormatter = Date.from(
            parsedDate.atStartOfDay(java.time.ZoneId.systemDefault()).toInstant()
        )
        val fullDate =
            java.text.DateFormat.getDateInstance(java.text.DateFormat.MEDIUM, Locale.getDefault())
                .format(dateFormatter)
        fullDate
    } catch (e: Exception) {
        "Invalid Date"
    }
}

@SuppressLint("DefaultLocale")
fun formatTimezone(zonedDateTime: ZonedDateTime): String {
    val zoneId = zonedDateTime.zone.id // Get the Zone ID (e.g., Europe/Rome, UTC, Zulu, Universal)
    val zoneOffset = zonedDateTime.offset // Get the ZoneOffset (e.g., +01:00, +00:00)

    // List of aliases for UTC
    val utcAliases = setOf("Etc/UTC", "Etc/UCT", "Etc/Zulu", "Etc/Universal")

    return when {
        zoneId in utcAliases -> "UTC" // Check if zoneId matches any UTC alias
        zoneOffset.totalSeconds == 0 -> "GMT" // Zero offset but not explicitly a UTC alias
        else -> {
            val totalSeconds = zoneOffset.totalSeconds
            val hours = totalSeconds / 3600
            val minutes = (totalSeconds % 3600) / 60
            if (minutes == 0) {
                String.format("GMT%+d", hours) // GMT+H
            } else {
                String.format("GMT%+d:%02d", hours, minutes) // GMT+H:MM
            }
        }
    }
}