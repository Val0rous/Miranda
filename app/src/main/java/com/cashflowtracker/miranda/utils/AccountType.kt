package com.cashflowtracker.miranda.utils

enum class AccountType {
    BANK_ACCOUNT,
    CREDIT_CARD,
    WALLET,
    INVESTMENTS,
    SAFE,
    MONEY_BOX,

    /** These ones give an IBAN to users */
    ONLINE_PAYMENT_SYSTEM,

    /** These ones have no bank coordinates */
    ONLINE_PLATFORM,
}