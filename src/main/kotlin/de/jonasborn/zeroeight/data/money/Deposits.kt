package de.jonasborn.zeroeight.data.money

import org.jetbrains.exposed.dao.id.LongIdTable

object Deposits : LongIdTable() {
    val device = short("device")
    val user = varchar("user", 32)
    val amount = float("amount")
    val time = long("time")
    val origin = enumeration<Operator>("source", Operator::class)
    val details = varchar("details", 255)
}