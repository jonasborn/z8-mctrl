package de.jonasborn.zeroeight.data.money

import org.jetbrains.exposed.dao.id.LongIdTable

object Payments: LongIdTable() {
    val device = short("device")
    val token = varchar("token", 64)
    val amount = float("amount")
    val time = long("time")
}