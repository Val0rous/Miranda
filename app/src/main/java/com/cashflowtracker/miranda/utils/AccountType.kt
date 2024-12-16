package com.cashflowtracker.miranda.utils

import com.cashflowtracker.miranda.R

enum class AccountType(val type: Int, val icon: Int) {
    BANK(R.string.account_type_bank, R.drawable.ic_account_balance),
    WALLET(R.string.account_type_wallet, R.drawable.ic_wallet),
    CREDIT_DEBIT_CARD(R.string.account_type_credit_debit_card, R.drawable.ic_credit_card),
    INVESTMENTS_STOCKS(R.string.account_type_investments_stocks, R.drawable.ic_monitoring),
    SAFE(R.string.account_type_safe, R.drawable.ic_vpn_key),
    SAVINGS(R.string.account_type_savings, R.drawable.ic_savings),
    SIM_CARD(R.string.account_type_sim_card, R.drawable.ic_sim_card),
    BITCOIN_WALLET(R.string.account_type_bitcoin_wallet, R.drawable.ic_currency_bitcoin),

    /** These ones give an IBAN to users */
    PAYMENT_PLATFORM(R.string.account_type_payment_platform, R.drawable.ic_local_atm),

    /** These ones have no bank coordinates */
    OTHER_ACCOUNT(R.string.account_type_other_account, R.drawable.ic_universal_currency);

    companion object {
        fun getType(accountTypeName: String): Int {
            return entries.find {
                it.name == accountTypeName
            }?.type ?: 0
        }

        fun getIcon(type: String): Int {
            return entries.find {
                it.name == type
            }?.icon ?: R.drawable.ic_default_empty
        }
    }
}