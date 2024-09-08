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

enum class Notifications(val label: String, val time: Long) {
    AT_TIME("At time of transaction", 0L),
    FIVE_MINUTES_BEFORE("5 minutes before", 300000L),
    TEN_MINUTES_BEFORE("10 minutes before", 600000L),
    FIFTEEN_MINUTES_BEFORE("15 minutes before", 900000L),
    THIRTY_MINUTES_BEFORE("30 minutes before", 1800000L),
    ONE_HOUR_BEFORE("1 hour before", 3600000L),
    TWO_HOURS_BEFORE("2 hours before", 7200000L),
    THREE_HOURS_BEFORE("3 hours before", 10800000L),
    FOUR_HOURS_BEFORE("4 hours before", 14400000L),
    SIX_HOURS_BEFORE("6 hours before", 21600000L),
    EIGHT_HOURS_BEFORE("8 hours before", 28800000L),
    TWELVE_HOURS_BEFORE("12 hours before", 43200000L),
    ONE_DAY_BEFORE("1 day before", 86400000L),
    ONE_WEEK_BEFORE("1 week before", 604800000L),
    TWO_WEEKS_BEFORE("2 weeks before", 1209600000L),
}