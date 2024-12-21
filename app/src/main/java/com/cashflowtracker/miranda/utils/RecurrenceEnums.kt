package com.cashflowtracker.miranda.utils

import com.cashflowtracker.miranda.R

enum class Repeats(val label: Int) {
    DAILY(R.string.repeats_daily),
    WEEKLY(R.string.repeats_weekly),
    BIWEEKLY(R.string.repeats_biweekly),
    MONTHLY(R.string.repeats_monthly),
    BIMONTHLY(R.string.repeats_bimonthly),
    QUARTERLY(R.string.repeats_quarterly),
    YEARLY(R.string.repeats_yearly)
}

enum class Notifications(val label: Int, val shortDesc: Int) {
    AT_TIME(R.string.notification_at_time, R.string.notification_at_time_short),
    FIVE_MINUTES_BEFORE(
        R.string.notification_5_minutes_before,
        R.string.notification_5_minutes_before_short
    ),
    TEN_MINUTES_BEFORE(
        R.string.notification_10_minutes_before,
        R.string.notification_10_minutes_before_short
    ),
    FIFTEEN_MINUTES_BEFORE(
        R.string.notification_15_minutes_before,
        R.string.notification_15_minutes_before_short
    ),
    THIRTY_MINUTES_BEFORE(
        R.string.notification_30_minutes_before,
        R.string.notification_30_minutes_before_short
    ),
    ONE_HOUR_BEFORE(R.string.notification_1_hour_before, R.string.notification_1_hour_before_short),
    TWO_HOURS_BEFORE(
        R.string.notification_2_hours_before,
        R.string.notification_2_hours_before_short
    ),
    THREE_HOURS_BEFORE(
        R.string.notification_3_hours_before,
        R.string.notification_3_hours_before_short
    ),
    FOUR_HOURS_BEFORE(
        R.string.notification_4_hours_before,
        R.string.notification_4_hours_before_short
    ),
    SIX_HOURS_BEFORE(
        R.string.notification_6_hours_before,
        R.string.notification_6_hours_before_short
    ),
    EIGHT_HOURS_BEFORE(
        R.string.notification_8_hours_before,
        R.string.notification_8_hours_before_short
    ),
    TWELVE_HOURS_BEFORE(
        R.string.notification_12_hours_before,
        R.string.notification_12_hours_before_short
    ),
    ONE_DAY_BEFORE(R.string.notification_1_day_before, R.string.notification_1_day_before_short),
    ONE_WEEK_BEFORE(R.string.notification_1_week_before, R.string.notification_1_week_before_short),
    TWO_WEEKS_BEFORE(
        R.string.notification_2_weeks_before,
        R.string.notification_2_weeks_before_short
    ),
}