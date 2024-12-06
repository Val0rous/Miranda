package com.cashflowtracker.miranda.utils

import com.cashflowtracker.miranda.R

enum class SpecialType(
    val category: String,
    val icon: Int,
    val isSource: Boolean,
    val isDestination: Boolean
) {
    POCKET("Pocket", R.drawable.ic_guardian, true, false),
    EXTRA("Extra", R.drawable.ic_cake, true, false),
    DEBTS("Debts", R.drawable.ic_credit_card_clock, true, true),
    CREDITS("Credits", R.drawable.ic_money_bag, true, true);

    companion object {
        fun getType(category: String): String {
            return SpecialType.entries.find {
                it.name == category
            }?.category ?: ""
        }

        fun getIcon(category: String): Int {
            return SpecialType.entries.find {
                it.name == category
            }?.icon ?: R.drawable.ic_default_empty
        }
    }
}