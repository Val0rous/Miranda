package com.cashflowtracker.miranda.utils

enum class Repeats(val label: String, val time: Long) {
    DAILY("Every day", 86400000L),
    WEEKLY("Every week", 604800000L),
    BIWEEKLY("Every 2 weeks", 1209600000L),
    MONTHLY("Every month", 2592000000L),
    BIMONTHLY("Every 2 months", 5184000000L),
    QUARTERLY("Every 3 months", 7776000000L),
    YEARLY("Every year", 31536000000L)
}

enum class Notifications(val label: String, val time: Long, val shortDesc: String) {
    AT_TIME("At time of transaction", 0L, "now"),
    FIVE_MINUTES_BEFORE("5 minutes before", 300000L, "in 5 minutes"),
    TEN_MINUTES_BEFORE("10 minutes before", 600000L, "in 10 minutes"),
    FIFTEEN_MINUTES_BEFORE("15 minutes before", 900000L, "in 15 minutes"),
    THIRTY_MINUTES_BEFORE("30 minutes before", 1800000L, "in 30 minutes"),
    ONE_HOUR_BEFORE("1 hour before", 3600000L, "in 1 hour"),
    TWO_HOURS_BEFORE("2 hours before", 7200000L, "in 2 hours"),
    THREE_HOURS_BEFORE("3 hours before", 10800000L, "in 3 hours"),
    FOUR_HOURS_BEFORE("4 hours before", 14400000L, "in 4 hours"),
    SIX_HOURS_BEFORE("6 hours before", 21600000L, "in 6 hours"),
    EIGHT_HOURS_BEFORE("8 hours before", 28800000L, "in 8 hours"),
    TWELVE_HOURS_BEFORE("12 hours before", 43200000L, "in 12 hours"),
    ONE_DAY_BEFORE("1 day before", 86400000L, "in 1 day"),
    ONE_WEEK_BEFORE("1 week before", 604800000L, "in 1 week"),
    TWO_WEEKS_BEFORE("2 weeks before", 1209600000L, "in 2 weeks"),
}