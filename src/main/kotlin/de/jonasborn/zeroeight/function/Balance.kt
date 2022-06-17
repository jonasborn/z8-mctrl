package de.jonasborn.zeroeight.function

import de.jonasborn.zeroeight.data.User
import de.jonasborn.zeroeight.data.UserTable
import de.jonasborn.zeroeight.data.money.DepositTable
import de.jonasborn.zeroeight.data.money.PaymentTable
import de.jonasborn.zeroeight.data.money.PayoutTable
import de.jonasborn.zeroeight.data.token.TokenTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.Function
import org.jetbrains.exposed.sql.transactions.transaction

class Balance {

    companion object {
        fun getDeposits(user: User): Float {
            val result = UserTable.join(
                TokenTable, JoinType.INNER, additionalConstraint = { UserTable.id eq TokenTable.user }
            ).join(
                DepositTable, JoinType.INNER, additionalConstraint = { TokenTable.id eq DepositTable.token }
            ).slice(
                DepositTable.amount.sum()
            ).select { UserTable.id eq user.id }
            result.forEach{
                println(it[DepositTable.amount.sum()])
            }
            return result.first()[DepositTable.amount.sum()] ?: return 0f
        }

        fun getPayments(user: User): Float {
            val result = UserTable.join(
                TokenTable, JoinType.INNER, additionalConstraint = { UserTable.id eq TokenTable.user }
            ).join(
                PaymentTable, JoinType.INNER, additionalConstraint = { TokenTable.id eq PaymentTable.token }
            ).slice(
                PaymentTable.amount.sum()
            ).select{
                UserTable.id eq user.id
            }
            return result.first()[PaymentTable.amount.sum()] ?: return 0f
        }

        fun getPayouts(user: User): Float {
            val result = UserTable.join(
                TokenTable, JoinType.INNER, additionalConstraint = { UserTable.id eq TokenTable.user }
            ).join(
                PayoutTable, JoinType.INNER, additionalConstraint = { TokenTable.id eq PayoutTable.token }
            ).slice(
                PayoutTable.amount.sum()
            ).select{
                UserTable.id eq user.id
            }
            val first = result.first() ?: return 0f
            return first[PayoutTable.amount.sum()] ?: return 0f
        }

        fun getTotal(user: User): Float {
            val deposits = getDeposits(user)
            val payments = getPayments(user)
            val payouts = getPayouts(user)
            return deposits - payments - payouts
        }
    }

}