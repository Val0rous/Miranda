package com.cashflowtracker.miranda.utils

import com.cashflowtracker.miranda.R

enum class SpecialType(val category: String, val icon: Int) {
    POCKET("Pocket", R.drawable.ic_guardian),
    EXTRA("Extra", R.drawable.ic_cake);

    companion object {
        fun getIcon(category: String): Int {
            return SpecialType.entries.find {
                it.category == category
            }?.icon ?: R.drawable.ic_default_empty
        }
    }
}