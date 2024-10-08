package com.cashflowtracker.miranda.utils

import androidx.compose.runtime.MutableDoubleState
import androidx.compose.runtime.MutableState
import com.cashflowtracker.miranda.data.database.Transaction
import com.cashflowtracker.miranda.ui.viewmodels.AccountsViewModel
import java.util.UUID

fun calculateBalance(
    amount: Double,
    transactionType: String,
    source: String,
    destination: String,
    accountsVm: AccountsViewModel,
    userId: UUID
) {
    if (amount != 0.0) {
        when (transactionType) {
            "Output" -> {
                val sourceAccountId =
                    accountsVm.actions.getByTitleOrNull(
                        source,
                        userId
                    )?.accountId
                if (sourceAccountId != null) {
                    accountsVm.actions.updateBalance(
                        sourceAccountId,
                        -amount
                    )
                }
            }

            "Input" -> {
                val destinationAccountId =
                    accountsVm.actions.getByTitleOrNull(
                        destination,
                        userId
                    )?.accountId
                if (destinationAccountId != null) {
                    accountsVm.actions.updateBalance(
                        destinationAccountId,
                        +amount
                    )
                }
            }

            "Transfer" -> {
                val sourceAccountId =
                    accountsVm.actions.getByTitleOrNull(
                        source,
                        userId
                    )?.accountId
                val destinationAccountId =
                    accountsVm.actions.getByTitleOrNull(
                        destination,
                        userId
                    )?.accountId
                if (sourceAccountId != null && destinationAccountId != null) {
                    accountsVm.actions.updateBalance(
                        sourceAccountId,
                        -amount
                    )
                    accountsVm.actions.updateBalance(
                        destinationAccountId,
                        +amount
                    )
                }
            }
        }
    }
}

fun revertTransaction(transaction: Transaction, accountsVm: AccountsViewModel, userId: UUID) {
    revertTransaction(
        transaction.amount,
        transaction.type,
        transaction.source,
        transaction.destination,
        accountsVm,
        userId
    )
}

fun revertTransaction(
    amount: Double,
    transactionType: String,
    source: String,
    destination: String,
    accountsVm: AccountsViewModel,
    userId: UUID
) {
    if (amount != 0.0) {
        when (transactionType) {
            "Output" -> {
                val sourceId = accountsVm.actions.getByTitleOrNull(
                    source,
                    userId
                )?.accountId
                if (sourceId != null) {
                    accountsVm.actions.updateBalance(
                        sourceId,
                        +amount
                    )
                }
            }

            "Input" -> {
                val destinationId =
                    accountsVm.actions.getByTitleOrNull(
                        destination,
                        userId
                    )?.accountId
                if (destinationId != null) {
                    accountsVm.actions.updateBalance(
                        destinationId,
                        -amount
                    )
                }
            }

            "Transfer" -> {
                val sourceId = accountsVm.actions.getByTitleOrNull(
                    source,
                    userId
                )?.accountId
                val destinationId =
                    accountsVm.actions.getByTitleOrNull(
                        destination,
                        userId
                    )?.accountId
                if (sourceId != null && destinationId != null) {
                    accountsVm.actions.updateBalance(
                        sourceId,
                        +amount
                    )
                    accountsVm.actions.updateBalance(
                        destinationId,
                        -amount
                    )
                }
            }
        }
    }
}