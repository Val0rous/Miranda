package com.cashflowtracker.miranda.utils

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

data class TimeZoneEntry(
    val displayName: String,
    val gmtFormat: String,
    val country: String,
) : Serializable