package com.cashflowtracker.miranda.utils

import java.time.Instant
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

fun addMillisToTime(zonedDateTime: ZonedDateTime, millis: Long): String {
    return Instant.ofEpochMilli(
        zonedDateTime.toInstant()
            .toEpochMilli() + millis
    ).atZone(zonedDateTime.zone)
        .format(DateTimeFormatter.ISO_ZONED_DATE_TIME)
}

fun getRepeatTime(zdt: ZonedDateTime, repeatInterval: Repeats): String {
    return when (repeatInterval) {
        Repeats.DAILY -> zdt.plusDays(1)
        Repeats.WEEKLY -> zdt.plusWeeks(1)
        Repeats.BIWEEKLY -> zdt.plusWeeks(2)
        Repeats.MONTHLY -> zdt.plusMonths(1)
        Repeats.BIMONTHLY -> zdt.plusMonths(2)
        Repeats.QUARTERLY -> zdt.plusMonths(3)
        Repeats.YEARLY -> zdt.plusYears(1)
    }.format(DateTimeFormatter.ISO_ZONED_DATE_TIME)
}

fun getNotificationTime(zdt: ZonedDateTime, notificationInterval: Notifications): String {
    return when (notificationInterval) {
        Notifications.AT_TIME -> zdt
        Notifications.FIVE_MINUTES_BEFORE -> zdt.minusMinutes(5)
        Notifications.TEN_MINUTES_BEFORE -> zdt.minusMinutes(10)
        Notifications.FIFTEEN_MINUTES_BEFORE -> zdt.minusMinutes(15)
        Notifications.THIRTY_MINUTES_BEFORE -> zdt.minusMinutes(30)
        Notifications.ONE_HOUR_BEFORE -> zdt.minusHours(1)
        Notifications.TWO_HOURS_BEFORE -> zdt.minusHours(2)
        Notifications.THREE_HOURS_BEFORE -> zdt.minusHours(3)
        Notifications.FOUR_HOURS_BEFORE -> zdt.minusHours(4)
        Notifications.SIX_HOURS_BEFORE -> zdt.minusHours(6)
        Notifications.EIGHT_HOURS_BEFORE -> zdt.minusHours(8)
        Notifications.TWELVE_HOURS_BEFORE -> zdt.minusHours(12)
        Notifications.ONE_DAY_BEFORE -> zdt.minusDays(1)
        Notifications.ONE_WEEK_BEFORE -> zdt.minusWeeks(1)
        Notifications.TWO_WEEKS_BEFORE -> zdt.minusWeeks(2)
    }.format(DateTimeFormatter.ISO_ZONED_DATE_TIME)
}