package com.cashflowtracker.miranda.utils

import com.cashflowtracker.miranda.R

enum class SpecialType(
    override val category: Int,
    override val icon: Int,
    val isSource: Boolean,
    val isDestination: Boolean,
    override val description: Int = 0
) : DescriptionCategory {
    POCKET(R.string.special_type_pocket, R.drawable.ic_guardian, true, false),
    EXTRA(R.string.special_type_extra, R.drawable.ic_cake, true, false),
    DEBTS(R.string.special_type_debts, R.drawable.ic_credit_card_clock, true, true),
    CREDITS(R.string.special_type_credits, R.drawable.ic_money_bag, true, true);

    companion object {
        fun getCategory(categoryName: String): Int {
            return SpecialType.entries.find {
                it.name == categoryName
            }?.category ?: 0
        }

        fun getIcon(category: String): Int {
            return SpecialType.entries.find {
                it.name == category
            }?.icon ?: R.drawable.ic_default_empty
        }
    }
}

interface DescriptionCategory {
    val category: Int;
    val icon: Int;
    val description: Int;
}