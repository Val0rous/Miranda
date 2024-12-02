package com.cashflowtracker.miranda.utils

import android.annotation.SuppressLint
import android.content.Context
import android.text.format.DateFormat
import com.cashflowtracker.miranda.data.database.Transaction
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.time.Duration
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
    val numberFormat = NumberFormat.getNumberInstance(Locale.getDefault()) //as DecimalFormat
    numberFormat.minimumFractionDigits = if (currency.showDecimals) 2 else 0
    numberFormat.maximumFractionDigits = if (currency.showDecimals) 2 else 0

    val formattedNumber = numberFormat.format(amount)
    val currencySymbol = Currency.getInstance(currency.name).symbol

    val sign = when (transactionType) {
        TransactionType.OUTPUT.name -> if (amount != 0.0) "-" else ""
        TransactionType.INPUT.name -> if (amount != 0.0) "+" else ""
        else -> ""
    }
    return "$sign$formattedNumber $currencySymbol"
}

fun formatAmountAsInt(amount: Float, currency: Currencies): String {
    val numberFormat = NumberFormat.getNumberInstance(Locale.getDefault()) //as DecimalFormat
    numberFormat.minimumFractionDigits = 0
    numberFormat.maximumFractionDigits = 1

    val suffix = when {
        amount >= 1_000_000_000 -> "B"
        amount >= 1_000_000 -> "M"
        amount >= 1_000 -> "K"
        else -> ""
    }
    val divisor = when (suffix) {
        "B" -> 1_000_000_000
        "M" -> 1_000_000
        "K" -> 1_000
        else -> 1
    }

    val scaledAmount = amount / divisor
    val formattedNumber = if (suffix.isNotEmpty()) {
        numberFormat.format(scaledAmount)
    } else {
        scaledAmount.toInt().toString()
    }
    val currencySymbol = Currency.getInstance(currency.name).symbol

    return "$formattedNumber$suffix$currencySymbol"
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

fun formatRenewal(dateTime: String): String {
    val now = ZonedDateTime.now()
    val renewalTime = ZonedDateTime.parse(dateTime)
    val duration = Duration.between(now, renewalTime)

    if (duration.isNegative) {
        return "Already renewed"
    }

    val days = duration.toDays()
    val hours = duration.toHours()
    val minutes = duration.toMinutes()
    val seconds = duration.seconds

    // Decide the message based on the largest applicable unit
    val message = when {
        days >= 730 -> "in ${days / 365} years" // 730 days = 2 years
        days >= 365 -> "in 1 year"
        days >= 60 -> "in ${days / 30} months" // Approximate months as 30 days
        days >= 30 -> "in 1 month"
        days >= 14 -> "in ${days / 7} weeks"
        days >= 7 -> "in 1 week"
        days >= 2 -> "in $days days"
        days == 1L -> "in 1 day"
        hours >= 2 -> "in ${hours % 24} hours"
        hours == 1L -> "in 1 hour"
        minutes >= 2 -> "in ${minutes % 60} minutes"
        minutes == 1L -> "in 1 minute"
        seconds >= 0 -> "in less than a minute"
        else -> return "Already renewed"
    }

    return "Renews $message"
}

fun formatSource(source: String, transactionType: String): String {
    return when (transactionType) {
        TransactionType.OUTPUT.name -> AccountType.getType(source).ifEmpty { source }
        TransactionType.INPUT.name -> when (source) {
            SpecialType.POCKET.name, SpecialType.EXTRA.name -> SpecialType.getType(source)
            else -> DefaultCategories.getCategory(source)
        }

        else -> AccountType.getType(source).ifEmpty { source }
    }
}

fun formatDestination(destination: String, transactionType: String): String {
    return when (transactionType) {
        TransactionType.OUTPUT.name -> DefaultCategories.getCategory(destination)
        TransactionType.INPUT.name -> AccountType.getType(destination).ifEmpty { destination }
        else -> AccountType.getType(destination).ifEmpty { destination }
    }
}

fun getInitials(fullName: String): String {
    val names = fullName.trim().split("\\s+".toRegex()) // Split by any whitespace
    return when (names.size) {
        0 -> ""
        1 -> names.first().take(1).uppercase()
        else -> {
            val firstInitial = names.first().take(1).uppercase()
            val lastInitial = names.last().take(1).uppercase()
            firstInitial + lastInitial
        }
    }
}