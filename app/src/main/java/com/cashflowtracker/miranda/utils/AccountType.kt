package com.cashflowtracker.miranda.utils

import com.cashflowtracker.miranda.R

enum class AccountType(val type: String, val icon: Int) {
    BANK("Bank", R.drawable.ic_account_balance),
    WALLET("Wallet", R.drawable.ic_wallet),
    CREDIT_DEBIT_CARD("Credit/Debit Card", R.drawable.ic_credit_card),
    INVESTMENTS_STOCKS("Investments/Stocks", R.drawable.ic_monitoring),
    SAFE("Safe", R.drawable.ic_vpn_key),
    SAVINGS("Savings", R.drawable.ic_savings),
    BITCOIN_WALLET("Bitcoin Wallet", R.drawable.ic_currency_bitcoin),

    /** These ones give an IBAN to users */
    PAYMENT_PLATFORM("Payment Platform", R.drawable.ic_local_atm),

    /** These ones have no bank coordinates */
    OTHER_ACCOUNT("Other Account", R.drawable.ic_universal_currency);

    companion object {
        fun getIcon(type: String): Int {
            return entries.find {
                it.type == type
            }?.icon ?: R.drawable.ic_default_empty
        }
    }
}