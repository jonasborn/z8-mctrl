package de.jonasborn.zeroeight.data

import de.jonasborn.zeroeight.data.money.Deposits
import org.jetbrains.exposed.dao.id.LongIdTable

object Users : LongIdTable() {
    val device = Deposits.short("device")
    val name = Deposits.varchar("name", 32)
    val time = Deposits.long("time")
}