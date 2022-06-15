package de.jonasborn.zeroeight.data.money

import org.jetbrains.exposed.dao.id.IntIdTable

object Payouts : IntIdTable(){
    val device = Deposits.short("device")
    val user = Deposits.varchar("user", 32)
    val amount = Deposits.float("amount")
    val time = Deposits.long("time")
    val origin = Deposits.enumeration<Operator>("source", Operator::class)
    val details = Deposits.varchar("details", 255)
}